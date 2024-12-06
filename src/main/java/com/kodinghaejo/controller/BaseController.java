package com.kodinghaejo.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.BannerEntity;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.service.AdminService;
import com.kodinghaejo.service.BaseService;
import com.kodinghaejo.service.TestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class BaseController {

	private final AdminService adminservice;
	private final TestService testService;
	private final BaseService baseService;

	@GetMapping({ "/", "/index" })
	public String getIndex(Model model, HttpServletRequest request) {
		adminservice.upTodayVisitorCount(request);

		List<BannerEntity> banners;
		banners = adminservice.getBanner();

		if (banners != null && !banners.isEmpty()) {
			Random random = new Random();
			BannerEntity randomBanner = banners.get(random.nextInt(banners.size()));
			model.addAttribute("banners", randomBanner);
		} else {
			model.addAttribute("banners", null);
		}

		//난이도별 문제
		List<TestDTO> diffProblem = testService.getDiffTest();
		model.addAttribute("diffProblems", diffProblem);

		//랭킹
		List<MemberDTO> members = baseService.memberRank("");
		for (MemberDTO member : members) {
			String grade = baseService.calGrade(member.getScore());
			member.setGrade(grade);
		}
		model.addAttribute("members", members);

		return "index";
	}

	//랭킹
	@GetMapping("/rank/rank")
	public void getRank(Model model, @RequestParam(name = "kind", defaultValue = "") String kind) {
		List<MemberDTO> members = baseService.memberRank(kind);

		for (MemberDTO member : members) {
			String grade = baseService.calGrade(member.getScore());
			member.setGrade(grade);
		}

		model.addAttribute("members", members);
	}

	//가장 많이 풀어본 문제
	@GetMapping("/test/popularTest")
	public String getPopularTest() {
		Long idx = testService.getMostPopularTest();

		return "redirect:/test/challenge?idx=" + idx;
	}

	//등록일 기준 신규 문제
	@GetMapping("/test/newTest")
	public String getNewTest() {
		Long randomIdx = testService.getNewTest(5);

		return "redirect:/test/challenge?idx=" + randomIdx;
	}

	//난이도 0 기준 랜덤 문제
	@GetMapping("/test/randomTest")
	public String getRandomTest() {
		Long randomIdx = testService.getRandomTest();

		return "redirect:/test/challenge?idx=" + randomIdx;
	}

	//등록일 기준 신규 공지
	@ResponseBody
	@GetMapping("/noticeBell")
	public List<BoardEntity> getNewNotice(Model model) {

		List<BoardEntity> boardEntities = baseService.getNewNotice(3);
		model.addAttribute("notice", boardEntities);

		return boardEntities;
	}

	//배너 이미지 보기
	@GetMapping("/banner/img/{fileName}")
	public void downloadBannerImage(@PathVariable("fileName") String fileName, HttpServletResponse rs) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if (os.contains("win")) {
			path = "D:\\공유폴더\\임시저장소\\프로젝트관리\\1회차\\2조\\Repository\\banner\\";
		} else {
			path = "/home/mklee/Repository/Kodinghaejo/banner/";
		}

		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(path + fileName);
		if (!file.exists() || !file.isFile()) {
			rs.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		byte[] fileBytes = FileUtils.readFileToByteArray(file);

		String contentType = Files.probeContentType(file.toPath());
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		rs.setContentType(contentType);
		rs.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
		rs.setContentLength(fileBytes.length);

		rs.getOutputStream().write(fileBytes);
		rs.getOutputStream().flush();
		rs.getOutputStream().close();
	}

}