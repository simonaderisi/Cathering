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
import it.uniroma3.siw.catering.service.BuffetService;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;
	
	@Autowired
	private BuffetValidator buffetValidator;
	
	
	/* metodi per visualizzare buffets */
	
	@GetMapping("/buffet/elenco")
	public String allBuffets(Model model) {
		model.addAttribute("buffets", this.buffetService.findAll());
		return "buffet/elencoBuffets.html"; //impl
	}
	
	
	@GetMapping("/buffet/{id}")
	public String showBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "buffet/dettaglioBuffet.html";
	}
	
	/* metodi (accessibili solo agli amministratori) per aggiungere buffet */
	
	@PostMapping("/admin/buffet/add")
	public String aggiungiBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
        this.buffetValidator.validate(buffet, bindingResult);
		if(!bindingResult.hasErrors()) {
        	buffetService.save(buffet);
            model.addAttribute("buffet", buffet);
            return "buffet/dettaglioBuffet.html"; //da implementare
        }
        return "index.html";
    }
	
	@GetMapping("/admin/buffet/add")
	public String buffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		return "buffet/aggiungiBuffet.html"; //impl
	}
	
	/* metodi (accessibili solo agli amministratori) per modificare buffet */
	
	@GetMapping("/admin/buffet/modifica/richiesta/{id}")
	public String chiediModificaBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "buffet/formModificaBuffet.html";
	}
	
	@PostMapping("/admin/buffet/modifica/conferma/{id}")
	public String confermaModificaBuffet(@Valid @ModelAttribute("buffet")  Buffet buffet, 
											  BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		this.buffetValidator.validate(buffet, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.buffetService.modifyById(id, buffet);
			model.addAttribute("buffet", this.buffetService.findById(id));
			return "buffet/dettaglioBuffet.html";
		}
		return "buffet/formModificaBuffet.html";
	}
	
	/** metodi (accessibili solo agli amministratori) per eliminare buffet **/
	
	@GetMapping("/admin/buffet/elimina/richiesta/{id}")
	public String chiediEliminazioneBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", this.buffetService.findById(id));
		return "buffet/confermaEliminazioneBuffet.html";
	}
	
	@PostMapping("/admin/buffet/elimina/conferma/{id}")
	public String confermaEliminazioneBuffet(@PathVariable("id") Long id, Model model) {
		this.buffetService.deleteById(id);
		Collection<Buffet> buffets = this.buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "buffet/elencoBuffets.html";
	}
	


	
}
