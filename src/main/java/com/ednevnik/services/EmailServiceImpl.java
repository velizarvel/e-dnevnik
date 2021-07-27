package com.ednevnik.services;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ednevnik.entities.NastavnikEntity;
import com.ednevnik.entities.OcenaEntity;
import com.ednevnik.entities.RoditeljEntity;
import com.ednevnik.models.EmailObject;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	private final String EMAIL = "velizarvel.proba@gmail.com";

	@Override
	public void sendSimpleEmail(EmailObject emailObject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(EMAIL);
		message.setTo(emailObject.getTo());
		message.setSubject(emailObject.getSubject());
		message.setText(emailObject.getText());

		emailSender.send(message);
	}

	@Override
	public void sendTemplateMessage(OcenaEntity ocena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
		helper.setFrom(EMAIL);

		helper.setSubject(
				"Podaci o oceni za ucenika: " + ocena.getUcenik().getIme() + " " + ocena.getUcenik().getPrezime());

		NastavnikEntity nastavnik = ocena.getUcenik().getOdeljenje().getPredmetNastavnikMapa().get(ocena.getPredmet());
		String text = "<table style=\"border-collapse: collapse; width: 100%;\" border=\"2\">\r\n" + "<tbody>\r\n"
				+ "<tr>\r\n" + "<td style=\"width: 17%;\"><strong>Ucenik</strong></td>\r\n"
				+ "<td style=\"width: 17%;\"><strong>Odeljenje</strong></td>\r\n"
				+ "<td style=\"width: 17%;\"><strong>Predmet</strong></td>\r\n"
				+ "<td style=\"width: 15%;\"><strong>Ocena</strong></td>\r\n"
				+ "<td style=\"width: 17%;\"><strong>Aktivnost</strong></td>\r\n"
				+ "<td style=\"width: 17%;\"><strong>Nastavnik</strong></td>\r\n" + "</tr>\r\n" + "<tr>\r\n"
				+ "<td style=\"width: 17%;\">" + ocena.getUcenik().getIme() + " " + ocena.getUcenik().getPrezime()
				+ "</td>\r\n" + "<td style=\"width: 17%;\">" + ocena.getUcenik().getOdeljenje().getRazredIOdeljenje()
				+ "</td>\r\n" + "<td style=\"width: 15%;\">" + ocena.getPredmet().getNazivPredmeta() + "</td>\r\n"
				+ "<td style=\"width: 17%;\">" + ocena.getVrednostOcene() + "</td>\r\n" + "<td style=\"width: 17%;\">"
				+ ocena.getAktivnost() + "</td>\r\n" + "<td style=\"width: 17%;\"> " + nastavnik.getIme() + " "
				+ nastavnik.getPrezime() + "</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>";

		helper.setText(text, true);

		for (RoditeljEntity roditelj : ocena.getUcenik().getRoditelji()) {
			helper.setTo(roditelj.getEmail());
			emailSender.send(mail);
		}
	}

	@Override
	public void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setFrom(EMAIL);
		helper.setTo(object.getTo());
		helper.setSubject(object.getSubject());
		helper.setText(object.getText(), false);
		FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
		helper.addAttachment(file.getFilename(), file);
		emailSender.send(mail);
	}

}
