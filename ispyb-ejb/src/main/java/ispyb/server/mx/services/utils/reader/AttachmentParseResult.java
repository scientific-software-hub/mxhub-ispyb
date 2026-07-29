package ispyb.server.mx.services.utils.reader;

import java.util.List;

/**
 * Typed result of {@link AutoProcProgramaAttachmentFileReader#readAttachment}
 * — replaces the previous {@code HashMap<String, Object>} return value.
 *
 * <p>Fields are deliberately non-private: the {@code /xscale/plot} REST
 * endpoint serializes this object straight to JSON via the base-class
 * {@code sendResponse(Object)}, which uses Gson field reflection configured
 * with {@code excludeFieldsWithModifiers(Modifier.PRIVATE)} (see
 * {@code ParentWebService#newGson}). Keeping the fields protected (rather
 * than private, as {@link AutoProcessingData} already does) preserves the
 * existing {@code xscaleFile}/{@code truncateLog}/{@code noanomAimlessLog}/
 * {@code autoProcessingData} JSON keys the client already consumes.
 */
public class AttachmentParseResult {

	protected boolean xscaleFile;

	protected boolean truncateLog;

	protected boolean noanomAimlessLog;

	protected List<AutoProcessingData> autoProcessingData;

	AttachmentParseResult(boolean xscaleFile, boolean truncateLog, boolean noanomAimlessLog,
			List<AutoProcessingData> autoProcessingData) {
		this.xscaleFile = xscaleFile;
		this.truncateLog = truncateLog;
		this.noanomAimlessLog = noanomAimlessLog;
		this.autoProcessingData = autoProcessingData;
	}

	public boolean isXscaleFile() {
		return xscaleFile;
	}

	public boolean isTruncateLog() {
		return truncateLog;
	}

	public boolean isNoanomAimlessLog() {
		return noanomAimlessLog;
	}

	public List<AutoProcessingData> getAutoProcessingData() {
		return autoProcessingData;
	}
}
