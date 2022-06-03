package it.uniroma3.siw.catering.repository.users;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.catering.model.users.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);

}
