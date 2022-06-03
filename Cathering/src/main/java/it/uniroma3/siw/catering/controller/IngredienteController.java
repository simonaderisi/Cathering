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

import it.uniroma3.siw.catering.controller.validator.IngredienteValidator;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;

@Controller
public class IngredienteController {
	
	@Autowired
	private IngredienteService ingredienteService;
	
	@Autowired
	private IngredienteValidator ingredienteValidator;
	
	
	/** metodi per visualizzare ingredienti **/
	@GetMapping("/ingrediente/elenco")
	public String allIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "ingrediente/elencoIngredienti.html"; //impl
	}
	
	@GetMapping("/ingrediente/{id}")
	public String showIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente/dettaglioIngrediente.html";
	}
	
	/** metodi (accessibili solo agli amministratori) per aggiungere ingredienti **/
	
	@PostMapping("/admin/ingrediente/add")
	public String aggiungiIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
        this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
            ingredienteService.save(ingrediente);
            model.addAttribute("ingrediente", ingrediente);
            return "ingrediente/dettaglioIngrediente.html";
        }
        return "index.html";
    }
	
	@GetMapping("/admin/ingrediente/add")
	public String ingredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "ingrediente/aggiungiIngrediente.html";
	}
	
	
	/** metodi (accessibili solo agli amministratori) per modificare ingredienti **/
	
	@GetMapping("/admin/ingrediente/modifica/richiesta/{id}")
	public String chiediModificaIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente/formModificaIngrediente.html";
	}
	
	@PostMapping("/admin/ingrediente/modifica/conferma/{id}")
	public String confermaModificaIngrediente(@Valid @ModelAttribute("ingrediente")  Ingrediente ingrediente, 
											  BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.ingredienteService.modifyById(id, ingrediente);
			model.addAttribute("ingrediente", this.ingredienteService.findById(id));
			return "ingrediente/dettaglioIngrediente.html";
		}
		return "ingrediente/formModificaIngrediente.html";
	}
	
	/** metodi (accessibili solo agli amministratori) per eliminare ingredienti **/
	
	@GetMapping("/admin/ingrediente/elimina/richiesta/{id}")
	public String chiediEliminazioneIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente/confermaEliminazioneIngrediente.html";
	}
	
	@PostMapping("/admin/ingrediente/elimina/conferma/{id}")
	public String confermaEliminazioneIngrediente(@PathVariable("id") Long id, Model model) {
		this.ingredienteService.deleteById(id);
		Collection<Ingrediente> ingredienti = this.ingredienteService.findAll();
		model.addAttribute("ingredienti", ingredienti);
		return "ingrediente/elencoIngredienti.html";
	}


}
