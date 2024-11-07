package com.kodinghaejo.service;

import java.util.List;

import com.kodinghaejo.dto.TestDTO;

public interface AdminService {

	//문제 작성
	public void saveTestWrite(TestDTO test);
	
	//문제 보여주기
	public List<TestDTO> testAllList();
	
}