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
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
@RequestMapping("/piatto")
public class PiattoController {

	@Autowired
	private PiattoService piattoService;
	
	@PostMapping("/add")
	public String aggiungiPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()) {
            piattoService.save(piatto);
            model.addAttribute("piatto", piatto);
            return "dettaglioPiatto.html"; //da implementare
        }
        return "index.html";
    }
	
	@GetMapping("/add")
	public String piattoForm(Model model) {
		model.addAttribute("piatto", new Piatto());
		return "aggiungiPiatto.html"; //impl
	}
	
	@GetMapping("/elenco")
	public String allPiatti(Model model) {
		model.addAttribute("piatti", this.piattoService.findAll());
		return "elencoPiatti.html"; //impl
	}
	
	@GetMapping("/{id}")
	public String showPiatto(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(id));
		return "dettaglioPiatto.html"; //impl
	}
}
