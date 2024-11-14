package com.kodinghaejo.service;

public interface MailService {
	
	public void sendSimpleMailMessage(String email, String password);
	
	public void sendMimeMessage(String email, String password);

}