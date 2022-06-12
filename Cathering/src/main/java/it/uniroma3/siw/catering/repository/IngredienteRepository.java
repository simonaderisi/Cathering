package it.uniroma3.siw.catering.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.Ingrediente;
import it.uniroma3.siw.catering.model.Piatto;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {
	
	public boolean existsByNomeAndOrigineAndPiatto(String nome, String origine, Piatto piatto);

}
