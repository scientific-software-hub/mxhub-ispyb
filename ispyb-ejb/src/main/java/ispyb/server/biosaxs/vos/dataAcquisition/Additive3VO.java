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

// Generated Jun 4, 2012 4:05:58 PM by Hibernate Tools 3.4.0.CR1

import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Additive3VO generated by hbm2java
 */
@Entity
@Table(name="Additive")
public class Additive3VO  implements java.io.Serializable {


	 protected Integer additiveId;
     protected String name;
     protected String additiveType;
     protected String chemFormulaHead;
     protected String chemFormulaTail;
     protected String comments;
     private Set<Bufferhasadditive3VO> bufferhasadditive3VOs = new HashSet<Bufferhasadditive3VO>(0);

    public Additive3VO() {
    }

    public Additive3VO(String name, String type, String comments, Set<Bufferhasadditive3VO> bufferhasadditive3VOs) {
       this.name = name;
       this.additiveType = type;
       this.comments = comments;
       this.bufferhasadditive3VOs = bufferhasadditive3VOs;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="additiveId", unique=true, nullable=false)
    public Integer getAdditiveId() {
        return this.additiveId;
    }
    
    public void setAdditiveId(Integer additiveId) {
        this.additiveId = additiveId;
    }

    
    @Column(name="name", length=45)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="additiveType", length=45)
    public String getType() {
        return this.additiveType;
    }
    
    public void setType(String type) {
        this.additiveType = type;
    }
    
    @Column(name="chemFormulaHead", length=25)
    public String getChemFormulaHead() {
        return this.chemFormulaHead;
    }
    
    public void setChemFormulaHead(String chemFormulaHead) {
        this.chemFormulaHead = chemFormulaHead;
    }
    
    @Column(name="chemFormulaTail", length=25)
    public String getChemFormulaTail() {
        return this.chemFormulaTail;
    }
    
    public void setChemFormulaTail(String chemFormulaTail) {
        this.chemFormulaTail = chemFormulaTail;
    }

    
    @Column(name="comments", length=512)
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    @XmlTransient
    @OneToMany(fetch=FetchType.EAGER, mappedBy="additive3VO", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    public Set<Bufferhasadditive3VO> getBufferhasadditive3VOs() {
        return this.bufferhasadditive3VOs;
    }
    
    public void setBufferhasadditive3VOs(Set<Bufferhasadditive3VO> bufferhasadditive3VOs) {
        this.bufferhasadditive3VOs = bufferhasadditive3VOs;
    }

}


