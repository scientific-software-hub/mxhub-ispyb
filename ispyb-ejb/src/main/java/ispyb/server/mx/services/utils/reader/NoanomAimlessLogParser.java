package ispyb.server.mx.services.utils.reader;

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the three fast_dp/AIMLESS "$TABLE:" sub-tables (R-factor+I/sigma,
 * completeness, CC(1/2)) from a noanom aimless.log attachment and assembles
 * them into per-resolution-shell rows.
 */
class NoanomAimlessLogParser implements LogSectionParser {

	private final Logger logger = Logger.getLogger(getClass());

	private final AutoProcProgramAttachment3VO attachment;

	private final List<Double> dmid = new ArrayList<>();
	private final List<Double> completeness = new ArrayList<>();
	private final List<Double> rfactor = new ArrayList<>();
	private final List<Double> isigma = new ArrayList<>();
	private final List<Double> cc2 = new ArrayList<>();

	private boolean readingRfactor = false;
	private boolean readingCompleteness = false;
	private boolean readingIsigma = false;
	private boolean readingCc2 = false;
	private boolean sectionClosed = false;

	NoanomAimlessLogParser(AutoProcProgramAttachment3VO attachment) {
		this.attachment = attachment;
	}

	@Override
	public void accept(String line) {
		if (line.contains("Analysis against resolution, XDSdataset")) {
			readingRfactor = true;
			readingCompleteness = false;
			readingIsigma = true;
			readingCc2 = false;
			sectionClosed = false;
		} else if (line.contains("Completeness & multiplicity v. resolution, XDSdataset")) {
			readingRfactor = false;
			readingCompleteness = true;
			readingIsigma = false;
			readingCc2 = false;
			sectionClosed = false;
		} else if (line.contains("Correlations CC(1/2) within dataset, XDSdataset")) {
			readingRfactor = false;
			readingCompleteness = false;
			readingIsigma = false;
			readingCc2 = true;
			sectionClosed = false;
		}

		boolean reading = readingRfactor || readingCompleteness || readingIsigma || readingCc2;
		if (reading && !sectionClosed && isDataLine(line)) {
			collectRow(line);
		}
		if (reading && line.contains("Overall")) {
			sectionClosed = true;
			readingRfactor = false;
			readingCompleteness = false;
			readingIsigma = false;
			readingCc2 = false;
		}
	}

	private static boolean isDataLine(String line) {
		return !line.contains("$$") && !line.isEmpty() && !line.contains("I/sigma")
				&& !line.contains("Filtered") && !line.contains("Mean") && !line.contains("Rmerge")
				&& !line.contains("Average") && !line.contains("Fractional")
				&& !line.contains("$GRAPHS:Completeness v Resolution")
				&& !line.contains(":Multiplicity v Resolution")
				&& !line.contains("$GRAPHS: CC(1/2) v resolution")
				&& !line.contains("RMS anomalous correlation ratio")
				&& !line.contains("Analysis against resolution, XDSdataset")
				&& !line.contains("Completeness & multiplicity v. resolution, XDSdataset")
				&& !line.contains("$TABLE:  Correlations CC(1/2) within dataset, XDSdataset:")
				&& !line.contains("Rsplit")
				&& !line.contains("Overall");
	}

	private void collectRow(String line) {
		String[] values = line.split(" ");
		String[] val = new String[20];
		int i = 0;
		for (String value : values) {
			if (i <= 19 && !value.isEmpty()) {
				val[i] = "-".equals(value) ? "0" : value;
				i++;
			}
		}

		if (readingRfactor || readingIsigma) {
			dmid.add(Double.parseDouble(val[2]));
			rfactor.add(100 * Double.parseDouble(val[6]));
			isigma.add(Double.parseDouble(val[13]));
		} else if (readingCc2) {
			cc2.add(100 * Double.parseDouble(val[6]));
		} else if (readingCompleteness) {
			completeness.add(Double.parseDouble(val[6]));
		}
	}

	@Override
	public List<AutoProcessingData> results() {
		List<AutoProcessingData> data = new ArrayList<>();
		try {
			int imax = 21;
			for (int i = 0; i < imax; i++) {
				data.add(new AutoProcessingData(attachment.getFileName(),
						dmid.get(i), completeness.get(i), rfactor.get(i),
						isigma.get(i), cc2.get(i), null,
						null, attachment.getFileName(), attachment.getAutoProcProgramVO().getAutoProcProgramId()));
			}
		} catch (Exception e) {
			logger.debug("fast_dp sub-tables yielded fewer than 21 shells: " + e.getMessage());
		}
		return data;
	}
}
