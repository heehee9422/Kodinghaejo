package com.kodinghaejo.service;

public interface MailService {

	public void sendSimpleMailMessage(String type, String email, String value);

	public void sendMimeMessage(String type, String email, String value);

}