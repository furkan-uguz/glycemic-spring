package com.glycemic.core.mail;

import java.util.Map;

import jakarta.mail.MessagingException;

public interface IEmailSender {
	void sendSimpleMessage(String to, String subject, String text);

	void sendSimpleMessageWithTemplate(String to, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException;

}
