package com.kodinghaejo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @PostMapping("/run")
    @ResponseBody // 메서드에서 반환되는 객체를 JSON으로 변환하여 응답 본문에 포함
    public String submitCode(@RequestParam("code") String code, @RequestParam("language") String language) {
        try {
            // 파일명 결정
            String filename = "Solution." + (language.equals("java") ? "java" : "js");

            // 코드 파일 저장 경로
            Path path = Paths.get("submissions/" + filename);
            Files.createDirectories(path.getParent()); // 경로가 없으면 생성
            Files.write(path, code.getBytes()); // 코드 파일로 저장

            // 코드 실행 로직 호출
            return testCodeService.testCode(language, path.toString()); // 실행 결과 반환
        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving or executing code!";
        }
    }
}