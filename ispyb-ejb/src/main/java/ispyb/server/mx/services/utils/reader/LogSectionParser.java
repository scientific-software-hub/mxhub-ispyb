package ispyb.server.mx.services.utils.reader;

import java.util.List;

/**
 * Strategy for turning one autoprocessing log/attachment, fed line by line in
 * file order via {@link #accept(String)}, into the {@link AutoProcessingData}
 * rows it carries.
 */
interface LogSectionParser {

	void accept(String line);

	List<AutoProcessingData> results();
}
