package ispyb.server.mx.services.utils.reader;

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the Wilson-plot and cumulative-intensity-distribution "$TABLE:"
 * sub-tables from a truncate.log attachment.
 */
class TruncateLogParser implements LogSectionParser {

	private final AutoProcProgramAttachment3VO attachment;
	private final List<AutoProcessingData> data = new ArrayList<>();
	private boolean readingWilson = false;
	private boolean readingCumulative = false;

	TruncateLogParser(AutoProcProgramAttachment3VO attachment) {
		this.attachment = attachment;
	}

	@Override
	public void accept(String line) {
		if (line.contains("$TABLE: Wilson Plot")) {
			readingWilson = true;
		} else if (readingWilson) {
			parseWilsonRow(line);
		} else if (line.contains("$TABLE: Cumulative intensity distribution")) {
			readingCumulative = true;
		} else if (readingCumulative) {
			parseCumulativeRow(line);
		}
		if (line.contains("<hr>")) {
			readingWilson = false;
		}
	}

	private void parseWilsonRow(String line) {
		try {
			String[] val = tokenize(line, 10);
			data.add(new AutoProcessingData(
					attachment.getFileName(),
					Double.parseDouble(val[5]), Double.parseDouble(val[7]),
					attachment.getFileName(), attachment.getAutoProcProgramVO().getAutoProcProgramId()));
		} catch (Exception e) {
			// not a data row
		}
	}

	private void parseCumulativeRow(String line) {
		try {
			String[] val = tokenize(line, 6);
			data.add(new AutoProcessingData(attachment.getFileName(),
					Double.parseDouble(val[0]), Double.parseDouble(val[1]), Double.parseDouble(val[2]),
					Double.parseDouble(val[3]), Double.parseDouble(val[4]), Double.parseDouble(val[5]),
					attachment.getFileName(), attachment.getAutoProcProgramVO().getAutoProcProgramId()));
		} catch (Exception e) {
			// not a data row
		}
	}

	private static String[] tokenize(String line, int size) {
		String[] values = line.split(" ");
		String[] val = new String[size];
		int i = 0;
		for (String value : values) {
			if (i <= 9 && !value.isEmpty()) {
				val[i] = value.endsWith(".") ? value.substring(0, value.length() - 1) : value;
				i++;
			}
		}
		return val;
	}

	@Override
	public List<AutoProcessingData> results() {
		return data;
	}
}
