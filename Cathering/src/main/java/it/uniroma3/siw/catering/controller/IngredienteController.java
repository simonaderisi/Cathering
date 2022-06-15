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

import it.uniroma3.siw.catering.controller.validator.IngredienteValidator;
import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;

@Controller
public class IngredienteController {
	
	@Autowired
	private IngredienteService ingredienteService;
	
	@Autowired
	private IngredienteValidator ingredienteValidator;
	
	@Autowired 
	private PiattoService piattoService;
	
	
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
	
	
	
	/*** ADMIN ***/
	
	/** metodi (accessibili solo agli amministratori) per aggiungere ingredienti **/

	
	@PostMapping("/admin/ingrediente/add/{id}")
	public String aggiungiIngredienteAlPiatto(@PathVariable("id") Long idPiatto, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, 
									  BindingResult bindingResult, Model model) {
        Piatto piatto = this.piattoService.findById(idPiatto);
		ingrediente.setPiatto(piatto);
        this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
            piatto.getIngredienti().add(ingrediente);
            this.piattoService.modifyById(idPiatto, piatto);
            model.addAttribute("piatto", piatto);
            return "admin/piatto/formModificaPiatto.html";
        }
		model.addAttribute("piatto", piattoService.findById(idPiatto));
		model.addAttribute("ingrediente", ingrediente);
        return "admin/ingrediente/aggiungiIngrediente.html";
    }
	
	
	@GetMapping("/admin/ingrediente/add/form/{id}")
	public String ingredienteForm(@PathVariable("id") Long idPiatto, Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		model.addAttribute("piatto", piattoService.findById(idPiatto));
		return "admin/ingrediente/aggiungiIngrediente.html";
	}
	
	
	/** metodi (accessibili solo agli amministratori) per modificare ingredienti **/
	
	@GetMapping("/admin/ingrediente/modifica/richiesta/{id}")
	public String chiediModificaIngrediente(@PathVariable("id") Long idIngrediente, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(idIngrediente));
		return "admin/ingrediente/formModificaIngrediente.html";
	}
	
	@PostMapping("/admin/ingrediente/modifica/conferma/{id}")
	public String confermaModificaIngrediente(@Valid @ModelAttribute("ingrediente")  Ingrediente ingrediente, 
											  BindingResult bindingResult, @PathVariable("id") Long idIngrediente, Model model) {
		//setto il piatto perche il validator prende anche il piatto come parametro
		ingrediente.setPiatto(ingredienteService.findById(idIngrediente).getPiatto());
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.ingredienteService.modifyById(idIngrediente, ingrediente);
			model.addAttribute("piatto", this.ingredienteService.findById(idIngrediente).getPiatto());
			return "admin/piatto/formModificaPiatto.html";
		}
		return chiediModificaIngrediente(idIngrediente, model);
	}
	
	
	/** metodi (accessibili solo agli amministratori) per eliminare ingredienti **/
	
	@GetMapping("/admin/ingrediente/elimina/richiesta/{id}")
	public String chiediEliminazioneIngrediente(@PathVariable("id") Long idIngrediente, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(idIngrediente));
		return "admin/ingrediente/confermaEliminazioneIngrediente.html";
	}
	
	@PostMapping("/admin/ingrediente/elimina/conferma/{id}")
	public String confermaEliminazioneIngrediente(@PathVariable("id") Long idIngrediente, Model model) {
		Ingrediente toDelete = this.ingredienteService.findById(idIngrediente);
		Piatto piatto = toDelete.getPiatto();
		piatto.getIngredienti().remove(toDelete);
		this.ingredienteService.deleteById(idIngrediente);
		model.addAttribute("piatto", piatto);
		return "admin/piatto/formModificaPiatto.html";
	}


}
