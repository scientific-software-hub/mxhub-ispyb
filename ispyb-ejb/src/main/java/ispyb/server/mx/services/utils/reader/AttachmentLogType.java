package ispyb.server.mx.services.utils.reader;

import java.util.Optional;

/**
 * The kinds of autoprocessing attachment file this reader understands,
 * detected from the attachment's file name. Checked in priority order
 * (XSCALE before AUTOPROC before TRUNCATE before NOANOM_AIMLESS) to match the
 * original detection order in case a name could satisfy more than one rule.
 */
enum AttachmentLogType {
	XSCALE,
	AUTOPROC,
	TRUNCATE,
	NOANOM_AIMLESS;

	static Optional<AttachmentLogType> detect(String fileName) {
		if (fileName == null) {
			return Optional.empty();
		}
		String lower = fileName.toLowerCase();
		if (lower.endsWith("xscale.lp")) {
			return Optional.of(XSCALE);
		}
		if (lower.endsWith(".log") && lower.contains("autoproc")) {
			return Optional.of(AUTOPROC);
		}
		if (lower.endsWith(".log") && lower.contains("truncate")) {
			return Optional.of(TRUNCATE);
		}
		if (lower.endsWith(".log") && lower.contains("aimless")) {
			return Optional.of(NOANOM_AIMLESS);
		}
		return Optional.empty();
	}
}
