package org.acme.resource;

import org.acme.entity.RandomPlace;
import org.acme.exception.RandomPlaceException;
import org.acme.multipart.MultipartBody;
import org.acme.service.RandomPlaceService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/RandomPlace")
public class RandomPlaceResource {
	
	@Inject
	private RandomPlaceService randomPlaceService;
	
	@Operation(summary="Dodavanje RandomPlace objekta ", description="prima JSON kod i dodaje kreirani RandomPlace objekat u bazu")
	@APIResponse(responseCode="200", description="RandomPlace je uspjesno sacuvan")
	@APIResponse(responseCode="400", description="doslo je do greske pri cuvanju RandomPlace objekta")
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRandomPlace(RandomPlace randomPlace) {
		try {
				RandomPlace saved=randomPlaceService.createRandomPlace(randomPlace);
				return Response.ok(saved).build();
				
		
		}catch(RandomPlaceException e) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage())
					.build();
				
		}
		
	}
	@Operation(summary="odredjuje longt lat i nadmorsku visinu", description="preko geonames apija dobija longitude i atitude koji proslijedjujemo open meteo apiju da bismo dobili nadmorsku visinu")
	@APIResponse(responseCode="200", description="uspjesno su generisani detaljni podaci o RandomPlace objektu")
	@APIResponse(responseCode="400", description = "doslo je do reske pri generisanju potrebnih podataka")
	@PUT
	@Path("/generate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generate(@QueryParam("id") Long id) {
		try {
			RandomPlace randomPlace=randomPlaceService.generate(id);
			return Response.ok(randomPlace).build();
		}catch(RandomPlaceException e) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage())
					.build();
		}
	}
	@Operation(summary="dozvoljava postavljanje slike", description="uzima id na koji vezuje sliku, kreira folder tako da se slike cuva u filesystemu")
	@APIResponse(responseCode="200", description="uspjesno sacuvana slika")
	@APIResponse(responseCode="400", description="doslo je do greske i slika nije sacuvana")

	@POST
	@Path("/addImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addImage(@QueryParam("id") Long id, MultipartBody multipartBody) {
		try {
			RandomPlace randomPlace= randomPlaceService.addImage(id, multipartBody);
			return Response.ok(randomPlace).build();
		}catch(RandomPlaceException e) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage())
					.build();
		}
	}
	@Operation(summary="ucitava sliku", description="uzima id RandomPlace objekta trazi putanju do slike, dekodira je i prikazuje je")
	@APIResponse(responseCode="200", description="uspjesno je prikazana slika")
	@APIResponse(responseCode="400", description="doslo je do greske i slika nije prikazana")
	@GET
	@Path("/getRandomPlace")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRandomPlace(@QueryParam("id") Long id) {
		try {
			RandomPlace randomPlace=randomPlaceService.getRandomPlace(id);
			return Response.ok(randomPlace).build();
		}catch(RandomPlaceException e) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(e.getMessage())
					.build();
		}
	}
	

}
