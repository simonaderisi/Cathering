package it.uniroma3.siw.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.repository.BuffetRepository;

@Service
public class BuffetService implements CateringService<Buffet> {
	
	@Autowired
	private BuffetRepository buffetRepository;

	@Override
	public void save(Buffet buffet) {
		this.buffetRepository.save(buffet);
	}

	@Override
	public Buffet findById(Long id) {
		return this.buffetRepository.findById(id).get();
	}

	@Override
	public List<Buffet> findAll() {
		List<Buffet> buffets = new ArrayList<>();
		for(Buffet b: this.buffetRepository.findAll())
			buffets.add(b);
		return buffets;
	}

	@Override
	public void deleteById(Long id) {
		this.buffetRepository.deleteById(id);
		
	}

	@Override
	public void modifyById(Long id, Buffet buffet) {
		Buffet toModify = this.findById(id);
		if(buffet.getNome()!=null)
			toModify.setNome(buffet.getNome());
		if(buffet.getDescrizione() != null)
			toModify.setDescrizione(buffet.getDescrizione());
		if(buffet.getChef() != null)
			toModify.setChef(buffet.getChef());
		if(buffet.getPiatti()!=null)
			toModify.setPiatti(buffet.getPiatti());
		if(buffet.getChefIdent()!=null)
			toModify.setChefIdent(buffet.getChefIdent());
		this.buffetRepository.save(toModify);
	}

	@Override
	public boolean alreadyExist(Buffet buffet) {
		return this.buffetRepository.existsByNomeAndDescrizioneAndChefIdent(buffet.getNome(), buffet.getDescrizione(), buffet.getChefIdent());
	}
	
	@Transactional
	public void addPiatto(Long id, Piatto piatto) {
		Buffet buffet = this.buffetRepository.findById(id).get();
		buffet.getPiatti().add(piatto);
		this.buffetRepository.save(buffet);
	}

}
