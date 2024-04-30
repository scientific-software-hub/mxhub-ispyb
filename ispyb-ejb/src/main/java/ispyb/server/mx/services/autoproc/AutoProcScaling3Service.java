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

package ispyb.server.mx.services.autoproc;

import java.util.List;

import ispyb.server.mx.vos.autoproc.AutoProcScaling3VO;

import jakarta.ejb.Remote;

@Remote
public interface AutoProcScaling3Service {

	/**
	 * Create new AutoProcScaling3.
	 * @param vo the entity to persist
	 * @return the persisted entity
	 */
	public AutoProcScaling3VO create(final AutoProcScaling3VO vo) throws Exception;

	/**
	 * Update the AutoProcScaling3 data.
	 * @param vo the entity data to update
	 * @return the updated entity
	 */
	public AutoProcScaling3VO update(final AutoProcScaling3VO vo) throws Exception;

	/**
	 * Remove the AutoProcScaling3 from its pk.
	 * @param vo the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AutoProcScaling3.
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcScaling3VO vo) throws Exception;

	/**
	 * Finds a AutoProcScaling3 entity by its primary key and set linked value objects if necessary.
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AutoProcScaling3VO findByPk(final Integer pk) throws Exception;

	/**
	 * Find all AutoProcScaling3 and set linked value objects if necessary.
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProcScaling3VO> findAll()
			throws Exception;

}