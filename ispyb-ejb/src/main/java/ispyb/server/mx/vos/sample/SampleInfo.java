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
package ispyb.server.mx.vos.sample;

import java.io.Serializable;

/**
 * Sample info needed for mxCuBE to display the list of samples (on a beamline, with the 'processing' status)
 * @author BODIN
 *
 */
public class SampleInfo implements Serializable, Cloneable{

	private static final long serialVersionUID = -3203393739645665888L;
	
	// sample info, BLSample table
	public Integer sampleId;  //column blSampleId
	public String sampleName; //column name
	public String code;
	public Double holderLength;
	public String sampleLocation; //column location
	public String smiles; //column SMILES
	public Integer blDiffractionPlanId;

	// protein
	public String proteinAcronym; // column acronym
	
	// crystal
	public Integer crystalId;
	public String crystalSpaceGroup; //column spaceGroup
	public Double cellA; //column cell_a
	public Double cellB; //column cell_b
	public Double cellC; //column cell_c
	public Double cellAlpha; //column cell_alpha
	public Double cellBeta; //column cell_beta
	public Double cellGamma; //column cell_gamma
	public Integer crystalDiffractionPlanId;
	
	// diffractionPlan
	public Double minimalResolution;
	public String experimentType; //column experimentKind
	public DiffractionPlanWS3VO diffractionPlan;// TODO check fields
	
	// container
	public String containerSampleChangerLocation; //sampleChangerLocation
	public String containerCode;
	
	// imagepath
	public String blsampleImagePath;
	
	public SampleInfo() {
		super();
	}

	public SampleInfo(Integer sampleId, String sampleName, String code,
			Double holderLength, String sampleLocation, String smiles, String proteinAcronym, Integer crystalId,
			String crystalSpaceGroup, Double cellA, Double cellB, Double cellC, Double cellAlpha, 
			Double cellBeta, Double cellGamma, Double minimalResolution, String experimentType,
			String containerSampleChangerLocation, String containerCode, DiffractionPlanWS3VO diffractionPlan, String blsampleImagePath) {
		super();
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.code = code;
		this.holderLength = holderLength;
		this.sampleLocation = sampleLocation;
		this.containerCode = containerCode;
		this.smiles = smiles;
		this.proteinAcronym = proteinAcronym;
		this.crystalId=crystalId;
		this.crystalSpaceGroup = crystalSpaceGroup;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
		this.diffractionPlan = diffractionPlan ;
		this.minimalResolution = minimalResolution;
		this.experimentType = experimentType;
		this.containerSampleChangerLocation = containerSampleChangerLocation;
		this.blsampleImagePath = blsampleImagePath;
	}
}
