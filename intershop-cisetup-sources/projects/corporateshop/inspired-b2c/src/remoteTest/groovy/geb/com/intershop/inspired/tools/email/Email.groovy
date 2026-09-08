package geb.com.intershop.inspired.tools.email

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart;

import junit.framework.TestCase;

class Email
{

    /** The timestamp of the email. */
    public String timestamp;
    /** The sender of the email. */
    public String sender;
    /** The recipient of the email. */
    public String recipient;
    /** The subject of the email. */
    public String subject;
    /** The raw content of the email. */
    public String rawcontent;

    /** The message of the email. */
    public MimeMessage message;
    /** The content of the email. */
    public String content;

    /**
     * Only constructor for email.
     * 
     * @param sender
     *            the sender
     * @param recipient
     *            the recipient
     * @param subject
     *            the subject
     * @param rawcontent
     *            the raw content
     * @param timestamp
     *            time stamp
     * @param message
     *            the message
     */
    public Email(String sender, String recipient, String subject, String rawcontent, String timestamp,
                    MimeMessage message)
    {
        this.sender = sender;
        this.recipient = recipient;
        this.rawcontent = rawcontent;
        this.timestamp = timestamp;
        this.subject = subject;
        this.message = message;
        this.content = getMessageText();
    }

    @Override
    public String toString()
    {
        String emailAsString = null;

        try
        {
            StringBuilder buffer = new StringBuilder();
            buffer.append("Sender='");
            buffer.append(sender);
            buffer.append("', Recipient=");
            buffer.append(recipient);
            buffer.append("', Subject='");
            buffer.append(subject);
            buffer.append("', TimeStamp='");
            buffer.append(timestamp);
            buffer.append("', ContentType='");
            buffer.append(message.getContentType());
            buffer.append("'");

            emailAsString = buffer.toString();
        }
        catch(MessagingException exception)
        {
            TestCase.fail(exception.getMessage());
        }

        return emailAsString;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     * @return true if object is equal to current object, otherwise false
     */
    @Override
    public boolean equals(Object obj)
    {
        boolean isEmailEqual = false;

        if (obj instanceof Email)
        {
            isEmailEqual = this.toString().equals(((Email)obj).toString());
        }

        return isEmailEqual;
    }

    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    /**
     * Get the message text.
     * 
     * @return the message text
     */
    private String getMessageText()
    {
        String messageText = null;
        try
        {
            Object messageContent = message.getContent();

            if (messageContent instanceof String)
            {
                messageText = (String)messageContent;
            }
            else if (messageContent instanceof MimeMultipart)
            {
                MimeMultipart multipart = (MimeMultipart)messageContent;
                messageText = getMimeTypePart(multipart, "text/xhtml");
                if (messageText == null)
                {
                    messageText = getMimeTypePart(multipart, "text/html");
                }
                if (messageText == null)
                {
                    messageText = getMimeTypePart(multipart, "text/plain");
                }
                if (messageText == null)
                {
                    messageText = getMimeTypePart(multipart, "multipart/mixed");
                }
                if (messageText == null)
                {
                    List<String> types = new ArrayList<String>();
                    for (int t = 0; t < multipart.getCount(); t++)
                    {
                        BodyPart part = multipart.getBodyPart(t);
                        types.add(part.getContentType());
                    }
                    TestCase.fail("No text part found in message with content type 'text/xhtml', 'text/html' or 'text/plain'. Got only "
                                    + types);
                }
            }
            else
            {
                TestCase.fail("Unsupported message content type '" + messageContent.getClass().getName() + "'.");
            }
        }
        catch(IOException | MessagingException exception)
        {
            TestCase.fail(exception.getMessage());
        }
        return messageText;
    }

    /**
     * Get the mime type part out of the given mime multi part and mime type.
     * 
     * @param multipart
     *            the mime multi part
     * @param mimeType
     *            the mime type
     * @return the mime type part
     */
    private String getMimeTypePart(MimeMultipart multipart, String mimeType)
    {
        String mimeTypePart = null;

        try
        {
            for (int t = 0; t < multipart.getCount(); t++)
            {
                BodyPart part = multipart.getBodyPart(t);
                if (part.getContentType().startsWith(mimeType))
                {
                    mimeTypePart = String.valueOf(part.getContent());
                    break;
                }
            }
        }
        catch(IOException | MessagingException exception)
        {
            TestCase.fail(exception.getMessage());
        }
        return mimeTypePart;
    }

    /**
     * Get the attachments of the email.
     * 
     * @return the attachment list
     */
    public List<BodyPart> getAttachments()
    {
        List<BodyPart> bodyParts = new ArrayList<BodyPart>();
        try
        {
            if (message.getContent() instanceof MimeMultipart)
            {
                MimeMultipart multipart = (MimeMultipart)message.getContent();
                for (int t = 0; t < multipart.getCount(); t++)
                {
                    BodyPart part = multipart.getBodyPart(t);
                    if (part.getFileName() != null)
                    {
                        bodyParts.add(part);
                    }
                }
            }
        }
        catch(IOException | MessagingException exception)
        {
            TestCase.fail(exception.getMessage());
        }
        return bodyParts;
    }

}
