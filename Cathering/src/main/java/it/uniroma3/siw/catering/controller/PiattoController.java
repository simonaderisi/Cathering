package it.uniroma3.siw.catering.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.catering.controller.validator.PiattoValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class PiattoController {

	@Autowired
	private PiattoService piattoService;
	
	@Autowired
	private PiattoValidator piattoValidator;
	
	@Autowired
	private BuffetService buffetService;
	
	
	/* metodi per visualizzare piatti */
	
	@GetMapping("/piatto/elenco")
	public String allPiatti(Model model) {
		model.addAttribute("piatti", this.piattoService.findAll());
		return "piatto/elencoPiatti.html"; //impl
	}
	
	@GetMapping("/piatto/{id}")
	public String showPiatto(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(id));
		return "piatto/dettaglioPiatto.html"; //impl
	}
	
	/* metodi (accessibili solo agli amministratori) per aggiungere piatti */
	/*
	@PostMapping("/admin/piatto/add")
	public String aggiungiPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model) {
        this.piattoValidator.validate(piatto, bindingResult);
		if(!bindingResult.hasErrors()) {
            piattoService.save(piatto);
            model.addAttribute("piatto", piatto);
            return "piatto/dettaglioPiatto.html"; //da implementare
        }
        return "index.html";
    }*/
	
	@PostMapping("/admin/piatto/add/{id}")
	public String aggiungiPiattoAlBuffet(@PathVariable("id") Long idBuffet, @Valid @ModelAttribute("piatto") Piatto piatto, 
										 BindingResult bindingResult, Model model) {
		Buffet buffet = this.buffetService.findById(idBuffet);
		piatto.setBuffet(buffet);
		this.piattoValidator.validate(piatto, bindingResult);
		if(!bindingResult.hasErrors()) {
            buffet.getPiatti().add(piatto);
            this.buffetService.modifyById(idBuffet, buffet);
            model.addAttribute("buffet", buffet);
            return "buffet/formModificaBuffet.html"; 
        }
        return "index.html";
	}
	
	
	@GetMapping("/admin/piatto/add/form/{id}")
	public String piattoForm(@PathVariable("id") Long idBuffet, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(idBuffet));
		model.addAttribute("piatto", new Piatto());
		return "piatto/aggiungiPiatto.html";
	}
	
	/* metodi (accessibili solo agli amministratori) per modificare piatti */
	
	@GetMapping("/admin/piatto/modifica/richiesta/{id}")
	public String chiediModificaPiatto(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(id));
		return "piatto/formModificaPiatto.html";
	}
	
	@PostMapping("/admin/piatto/modifica/conferma/{id}")
	public String confermaModificaPiatto(@Valid @ModelAttribute("piatto")  Piatto piatto, 
											  BindingResult bindingResult, @PathVariable("id") Long idPiatto, Model model) {
		piatto.setBuffet(this.piattoService.findById(idPiatto).getBuffet());
		this.piattoValidator.validate(piatto, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.piattoService.modifyById(idPiatto, piatto);
			model.addAttribute("buffet", this.piattoService.findById(idPiatto).getBuffet());
			return "buffet/formModificaBuffet.html";
		}
		return "piatto/formModificaPiatto.html";
	}
	
	/** metodi (accessibili solo agli amministratori) per eliminare piatti **/
	
	@GetMapping("/admin/piatto/elimina/richiesta/{id}")
	public String chiediEliminazionePiatto(@PathVariable("id") Long idPiatto, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(idPiatto));
		return "piatto/confermaEliminazionePiatto.html";
	}
	
	@PostMapping("/admin/piatto/elimina/conferma/{id}")
	public String confermaEliminazionePiatto(@PathVariable("id") Long idPiatto, Model model) {
		Buffet buffet = this.piattoService.findById(idPiatto).getBuffet();
		buffet.getPiatti().remove(piattoService.findById(idPiatto));
		this.piattoService.deleteById(idPiatto);
		model.addAttribute("buffet", buffet);
		return "buffet/formModificaBuffet.html";
	}
	
	
}
