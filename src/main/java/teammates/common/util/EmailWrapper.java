package teammates.common.util;

import org.apache.http.HttpStatus;

import teammates.logic.api.EmailSender;

/**
 * Represents an email message and its important metadata.
 */
public class EmailWrapper {

    private String senderName;
    private String senderEmail;
    private String replyTo;
    private String recipient;
    private String bcc;
    private String subject;
    private String content;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfoForLogging() {
        return "[Email sent]to=" + getRecipient()
               + "|from=" + getSenderEmail()
               + "|subject=" + getSubject();
    }

	/**
	 * Sends the given {@code message} and generates a log report.
	 *
	 * @param emailSender TODO
	 * @return The HTTP status of the email request.
	 */
	public EmailSendingStatus sendEmail(EmailSender emailSender) {
	    if (emailSender.isTestingAccount(getRecipient())) {
	        return new EmailSendingStatus(HttpStatus.SC_OK, "Not sending email to test account");
	    }
	
	    EmailSendingStatus status;
	    try {
	        status = emailSender.service.sendEmail(this);
	    } catch (Exception e) {
	        status = new EmailSendingStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	    }
	    if (!status.isSuccess()) {
	        EmailSender.log.severe("Email failed to send: " + status.getMessage());
	    }
	
	    String emailLogInfo = String.join("|||", "TEAMMATESEMAILLOG",
	            getRecipient(), getSubject(), getContent(),
	            status.getMessage() == null ? "" : status.getMessage());
	    EmailSender.log.info(emailLogInfo);
	    return status;
	}

}
