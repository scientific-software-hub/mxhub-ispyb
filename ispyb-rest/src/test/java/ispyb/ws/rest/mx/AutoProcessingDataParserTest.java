package ispyb.ws.rest.mx;

import ispyb.server.mx.services.utils.reader.AutoProcessingData;
import ispyb.server.mx.services.utils.reader.AutoProcessingDataParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Verifies the CSV contract between AutoProcessingDataParser and the JS client (autoprocintegrationplots.js).
 *
 * The JS loadUrl() does: csv.split("\n").reverse(), slice off the empty tail and the header,
 * then splits each row by "," and collects every odd-indexed column as a value.
 * Requirements:
 *   - CSV ends with "\n" so reverse+slice trims correctly
 *   - Header: "Resolution,<id1>,...,<idn>"  (n+1 tokens)
 *   - Data row: "<res>,<v1>,0,...,<vn>,0"   (2n+1 tokens, value+error pairs)
 *   - At least one data row so JS calls render()
 */
public class AutoProcessingDataParserTest {

    private static final int PROGRAM_ID = 42;
    private static final String ATTACH_ID = "XSCALE.LP";

    // ── factory helpers ──────────────────────────────────────────────────────

    private AutoProcessingData row(double res, double completeness, double rfactor,
                                   double iSigma, double cc2, double sigAno, int anomalCorr) {
        return new AutoProcessingData(ATTACH_ID, res, completeness, rfactor,
                iSigma, cc2, sigAno, anomalCorr, ATTACH_ID, PROGRAM_ID);
    }

    private AutoProcessingDataParser parserOf(AutoProcessingData... rows) {
        return new AutoProcessingDataParser(Collections.singletonList(Arrays.asList(rows)));
    }

    private AutoProcessingDataParser parserOf(List<List<AutoProcessingData>> lists) {
        return new AutoProcessingDataParser(lists);
    }

    private AutoProcessingData sampleRow(double res) {
        return row(res, 99.8, 5.4, 15.0, 99.3, 1.234, 78);
    }

    // ── helpers for CSV assertions ────────────────────────────────────────────

    /** Asserts the CSV is consumable by the JS client for n integration IDs. */
    private void assertJsConsumable(String csv, int numIntegrations) {
        assertNotNull("CSV must not be null", csv);
        assertTrue("CSV must end with \\n so JS reverse/slice works", csv.endsWith("\n"));

        String[] lines = csv.split("\n");
        assertTrue("CSV must have header + at least one data row", lines.length >= 2);

        // header: Resolution,<id1>,...,<idn>
        String[] header = lines[0].split(",");
        assertEquals("Header must have n+1 columns", numIntegrations + 1, header.length);
        assertEquals("First header column must be 'Resolution'", "Resolution", header[0]);

        // data rows: <res>,<v1>,0,...,<vn>,0
        int expectedDataCols = 2 * numIntegrations + 1;
        for (int i = 1; i < lines.length; i++) {
            String[] cols = lines[i].split(",", -1);
            assertEquals("Data row " + i + " must have " + expectedDataCols + " columns",
                    expectedDataCols, cols.length);
            // resolution must parse as a number
            assertDoesNotThrow("Resolution column must be numeric",
                    () -> Double.parseDouble(cols[0]));
        }
    }

