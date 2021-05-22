package com.mojave.service.mailsender;

import java.util.List;
import java.util.Map;

public interface MailSender {

    void sendMessageWithTemplate(List<String> recipientEmails, String subject, Map<String, Object> model);

    void sendMessageWithTemplate(String recipient, String subject, Map<String, Object> model);
}
