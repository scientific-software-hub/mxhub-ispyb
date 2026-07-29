package ispyb.server.mx.services.utils.reader;

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the CORRECT.LP-format "SUBSET OF INTENSITY DATA WITH SIGNAL/NOISE"
 * table from a standalone XSCALE.LP attachment (also produced by EDNA_proc
 * and XDSAPP under this same file naming convention).
 */
class XScaleLpParser implements LogSectionParser {

	private final AutoProcProgramAttachment3VO attachment;
	private final List<AutoProcessingData> data = new ArrayList<>();
	private boolean reading = false;

	XScaleLpParser(AutoProcProgramAttachment3VO attachment) {
		this.attachment = attachment;
	}

	@Override
	public void accept(String line) {
		if (line.contains("SUBSET OF INTENSITY DATA WITH SIGNAL/NOISE")) {
			reading = true;
		} else if (reading) {
			CorrectLpRow row = CorrectLpRow.parse(line);
			if (row != null) {
				data.add(toAutoProcessingData(row));
			}
		}
		if (line.contains("STATISTICS OF INPUT DATA SET")) {
			reading = false;
		}
	}

	private AutoProcessingData toAutoProcessingData(CorrectLpRow row) {
		return new AutoProcessingData(
				attachment.getAutoProcProgramAttachmentId().toString(),
				row.resolutionLimit, row.completeness, row.rFactorObserved,
				row.iSigma, row.cc2, row.sigAno, row.anomalCorr,
				attachment.getFileName(), attachment.getAutoProcProgramVOId());
	}

	@Override
	public List<AutoProcessingData> results() {
		return data;
	}
}
