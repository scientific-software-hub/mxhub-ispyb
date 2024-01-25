/*************************************************************************************************
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
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ****************************************************************************************************/

package ispyb.server.mx.vos.autoproc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Phasing3 value object mapping table Phasing
 * 
 */
@Entity
@Table(name="Phasing")
@SqlResultSetMapping(name = "phasingNativeQuery", entities = { @EntityResult(entityClass = Phasing3VO.class) })
public class Phasing3VO extends ISPyBValueObject implements Cloneable {
	
	private final static Logger LOG = Logger.getLogger(Phasing3VO.class);
	
	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;
	
	@Id
	@GeneratedValue
	@Column(name="phasingId")	
	protected Integer phasingId;
	
	@ManyToOne
	@JoinColumn(name = "phasingAnalysisId")
	protected PhasingAnalysis3VO phasingAnalysisVO;
	
	@ManyToOne
	@JoinColumn(name = "phasingProgramRunId")
	private PhasingProgramRun3VO phasingProgramRunVO;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "spaceGroupId")
	protected SpaceGroup3VO spaceGroupVO;
	
	@Column(name = "method")
	protected String method;
	
	@Column(name = "solventContent")
	protected Double solventContent;
	
	@Column(name = "enantiomorph")
	protected Boolean enantiomorph;
	
	@Column(name = "lowRes")
	protected Double lowRes;
	
	@Column(name = "highRes")
	protected Double highRes;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	public Phasing3VO() {
		super();
	}
	
	public Phasing3VO(Integer phasingId, PhasingAnalysis3VO phasingAnalysisVO,
			PhasingProgramRun3VO phasingProgramRunVO,
			SpaceGroup3VO spaceGroupVO, String method, Double solventContent,
			Boolean enantiomorph, Double lowRes, Double highRes,
			Date recordTimeStamp) {
		super();
		this.phasingId = phasingId;
		this.phasingAnalysisVO = phasingAnalysisVO;
		this.phasingProgramRunVO = phasingProgramRunVO;
		this.spaceGroupVO = spaceGroupVO;
		this.method = method;
		this.solventContent = solventContent;
		this.enantiomorph = enantiomorph;
		this.lowRes = lowRes;
		this.highRes = highRes;
		this.recordTimeStamp = recordTimeStamp;
	}


	public Integer getPhasingId() {
		return phasingId;
	}

	public void setPhasingId(Integer phasingId) {
		this.phasingId = phasingId;
	}

	public PhasingAnalysis3VO getPhasingAnalysisVO() {
		return phasingAnalysisVO;
	}

	public void setPhasingAnalysisVO(PhasingAnalysis3VO phasingAnalysisVO) {
		this.phasingAnalysisVO = phasingAnalysisVO;
	}

	public PhasingProgramRun3VO getPhasingProgramRunVO() {
		return phasingProgramRunVO;
	}

	public void setPhasingProgramRunVO(PhasingProgramRun3VO phasingProgramRunVO) {
		this.phasingProgramRunVO = phasingProgramRunVO;
	}

	public SpaceGroup3VO getSpaceGroupVO() {
		return spaceGroupVO;
	}

	public void setSpaceGroupVO(SpaceGroup3VO spaceGroupVO) {
		this.spaceGroupVO = spaceGroupVO;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Double getSolventContent() {
		return solventContent;
	}

	public void setSolventContent(Double solventContent) {
		this.solventContent = solventContent;
	}

	public Boolean getEnantiomorph() {
		return enantiomorph;
	}

	public void setEnantiomorph(Boolean enantiomorph) {
		this.enantiomorph = enantiomorph;
	}

	public Double getLowRes() {
		return lowRes;
	}

	public void setLowRes(Double lowRes) {
		this.lowRes = lowRes;
	}

	public Double getHighRes() {
		return highRes;
	}

	public void setHighRes(Double highRes) {
		this.highRes = highRes;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		String[] listMethod = {"solvent flattening", "solvent flipping"};
		//phasingProgram
		if(phasingProgramRunVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("Phasing", "phasingProgramRunVO"));
		//phasingAnalysis
		if(phasingAnalysisVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("Phasing", "phasingAnalysisVO"));
		// method
		if(!StringUtils.isStringInPredefinedList(this.method, listMethod, true))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("Phasing", "method", listMethod));
		
	}
	
	public void  fillVOFromWS(PhasingWS3VO vo) {
		this.phasingId = vo.getPhasingId();
		this.phasingAnalysisVO = null;
		this.phasingProgramRunVO = null;
		this.spaceGroupVO = null;
		this.method = vo.getMethod();
		this.solventContent = vo.getSolventContent();
		this.enantiomorph = vo.getEnantiomorph();
		this.lowRes = vo.getLowRes();
		this.highRes = vo.getHighRes();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	
	@Override
	public Phasing3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Phasing3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "phasingId="+this.phasingId +", "+
		"method="+this.method+", "+
		"solventContent="+this.solventContent+", "+
		"enantiomorph="+this.enantiomorph+", "+
		"lowRes="+this.lowRes+", "+
		"highRes="+this.highRes+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}
