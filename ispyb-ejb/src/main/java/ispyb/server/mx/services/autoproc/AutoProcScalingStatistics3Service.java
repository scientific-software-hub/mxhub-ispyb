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

import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;

import java.util.List;

import jakarta.ejb.Remote;

@Remote
public interface AutoProcScalingStatistics3Service {

	/**
	 * Create new AutoProcScalingStatistics3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public AutoProcScalingStatistics3VO create(final AutoProcScalingStatistics3VO vo) throws Exception;

	/**
	 * Update the AutoProcScalingStatistics3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public AutoProcScalingStatistics3VO update(final AutoProcScalingStatistics3VO vo) throws Exception;

	/**
	 * Remove the AutoProcScalingStatistics3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the AutoProcScalingStatistics3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AutoProcScalingStatistics3VO vo) throws Exception;

	/**
	 * Finds a AutoProcScalingStatistics3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public AutoProcScalingStatistics3VO findByPk(final Integer pk)
			throws Exception;

	/**
	 * Find all AutoProcScalingStatistics3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProcScalingStatistics3VO> findAll()
			throws Exception;

	public List<AutoProcScalingStatistics3VO> findByAutoProcId(final Integer autoProcId, final String string)
			throws Exception;

	public AutoProcScalingStatistics3VO getBestAutoProcScalingStatistic(List<AutoProcScalingStatistics3VO> values);

}
