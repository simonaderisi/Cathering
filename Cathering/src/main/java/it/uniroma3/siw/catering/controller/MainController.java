package it.uniroma3.siw.catering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	
	@GetMapping("/home")
	public String returnToHome() {
		return "index.html";
	}
	
	@GetMapping("/homeAdmin")
	public String returnToHomeAdmin(){
		return "admin/indexAdmin.html";
	}
}
