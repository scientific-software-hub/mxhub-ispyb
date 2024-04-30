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

package ispyb.server.mx.services.collections;

import ispyb.server.mx.vos.collections.Image3VO;

import java.util.List;

import jakarta.ejb.Remote;

@Remote
public interface Image3Service {

	/**
	 * Create new Image3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public Image3VO create(final Image3VO vo) throws Exception;

	/**
	 * Update the Image3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public Image3VO update(final Image3VO vo) throws Exception;

	/**
	 * Remove the Image3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Image3.
	 * @param vo the entity to remove.
	 */
	public void delete(final Image3VO vo) throws Exception;

	/**
	 * Finds a Image3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public Image3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all Image3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Image3VO> findAll()
			throws Exception;
	
	/**
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<Image3VO> findFiltered(final String fileLocation, final String fileName) throws Exception;
	
	
	public List<Image3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception ;
	
	public List<Image3VO> findByDataCollectionGroupId(final Integer dataCollectionGroupId) throws Exception ;
	
	/**
	 * two variables to guarantee the user fecths only its own images
	 * @param imageId
	 * @param proposalId
	 * @return
	 */
	public List<Image3VO> findByImageIdAndProposalId (final Integer imageId, final Integer proposalId)throws Exception;

}