package file;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.io.InputStream;

public class FileUploadForm {

	@Multipart("file")
	private InputStream inputStream;

	@Multipart("description")
	private String description;

	@Multipart("fileName")
	private String fileName;
	
	@Multipart("groupName")
	private String groupName;

	@Multipart("type")
	private String type;

	@Multipart("multiplicity")
	private String multiplicity;

	@Multipart("uniprotId")
	private String uniprotId;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getUniprotId() {
		return uniprotId;
	}

	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}

}


	



