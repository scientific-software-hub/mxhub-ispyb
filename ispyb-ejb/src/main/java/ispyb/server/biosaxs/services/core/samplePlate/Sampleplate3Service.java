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

package ispyb.server.biosaxs.services.core.samplePlate;


import java.util.List;

import ispyb.server.biosaxs.vos.dataAcquisition.plate.PlateGroup3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;

import jakarta.ejb.Remote;


@Remote
public interface Sampleplate3Service {

	public abstract Sampleplate3VO merge(Sampleplate3VO detachedInstance);

	public abstract Sampleplateposition3VO merge(Sampleplateposition3VO sampleplateposition3vo);
	
	public abstract List<Sampleplate3VO> getSamplePlatesByBoxId(int dewarId);
	
	public abstract PlateGroup3VO merge(PlateGroup3VO plateGroup);
	
}