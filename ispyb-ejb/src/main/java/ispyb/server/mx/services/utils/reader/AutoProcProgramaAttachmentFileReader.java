package ispyb.server.mx.services.utils.reader;

import ispyb.common.util.PathUtils;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AutoProcProgramaAttachmentFileReader {

	private static final Logger logger = Logger.getLogger(AutoProcProgramaAttachmentFileReader.class);

	public static List<AutoProcessingData> getAutoProcessingDataFromAttachemt(AutoProcProgramAttachment3VO attachment) throws Exception {
		return AutoProcProgramaAttachmentFileReader.readAttachment(attachment).getAutoProcessingData();
	}

	public static AttachmentParseResult readAttachment(AutoProcProgramAttachment3VO attachment) throws Exception {
		if (attachment == null) {
			return new AttachmentParseResult(false, false, false, List.of());
		}

		String fileName = attachment.getFileName();
		Optional<AttachmentLogType> type = AttachmentLogType.detect(fileName);
		if (type.isEmpty()) {
			return new AttachmentParseResult(false, false, false, List.of());
		}

		LogSectionParser parser = createParser(type.get(), attachment);
		String sourceFileName = PathUtils.FitPathToOS(attachment.getFilePath() + "/" + fileName);
		logger.debug("Reading autoprocessing attachment: " + sourceFileName);
		try (Scanner scanner = new Scanner(Path.of(sourceFileName), StandardCharsets.UTF_8)) {
			while (scanner.hasNextLine()) {
				parser.accept(scanner.nextLine());
			}
		}

		return new AttachmentParseResult(
				type.get() == AttachmentLogType.XSCALE,
				type.get() == AttachmentLogType.TRUNCATE,
				type.get() == AttachmentLogType.NOANOM_AIMLESS,
				parser.results());
	}

	private static LogSectionParser createParser(AttachmentLogType type, AutoProcProgramAttachment3VO attachment) {
		return switch (type) {
			case XSCALE -> new XScaleLpParser(attachment);
			case TRUNCATE -> new TruncateLogParser(attachment);
			case NOANOM_AIMLESS -> new NoanomAimlessLogParser(attachment);
			case AUTOPROC -> new AutoProcDetailedTableParser(attachment, wantStaraniso(attachment));
		};
	}

	// The same autoPROC.log is attached identically to both the "autoPROC" and
	// "autoPROC_staraniso" AutoProcProgram entries; this tells the two apart so
	// AutoProcDetailedTableParser can pick the matching per-shell statistics table.
	private static boolean wantStaraniso(AutoProcProgramAttachment3VO attachment) {
		return attachment.getAutoProcProgramVO() != null
				&& attachment.getAutoProcProgramVO().getProcessingPrograms() != null
				&& attachment.getAutoProcProgramVO().getProcessingPrograms().toLowerCase().contains("staraniso");
	}
}
