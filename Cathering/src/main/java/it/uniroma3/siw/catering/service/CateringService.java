package it.uniroma3.siw.catering.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public interface CateringService <T> {

	@Transactional
	public void save(T t);
	
	public T findById(Long id);
	
	public List<T> findAll();
	
	@Transactional
	public void deleteById(Long id);
	
	@Transactional
	public void modifyById(Long id, T t);
	
}
