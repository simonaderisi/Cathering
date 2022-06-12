package it.uniroma3.siw.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.model.Piatto;

public interface BuffetRepository extends CrudRepository<Buffet, Long> {
	
	public boolean existsByNomeAndPiattiInAndChef(String nome, List<Piatto> piatti, Chef chef);
	
	public boolean existsByNomeAndDescrizioneAndChefIdent(String nome, String descrizione, Long chefIdent);


}
