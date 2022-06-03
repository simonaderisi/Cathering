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

import it.uniroma3.siw.catering.controller.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;
	
	@Autowired
	private ChefValidator chefValidator;
	
	/** metodi per visualizzare chef*/
	
	@GetMapping("/chef/elenco")
	public String allChefs(Model model) {
		model.addAttribute("chefs", this.chefService.findAll());
		return "chef/elencoChefs.html";
	}
	
	@GetMapping("/chef/{id}")
	public String showChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef/dettaglioChef.html";
	}
	
	
	/** metodi (riservati agli amministratori) per aggiungere uno chef */ 
	
	@PostMapping("/admin/chef/add")
	public String aggiungiChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
        this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
            chefService.save(chef);
            model.addAttribute("chef", chef);
            return "chef/dettaglioChef.html";
        }
        return "index.html";
    }
	
	@GetMapping("/admin/chef/add")
	public String chefForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "chef/aggiungiChef.html";
	}
	
	
/* metodi (accessibili solo agli amministratori) per modificare chef */
	
	@GetMapping("/admin/chef/modifica/richiesta/{id}")
	public String chiediModificaChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef/formModificaChef.html";
	}
	
	@PostMapping("/admin/chef/modifica/conferma/{id}")
	public String confermaModificaChef(@Valid @ModelAttribute("buffet")  Chef chef, 
											  BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.chefService.modifyById(id, chef);
			model.addAttribute("chef", this.chefService.findById(id));
			return "chef/dettaglioChef.html";
		}
		return "chef/formModificaChef.html";
	}
	
	/** metodi (accessibili solo agli amministratori) per eliminare chef **/
	
	@GetMapping("/admin/chef/elimina/richiesta/{id}")
	public String chiediEliminazioneChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef/confermaEliminazioneChef.html";
	}
	
	@PostMapping("/admin/chef/elimina/conferma/{id}")
	public String confermaEliminazioneChef(@PathVariable("id") Long id, Model model) {
		this.chefService.deleteById(id);
		Collection<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "chef/elencoChefs.html";
	}
	
	
}
