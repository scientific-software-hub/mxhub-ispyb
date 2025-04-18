/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.vos.dataAcquisition;

// Generated May 25, 2012 9:27:44 AM by Hibernate Tools 3.4.0.CR1

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;



/**
 * Bufferhasadditive3VO generated by hbm2java
 */
@Entity
@Table(name = "BufferHasAdditive")
public class Bufferhasadditive3VO implements java.io.Serializable {

	private Buffer3VO buffer3VO;
	protected Integer bufferHasAdditiveId;
	protected Additive3VO additive3VO;
	protected Measurementunit3VO measurementunit3VO;
	protected String quantity;
	
	

	public Bufferhasadditive3VO() {
	}

	public Bufferhasadditive3VO(int bufferHasAdditiveId) {
		this.bufferHasAdditiveId = bufferHasAdditiveId;
	}

	public Bufferhasadditive3VO(int bufferHasAdditiveId,
			Additive3VO additive3VO, Measurementunit3VO measurementunit3VO,
			Buffer3VO buffer3VO, String quantity) {
		this.bufferHasAdditiveId = bufferHasAdditiveId;
		this.additive3VO = additive3VO;
		this.measurementunit3VO = measurementunit3VO;
		this.buffer3VO = buffer3VO;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bufferHasAdditiveId", unique = true, nullable = false)
	public Integer getBufferHasAdditiveId() {
		return this.bufferHasAdditiveId;
	}

	public void setBufferHasAdditiveId(Integer bufferHasAdditiveId) {
		this.bufferHasAdditiveId = bufferHasAdditiveId;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name = "additiveId")
	public Additive3VO getAdditive3VO() {
		return this.additive3VO;
	}

	public void setAdditive3VO(Additive3VO additive3VO) {
		this.additive3VO = additive3VO;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "measurementUnitId")
	public Measurementunit3VO getMeasurementunit3VO() {
		return this.measurementunit3VO;
	}

	public void setMeasurementunit3VO(Measurementunit3VO measurementunit3VO) {
		this.measurementunit3VO = measurementunit3VO;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bufferId")
	public Buffer3VO getBuffer3VO() {
		return this.buffer3VO;
	}

	public void setBuffer3VO(Buffer3VO buffer3VO) {
		this.buffer3VO = buffer3VO;
	}

	@Column(name = "quantity", length = 45)
	public String getQuantity() {
		return this.quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