    private void assertDoesNotThrow(String message, Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            fail(message + ": " + e.getMessage());
        }
    }

    // ── tests: each parser method ────────────────────────────────────────────

    @Test
    public void parseCompleteness_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57), sampleRow(2.77));
        String csv = parser.parseCompleteness();
        assertJsConsumable(csv, 1);
        assertTrue("Completeness value 99.8 must appear in CSV", csv.contains("99.8"));
    }

    @Test
    public void parseRfactor_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parseRfactor();
        assertJsConsumable(csv, 1);
        assertTrue("Rfactor value 5.4 must appear in CSV", csv.contains("5.4"));
    }

    @Test
    public void parseISigma_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parseISigma();
        assertJsConsumable(csv, 1);
        assertTrue("I/Sigma value 15.0 must appear in CSV", csv.contains("15.0"));
    }

    @Test
    public void parsecc2_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parsecc2();
        assertJsConsumable(csv, 1);
        assertTrue("CC2 value 99.3 must appear in CSV", csv.contains("99.3"));
    }

    @Test
    public void parseSigmaAno_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parseSigmaAno();
        assertJsConsumable(csv, 1);
        assertTrue("SigAno value 1.234 must appear in CSV", csv.contains("1.234"));
    }

    @Test
    public void parseAnomCorrection_withData_returnsJsConsumableCsv() {
        AutoProcessingDataParser parser = parserOf(sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parseAnomCorrection();
        assertJsConsumable(csv, 1);
        assertTrue("AnomalCorr value 78 must appear in CSV", csv.contains("78"));
    }

    // ── multi-integration test ────────────────────────────────────────────────

    @Test
    public void parseCompleteness_twoIntegrations_headerHasThreeColumns() {
        AutoProcessingData row1 = new AutoProcessingData("id1", 2.40, 99.8, 5.4, 15.0, 99.3, 1.234, 78, "id1", 11);
        AutoProcessingData row2 = new AutoProcessingData("id2", 2.40, 97.5, 6.1, 12.0, 98.1, 0.900, 70, "id2", 22);
        List<List<AutoProcessingData>> twoFiles = Arrays.asList(
                Collections.singletonList(row1),
                Collections.singletonList(row2));
        String csv = parserOf(twoFiles).parseCompleteness();
        assertJsConsumable(csv, 2);
    }

    // ── bug-regression test: empty data list ─────────────────────────────────

    /**
     * Documents the current bug: when findXScale() returns no attachments,
     * autoProcessingDataListList is empty and every parse method returns only
     * "Resolution\n".  The JS client receives this, gets zero data rows, and
     * never calls render() – resulting in blank plots.
     *
     * This test MUST PASS once the bug is fixed (i.e. the CSV has > 1 line).
     */
    @Test
    public void allParsers_emptyDataList_returnHeaderOnly_documentsBug() {
        AutoProcessingDataParser empty = new AutoProcessingDataParser(Collections.emptyList());

        for (String csv : new String[]{
                empty.parseCompleteness(), empty.parseRfactor(), empty.parseISigma(),
                empty.parsecc2(), empty.parseSigmaAno(), empty.parseAnomCorrection()}) {

            assertEquals("Empty data produces 'Resolution\\n' – no data rows for JS to render",
                    "Resolution\n", csv);
        }
    }

    // ── autoPROC / CORRECT.LP data ────────────────────────────────────────────

    /**
     * autoPROC embeds the CORRECT.LP table in its main log.  The table uses the
     * exact same column layout as XSCALE.LP but AnomalCorr (val[11]) can be a
     * NEGATIVE integer (e.g. -6).  Verify the parser handles this correctly.
     */
    @Test
    public void parseAnomCorrection_negativeAnomalCorr_isSerialisedCorrectly() {
        // Values taken directly from the autoPROC CORRECT.LP table in production log
        AutoProcessingData d1 = row(5.54, 99.8, 12.0, 21.08, 99.9, 0.795, -6);
        AutoProcessingData d2 = row(3.92, 100.0, 76.2,  5.11, 98.8, 0.753, -4);
        AutoProcessingData d3 = row(3.20,  99.9, 241.3, 1.70, 93.6, 0.641, -2);

        String csv = parserOf(d1, d2, d3).parseAnomCorrection();
        assertJsConsumable(csv, 1);
        assertTrue("Negative anomal corr -6 must appear in CSV", csv.contains("-6"));
        assertTrue("Negative anomal corr -4 must appear in CSV", csv.contains("-4"));
    }

    // ── resolution ordering ───────────────────────────────────────────────────

    @Test
    public void parseCompleteness_resolutionIsSortedAscending() {
        AutoProcessingDataParser parser = parserOf(sampleRow(3.00), sampleRow(2.40), sampleRow(2.57));
        String csv = parser.parseCompleteness();
        String[] lines = csv.split("\n");
        assertTrue("Must have at least 3 data rows", lines.length >= 4);
        double prev = Double.parseDouble(lines[1].split(",")[0]);
        for (int i = 2; i < lines.length; i++) {
            double current = Double.parseDouble(lines[i].split(",")[0]);
            assertTrue("Resolution values must be sorted ascending", current >= prev);
            prev = current;
        }
    }
}
