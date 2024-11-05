package com.kodinghaejo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
<<<<<<< Updated upstream

import org.springframework.stereotype.Controller;
=======
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

<<<<<<< Updated upstream
=======
import com.kodinghaejo.entity.TestLngEntity;
>>>>>>> Stashed changes
import com.kodinghaejo.service.TestService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class TestController {
	
	private final TestService testCodeService;
	
	@GetMapping("/challenge")
	public void getChallenge() {
		
	}
	
	// 코드 제출을 처리하는 POST 메서드
    @PostMapping("/submit")
    @ResponseBody // 메서드에서 반환되는 객체를 JSON으로 변환하여 응답 본문에 포함
    public String submitCode(@RequestParam("code") String code, @RequestParam("language") String language) {
        try {
        	
        	if ("javascript".equals(language)) {
        		code += "\n\nmodule.exports = solution;";
        	}
        	
            // 파일명 결정
            String filename = "Solution." + (language.equals("java") ? "java" : "js");

<<<<<<< Updated upstream
            // 코드 파일 저장 경로
            Path path = Paths.get("submissions/" + filename);
            Files.createDirectories(path.getParent()); // 경로가 없으면 생성
            
            Files.write(path, code.getBytes()); // 코드 파일로 저장
            if("java".equals(language)) {
            	testCodeService.createMainJavaFile();
            } else if("javascript".equals(language)) {
            	testCodeService.createMainJsFile();
            }
            // 코드 실행 로직 호출
            return testCodeService.testCode(language, path.toString()); // 실행 결과 반환
        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving or executing code!";
        }
    }
=======
	private final TestService service;

	//코딩테스트 문제 화면
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
	@PostMapping("/test/lng")
	public Map<String, Object> postLng(@RequestParam("test_idx") Long testIdx, @RequestParam("language") String language) throws Exception {
		log.info("========== test_idx: {} ==========", testIdx);
		log.info("========== language: {} ==========", language);
		
		TestLngEntity result =  service.loadTestLng(testIdx, language);
		log.info("========== result.getIdx(): {} ==========", result.getIdx());
		log.info("========== result.getTestIdx(): {} ==========", result.getTestIdx().getIdx());
		log.info("========== result.getLng(): {} ==========", result.getLng());
		log.info("========== result.getContent(): {} ==========", result.getContent());
		log.info("========== result.getCorrect(): {} ==========", result.getCorrect());
		log.info("========== result.getMainSrc(): {} ==========", result.getMainSrc());
		log.info("========== result.getRegdate(): {} ==========", result.getRegdate());
		log.info("========== result.getIsUse(): {} ==========", result.getIsUse());
		
		Map<String, Object> data = new HashMap<>();
		data.put("content", result.getContent());
		data.put("idx", result.getIdx());
		data.put("correct", result.getCorrect());
		data.put("main_src", result.getMainSrc());
		
		return data;
	}

	//코딩테스트 문제 제출 처리
	@ResponseBody //메소드에서 반환되는 객체를 JSON으로 변환하여 응답 본문에 포함
	@PostMapping("/test/submit")
	public String submitCode(@RequestParam("tl_idx") Long tlIdx, @RequestParam("code") String code,
			@RequestParam("correct") String correctSrc, @RequestParam("main") String mainSrc,
			@RequestParam("language") String language) {
		try {
			if (language.equals("javascript"))
				code += "\n\nmodule.exports = solution;";
			
			//파일명 결정
			String filename = "Solution." + language;

			//코드 파일 저장 경로
			Path path = Paths.get("submissions/" + filename);
			Files.createDirectories(path.getParent()); //경로가 없으면 생성

			Files.write(path, code.getBytes()); //코드 파일로 저장
			if (language.equals("java")) {
				service.createMainJavaFile();
			} else if ("javascript".equals(language)) {
				service.createMainJsFile();
			}
			
			// 코드 실행 로직 호출
			return service.testCode(language, path.toString()); //실행 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
			return "Error saving or executing code!";
		}
	}
>>>>>>> Stashed changes
}