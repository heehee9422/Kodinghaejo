package com.kodinghaejo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.BannerEntity;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.service.AdminService;
import com.kodinghaejo.service.BaseService;
import com.kodinghaejo.service.TestService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BaseController {

	private final AdminService adminservice;
	private final TestService testService;
	private final BaseService baseService;

	@GetMapping("/index")
	public String getIndex(Model model, HttpServletRequest request) {
		adminservice.upTodayVisitorCount(request);

		List<BannerEntity> banners;
		banners = adminservice.getBanner();

		Random random = new Random();
		BannerEntity randomBanner = banners.get(random.nextInt(banners.size()));

		model.addAttribute("banners", randomBanner);

		//난이도별 문제
		List<TestDTO> diffProblem = testService.getDiffTest();
		model.addAttribute("diffProblems", diffProblem);

		return "index";
	}

	@GetMapping("/rank/rank")
	public void getRank() { }

	//가장 많이 풀어본 문제
	@GetMapping("/popularTest")
	public String getPopularTest() {
		Long idx = testService.getMostPopularTest();

		return "redirect:/test/challenge?test_idx=" + idx;
	}

	//등록일 기준 신규 문제
	@GetMapping("/newTest")
	public String getNewTest() {
		Long randomIdx = testService.getNewTest(5);

		return "redirect:/test/challenge?test_idx=" + randomIdx;
	}

	//난이도 0 기준 랜덤 문제
	@GetMapping("/randomTest")
	public String getRandomTest() {
		Long randomIdx = testService.getRandomTest();

		return "redirect:/test/challenge?test_idx=" + randomIdx;
	}

	//등록일 기준 신규 공지
	@ResponseBody
	@GetMapping("/noticeBell")
	public List<BoardEntity> getNewNotice(Model model) {

		List<BoardEntity> boardEntities = baseService.getNewNotice(3);
		model.addAttribute("notice", boardEntities);

		return boardEntities;
	}

}