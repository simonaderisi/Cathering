package it.uniroma3.siw.catering.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class IndexController {
	
	@Autowired
	private ChefService chefService;
	
	@Autowired
	private BuffetService buffetService;

	@PostMapping("/addChef")
	public String aggiungiChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()) {
            chefService.save(chef);
            model.addAttribute("chef", chef);
            return "chef.html";
        }
        return "index.html";
    }
	
	@GetMapping("/aggiungiChef")
	public String chefForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "aggiungiChef.html";
	}
	
	@GetMapping("/elencoChef")
	public String allChefs(Model model) {
		model.addAttribute("chefs", this.chefService.findAll());
		return "elencoChefs.html";
	}
	
	@GetMapping("/chef/{id}")
	public String showChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef.html";
	}
	
	@GetMapping("/buffet/{id}")
	public String showBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "buffet.html";
	}
	


}
