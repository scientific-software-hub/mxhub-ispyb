/*
 * TrackingEmail.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import ispyb.server.common.services.shipping.DewarAPIService;
import ispyb.server.common.vos.shipping.DewarAPIBean;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import bcr.client.security.util.LdapConnection;
import bcr.client.util.Formatter;
import bcr.client.util.SendMailUtils;
import bcr.common.util.Constants;
import bcr.common.util.Ejb3ServiceLocatorBCR;

public class TrackingEmail {

	private static Logger LOG = Logger.getLogger(TrackingEmail.class);

	/**
	 * 
	 */
	public TrackingEmail() {
	}

	/**
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @param location
	 * @return
	 */
	public static boolean sendArrivalEmailToLabContact(boolean inTest, String emailStores, String emailMxInd, String dewarBarCode,
			Timestamp dateTime, String location) throws Exception {

		LOG.debug("Dewar Tracking / sendArrivalEmailToLabContact for dewar barcode "+dewarBarCode);

		try {
			// Get dewar info
			// Create API
			DewarAPIService dewarAPIService = (DewarAPIService) Ejb3ServiceLocatorBCR.getInstance().getRemoteService(
					DewarAPIService.class);
			String emailTo = "";
			String emailCc = "";
			String emailBcc = Constants.getProperty("mail.dewarTracking.bcc");
			String emailReply = "";
			String emailSignature = "";
			
			DewarAPIBean dewarAPI = dewarAPIService.fetchDewar(dewarBarCode);

			if (dewarAPI != null) {

				// Get dewar values
				String parcelName = dewarAPI.getParcelName();
				String sendingLabContactEmail = dewarAPI.getSendingLabContactEmail();
				String returnLabContactEmail = dewarAPI.getReturnLabContactEmail();
				String shippingName = dewarAPI.getShippingName();
				String beamLineName = dewarAPI.getBeamLineName();
				String proposalTitle = dewarAPI.getProposalTitle();
				String proposalCode = dewarAPI.getProposalCode();
				String localContact = dewarAPI.getLocalContact();
				String proposalName = dewarAPI.getProposalCode() + dewarAPI.getProposalNumber();
				Date startDate = dewarAPI.getStartDate();
				String startDateStr = Formatter.formatDate(startDate);
				if (startDateStr == null)
					startDateStr = "unknown";

				// Get localContact email
				String emailLocalContact = "";
				if (localContact != null && !localContact.equals("")) {

					// Get first letter of firstName + *
					String firstNameLetter = "*";
					if (localContact.substring(localContact.length() - 2, localContact.length() - 1).equals(" "))
						firstNameLetter = localContact.substring(localContact.length() - 1, localContact.length()) + "*";

					String lastName = localContact;
					if (firstNameLetter.length() == 2) {
						// Get lastName without first letter of firstName in case there was a firstname letter
						lastName = localContact.substring(0, localContact.length() - 2);
						if (lastName.endsWith(" "))
							lastName = lastName.substring(0, lastName.length() - 1);
					}
					// TODO fix discrepancies between ldap and smis accounts
					// particular case of Sean MC SWEENEY
					if (lastName.equals("MCSWEENEY"))
						lastName = "MC SWEENEY";
					if (lastName.equals("MONACO"))
						lastName = "MALBET MONACO";
					if (lastName.equals("MCCARTHY"))
						lastName = "MC CARTHY";

					// Get local contact email
					String email = LdapConnection.getLocalContactEmail(lastName, firstNameLetter);
					if (email != null && !email.equals(""))
						emailLocalContact = email;
					LOG.debug("LocalContact email: " + lastName + "/" + firstNameLetter + " = " + emailLocalContact);
				} else {
					LOG.debug("Local contact is empty (email will not be sent to Local contact).");
				}

				// Customize email depending on proposal type
				if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
					// FX proposals
					emailTo = sendingLabContactEmail;
					emailCc = emailMxInd;
					if (emailLocalContact != null && !emailLocalContact.equals(""))
						emailCc += "," + emailLocalContact;
					emailReply = emailMxInd;
					emailSignature = "The MXpress team";
				} else {
					// Other proposals
					emailTo = sendingLabContactEmail;
					emailCc = "";
					if (emailLocalContact != null && !emailLocalContact.equals(""))
						emailCc = emailLocalContact;
					emailReply = emailStores;
					emailSignature = "The ESRF stores";
				}
				
				// Email addresses on testing
				if (inTest) {
					emailTo = Constants.getProperty("mail.dewarTracking.to.test");
					emailLocalContact = Constants.getProperty("mail.labContact.to.test");
					emailBcc = Constants.getProperty("mail.dewarTracking.bcc.test");
				}


				// Email subject
				String emailSubject = "ESRF - Parcel received - " + proposalName + " / " + startDateStr + " / " + shippingName + " / "
						+ parcelName;
				// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
				String formatedDateTime = dateFormat.format(dateTime);

				String emailBody = "<FONT face='Courier New' size=2>" + "Dear User,<BR><BR>" + "Your parcel <B>" + parcelName
						+ "</B> " + "(" + "Proposal: <B>" + proposalName + "</B>, " + "Session date: <B>" + startDateStr + "</B>, "
						+ "Shipment: <B>" + shippingName + "</B>, " + "Barcode: <B>" + dewarBarCode + "</B>" + ") "
						+ "has been received by the ESRF on " + formatedDateTime + " and will be dispatched to beamline <B>"
						+ beamLineName + "</B>." + "<BR>" + "<BR>You can check its location at anytime via <A title='"
						+ Constants.ISPYB_URL_HELP + "' href='" + Constants.ISPYB_URL + "'>" + "ISPyB" + "</A>." + "<BR>"
						+ "<BR>Don't hesitate to contact us at <A HREF='mailto:" + emailReply + "'>" + emailReply + "</A>."
						+ "<BR><BR>Best regards" + "<BR><BR>" + emailSignature + "</FONT>";

				// Send email
				try {
					SendMailUtils.sendMail(null, emailTo, emailCc, emailBcc, emailReply, emailSubject, emailBody, true);
				} catch (Exception e) {
					LOG.debug("Dewar Tracking / Error sending email to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc
							+ "' : Dewar '" + dewarBarCode + "' located at '" + location + "' ");
					e.printStackTrace();
					return false;
				}
				LOG.debug("Dewar Tracking / Email sent to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc + "' : Dewar '"
						+ dewarBarCode + "' located at '" + location + "' ");
			} else {
				LOG.debug("Dewar Tracking / Cannot find info for dewar barcode '" + dewarBarCode + "'");
				return false;
			}

		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Cannot find shipping info for dewar barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @param location
	 * @return
	 */
	public static boolean sendBeamlineEmailToLabContact(boolean inTest, String emailStores, String emailMxInd, String dewarBarCode,
			Timestamp dateTime, String location) {

		// LOG.debug("Dewar Tracking / sendArrivalEmailToLabContact for dewar barcode
		// '"+dewarBarCode+"'");

		try {
			// Get dewar info
			// Create API
			DewarAPIService dewarAPIService = (DewarAPIService) Ejb3ServiceLocatorBCR.getInstance().getRemoteService(
					DewarAPIService.class);
			String emailTo = "";
			String emailCc = "";
			String emailBcc = Constants.getProperty("mail.dewarTracking.bcc");
			String emailReply = "";
			String emailSignature = "";

			DewarAPIBean dewarAPI = dewarAPIService.fetchDewar(dewarBarCode);
			if (dewarAPI!= null) {

				// Get dewar values
				String parcelName = dewarAPI.getParcelName();
				String sendingLabContactEmail = dewarAPI.getSendingLabContactEmail();
				String returnLabContactEmail = dewarAPI.getReturnLabContactEmail();
				String shippingName = dewarAPI.getShippingName();
				String beamLineName = dewarAPI.getBeamLineName();
				String proposalTitle = dewarAPI.getProposalTitle();
				String proposalCode = dewarAPI.getProposalCode();
				String localContact = dewarAPI.getLocalContact();
				String proposalName = dewarAPI.getProposalCode() + dewarAPI.getProposalNumber();
				Date startDate = dewarAPI.getStartDate();
				String startDateStr = Formatter.formatDate(startDate);
				if (startDateStr == null)
					startDateStr = "unknown";


				// Customize email depending on proposal type
				if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
					// FX proposals
					emailTo = sendingLabContactEmail;
					emailCc = emailMxInd;
					emailReply = emailMxInd;
					emailSignature = "The MXpress team";
				} else {
					// Other proposals
					emailTo = sendingLabContactEmail;
					emailCc = "";
					emailReply = emailStores;
					emailSignature = "The ESRF stores";
				}
				
				// Email addresses on testing
				if (inTest) {
					emailTo =  Constants.getProperty("mail.labContact.to.test");
					emailBcc = Constants.getProperty("mail.dewarTracking.bcc.test");
					emailCc = "";
				}


				// Email subject
				String emailSubject = "ESRF - Parcel dispatched to beamline - " + proposalName + " / " + startDateStr + " / "
						+ shippingName + " / " + parcelName;
				// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
				String formatedDateTime = dateFormat.format(dateTime);

				String emailBody = "<FONT face='Courier New' size=2>" + "Dear User,<BR><BR>" + "Your parcel <B>" + parcelName
						+ "</B> " + "(" + "Proposal: <B>" + proposalName + "</B>, " + "Session date: <B>" + startDateStr + "</B>, "
						+ "Shipment: <B>" + shippingName + "</B>, " + "Barcode: <B>" + dewarBarCode + "</B>" + ") "
						+ "has been dispatched to beamline <B>" + location + "</B>." + "<BR>"
						+ "<BR>You can check its location at anytime via <A title='" + Constants.ISPYB_URL_HELP + "' href='"
						+ Constants.ISPYB_URL + "'>" + "ISPyB" + "</A>." + "<BR>"
						+ "<BR>Don't hesitate to contact us at <A HREF='mailto:" + emailReply + "'>" + emailReply + "</A>."
						+ "<BR><BR>Best regards" + "<BR><BR>" + emailSignature + "</FONT>";

				// Send email
				try {
					SendMailUtils.sendMail(null, emailTo, emailCc, emailBcc, emailReply, emailSubject, emailBody, true);
				} catch (Exception e) {
					LOG.debug("Dewar Tracking / Error sending email to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc
							+ "' : Dewar '" + dewarBarCode + "' located at '" + location + "' ");
					e.printStackTrace();
					return false;
				}
				LOG.debug("Dewar Tracking / Email sent to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc + "' : Dewar '"
						+ dewarBarCode + "' located at '" + location + "' ");
			} else {
				LOG.debug("Dewar Tracking / Cannot find info for dewar barcode '" + dewarBarCode + "'");
				return false;
			}

		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Cannot find shipping info for dewar barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @param location
	 * @return
	 */
	public static boolean sendReadyToBePickedEmailToLabContact(boolean inTest, String emailStores, String emailMxInd, String dewarBarCode,
			Timestamp dateTime, String location) throws Exception {

		// LOG.debug("Dewar Tracking / sendArrivalEmailToLabContact for dewar barcode
		// '"+dewarBarCode+"'");

		try {
			// Get dewar info
			// Create API
			DewarAPIService dewarAPIService = (DewarAPIService) Ejb3ServiceLocatorBCR.getInstance().getRemoteService(
					DewarAPIService.class);
			String emailTo = "";
			String emailCc = "";
			String emailBcc = Constants.getProperty("mail.dewarTracking.bcc");
			String emailReply = "";
			String emailSignature = "";
			
			DewarAPIBean dewarAPI = dewarAPIService.fetchDewar(dewarBarCode);

			if (dewarAPI != null) {

				// Get dewar values
				String parcelName = dewarAPI.getParcelName();
				String sendingLabContactEmail = dewarAPI.getSendingLabContactEmail();
				String returnLabContactEmail = dewarAPI.getReturnLabContactEmail();
				String shippingName = dewarAPI.getShippingName();
				String beamLineName = dewarAPI.getBeamLineName();
				String proposalTitle = dewarAPI.getProposalTitle();
				String proposalCode = dewarAPI.getProposalCode();
				String localContact = dewarAPI.getLocalContact();
				String proposalName = dewarAPI.getProposalCode() + dewarAPI.getProposalNumber();
				Date startDate = dewarAPI.getStartDate();
				String startDateStr = Formatter.formatDate(startDate);
				if (startDateStr == null)
					startDateStr = "unknown";

				// Get localContact email
				String emailLocalContact = "";
				if (localContact != null && !localContact.equals("")) {

					// Get first letter of firstName + *
					String firstNameLetter = "*";
					if (localContact.substring(localContact.length() - 2, localContact.length() - 1).equals(" "))
						firstNameLetter = localContact.substring(localContact.length() - 1, localContact.length()) + "*";

					String lastName = localContact;
					if (firstNameLetter.length() == 2) {
						// Get lastName without first letter of firstName in case there was a firstname letter
						lastName = localContact.substring(0, localContact.length() - 2);
						if (lastName.endsWith(" "))
							lastName = lastName.substring(0, lastName.length() - 1);
					}
					// TODO fix discrepancies between ldap and smis accounts
					// particular case of Sean MC SWEENEY
					if (lastName.equals("MCSWEENEY"))
						lastName = "MC SWEENEY";
					if (lastName.equals("MONACO"))
						lastName = "MALBET MONACO";
					if (lastName.equals("MCCARTHY"))
						lastName = "MC CARTHY";

					// Get local contact email
					String email = LdapConnection.getLocalContactEmail(lastName, firstNameLetter);
					if (email != null && !email.equals(""))
						emailLocalContact = email;
					LOG.debug("LocalContact email: " + lastName + "/" + firstNameLetter + " = " + emailLocalContact);
				} else {
					LOG.debug("Local contact is empty (email will not be sent to Local contact).");
				}

				// Customize email depending on proposal type
				if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
					// FX proposals
					emailTo = sendingLabContactEmail;
					emailCc = emailMxInd;
					if (emailLocalContact != null && !emailLocalContact.equals(""))
						emailCc += "," + emailLocalContact;
					emailReply = emailMxInd;
					emailSignature = "The MXpress team";
				} else {
					// Other proposals
					emailTo = sendingLabContactEmail;
					emailCc = "";
					if (emailLocalContact != null && !emailLocalContact.equals(""))
						emailCc = emailLocalContact;
					emailReply = emailStores;
					emailSignature = "The ESRF stores";
				}
				
				// Email addresses on testing
				if (inTest) {
					emailTo = Constants.getProperty("mail.dewarTracking.to.test");
					emailLocalContact = Constants.getProperty("mail.labContact.to.test");
					emailBcc = Constants.getProperty("mail.dewarTracking.bcc.test");
					emailReply = Constants.getProperty("mail.dewarTracking.to.test");
				}


				// Email subject
				String emailSubject = "ESRF - Parcel ready to be picked up - " + proposalName + " / " + startDateStr + " / " + shippingName + " / "
						+ parcelName;
				// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
				String formatedDateTime = dateFormat.format(dateTime);

				String emailBody = "<FONT face='Courier New' size=2>" + "Dear User,<BR><BR>" + "Your parcel <B>" + parcelName
						+ "</B> " + "(" + "Proposal: <B>" + proposalName + "</B>, " + "Session date: <B>" + startDateStr + "</B>, "
						+ "Shipment: <B>" + shippingName + "</B>, " + "Barcode: <B>" + dewarBarCode + "</B>" + ") "
						+ "has been sent back to the stores on " + formatedDateTime + " and is ready to be picked up by your courier company." + "<BR>" 
						+ "<BR>You can check its location at anytime via <A title='"
						+ Constants.ISPYB_URL_HELP + "' href='" + Constants.ISPYB_URL + "'>" + "ISPyB" + "</A>." + "<BR>"
						+ "<BR>Don't hesitate to contact us at <A HREF='mailto:" + emailReply + "'>" + emailReply + "</A>."
						+ "<BR><BR>Best regards" + "<BR><BR>" + emailSignature + "</FONT>";

				// Send email
				try {
					SendMailUtils.sendMail(null, emailTo, emailCc, emailBcc, emailReply, emailSubject, emailBody, true);
				} catch (Exception e) {
					LOG.debug("Dewar Tracking / Error sending email to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc
							+ ", reply-To '" + emailReply + "' : Dewar '" + dewarBarCode + "' located at '" + location + "' ");
					e.printStackTrace();
					return false;
				}
				LOG.debug("Dewar Tracking / Email sent to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc 
						+ ", reply-To '" + emailReply + "' : Dewar '"
						+ dewarBarCode + "' located at '" + location + "' ");
			} else {
				LOG.debug("Dewar Tracking / Cannot find info for dewar barcode '" + dewarBarCode + "'");
				return false;
			}

		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Cannot find shipping info for dewar barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @param courierName
	 * @param trackingNumber
	 * @return
	 */
	public static boolean sendShippingEmailToLabContact(boolean inTest, String emailStores, String emailMxInd, String dewarBarCode,
			Timestamp dateTime, String courierName, String trackingNumber) {

		// LOG.debug("Dewar Tracking / sendShippingEmailToLabContact for dewar barcode
		// '"+dewarBarCode+"'");
		try {
			// Get dewar info
			// Create API
			DewarAPIService dewarAPIService = (DewarAPIService) Ejb3ServiceLocatorBCR.getInstance().getRemoteService(
					DewarAPIService.class);
			String emailTo = "";
			String emailCc = "";
			String emailBcc = Constants.getProperty("mail.dewarTracking.bcc");
			String emailReply = "";
			String emailSignature = "";
			
			DewarAPIBean dewarAPI= dewarAPIService.fetchDewar(dewarBarCode);

			if (dewarAPI != null) {

				// Get dewar values
				String parcelName = dewarAPI.getParcelName();
				String sendingLabContactEmail = dewarAPI.getSendingLabContactEmail();
				String returnLabContactEmail = dewarAPI.getReturnLabContactEmail();
				String shippingName = dewarAPI.getShippingName();
				String proposalTitle = dewarAPI.getProposalTitle();
				String proposalCode = dewarAPI.getProposalCode();
				String proposalName = dewarAPI.getProposalCode() + dewarAPI.getProposalNumber();
				Date startDate = dewarAPI.getStartDate();
				String startDateStr = Formatter.formatDate(startDate);
				if (startDateStr == null)
					startDateStr = "unknown";

				// Customize email depending on proposal type
				if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
					// FX proposals
					emailTo = returnLabContactEmail;
					emailCc = emailMxInd;
					emailReply = emailStores;
					emailSignature = "The ESRF stores";
				} else {
					// Other proposals
					emailTo = returnLabContactEmail;
					emailCc = "";
					emailReply = emailStores;
					emailSignature = "The ESRF stores";
				}
				
				// Email addresses on testing
				if (inTest) {
					emailBcc = Constants.getProperty("mail.dewarTracking.bcc.test");
					emailTo =  Constants.getProperty("mail.labContact.to.test");
					emailCc = "";
				}


				// Format tracking url
				String courierLink = "";
				String trackingLink = "";
				String courierUrl = TrackingUtils.getCourierUrl(courierName);
				String courierUrlParameter = TrackingUtils.getCourierUrlParameter(courierName);
				if (!courierUrl.equals("") && !courierUrlParameter.equals("")) {
					courierLink = "<BR>If you want to track your parcel on " + courierName + " click " + "<A href=\"" + courierUrl
							+ courierUrlParameter + "=" + trackingNumber + "\">" + "here" + "</A>.<BR>";
				}
				if (!courierUrl.equals("") && !courierUrlParameter.equals("")) {
					trackingLink = "<A title='" + Constants.TRACKING_URL_HELP + "' " + "href=\"" + courierUrl + courierUrlParameter
							+ "=" + trackingNumber + "\">" + trackingNumber + "</A>";
				} else {
					trackingLink = trackingNumber;
				}

				// Format message
				String emailSubject = "ESRF - Parcel sent - " + proposalName + " / " + startDateStr + " / " + shippingName + " / "
						+ parcelName;

				// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
				String formatedDateTime = dateFormat.format(dateTime);
				String emailBody = "<FONT face='Courier New' size=2>" + "Dear User,<BR><BR>" + "Your parcel <B>" + parcelName
						+ "</B> " + "(" + "Proposal: <B>" + proposalName + "</B>, " + "Session date: <B>" + startDateStr + "</B>, "
						+ "Shipment: <B>" + shippingName + "</B>, " + "Barcode: <B>" + dewarBarCode + "</B>" + ") "
						+ "has left the ESRF and has been sent to your lab by <B>" + courierName + "</B> " + "(Tracking Number: <B>"
						+ trackingLink + "</B>) " + "on " + formatedDateTime + "."
						+ "<BR>"
						// + courierLink
						+ "<BR>Don't hesitate to contact us at <A HREF='mailto:" + emailReply + "'>" + emailReply + "</A> "
						+ "if you encounter problems with its transport." + "<BR><BR>Best regards" + "<BR><BR>" + emailSignature
						+ "</FONT>";

				// Send email
				try {
					SendMailUtils.sendMail(null, emailTo, emailCc, emailBcc, emailReply, emailSubject, emailBody, true);
				} catch (Exception e) {
					LOG.debug("Dewar Tracking / Error sending email to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc
							+ "' : Dewar '" + dewarBarCode + "' sent via '" + courierName + "' ('TN= " + trackingNumber + ")");
					e.printStackTrace();
					return false;
				}

				LOG.debug("Dewar Tracking / Email sent to '" + emailTo + "', cc '" + emailCc + ", bcc '" + emailBcc + "' : Dewar '"
						+ dewarBarCode + "' sent via '" + courierName + "' ('TN= '" + trackingNumber + "')");
			} else {
				LOG.debug("Dewar Tracking / Cannot find shipping info for dewar barcode '" + dewarBarCode + "'");
				return false;
			}

		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Cannot find shipping info for dewar barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param to
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @return
	 */
	public static boolean sendErrorCannotFind(String to, String cc, String dewarBarCode, Timestamp dateTime) {

		LOG.debug("Dewar Tracking / sendErrorCannotFind for dewar barcode '" + dewarBarCode + "'");

		String emailBcc = Constants.DEBUG_BCC_EMAIL;

		// Email subject
		String emailSubject = "ESRF/ISPyB - Unknown Dewar " + dewarBarCode;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
		String formatedDateTime = dateFormat.format(dateTime);

		String emailBody = "<FONT face='Courier New' size=2>" + "Dear ISPyB User,<BR><BR>" + "The parcel with barcode <B>"
				+ dewarBarCode + "</B> " + "localized at the ESRF on " + formatedDateTime + " "
				+ "is not known or has incomplete information in the ISPyB database.<BR>" + "<BR><BR>" + "ISPyB (automatic email)"
				+ "</FONT>";

		// Send email
		try {
			SendMailUtils.sendMail(null, to, cc, emailBcc, null, emailSubject, emailBody, true);
		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Error sending email to '" + to + "', cc '" + cc + "' : Dewar '" + dewarBarCode + "' ");
			e.printStackTrace();
			return false;
		}
		LOG.debug("Dewar Tracking / Email sent to '" + to + "', cc '" + cc + "' : Dewar '" + dewarBarCode + "' ");
		return true;

	}

	/**
	 * @param to
	 * @param cc
	 * @param dewarBarCode
	 * @param dateTime
	 * @return
	 */
	public static boolean sendErrorCannotEmail(String to, String cc, String dewarBarCode, Timestamp dateTime) {

		LOG.debug("Dewar Tracking / sendErrorCannotEmail for dewar barcode '" + dewarBarCode + "'");

		String emailBcc = Constants.DEBUG_BCC_EMAIL;

		// Email subject
		String emailSubject = "ESRF/ISPyB - Email error for dewar " + dewarBarCode;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' K:mm a '(GMT'Z')'");
		String formatedDateTime = dateFormat.format(dateTime);

		String emailBody = "<FONT face='Courier New' size=2>" + "Dear ISPyB User,<BR><BR>"
				+ "The ISPyB system was not able to send emails to the Lab Contact " + "for the parcel with barcode <B>"
				+ dewarBarCode + "</B> " + "localized at the ESRF on " + formatedDateTime
				+ " (imcomplete data in the ISPyB database or emailing problem).<BR>" + "<BR><BR>" + "ISPyB (automatic email)"
				+ "</FONT>";

		// Send email
		try {
			SendMailUtils.sendMail(null, to, cc, emailBcc, null, emailSubject, emailBody, true);
		} catch (Exception e) {
			LOG.debug("Dewar Tracking / Error sending email to '" + to + "', cc '" + cc + "' : Dewar '" + dewarBarCode + "' ");
			e.printStackTrace();
			return false;
		}
		LOG.debug("Dewar Tracking / Email sent to '" + to + "', cc '" + cc + "' : Dewar '" + dewarBarCode + "' ");
		return true;

	}
}
