package ispyb.common.util.export;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 * Characterization test for {@link AutoProcBestResultExtractor#extractBestAutoproc}.
 * <p>
 * The row below mirrors the "P 41 21 2" example from the industrial-client
 * report request: real data is Overall/Inner/Outer completeness 100.0%/99.6%/100.0%,
 * resolution 39.4-1.6 / 39.4-6.0 / 1.61-1.56, Rmerge 3.7/1.7/73.5 (the PDF's own
 * "Res."/"Compl." column headers are swapped in the client's account, but the
 * underlying stored values used here are the correct, un-swapped ones).
 * <p>
 * This test pins the exact expected 18-element array, which is what
 * {@code ExiPdfRtfExporter} produced before the selection logic was extracted
 * out into {@link AutoProcBestResultExtractor} — i.e. a PDF-parity check.
 */
public class AutoProcBestResultExtractorTest {

	private static final String SPACE_GROUP = "P 41 21 2";

	private final AutoProcBestResultExtractor extractor = new AutoProcBestResultExtractor();

	private Map<String, Object> representativeRow() {
		Map<String, Object> row = new HashMap<>();
		// order per shell: [inner, outer, overall]
		row.put("scalingStatisticsTypes", "innerShell,outerShell,overall");
		row.put("AutoProc_spaceGroups", SPACE_GROUP + "," + SPACE_GROUP + "," + SPACE_GROUP);
		row.put("completenessList", "99.6,100.0,100.0");
		row.put("resolutionsLimitLow", "39.4,1.61,39.4");
		row.put("resolutionsLimitHigh", "6.0,1.56,1.6");
		row.put("rMerges", "1.7,73.5,3.7");
		row.put("Autoprocessing_anomalous", "0,0,0");
		row.put("Autoprocessing_cell_a", "78.81,78.81,78.81");
		row.put("Autoprocessing_cell_b", "78.81,78.81,78.81");
		row.put("Autoprocessing_cell_c", "37.10,37.10,37.10");
		row.put("Autoprocessing_cell_alpha", "90,90,90");
		row.put("Autoprocessing_cell_beta", "90,90,90");
		row.put("Autoprocessing_cell_gamma", "90,90,90");
		return row;
	}

	private Map<String, Integer> spgMap() {
		Map<String, Integer> spgMap = new HashMap<>();
		spgMap.put(SPACE_GROUP, 92);
		return spgMap;
	}

	@Test
	public void extractBestAutoproc_representativeRow_matchesPdfParityExpectation() throws Exception {
		String[] bestAutoproc = extractor.extractBestAutoproc(representativeRow(), spgMap());

		String[] expected = new String[] {
				// inner (best)
				SPACE_GROUP, "1.7", "99.6", "39.40/6.00",
				// cell
				"78.81", "78.81", "37.10", "90", "90", "90",
				// outer
				SPACE_GROUP, "73.5", "100.0", "1.61/1.56",
				// overall
				SPACE_GROUP, "3.7", "100.0", "39.40/1.60"
		};

		assertArrayEquals(expected, bestAutoproc);
	}

	@Test
	public void extractBestAutoproc_noAutoprocData_returnsNull() throws Exception {
		Map<String, Object> row = new HashMap<>();
		row.put("completenessList", null);

		assertNull(extractor.extractBestAutoproc(row, spgMap()));
	}

	@Test
	public void extractBestAutoproc_emptyCompletenessList_returnsNull() throws Exception {
		Map<String, Object> row = new HashMap<>();
		row.put("completenessList", "");
		row.put("AutoProc_spaceGroups", SPACE_GROUP);

		assertNull(extractor.extractBestAutoproc(row, spgMap()));
	}
}
