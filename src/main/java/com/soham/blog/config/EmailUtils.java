package com.soham.blog.config;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String from, String to, String subject, String body) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setText(body, body);
                mimeMessage.setSubject(subject);
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setText(body, body);
            }
        };

        try {
            javaMailSender.send(preparator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMailWithAttachment(String from, String to, String subject, String body, List<MultipartFile> files) {
        List<File> listFile = new ArrayList<File>();
        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                @Override
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    mimeMessage.setFrom(new InternetAddress(from));
                    mimeMessage.setText(body, "UTF-8");
                    mimeMessage.setSubject(subject);
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                    mimeMessageHelper.setText(body, true);
                    for (MultipartFile file : files) {
                        File convFile = new File(file.getOriginalFilename());
                        file.transferTo(convFile);
                        listFile.add(convFile);
                        mimeMessageHelper.addAttachment(convFile.getName(), convFile);
                    }
                }
            };
            javaMailSender.send(preparator);
            for (File file : listFile) {
                file.delete();
                file.exists();
            }
            listFile.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
        }
    }

    public void sendMailWithInlineResources(String from, String to, String subject, String body,
                                            List<MultipartFile> listMultipartFile) {
        List<File> listFile = new ArrayList<File>();

        try {
            MimeMessagePreparator preparator = mimeMessage -> {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setSubject(subject);

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setText(body, true);

                for (MultipartFile multipartFile : listMultipartFile) {
                    File file = convertMultiPartToFile(multipartFile);
                    helper.addInline(file.getName(), file);
                    listFile.add(file);
                }
            };

            javaMailSender.send(preparator);
        } catch (MailException ex) {
            ex.printStackTrace();
        } finally {
            // Cleanup files after sending email
            for (File file : listFile) {
                file.delete();
            }
            listFile.clear();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

}
