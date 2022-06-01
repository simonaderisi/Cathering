package it.uniroma3.siw.catering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class MainController {
	
	@Autowired
	private ChefService chefService;
	
	@Autowired
	private BuffetService buffetService;

	
	public void getChefs(Model model) {
		model.addAttribute("chefs", this.chefService.findAll());
	}
	
	public void getBuffets(Model model) {
		model.addAttribute("buffets", this.buffetService.findAll());
	}
	
	@GetMapping("/")
	public String init(Model model) {
		this.getChefs(model);
		this.getBuffets(model);
		return "index.html";
	}
}
