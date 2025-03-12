package ispyb.ws.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.ParentWebService;

import jakarta.ws.rs.Path;

public class RestWebService extends ParentWebService {
	protected long now;
		
	
	protected List<Map<String, Object>> getProposalsFromToken (String token) throws Exception {
		Login3VO login3VO = this.getLogin3Service().findByToken(token);
		List<Map<String, Object>> proposals = new ArrayList<Map<String,Object>>(); 
		
		if (login3VO != null){
			if (login3VO.isValid()){
				
				if (login3VO.isLocalContact() || login3VO.isManager()){
					proposals = this.getProposal3Service().findProposals();
				}
				else{
					proposals = this.getProposal3Service().findProposals(login3VO.getUsername());
				}				
			}
		}	else {		
			throw new Exception("Token is not valid");
		}
		return (proposals);
	}

	protected int getPersonIdFromToken(String token) throws Exception {
		Login3VO login3VO = this.getLogin3Service().findByToken(token);
		int personId =0;
		if (login3VO != null && login3VO.isValid()){
			Person3VO person = this.getPerson3Service().findByLogin(login3VO.getUsername());
			personId = person.getPersonId();
		} else {
			throw new Exception("Token is not valid");
		}
		return personId;
	}

	protected List<Session3VO> getSessionsFromToken (String token) throws Exception {
		Login3VO login3VO = this.getLogin3Service().findByToken(token);
		if (login3VO != null && login3VO.isValid()){
			return this.getSession3Service().findSessionsByLoginName(login3VO.getUsername());
		} else {
			throw new Exception("Token is not valid");
		}
	}
}