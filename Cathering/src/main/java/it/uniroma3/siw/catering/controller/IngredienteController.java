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

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.service.IngredienteService;

@Controller
@RequestMapping("/ingrediente")
public class IngredienteController {
	
	@Autowired
	private IngredienteService ingredienteService;
	
	@PostMapping("/add")
	public String aggiungiIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()) {
            ingredienteService.save(ingrediente);
            model.addAttribute("ingrediente", ingrediente);
            return "ingrediente/dettaglioIngrediente.html";
        }
        return "index.html";
    }
	
	@GetMapping("/add")
	public String ingredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "ingrediente/aggiungiIngrediente.html";
	}
	
	@GetMapping("/elenco")
	public String allIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "ingrediente/elencoIngredienti.html"; //impl
	}
	
	@GetMapping("/{id}")
	public String showIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente/dettaglioIngrediente.html";
	}

}
