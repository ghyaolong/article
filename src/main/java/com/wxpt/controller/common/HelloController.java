package com.wxpt.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/he")
public class HelloController {
	/**
	 * @param model
	 * @return
	 */
	@RequestMapping("/hello")
	public String save(Model model) {
		model.addAttribute("msg", "Hello");
		return "show";
	}
	
	
	
}
