package geb.com.intershop.inspired.tools.email

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase

import org.apache.commons.io.FileUtils

class EmailUtils
{

    private static final String mailShare = "\\\\127.0.0.1\\test-mails"
    
    /** Dummy constructor for security reasons. */
    private EmailUtils()
    {
    }

    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String SUBJECT = "Subject: ";
    private static final String DATE = "Date: ";
    private static final String TO = "To: ";
    private static final String FROM = "From: ";

    /** The waiting interval in seconds. */
    private static int waitingInterval = 5;

    /** The waiting time in seconds. */
    private static int waitingTime = 30;

    /**
     * Gets a unique email address.
     * 
     * @return the email address
     */
//    public static String getUniqueEmailAddress()
//    {
//        return getEmailAddress(String.valueOf(DateUtils.getRandomDate().getTime()));
//    }

    /**
     * Gets an email address with given prefix.
     * 
     * @param prefix
     *            the prefix
     * @return the email address
     */
    public static String getEmailAddress(String prefix)
    {
        if (null == prefix || prefix.isEmpty())
        {
//            UCMHTTPTestCase.fail("Prefix is empty.");
        }

        return prefix + "@test.intershop.de";
    }

    /**
     * Gets emails for array of matching ids.
     * 
     * @param uniqueID
     *            The ID which is part of the file name.
     * @return the latest email with the given unique id
     */
    public static Email getLatestEmailbyUniqueID(String uniqueID)
    {
//        getLogger().logInfoFiner("Checking for mail...");
        Email[] mails = EmailUtils.getEmailsbyID(uniqueID);
//        UCMHTTPTestCase.assertFalse("No mails received for '" + uniqueID
//                        + "', please check email server is configured correctly", mails.length == 0);
        return getLatestEmail(mails);
    }

    /**
     * Gets emails for the given id.
     * 
     * @param id
     *            The id which is part of the file name.
     * @return all emails with the given id
     */
    public static Email[] getEmailsbyID(String id)
    {
        return getEmailsByIDSubject(id, null);
    }

    /**
     * Gets email matching the given id.
     * 
     * @param id
     *            The ID which is part of the file name.
     * @return the email with the given unique id
     */
    public static Email getEmailByID(String id)
    {
        Email[] mails = getEmailsByIDSubject(id, null);

        if (mails == null || mails.length == 0)
        {
//            UCMHTTPTestCase.fail("No mail received for '" + id + "', please check email server is configured correctly");
        }

//        UCMHTTPTestCase.assertFalse("More than 1 mail found for '" + id + "'", mails.length > 1);

        return (Email)mails[0];
    }

    /**
     * Gets email matching id and subject part.
     * 
     * @param id
     *            The id which is part of the file name.
     * @return the email with the given unique id
     */
    public static Email getEmailByIDSubject(String id, String subjectPart)
    {
        Email[] mails = getEmailsByIDSubject(id, subjectPart);

        if (mails == null || mails.length == 0)
        {
//            UCMHTTPTestCase.fail("No mail received for '" + id + "' and subject part '" + subjectPart
//                            + "', please check email server is configured correctly");
        }

//        UCMHTTPTestCase.assertFalse("More than 1 mail found for '" + id + "' and subject part '" + subjectPart + "'",
//                        mails.length > 1);

        return (Email)mails[0];
    }

    /**
     * Gets emails for the given id and subject part.
     * 
     * @param id
     *            the id
     * @param subjectPart
     *            the subject part, null if subject must be ignored
     * @return all emails with the given id and subject
     */
    public static Email[] getEmailsByIDSubject(String id, String subjectPart)
    {
        // Open shared directory
        File directory = new File(mailShare);
        if (directory.exists())
        {
            for (int i = 0; i < waitingTime; i = i + waitingInterval)
            {
                // wait some seconds
//                getLogger().logInfoFiner(
//                                "Waiting another " + waitingInterval
//                                                + " seconds to ensure email was sent and received, already waited for "
//                                                + i + " seconds");
//                getLogger().logDebug("Looking for emails containing the id '" + id + "'");

                try
                {
                    Thread.sleep(waitingInterval * 1000L);
                }
                catch(InterruptedException exception)
                {
//                    UCMHTTPTestCase.fail(exception.getMessage());
                }

                // read files of directory to find matching mails
                List<Email> mailList = new ArrayList<Email>();
                String[] files = directory.list();

                // loop on file list
                for (String file : files)
                {
                    boolean matchfound = true;

                    // loop on given id
                    if (file.indexOf(id) < 0)
                    {
                        matchfound = false;
                    }

                    if (matchfound)
                    {
                        // found matching mail -> restore contents
                        Email email = restoreEmailFromFile(new File(directory, file));
                        if (subjectPart == null)
                        {
                            mailList.add(email);
                        }
                        else if (email.subject.contains(subjectPart))
                        {
                            mailList.add(email);
                        }
                    }
                }

                if (!mailList.isEmpty())
                {
//                    getLogger().logInfoFiner(mailList.size() + " email(s) found for id '" + id + "'");
                    // return as mail array
                    return (Email[])mailList.toArray(new Email[mailList.size()]);
                }
            }
        }
        else
        {
//            getLogger().logDebug("Directory '" + directory.getAbsolutePath() + "' does not exist.");
        }
        return new Email[0];
    }

