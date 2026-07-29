package ispyb.server.mx.services.utils.reader;

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the correct AIMLESS/STARANISO per-shell "detailed statistics" table
 * from an autoPROC {@code .log} attachment: the {@code autoPROC} program
 * entry wants the first (OVERALL, isotropic) table, while
 * {@code autoPROC_staraniso} wants the STARANISO "observations" table.
 *
 */
class AutoProcDetailedTableParser implements LogSectionParser {

	private enum Table {
		OVERALL,
		STARANISO_MEASUREMENTS,
		STARANISO_OBSERVATIONS
	}

	private final AutoProcProgramAttachment3VO attachment;
	private final Table wanted;
	private final List<AutoProcessingData> data = new ArrayList<>();

	private Table pending = Table.OVERALL;
	private boolean inWantedBody = false;

	AutoProcDetailedTableParser(AutoProcProgramAttachment3VO attachment, boolean wantStaraniso) {
		this.attachment = attachment;
		this.wanted = wantStaraniso ? Table.STARANISO_OBSERVATIONS : Table.OVERALL;
	}

	@Override
	public void accept(String line) {
		if (line.contains("# detailed statistics")) {
			pending = line.contains("(measurements") ? Table.STARANISO_MEASUREMENTS : Table.STARANISO_OBSERVATIONS;
			return;
		}
		if (line.contains("#Rfac") && line.contains("SigAno")) {
			Table current = pending;
			pending = Table.OVERALL;
			inWantedBody = current == wanted;
			return;
		}
		if (!inWantedBody) {
			return;
		}
		if (line.contains("Total:")) {
			inWantedBody = false;
			return;
		}
		AimlessShellRow.parse(line).ifPresent(row -> data.add(toAutoProcessingData(row)));
	}

	private AutoProcessingData toAutoProcessingData(AimlessShellRow row) {
		return new AutoProcessingData(
				attachment.getAutoProcProgramAttachmentId().toString(),
				row.resolutionLimit(), row.completeness(), row.rFactorObserved(),
				row.iSigma(), row.cc2(), row.sigAno(), row.anomalCorr(),
				attachment.getFileName(), attachment.getAutoProcProgramVOId());
	}

	@Override
	public List<AutoProcessingData> results() {
		return data;
	}
}
