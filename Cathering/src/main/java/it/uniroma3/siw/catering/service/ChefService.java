package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.repository.ChefRepository;

@Service
public class ChefService implements CateringService<Chef> {
	
	@Autowired
	private ChefRepository chefRepository;

	@Override
	public void save(Chef chef) {
		this.chefRepository.save(chef);
		
	}

	@Override
	public Chef findById(Long id) {
		return this.chefRepository.findById(id).get();
	}

	@Override
	public List<Chef> findAll() {
		List<Chef> chefs = new ArrayList<>();
		for(Chef c: this.chefRepository.findAll())
			chefs.add(c);
		return chefs;
	}

	@Override
	public void deleteById(Long id) {
		Chef chef = findById(id);
		for(Buffet buffet : chef.getBuffet()) {
			buffet.setChef(null);
		}
		
		this.chefRepository.deleteById(id);
	}

	@Override
	public void modifyById(Long id, Chef chef) {
		Chef toModify = this.findById(id);
		toModify.setNome(chef.getNome());
		toModify.setCognome(chef.getCognome());
		toModify.setNazionalita(chef.getNazionalita());
		toModify.setBuffet(chef.getBuffet());
		if(chef.getPhoto()!=null)
			toModify.setPhoto(chef.getPhoto());
		this.chefRepository.save(toModify);
	}

	@Override
	public boolean alreadyExist(Chef chef) {
		return this.chefRepository.existsByNomeAndCognome(chef.getNome(), chef.getCognome());
	}

}
