package ispyb.ws.rest.security;

import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.rest.security.login.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class AuthenticationRestWebService extends RestWebService {
	private final static Logger logger = Logger.getLogger(AuthenticationRestWebService.class);
	
	private String generateRamdomUUID() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update( UUID.randomUUID().toString().getBytes("UTF-8"));
		return byteToHex(crypt.digest());
	}
	
	@Override
	protected long logInit(String methodName, Logger logger, Object... args) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		ArrayList<String> params = new ArrayList<String>();
		for (Object object : args) {
			if (object != null){
				params.add(object.toString());
			}
			else{
				params.add("null");
			}
		}
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_API_LOGIN, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), this.newGson().toJson(params));
		return this.now;
	}
	
	@PermitAll
	@POST
	@Path("/authenticate")
	@Produces({ "application/json" })
	public Response authenticate(
			@FormParam("login") String login, 
			@FormParam("password") String password,
			@QueryParam("site") String site) throws Exception {
		String methodName = "authenticate";
		long id = this.logInit(methodName, logger, login, site);
		
		/** siteId is need in some cases to get the sessions when, for instance, user is local contact **/
		String siteId = "";
		try {
			List<String> roles = new ArrayList<String>();
			if (!password.isEmpty()){
				if (site != null){
					switch (site) {
					case "DESY":
						roles = DESYLoginModule.authenticate(login, password);
						break;
					case "DESY_LOCAL":
						roles = PropertyLoginModule.authenticate(login, password);
						break;
					default:
						throw new Exception("Site is not defined");
					}
				}
			}
			else{
				throw new Exception("Empty passwords are not allowed");
			}

			if (roles.size() > 0){
				String token = generateRamdomUUID();
				
				HashMap<String, Object> cookie = new HashMap<String, Object>();
				cookie.put("token", token);
				cookie.put("roles", roles);
				
				/** Creating the login token on database **/
				Login3VO login3vo = new Login3VO();
				login3vo.setToken(token);
				login3vo.setUsername(login);
				login3vo.setRoles(roles.toString());
				login3vo.setSiteId(siteId);
				/** Retrieving the proposals attached to a User **/
				Set<String> proposalsAuthorized = new HashSet<String>();
				Set<Session3VO> ss = getSession3Service().findSessionsByLoginName(login);
				for (Session3VO s :ss){
					proposalsAuthorized.add(s.getProposalCodeAndId());
				}
				login3vo.setAuthorized(proposalsAuthorized.toString());
							
				/** Calculating expiration time **/
				Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
				calendar.add(Calendar.HOUR, 3);
				Date expirationTime = calendar.getTime();
				
				login3vo.setExpirationTime(expirationTime);

				/** Saving login on database **/
				this.getLogin3Service().persist(login3vo);
				return sendResponse(cookie);
			}
			else{
				throw new Exception("User is not allowed");
			}
		} catch (Exception e) {
			this.logError(methodName, e, id, logger, LoggerFormatter.Package.ISPyB_API_LOGIN_ERROR);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
