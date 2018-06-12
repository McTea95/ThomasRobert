package de.htw.ai.kbe.songsRX.service;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.htw.ai.kbe.songsRX.storage.UserI;

@Provider
public class AuthFilter implements ContainerRequestFilter{

	private UserI useri;
	
	@Inject
	public AuthFilter(UserI useri){
		this.useri = useri;
	}
	
	@Override
	public void filter(ContainerRequestContext containerRequest) throws IOException {
		List<String> authHeader = containerRequest.getHeaders().get("Authorization");
		
		if(!containerRequest.getUriInfo().getPath().contains("auth") && authHeader.get(0)!=null){
			
			String authToken = authHeader.get(0);
			String userId = useri.getUserId(authToken);
		
			if(!useri.tokenExists(authToken)) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		}
	}

}
