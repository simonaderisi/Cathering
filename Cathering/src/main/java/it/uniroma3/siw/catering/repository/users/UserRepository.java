package it.uniroma3.siw.catering.repository.users;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.users.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public Optional<User> findByEmail(String email);

}
