package com.phongnghia.example_websocket.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class SendMailServiceImpl implements SendMailService{

    private final JavaMailSender m_javaMailSender;

    private final TemplateEngine m_templateEngine;

    @Autowired
    public SendMailServiceImpl (JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.m_javaMailSender = javaMailSender;
        this.m_templateEngine = templateEngine;
    }

    @Override
    public void sendMailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables) throws MessagingException {
        MimeMessage mimeMessage = m_javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariables(variables);

        String htmlContent = m_templateEngine.process(templateName, context);

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true);

        m_javaMailSender.send(mimeMessage);
    }
}
