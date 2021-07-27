package com.ednevnik.services;

import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.models.EmailObject;

public interface EmailService {

	public void sendSimpleEmail(EmailObject emailObject);

	void sendTemplateMessage(OcenaEntity ocena) throws Exception;

	void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception;

}