    /**
     * Helper reading file system email.
     * 
     * @param file
     *            the file
     * @return an email wrapper
     */
    private static Email restoreEmailFromFile(File file)
    {
        BufferedReader reader = null;
        String sender = null;
        String recipient = null;
        String subject = null;
        StringBuilder content = new StringBuilder((int)file.length());
        String timeStamp = null;
        String separator = System.getProperty("line.separator");
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        try
        {
            reader = new BufferedReader(new FileReader(file));
            boolean contentStarted = false;

            // read file by line and assign fields
            String line = reader.readLine();
            while(line != null)
            {
                line = line.replace(separator + "..", separator + ".");
                if (contentStarted)
                {
                    content.append(line).append(separator);
                }
                else
                {
                    if (line.startsWith(FROM))
                    {
                        sender = line.substring(FROM.length());
                    }
                    else if (line.startsWith(TO))
                    {
                        recipient = line.substring(TO.length());
                    }
                    else if (line.startsWith(DATE))
                    {
                        //timeStamp = line.substring(DATE.length());
                        timeStamp = attr.creationTime();
                    }
                    else if (line.startsWith(SUBJECT))
                    {
                        subject = line.substring(SUBJECT.length());
                    }
                    else if (line.startsWith(CONTENT_DISPOSITION) || line.startsWith(CONTENT_TRANSFER_ENCODING))
                    {
                        contentStarted = true;
                    }
                }
                line = reader.readLine();
            }

            MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),
                            new ByteArrayInputStream(FileUtils.readFileToByteArray(file)));

            // create restored email
            return new Email(sender, recipient, subject, content.toString(), timeStamp, message);
        }
        catch(IOException | MessagingException exception)
        {
//            UCMHTTPTestCase.fail(exception.getMessage());
        }
        finally
        {
            // try to close stream
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch(IOException exception)
            {
//                UCMHTTPTestCase.fail(exception.getMessage());
            }
        }

        return null;
    }

    /**
     * Delete the given email from file system.
     * 
     * @param email
     *            the email
     */
    public void deleteEmailFromFS(Email email)
    {
        File dir = new File(mailShare);
        if (dir.exists())
        {
            String[] files = dir.list();
            for (String file : files)
            {
                String fn = file;
                if (fn.indexOf(email.timestamp) > 0 && fn.indexOf(email.recipient) > 0 && fn.indexOf(email.sender) > 0
                                && fn.indexOf(email.subject) > 0)
                {
                    new File(dir, file).delete();
                }
            }
        }
    }

    /**
     * Sets the waiting time in seconds.
     * 
     * @param seconds
     *            the seconds
     */
    public static void setWaitingTime(int seconds)
    {
        waitingTime = seconds;
    }

    /**
     * parses new password from content string
     * 
     * @param startString
     *            the start string
     * @param endString
     *            the end string
     * @param content
     *            the content
     * @return the parsed password string.
     */
    public static String parseStringFromEmailContent(String startString, String endString, String content)
    {
        TestCase.assertNotNull("Content is null", content);

        if (startString == null || !content.contains(startString))
        {
            TestCase.fail("'" + startString + "' not found in '" + content + "'");
        }

        int start = content.indexOf(startString) + startString.length();

        if (endString == null || !content.contains(endString))
        {
            TestCase.fail("'" + endString + "' not found in '" + content + "'");
        }

        int end = content.indexOf(endString, start);

        return content.substring(start, end);
    }

    /**
     * Gets the password change URL from the email with the given email address
     * and subject.
     * 
     * @param emailAddress
     *            the email address
     * @param emailSubject
     *            the email subject
     * @return the URL
     */
    public static String getPasswordChangeUrlFromEmail(String emailAddress, String emailSubject)
    {
        // check email
        Email reminderMail = getEmailByIDSubject(emailAddress, emailSubject);

        // parse mail and try to link for login with new password.
        return getUrlByPipelineFromEmail("ViewForgotLoginData-NewPassword", reminderMail.content);
    }

    /**
     * Parses the email content for a link containing the provided pipeline and returns the contained url
     * 
     * @param pipeline
     *            the pipeline name - start node name string
     * @param emailContent
     *            the content of the email message
     * @return the url from the link
     */
    public static String getUrlByPipelineFromEmail(String pipeline, String emailContent)
    {
        TestCase.assertNotNull("Email content argument is required", emailContent);
        TestCase.assertNotNull("Email content argument is required", pipeline);

        int pipelineStart = emailContent.indexOf(pipeline) + pipeline.length();
        if (pipelineStart < 0)
        {
            TestCase.fail("Pipeline '" + pipeline + "' not found in '" + emailContent + '\'');
        }

        String contentBeforePipeline = emailContent.substring(0, pipelineStart);
        
        int linkStart = contentBeforePipeline.lastIndexOf("href=\"http") + "href=\"".length();
        if (linkStart < 0)
        {
            TestCase.fail("No link with pipeline '" + pipeline + "' found in '" + emailContent + '\'');
        }

        int linkEnd = emailContent.indexOf('"', pipelineStart + pipeline.length());

        String url = emailContent.substring(linkStart, linkEnd);
        return url;
    }

    /**
     * Gets the latest email out of the given emails.
     * 
     * @param mails
     *            the emails
     * @return the latest email
     */
    private static Email getLatestEmail(Email... mails)
    {
        Email latestEmail = null;

        for (Email mail : mails)
        {
            if (latestEmail == null)
            {
                latestEmail = mail;
            }
            else
            {
                if (latestEmail.timestamp.compareTo(mail.timestamp) < 0)
                {
                    latestEmail = mail;
                }
            }
        }

        return latestEmail;
    }
    
//    private static TestCaseLogger getLogger()
//    {
//        TestCaseLogger logger;
//
//        Logger currentLogger = Logger.getLogger("com.intershop.tools.etest.logging.TestCaseLogger");
//        if (currentLogger instanceof TestCaseLogger)
//        {
//            logger = (TestCaseLogger)currentLogger;
//        }
//        else
//        {
//            logger = TestCaseConsoleLogger.getLogger();
//        }
//
//        return logger;
//    }
}
