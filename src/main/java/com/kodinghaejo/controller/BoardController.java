package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BoardController {
	
	@GetMapping("/board/noticeboard")
	public void getNoticeboard() { }
	
	@GetMapping("/board/freeboard")
	public void getFreeboard() { }
	
	@GetMapping("/board/freeboardView")
	public void getFreeboardView() { }
	
	@GetMapping("/board/freeboardWrite")
	public void getFreeboardWrite() { }

}