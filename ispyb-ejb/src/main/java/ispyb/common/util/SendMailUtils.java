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
package ispyb.common.util;

/**
 * Class containing all the tool methods used by the other classes
 * 
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class SendMailUtils {
	
	private final static String MAIL_SERVER = Constants.getProperty("mail.smtp.host");	

	/**
	 * Send an e-mail using the mail.jar API. The byteArrayOutputStream will be written to a temp file 
	 * before being sent. Hence the IOException in the signature.
	 * 
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @param attachName     name of the attachment
	 * @param attachedFileData
	 * @param mimeType
	 *            (pdf will be "application/pdf"; GIF will be "image/gif"
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 * @throws IOException
	 *             if the temp file cannot be created
	 */
	final static public void sendMail(String from, String to, String cc, String bcc, String subject, String body,
			String attachName, ByteArrayOutputStream baos, String mimeType, boolean isHtml) throws MessagingException, IOException {
			
		File fileTemp = java.io.File.createTempFile("IspybUtilsMailTemp", attachName);
		FileOutputStream outPut = new FileOutputStream(fileTemp);
    	baos.writeTo(outPut);       		
		
		if (baos!=null ) {		
			sendMail(from, to, cc, bcc, subject, body, attachName, fileTemp, mimeType, isHtml);
		} else {
			sendMail(from, to, cc, bcc, subject, body, isHtml);
		}
		
	}	
	/**
	 * Send an e-mail using the mail.jar API
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @param attachName     name of the attachment
	 * @param File
	 * @param mimeType. pdf will be "application/pdf"; GIF will be "image/gif".
	 * 					if null then "application/octet-stream" will be set. 
	 * @throws MessagingException		if the mail cannot be sent
	 */

	final static public void sendMail(String from, String to, String cc, String bcc, String subject, String body,
			String attachName, File attachedFile, String mimeType) throws MessagingException  {
		privateSendMail(from, to, cc, bcc, subject, body, attachName, attachedFile, mimeType, false);
	}
	
	final static public void sendMail(String from, String to, String cc, String bcc, String subject, String body,
			String attachName, File attachedFile, String mimeType, boolean isHtml) throws MessagingException  {
		privateSendMail(from, to, cc, bcc, subject, body, attachName, attachedFile, mimeType, isHtml);
	}


	/**
	 * Send an e-mail using the mail.jar API
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @param isHtml
	 *            true, if the mail text is html formatted
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendMail(String from, String to, String cc, String bcc, String subject, String body,
			boolean isHtml) throws MessagingException {
		if (from == null)
			from = "ispyb@esrf.fr";

		if (subject == null)
			subject = "Automatic e-mail from ISPyB application";

		if (body == null)
			body = "EMPTY MESSAGE";

		Properties props = null;
		Session session = null;
		Message emailMessage = null;
		props = System.getProperties();
		props.put("mail.smtp.host", 	MAIL_SERVER);

		session = Session.getDefaultInstance(props, null);
		session.setDebug(false);

		emailMessage = new MimeMessage(session);
		emailMessage.setFrom(new InternetAddress(from));
		emailMessage.setSubject(subject);
		emailMessage.setHeader("X-Mailer", "MIS Software");
		emailMessage.setSentDate(new java.util.Date());

		// Set the "to" recipients

		emailMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		if (cc != null)
			emailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));

		if (bcc != null)
			emailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));

		// Actually send the e-mail
		if (isHtml) {
				emailMessage.setContent(body, "text/html");
		} else {
				emailMessage.setText(body);
		}
		Transport.send(emailMessage);
		
	}

	/**
	 * Send an e-mail using the mail.jar API
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendMail(String from, String to, String cc, String bcc, String subject, String body)
			throws MessagingException {
		sendMail(from, to, cc, bcc, subject, body, false);
	}

	/**
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendMail(String from, String to, String cc, String subject, String body)
			throws Exception {
		sendMail(from, to, cc, null, subject, body);
	}

	/**
	 * Send an e-mail using the mail.jar API
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param subject   the subject of the message
	 * @param body      the message body
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendMail(String from, String to, String subject, String body) throws MessagingException {
		sendMail(from, to, null, null, subject, body);
	}

	/**
	 * Send an e-mail containing an html document as text using the mail.jar API
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param cc 		the e-mail addresses of the copy recipients
	 * @param bcc    	the e-mail addresses of the blind copy recipients
	 * @param subject   the subject of the message
	 * @param htmlText
	 *            the message body, must be an html document
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendHtmlMail(String from, String to, String cc, String bcc, String subject,
			String htmlText) throws MessagingException {
		sendMail(from, to, cc, bcc, subject, htmlText, true);
	}

	/**
	 * Send an e-mail containing an html document as text using the mail.jar API
	 * 
	 * @param from
	 *            the e-mail address of the the sender
	 * @param to
	 *            the e-mail addresses of the recipients
	 * @param cc
	 *            the e-mail addresses of the copy recipients
	 * @param subject
	 *            the subject of the message
	 * @param htmlText
	 *            the message body, must be an html document
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendHtmlMail(String from, String to, String cc, String subject, String htmlText)
			throws MessagingException {
		sendMail(from, to, cc, null, subject, htmlText, true);
	}

	/**
	 * Send an e-mail containing an html document as text using the mail.jar API
	 * 
	 * @param from   	the e-mail address of the the sender
	 * @param to		the e-mail addresses of the recipients
	 * @param subject   the subject of the message
	 * @param htmlText
	 *            the message body, must be an html document
	 * @throws MessagingException
	 *             if the mail cannot be sent
	 */
	final static public void sendHtmlMail(String from, String to, String subject, String htmlText)
			throws MessagingException {
		sendMail(from, to, null, null, subject, htmlText, true);
	}

	/**
	 * Parse e-mail addresses
	 * 
	 * @param emailAddress
	 *            the e-mail address to be parsed
	 * @return true if the e-mail address is valid, false otherwise
	 */
	final public static boolean isValidEmail(String emailAddress) {
		if (emailAddress == null || emailAddress.length() < 3  || emailAddress.indexOf("@") < 0 || emailAddress.indexOf(".") < 0)
			return false;

		boolean isValid = false;
		try {
			InternetAddress iAddr = new InternetAddress(emailAddress);
			isValid = true;

		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param body
	 * @param attachName
	 * @param attachedFile
	 * @param mimeType
	 * @param isHtml
	 * @throws MessagingException
	 * @throws AddressException
	 */
	private static void privateSendMail(String from, String to, String cc, String bcc, String subject, String body, String attachName, File attachedFile, String mimeType, boolean isHtml) throws MessagingException, AddressException {
		if (from == null)
			from = "ispyb@esrf.fr";

		if (subject == null)
			subject = "Automatic e-mail from ISPyB application";

		if (body == null)
			body = "EMPTY MESSAGE";
		
		Properties props = null;
		Session session = null;
		Message emailMessage = null;
		props = System.getProperties();
		props.put("mail.smtp.host", 	MAIL_SERVER);

		session = Session.getDefaultInstance(props, null);
		session.setDebug(false);

		emailMessage = new MimeMessage(session);
		emailMessage.setFrom(new InternetAddress(from));
		emailMessage.setSubject(subject);
		emailMessage.setHeader("X-Mailer", "ISPyB Software");
		emailMessage.setSentDate(new java.util.Date());					
		if (to != null) emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		if (cc != null) emailMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
		if (bcc != null) emailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));

		//	Create the body message part
        BodyPart messageBodyPart = new MimeBodyPart();
		if (isHtml) messageBodyPart.setContent(body, "text/html");
		else messageBodyPart.setText(body);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        
        DataSource source = new FileDataSource(attachedFile);
        messageBodyPart.setDataHandler(new DataHandler(source));
        
        messageBodyPart.setFileName(attachName);
        multipart.addBodyPart(messageBodyPart);

        emailMessage.setContent(multipart);
        
		try {
				Transport.send(emailMessage);
		}
		catch (MessagingException e) {
				System.out.println(e);
				e.printStackTrace();
				throw e;

		} finally {
			
		}
	}
	
}
