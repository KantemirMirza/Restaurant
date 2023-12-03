package com.kani.restaurant.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailUtil {
    private JavaMailSender javaMailSender;
    public void sendSimpleMailMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kani@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list != null && list.size() > 0){
            message.setCc(getCcArray(list));
        }
        javaMailSender.send(message);
    }
    private String [] getCcArray(List<String> ccList){
        String []cc = new String[ccList.size()];
        for(int i = 0; i < ccList.size(); i ++ ){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void forgotPasswordEmail(String to, String subject, String password)throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("kani@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
       String htmlMessage = "<p><b>Your Login Details For Cafe Management System</b><br><b>E-Mail: </b></p>"
               + to + "<p><b>Password: </b></p>" + password + "<br><a href=\"http://localhost:4200/\">Click Here To Login</a></p>";
       mimeMessage.setContent(htmlMessage, "text/html");
       javaMailSender.send(mimeMessage);
    }
}
