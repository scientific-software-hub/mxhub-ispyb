package ispyb.server.common.vos.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

@Entity
@Table(name = "SchemaStatus")
public class SchemaStatusVO extends ISPyBValueObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "schemaStatusId")
	protected Integer schemaStatusId;

	@Column(name = "scriptName")
	protected String scriptName;

	@Column(name = "schemaStatus")
	protected String schemaStatus;
	
	
	public Integer getSchemaStatusId() {
		return schemaStatusId;
	}

	public void setSchemaStatusId(Integer schemaStatusId) {
		this.schemaStatusId = schemaStatusId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getSchemaStatus() {
		return schemaStatus;
	}

	public void setSchemaStatus(String schemaStatus) {
		this.schemaStatus = schemaStatus;
	}



	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
