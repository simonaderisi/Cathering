package it.uniroma3.siw.catering.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.catering.controller.validator.ChefValidator;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.util.FileManager;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;
	
	@Autowired
	private ChefValidator chefValidator;
	
	public static String DIR = System.getProperty("user.dir")+"/src/main/resources/static/images/chef-photos/";
	
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
	
	
	
	
	
	/*** ADMIN ***/
	@GetMapping("/admin/chef/manage")
		public String adminChef(Model model) {
			model.addAttribute("chefs", this.chefService.findAll());
			return "admin/chef/gestisciChef.html";
	}
	
	
	/** metodi (riservati agli amministratori) per aggiungere uno chef */ 
	
	@PostMapping("/admin/chef/add")
	public String aggiungiChef(@Valid @ModelAttribute("chef") Chef chef, 
							   @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) {
        this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			//prendi il nome del file
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			chef.setPhoto(fileName);
			
            chefService.save(chef);  
            //salva il file
            String uploadDir = DIR + chef.getId();
            FileManager.store(multipartFile, uploadDir);
            
            model.addAttribute("chefs", this.chefService.findAll());
            return "admin/chef/gestisciChef.html";
        }
		model.addAttribute("chef", chef);
        return "admin/chef/aggiungiChef.html";
    }
	
	@GetMapping("/admin/chef/add")
	public String chefForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "admin/chef/aggiungiChef.html";
	}
	
	
/* metodi (accessibili solo agli amministratori) per modificare chef */
	
	@GetMapping("/admin/chef/modifica/richiesta/{id}")
	public String chiediModificaChef(@PathVariable("id") Long idChef, Model model) {
		model.addAttribute("chef", this.chefService.findById(idChef));
		return "admin/chef/formModificaChef.html";
	}
	
	@PostMapping("/admin/chef/modifica/conferma/{id}")
	public String confermaModificaChef(@Valid @ModelAttribute("chef")  Chef chef, @RequestParam("image")MultipartFile multipartFile, 
											  BindingResult bindingResult, @PathVariable("id") Long idChef, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			if(! multipartFile.isEmpty()) {
				FileManager.removeImg(DIR + idChef, chef.getPhoto());
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				chef.setPhoto(fileName);    
	            String uploadDir = DIR + idChef;
	            FileManager.store(multipartFile, uploadDir);
			}
			this.chefService.modifyById(idChef, chef);
			model.addAttribute("chefs", this.chefService.findAll());
			return "admin/chef/gestisciChef.html";
		}
		return chiediModificaChef(idChef, model);
	}
	
	/** metodi (accessibili solo agli amministratori) per eliminare chef **/
	
	@GetMapping("/admin/chef/elimina/richiesta/{id}")
	public String chiediEliminazioneChef(@PathVariable("id") Long idChef, Model model) {
		model.addAttribute("chef", this.chefService.findById(idChef));
		return "admin/chef/confermaEliminazioneChef.html";
	}
	
	@PostMapping("/admin/chef/elimina/conferma/{id}")
	public String confermaEliminazioneChef(@PathVariable("id") Long idChef, Model model) {
		FileManager.dirEmptyEndDelete(DIR + idChef);
		this.chefService.deleteById(idChef);
		Collection<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "admin/chef/gestisciChef.html";
	}
}
