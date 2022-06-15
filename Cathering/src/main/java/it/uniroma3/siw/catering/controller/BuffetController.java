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

import it.uniroma3.siw.catering.controller.validator.BuffetValidator;
import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private BuffetValidator buffetValidator;

	@Autowired
	private ChefService chefService;


	/* metodi per visualizzare buffets */

	@GetMapping("/buffet/elenco")
	public String allBuffets(Model model) {
		model.addAttribute("buffets", this.buffetService.findAll());
		return "show/buffet/elencoBuffets.html";
	}


	@GetMapping("/buffet/{id}")
	public String showBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "show/buffet/dettaglioBuffet.html";
	}
	
	
	
	
	/*** ADMIN ***/
	@GetMapping("/admin/buffet/manage")
		public String adminBuffet(Model model) {
			model.addAttribute("buffets", this.buffetService.findAll());
			return "admin/buffet/gestisciBuffet.html";
	}
	

	/* metodi (accessibili solo agli amministratori) per aggiungere buffet */

	@PostMapping("/admin/buffet/add")
	public String aggiungiBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		this.buffetValidator.validate(buffet, bindingResult);
		Chef chef = this.chefService.findById(buffet.getChefIdent());
		buffet.setChef(chef);
		if(!bindingResult.hasErrors()) {
			buffetService.save(buffet);
			chef.getBuffet().add(buffet);
			this.chefService.save(chef);
			model.addAttribute("buffet", buffet);
			return "admin/buffet/gestisciBuffet.html";
		}
		model.addAttribute("buffet", buffet);
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/buffet/aggiungiBuffet.html";
	}

	@GetMapping("/admin/buffet/add")
	public String buffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/buffet/aggiungiBuffet.html";
	}

	/* metodi (accessibili solo agli amministratori) per modificare buffet */

	@GetMapping("/admin/buffet/modifica/richiesta/{id}")
	public String chiediModificaBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/buffet/formModificaBuffet.html";
	}

	@PostMapping("/admin/buffet/modifica/conferma/{id}")
	public String confermaModificaBuffet(@Valid @ModelAttribute("buffet")  Buffet buffet, 
			BindingResult bindingResult, @PathVariable("id") Long idBuffet, Model model) {
		Buffet toModify = this.buffetService.findById(idBuffet);
		if(buffet.getChefIdent()==null)
			buffet.setChefIdent(toModify.getChefIdent());
		buffet.setChef(this.chefService.findById(buffet.getChefIdent()));
		this.buffetValidator.validate(buffet, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.buffetService.modifyById(idBuffet, buffet);
			this.modificaChef(toModify, buffet);
			model.addAttribute("buffets", this.buffetService.findAll());
			return "admin/buffet/gestisciBuffet.html";
		}
		return chiediModificaBuffet(idBuffet, model);
	}

	public void modificaChef(Buffet toModify, Buffet nuovoBuffet) {
		if(nuovoBuffet.getChefIdent() != null) {
			if(! toModify.getChefIdent().equals(nuovoBuffet.getChefIdent())) {
				Chef previous = this.chefService.findById(toModify.getChefIdent());
				previous.getBuffet().remove(toModify);
				Chef newChef = this.chefService.findById(nuovoBuffet.getChefIdent());
				nuovoBuffet.setChef(newChef);
				newChef.getBuffet().add(toModify);
			}
		}
	}


	/** metodi (accessibili solo agli amministratori) per eliminare buffet **/

	@GetMapping("/admin/buffet/elimina/richiesta/{id}")
	public String chiediEliminazioneBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "admin/buffet/confermaEliminazioneBuffet.html";
	}

	@PostMapping("/admin/buffet/elimina/conferma/{id}")
	public String confermaEliminazioneBuffet(@PathVariable("id") Long id, Model model) {
		this.buffetService.deleteById(id);
		Collection<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "admin/buffet/gestisciBuffet.html";
	}




}
