package com.kodinghaejo.service;

<<<<<<< Updated upstream
public interface TestService {
	
	String testCode(String language, String filePath);
	
	void createMainJavaFile();
	
	void createMainJsFile();
=======
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestLngEntity;

public interface TestService {
	
	//코딩테스트 문제 가져오기
	public TestDTO loadTest(Long idx) throws Exception;
	
	//문제에서 선택 가능한 언어 확인
	public String lngAvlChk(Long idx, String lng) throws Exception;

	//코딩테스트 언어별 문제 가져오기
	public TestLngEntity loadTestLng(Long testIdx, String language) throws Exception;
	
	public String testCode(String language, String filePath);
	
	public void createMainJavaFile();
	
	public void createMainJsFile();
>>>>>>> Stashed changes
}