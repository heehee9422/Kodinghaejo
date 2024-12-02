package com.kodinghaejo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.service.BaseService;
import com.kodinghaejo.service.TestService;
import com.kodinghaejo.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class TestController {

	private final TestService service;
	private final BaseService baseService;

	//코딩테스트 문제 모아보기
	@GetMapping("/test/collect")
	public void getCollect(Model model, @SessionAttribute(name = "email", required = false) String email,
			@RequestParam(name = "page", defaultValue = "1") int pageNum,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "subm", defaultValue = "") String submSts,
			@RequestParam(name = "lng", defaultValue = "") String lng,
			@RequestParam(name = "diff", defaultValue = "") String diff) {
		int postNum = 10;
		int pageListCount = 5;

		PageUtil page = new PageUtil();
		Page<TestDTO> list = service.getTestList(pageNum, postNum, email, keyword, submSts, lng, diff);
		int totalCount = (int) list.getTotalElements();

		String params = "";
		params += (keyword.equals("")) ? "" : ("&keyword=" + keyword);
		params += (submSts.equals("")) ? "" : ("&subm=" + submSts);
		params += (lng.equals("")) ? "" : ("&lng=" + lng);
		params += (diff.equals("")) ? "" : ("&diff=" + diff);

		Map<String, Object> commonCode = baseService.loadUsedCommonCode();

		model.addAttribute("list", list);
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("subm", submSts);
		model.addAttribute("lng", lng);
		model.addAttribute("diff", diff);
		model.addAttribute("pageList", page.getPageList("/test/collect", "page", pageNum, postNum, pageListCount, totalCount, params));
		model.addAttribute("commonCode", commonCode);
	}

	//코딩테스트 문제 상세 화면
	@GetMapping("/test/challenge")
	public void getChallenge(Model model, @RequestParam("idx") Long idx,
			@SessionAttribute(name = "email", required = false) String email) throws Exception {
		TestDTO test = service.loadTest(idx);
		String descHtml = service.convertCode(test.getDescr());
		model.addAttribute("descHtml", descHtml);
		
		model.addAttribute("test", test);
		model.addAttribute("java", service.lngAvlChk(idx, "LNG-0001"));
		model.addAttribute("js", service.lngAvlChk(idx, "LNG-0002"));
		model.addAttribute("oracle", service.lngAvlChk(idx, "LNG-0003"));
		model.addAttribute("isbookmarked", service.isBookmarked(email, idx));
	}

	//코딩테스트 언어별 문제 가져오기
	@ResponseBody
	@PostMapping("/test/language")
	public Map<String, Object> postLanguage(@RequestParam("test_idx") Long testIdx,
			@RequestParam("language") String language) throws Exception {
		log.info("==================== test_idx: {} ====================", testIdx);
		log.info("==================== language: {} ====================", language);

		TestLngEntity result = service.loadTestLng(testIdx, language);
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
			@RequestParam("language") String language, HttpSession session) throws Exception {
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

		//코드 실행 로직 호출
		String result = service.testCode(language, path.toString()); //실행 결과 반환

		//제출결과 DB 저장
		if (type.equals("submit")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = new ObjectMapper().readValue(result, Map.class);

			String email = (String) session.getAttribute("email");
			int passCnt = (int) data.get("passcnt");
			String submSts = ((passCnt * 5) >= 70) ? "Y" : "N";

			boolean isAdd = service.submitTest(tlIdx, email, submSts, code);

			int diff = service.getTestDiff(tlIdx);
			
			long scoreToAdd = switch (diff) {
				case 0 -> 1L;
				case 1 -> 4L;
				case 2 -> 8L;
				case 3 -> 10L;
				default -> 0L;
			};
			
			if (isAdd && scoreToAdd > 0 && submSts.equals("Y")) {
				service.updateMemberScore(email, scoreToAdd);
			}
		}

		return result;
	}
	
	//북마크 
	@ResponseBody
	@PostMapping("/test/bookmark")
	public Map<String, Object> toggleLike(@RequestBody Map<String, Object> requestData, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Long testIdx = Long.valueOf(requestData.get("testIdx").toString());
		String action = (String) requestData.get("action");
		boolean isbookmarked = "add".equals(action) ? service.addBookmark(email, testIdx) : service.removeBookemark(email, testIdx);
		System.out.println(isbookmarked);
		Map<String, Object> response = new HashMap<>();
		response.put("isbookmarked", isbookmarked);
		return response;
	}	

}