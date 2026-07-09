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

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Stream;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import ispyb.common.util.export.dto.DataCollectionReportRow;

/**
 * Serializes {@link DataCollectionReportRow} rows into CSV, using the
 * super-csv library already declared for {@code ispyb-rest}
 * (see {@code ispyb.ws.rest.mx.MXStatsRestWebService.parseListToCSV}).
 * <p>
 * Owns the column order/headers requested by the client and the empty
 * "Observed Resolution" column, which is filled in manually after export and
 * therefore has no place on the DTO.
 * <p>
 * {@link #writeCsv(Writer, Stream)} writes incrementally row-by-row so a
 * caller can stream a large export straight to an HTTP response without
 * materializing the whole document; {@link #toCsv(List)} is a convenience
 * wrapper over it for callers (and tests) that want the full CSV as a
 * {@code String}.
 */
public class DataCollectionReportCsvSerializer {

	private static final String[] HEADERS = {
			"Protein",
			"Sample",
			"Status",
			"Diffraction Note",
			"Observed Resolution",
			"Recorded Resolution",
			"Processed Space Group",
			"Processed Resolution",
			"Comment"
	};

	public String toCsv(List<DataCollectionReportRow> rows) throws IOException {
		StringWriter output = new StringWriter();
		writeCsv(output, rows.stream());
		return output.toString();
	}

	/**
	 * Writes the header and one CSV row per element of {@code rows} directly
	 * to {@code out}, flushing as it goes so the caller can stream bytes to
	 * an HTTP response as they're produced rather than buffering the full
	 * CSV in memory first. Does not close {@code out} — lifecycle belongs to
	 * the caller (e.g. the {@code StreamingOutput} wrapping the response
	 * stream).
	 */
	public void writeCsv(Writer out, Stream<DataCollectionReportRow> rows) throws IOException {
		ICsvListWriter csvWriter = new CsvListWriter(out, CsvPreference.STANDARD_PREFERENCE);
		try {
			csvWriter.writeHeader(HEADERS);
			final ICsvListWriter writer = csvWriter;
			rows.forEach(row -> writeRow(writer, row));
			csvWriter.flush();
		} catch (UncheckedIOException e) {
			throw e.getCause();
		}
		// Deliberately not closing csvWriter/out here: super-csv's
		// CsvListWriter#close would close the underlying Writer too, but
		// that Writer may wrap the live HTTP response OutputStream, whose
		// lifecycle is owned by the StreamingOutput caller.
	}

	/**
	 * {@link ICsvListWriter#write} throws the checked {@link IOException},
	 * which can't propagate through {@link List#forEach}; wrap it in
	 * {@link UncheckedIOException} and unwrap it in {@link #toCsv}.
	 */
	private void writeRow(ICsvListWriter csvWriter, DataCollectionReportRow row)  {
		try {
			csvWriter.write(
					row.proteinAcronym(),
					row.sampleName(),
					row.status(),
					row.diffractionNote(),
					"", // Observed Resolution: filled in manually by the client
					row.recordedResolution(),
					row.processedSpaceGroup(),
					row.processedResolution(),
					row.comment());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
