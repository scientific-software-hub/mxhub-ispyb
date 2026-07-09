/*******************************************************************************
 * This file is part of ISPyB.
 *
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package ispyb.common.util.export;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ispyb.common.util.export.dto.DataCollectionReportRow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verifies {@link DataCollectionReportCsvSerializer#writeCsv(java.io.Writer, java.util.stream.Stream)}
 * — the incremental write path used by the streamed CSV export — produces
 * byte-for-byte the same output as {@link DataCollectionReportCsvSerializer#toCsv(List)},
 * the eager path it replaces in the REST endpoint (see
 * {@code ispyb.ws.rest.mx.DataCollectionRestWebService#getDataCollectionsReportBySessionIdCSV}).
 */
public class DataCollectionReportCsvSerializerTest {

	private final DataCollectionReportCsvSerializer serializer = new DataCollectionReportCsvSerializer();

	private DataCollectionReportRow row(String sampleName) {
		return new DataCollectionReportRow(
				"HEWL", sampleName, "OSC", "no ice", "1.62", "P 41 21 2", "1.56", "MXCuBE comment");
	}

	@Test
	public void writeCsv_matchesToCsv_forMultipleRows() throws Exception {
		List<DataCollectionReportRow> rows = Arrays.asList(row("s1"), row("s2"), row("s3"));

		String expected = serializer.toCsv(rows);

		StringWriter streamed = new StringWriter();
		serializer.writeCsv(streamed, rows.stream());

		assertEquals(expected, streamed.toString());
	}

	@Test
	public void writeCsv_emptyStream_writesHeaderOnly() throws Exception {
		StringWriter streamed = new StringWriter();
		serializer.writeCsv(streamed, Collections.<DataCollectionReportRow>emptyList().stream());

		String[] lines = streamed.toString().split("\r\n");
		assertEquals(1, lines.length);
		assertEquals("Protein,Sample,Status,Diffraction Note,Observed Resolution,Recorded Resolution,"
				+ "Processed Space Group,Processed Resolution,Comment", lines[0]);
	}

	@Test
	public void writeCsv_leavesObservedResolutionColumnBlank() throws Exception {
		StringWriter streamed = new StringWriter();
		serializer.writeCsv(streamed, Arrays.asList(row("s1")).stream());

		String[] lines = streamed.toString().split("\r\n");
		assertEquals(2, lines.length);
		// Protein,Sample,Status,Diffraction Note,<Observed Resolution>,Recorded Resolution,...
		assertTrue("Observed Resolution column should be empty: " + lines[1],
				lines[1].startsWith("HEWL,s1,OSC,no ice,,1.62,"));
	}
}
