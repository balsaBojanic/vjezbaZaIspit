package org.acme.client;

import org.acme.entity.CountryResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient(configKey="country-api")
public interface CountryApi {
	@GET
	 @Path("/")
	CountryResponse getRandomPlace(
			@QueryParam("randomland") String randomLand,
			@QueryParam("json") int json
			
			);

}
