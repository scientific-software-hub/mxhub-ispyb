package ispyb.ws.security;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.ext.Provider;

import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 * This SecurityInterceptor verify the access permissions for a user based on user name and method annotations
 * 
 * */
@Provider
public class SecurityInterceptor implements ContainerRequestFilter {
	private final static Logger logger = Logger.getLogger(SecurityInterceptor.class);

	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("Access denied for this resource").build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).entity("Nobody can access this resource").build();

	@Context
	private ResourceInfo info;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = info.getResourceMethod();

		if (method.isAnnotationPresent(PermitAll.class)) {
			logger.info("PermitAll " + method.getName() + " "+ method.getDeclaredAnnotations() + " " + method.getAnnotations() + " " + method.getParameterAnnotations());
			return;
		}

		/**  Access denied for all **/
		if (method.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(ACCESS_DENIED);
			return;
		}

		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
						
			String token = requestContext.getUriInfo().getPathParameters().get("token").get(0);

			Login3VO login = this.getLogin(token);
			
			if (login != null) {
				if (login.checkRoles(rolesSet)){
					if (login.isValid()) {
						if (login.isManager() || login.isLocalContact()) {
							/** TODO: ISPyB might want to check that local contact has the permission on this proposal **/
							return;
						}
						if (login.isUser() || login.isIndustrial()){
							/** special case to display the list of proposal, with no proposalname present in the url **/ 
							if (!requestContext.getUriInfo().getPathParameters().containsKey("proposal")){
								return;
							}
							else{
								String proposalname = requestContext.getUriInfo().getPathParameters().get("proposal").get(0);
								if (login.getAuthorized().toUpperCase().contains(proposalname.toUpperCase())) {
									return;
								} else {
									logger.info(String.format("Proposal %s not allowed for %s", proposalname, login.getUsername()));
									requestContext.abortWith(ACCESS_DENIED);
								}
							}
						}
					} else {
						logger.info("Token Expired");
						requestContext.abortWith(ACCESS_FORBIDDEN);
					}
				}
				else{
					logger.info("Roles not valid");
					requestContext.abortWith(ACCESS_FORBIDDEN);
				}
			} 
			requestContext.abortWith(ACCESS_FORBIDDEN);
		}

	}

	private Login3VO getLogin(String token) {
		try {
			Login3Service service = (Login3Service) Ejb3ServiceLocator.getInstance().getLocalService(Login3Service.class);
			return service.findByToken(token);
		} catch (NamingException e) {
			logger.error("", e);
			return null;
		}
	}

}