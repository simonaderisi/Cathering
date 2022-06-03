package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.repository.PiattoRepository;

@Service
public class PiattoService implements CateringService<Piatto> {
	
	@Autowired
	private PiattoRepository piattoRepository;

	@Override
	public void save(Piatto piatto) {
		this.piattoRepository.save(piatto);
		
	}

	@Override
	public Piatto findById(Long id) {
		return this.piattoRepository.findById(id).get();
	}

	@Override
	public List<Piatto> findAll() {
		List<Piatto> piatti = new ArrayList<>();
		for(Piatto p: this.piattoRepository.findAll())
			piatti.add(p);
		return piatti;
	}

	@Override
	public void deleteById(Long id) {
		this.piattoRepository.deleteById(id);
	}

	@Override
	public void modifyById(Long id, Piatto p) {
		Piatto old = piattoRepository.findById(id).get();
        if(!p.getNome().equals(old.getNome())) {
            old.setNome(p.getNome());
        }
        if(!p.getDescrizione().equals(old.getDescrizione())) {
            old.setDescrizione(p.getDescrizione());
        }
        if(!p.getIngredienti().equals(old.getIngredienti())) {
            old.setIngredienti(p.getIngredienti());
        }
        piattoRepository.save(old);	
	}

	@Override
	public boolean alreadyExist(Piatto piatto) {
		return this.piattoRepository.existsByNomeAndIngredientiIn(piatto.getNome(), piatto.getIngredienti());
	}

}
