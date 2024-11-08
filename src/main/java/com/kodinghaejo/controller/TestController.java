package com.kodinghaejo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.service.TestService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class TestController {

	private final TestService service;

	//코딩테스트 문제 상세 화면
	@GetMapping("/test/challenge")
	public void getChallenge(Model model, @RequestParam("idx") Long idx, @RequestParam("path") String path) throws Exception {
		model.addAttribute("test", service.loadTest(idx));
		model.addAttribute("path", path);
		model.addAttribute("java", service.lngAvlChk(idx, "LNG-0001"));
		model.addAttribute("js", service.lngAvlChk(idx, "LNG-0002"));
		model.addAttribute("oracle", service.lngAvlChk(idx, "LNG-0003"));
	}
	
	//코딩테스트 언어별 문제 가져오기
	@ResponseBody
	@PostMapping("/test/language")
	public Map<String, Object> postLanguage(@RequestParam("test_idx") Long testIdx, @RequestParam("language") String language) throws Exception {
		log.info("==================== test_idx: {} ====================", testIdx);
		log.info("==================== language: {} ====================", language);
		
		TestLngEntity result =  service.loadTestLng(testIdx, language);
		log.info("==================== result.getIdx(): {} ====================", result.getIdx());
		log.info("==================== result.getTestIdx(): {} ====================", result.getTestIdx().getIdx());
		log.info("==================== result.getLng(): {} ====================", result.getLng());
		log.info("==================== result.getContent(): {} ====================", result.getContent());
		log.info("==================== result.getCorrect(): {} ====================", result.getCorrect());
		log.info("==================== result.getRunSrc(): {} ====================", result.getRunSrc());
		log.info("==================== result.getSubmSrc(): {} ====================", result.getSubmSrc());
		log.info("==================== result.getRegdate(): {} ====================", result.getRegdate());
		log.info("==================== result.getIsUse(): {} ====================", result.getIsUse());
		
		Map<String, Object> data = new HashMap<>();
		data.put("idx", result.getIdx());
		data.put("content_src", result.getContent());
		data.put("correct_src", result.getCorrect());
		data.put("run_src", result.getRunSrc());
		data.put("subm_src", result.getSubmSrc());
		
		return data;
	}

	//코딩테스트 문제 실행 및 제출 처리
	@ResponseBody
	@PostMapping("/test/challenge")
	public String postChallenge(@RequestParam("type") String type, @RequestParam("tl_idx") Long tlIdx,
			@RequestParam("code") String code, @RequestParam("correct_src") String correctSrc,
			@RequestParam("run_src") String runSrc, @RequestParam("subm_src") String submSrc,
			@RequestParam("language") String language) throws Exception {
		if (!type.equals("run") && !type.equals("submit"))
			return "{\"message\":\"TYPE_NOT_AVAILABLE\"}";
		
		//자바스크립트인 경우
		if (language.equals("javascript"))
			code += "\n\nmodule.exports = solution;";
		
		//파일명 결정
		String filename = "Solution." + language;

		//코드 파일 저장 경로
		Path path = Paths.get("submissions/" + filename);
		Files.createDirectories(path.getParent()); //경로가 없으면 생성

		Files.write(path, code.getBytes()); //코드 파일로 저장
		service.createVerifyFiles((type.equals("run") ? runSrc : type.equals("submit") ? submSrc : ""), correctSrc);
		
		// 코드 실행 로직 호출
		return service.testCode(language, path.toString()); //실행 결과 반환
	}

}