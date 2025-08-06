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
 ******************************************************************************/
package ispyb.ws.rest.notification;

/**
 * Class containing all the tool methods used by the other classes
 * 
 */


import java.util.Properties;

import ispyb.common.util.Constants;
import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.services.proposals.Person3Service;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

@Path("/")
@ApplicationScoped
public class SendMailUtils {
	private final Logger logger = Logger.getLogger(SendMailUtils.class);

	private final String fromEmail = Constants.getProperty("mail.from");
	private final String toEmailFallback = Constants.getProperty("mail.to");
	private final Properties smtpConfig;
	{
		// SMTP Server Configuration
		String host = Constants.getProperty("mail.smtp.host");  // SMTP server
		String port = Constants.getProperty("mail.smtp.port");
		String sslEnabled = Constants.getProperty("mail.smtp.ssl.enable");

		// Set up properties for the SMTP connection
		smtpConfig = new Properties();//=System.getProperties();
		smtpConfig.put("mail.smtp.host", host);
		smtpConfig.put("mail.smtp.port", port);
		smtpConfig.put("mail.smtp.ssl.enable", sslEnabled);
		smtpConfig.put("mail.smtp.auth", "false");
		//TODO if ssl enabled
	}

	@Inject
	private Login3Service login3Service;

	@Inject
	private Person3Service person3Service;

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("/{token}/send")
	@Produces({ "application/json" })
	public Response send(@PathParam("token") String token,
										@FormParam("recipientEmail") String recipientEmail,
										@FormParam("subject") String subject,
										@FormParam("msgBody") String body) throws Exception {
		var login = login3Service.findByToken(token);
		var person = person3Service.findByLogin(login.getUsername());

		// Create a session with no authentication
		Session session = Session.getInstance(smtpConfig);
		session.setDebug(true);
		try {
			// Create the email message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			if(isValidEmail(recipientEmail))
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			else
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailFallback));
			if(isValidEmail(person.getEmailAddress()))
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(person.getEmailAddress()));
			message.setSubject(subject);
			message.setText(body);

			// Send the email
			Transport.send(message);
			logger.info("SendMailUtils: Notification email sent successfully!");
			return Response.ok().build();
		} catch (MessagingException e) {
			logger.error("SendMailUtils: Failed to send email.", e);
			return Response.serverError().build();
		}
	}

	/**
	 * Parse e-mail addresses
	 * 
	 * @param emailAddress
	 *            the e-mail address to be parsed
	 * @return true if the e-mail address is valid, false otherwise
	 */
	final public boolean isValidEmail(String emailAddress) {
		if (emailAddress == null || emailAddress.length() < 3  || emailAddress.indexOf("@") < 0 || emailAddress.indexOf(".") < 0)
			return false;
		try {
			InternetAddress iAddr = new InternetAddress(emailAddress);
			iAddr.validate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
