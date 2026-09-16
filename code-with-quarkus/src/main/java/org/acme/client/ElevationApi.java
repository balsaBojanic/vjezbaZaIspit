package org.acme.client;

import org.acme.entity.ElevationResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/v1/forecast")
@RegisterRestClient(configKey="elevation-api")
public interface ElevationApi {
	
	@GET
	ElevationResponse getElevation(
			@QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude
			);

}
