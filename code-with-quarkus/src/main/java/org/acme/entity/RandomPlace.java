package org.acme.entity;


import jakarta.persistence.*;

@Entity
public class RandomPlace {
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "random_place_seq")
	@SequenceGenerator(
			name= "random_place_seq",
			sequenceName="random_place_seq",
			allocationSize=1
			)
	private Long id;
	
	private String naziv;
	private Long brojStanovnika;
	private Double longituda;
	private Double latituda;
	private Double nadmorskaVisina;
	private String slika;
	private String putanjaDoSlike;
	
	public RandomPlace() {
		
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Long getBrojStanovnika() {
		return brojStanovnika;
	}

	public void setBrojStanovnika(Long brojStanovnika) {
		this.brojStanovnika = brojStanovnika;
	}

	public Double getLongituda() {
		return longituda;
	}

	public void setLongituda(Double longituda) {
		this.longituda = longituda;
	}

	public Double getLatituda() {
		return latituda;
	}

	public void setLatituda(Double latituda) {
		this.latituda = latituda;
	}

	public Double getNadmorskaVisina() {
		return nadmorskaVisina;
	}

	public void setNadmorskaVisina(Double nadmorskaVisina) {
		this.nadmorskaVisina = nadmorskaVisina;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getPutanjaDoSlike() {
		return putanjaDoSlike;
	}

	public void setPutanjaDoSlike(String putanjaDoSlike) {
		this.putanjaDoSlike = putanjaDoSlike;
	}

	

}
