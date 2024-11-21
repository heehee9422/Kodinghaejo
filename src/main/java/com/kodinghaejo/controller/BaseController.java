package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BaseController {

	@GetMapping("/index")
	public void getIndex(Model model) {

	}

	@GetMapping("/rank/rank")
	public void getRank() { }

}