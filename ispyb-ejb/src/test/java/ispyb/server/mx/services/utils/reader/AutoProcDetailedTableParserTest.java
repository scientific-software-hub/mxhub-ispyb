package ispyb.server.mx.services.utils.reader;

import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Verifies that autoPROC's OVERALL and STARANISO-observations per-shell
 * "detailed statistics" tables are read correctly and kept distinct — the
 * regression covered here is that both used to be fed from the wrong
 * (embedded CORRECT.LP) table and so produced identical graphs.
 */
public class AutoProcDetailedTableParserTest {

	private static final double DELTA = 1e-3;

	private List<AutoProcessingData> parse(boolean wantStaraniso) throws IOException {
		AutoProcProgram3VO program = new AutoProcProgram3VO();
		program.setAutoProcProgramId(1);
		program.setProcessingPrograms(wantStaraniso ? "autoPROC_staraniso" : "autoPROC");

		AutoProcProgramAttachment3VO attachment = new AutoProcProgramAttachment3VO();
		attachment.setAutoProcProgramAttachmentId(99);
		attachment.setFileName("autoPROC_log.txt");
		attachment.setAutoProcProgramVO(program);

		AutoProcDetailedTableParser parser = new AutoProcDetailedTableParser(attachment, wantStaraniso);
		try (InputStream in = getClass().getClassLoader()
				.getResourceAsStream("autoproc_detailed_statistics_fixture.log");
				Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
			while (scanner.hasNextLine()) {
				parser.accept(scanner.nextLine());
			}
		}
		return parser.results();
	}

	@Test
	public void overallTable_returnsTwentyRows_firstRowMatchesIsotropicTable() throws IOException {
		List<AutoProcessingData> rows = parse(false);

		assertEquals(20, rows.size());
		AutoProcessingData first = rows.get(0);
		assertEquals(3.551, first.getResolutionLimit(), DELTA);
		assertEquals(100.0, first.getCompleteness(), DELTA);
		assertEquals(3.2, first.getrFactorObserved(), DELTA);
		assertEquals(62.785, first.getiSigma(), DELTA);
		assertEquals(99.87, first.getCc2(), DELTA);
		assertEquals(0.952, first.getSigAno(), DELTA);
		assertEquals(-22, first.getAnomalCorr().intValue());
	}

	@Test
	public void staranisoObservations_returnsTwentyRows_firstRowMatchesObservationsTable() throws IOException {
		List<AutoProcessingData> rows = parse(true);

		assertEquals(20, rows.size());
		AutoProcessingData first = rows.get(0);
		assertEquals(3.649, first.getResolutionLimit(), DELTA);
		assertEquals(62.566, first.getiSigma(), DELTA);
		assertEquals(0.970, first.getSigAno(), DELTA);
		assertEquals(-22, first.getAnomalCorr().intValue());
	}

	@Test
	public void overallAndStaranisoObservations_produceDistinctData() throws IOException {
		List<AutoProcessingData> overall = parse(false);
		List<AutoProcessingData> staraniso = parse(true);

		assertNotEquals(overall.get(0).getResolutionLimit(), staraniso.get(0).getResolutionLimit(), DELTA);
		assertNotEquals(overall.get(0).getiSigma(), staraniso.get(0).getiSigma(), DELTA);
	}
}
