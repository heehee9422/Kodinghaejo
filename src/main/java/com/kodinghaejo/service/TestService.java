package com.kodinghaejo.service;

public interface TestService {
	
	String testCode(String language, String filePath);
	
	void createMainJavaFile();
	
	void createMainJsFile();
}