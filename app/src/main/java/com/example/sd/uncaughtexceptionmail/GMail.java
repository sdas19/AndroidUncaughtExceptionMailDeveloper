package com.example.sd.uncaughtexceptionmail;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMail {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    @SuppressWarnings("rawtypes")
    String toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;
    private static String TAG=GMail.class.getSimpleName();
    String filename;

    public GMail() {

    }

    @SuppressWarnings("rawtypes")
    public GMail(String fromEmail, String fromPassword,
                 String toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));

        Log.i("GMail","toEmail: "+toEmailList);
        emailMessage.addRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmailList));


        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.e("GMail", "Email sent successfully.");

    }

    public void addAttachment()
    {
        try{
            /*MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Hii");
            //3) create MimeBodyPart object and set your message content
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("How is This");
            //4) create new MimeBodyPart object and set DataHandler object to this object */
            MimeBodyPart attachement = new MimeBodyPart();
            //Location of file to be attached


            String path = Environment.getExternalStorageDirectory().toString()+"/MyAppLog";
            Log.e(TAG, "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.e(TAG, "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                filename = files[i].getName();
                Log.e(TAG, "FileName:" + files[i].getName());
            }
            filename=path+"/Log.txt";

            DataSource source = new FileDataSource(filename);
            attachement.setDataHandler(new DataHandler(source));
            attachement.setFileName("Current App Crash Log"+String.valueOf(new Date()));
            //5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachement);
            //6) set the multiplart object to the message object
            emailMessage.setContent(multipart);
            //7) send message
            /*Transport.send(message);
            System.out.println("MESSAGE SENT....");*/
        }catch (MessagingException ex)
        {
            ex.printStackTrace();
        }
    }

}