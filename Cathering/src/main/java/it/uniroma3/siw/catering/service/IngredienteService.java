package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.repository.IngredienteRepository;

@Service
public class IngredienteService implements CateringService<Ingrediente> {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	@Override
	public void save(Ingrediente ingrediente) {
		this.ingredienteRepository.save(ingrediente);
		
	}

	@Override
	public Ingrediente findById(Long id) {
		return this.ingredienteRepository.findById(id).get();
	}

	@Override
	public List<Ingrediente> findAll() {
		List<Ingrediente> ingredienti = new ArrayList<>();
		for(Ingrediente i: this.ingredienteRepository.findAll())
			ingredienti.add(i);
		return ingredienti;
	}

	@Override
	public void deleteById(Long id) {
		this.ingredienteRepository.deleteById(id);
		
	}

	@Override
	public void modifyById(Long id, Ingrediente ingrediente) {
		Ingrediente toModify = this.findById(id);
		toModify.setNome(ingrediente.getNome());
		toModify.setOrigine(ingrediente.getOrigine());
		toModify.setDescrizione(ingrediente.getDescrizione());
		this.ingredienteRepository.save(toModify);
	}

}
