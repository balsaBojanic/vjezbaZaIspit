package org.acme.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.acme.client.CountryApi;
import org.acme.client.ElevationApi;
import org.acme.entity.CountryResponse;
import org.acme.entity.ElevationResponse;
import org.acme.entity.RandomPlace;
import org.acme.exception.RandomPlaceException;
import org.acme.multipart.MultipartBody;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Dependent
public class RandomPlaceService {
	@Inject
	private EntityManager em;
	
	@Inject
	@RestClient
	private CountryApi countryApi;
	
	@Inject
	@RestClient
	private ElevationApi elevationApi;
	
	
	
	@Transactional
	public RandomPlace createRandomPlace(RandomPlace randomPlace) 
		throws RandomPlaceException{
			if (randomPlace==null){
				throw new RandomPlaceException("RandomPlace nije proslijedjen");
			}
			if(randomPlace.getNaziv() == null ||randomPlace.getNaziv().isEmpty()) {
				throw new RandomPlaceException("Naziv je prazan");
			}
			return em.merge(randomPlace);
		}
	
	@Transactional
	public RandomPlace generate(Long id) throws RandomPlaceException {
		if (id == null) {
		    throw new RandomPlaceException("Id nije proslijedjen");
		}
		try {
			RandomPlace randomPlace=em.find(RandomPlace.class, id);
			
			if(randomPlace==null) {
				throw new RandomPlaceException("Random place sa " + id + " ne postoji");
			};
			
			CountryResponse country= countryApi.getRandomPlace("yes", 1);
			
			if(country == null || country.nearest==null) {
				throw new RandomPlaceException("Nije moguce dobiti lokaciju");
			};


Double latituda = Double.parseDouble(country.nearest.latt);
randomPlace.setLatituda(latituda);
Double longituda = Double.parseDouble(country.nearest.longt);
randomPlace.setLongituda(longituda);

ElevationResponse elevation= elevationApi.getElevation(latituda, longituda);
if(elevation==null || elevation.elevation==null) {
			throw new RandomPlaceException("nije moguce dobiti nadmorsku visinu");
};

randomPlace.setNadmorskaVisina(elevation.elevation);

return em.merge(randomPlace);
		}catch (RandomPlaceException e) {
			throw e;

		} catch (NumberFormatException e) {
			throw new RandomPlaceException("dobijene koordinate nisu ispravne");
		}catch(Exception e) {
			throw new RandomPlaceException("Greska prilikom poziva eksternog servisa");
		}
	}
	
	@Transactional
	public RandomPlace addImage(Long id, MultipartBody multipartBody) throws RandomPlaceException {
		if (id == null) {
		    throw new RandomPlaceException("Id nije proslijedjen");
		}
		
			RandomPlace randomPlace=em.find(RandomPlace.class, id);
			
			if(randomPlace==null) {
				throw new RandomPlaceException("RandomPlace sa id " +id + " ne postoji");
			
		}
			if(multipartBody==null || multipartBody.file==null) {
				throw new RandomPlaceException("slika nije proslijedjena");
			}
			try {
				Path folder=Path.of("files");
				Files.createDirectories(folder);
				Path path = folder.resolve(multipartBody.file.fileName());
				Files.copy(multipartBody.file.uploadedFile(), path, StandardCopyOption.REPLACE_EXISTING );
				randomPlace.setPutanjaDoSlike(path.toString());
				
				return em.merge	(randomPlace);
			}catch(IOException e) {
				throw new RandomPlaceException("Greska prilikom cuvanja slike");
			}
	}
	public RandomPlace getRandomPlace(Long id) throws RandomPlaceException{
		if(id==null) {
			throw new RandomPlaceException("morate unijti id");
		}
		RandomPlace randomPlace = em.find(RandomPlace.class, id);
		if(randomPlace==null) {
			throw new RandomPlaceException("randomPlace sa id " +id + " ne postoji");
		}
		if(randomPlace.getPutanjaDoSlike()==null) {
			throw new RandomPlaceException("slika ne postoji");
		}
		try {
			byte[] bytes=Files.readAllBytes(Path.of(randomPlace.getPutanjaDoSlike()));
			String slika = Base64.getEncoder().encodeToString(bytes);
			randomPlace.setSlika(slika);
			return randomPlace;
			
			
		}catch(IOException e) {
			throw new RandomPlaceException("Nije moguce ucitati sliku");
		}
	}

}
