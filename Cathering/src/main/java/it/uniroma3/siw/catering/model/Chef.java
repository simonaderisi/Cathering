package it.uniroma3.siw.catering.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;



@Entity
public class Chef {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String nome;

	private String cognome;

	private String nazionalita;

	private String photo;

	@OneToMany(mappedBy = "chef")
	private List<Buffet> buffet;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public List<Buffet> getBuffet() {
		return buffet;
	}

	public void setBuffet(List<Buffet> buffet) {
		this.buffet = buffet;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Transient
	public String getPhotoPath() {
		if (photo==null) return null;
		return System.getProperty("user.dir")+"/src/main/resources/static/images/chef-photos/"+id.toString()+"/"+photo;
	}

	public String getDirectoryName() {

		return this.nome.replaceAll("\\s+","_")+"_"+this.cognome.replaceAll("\\s+","_");
	}

}
