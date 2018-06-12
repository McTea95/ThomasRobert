package de.htw.ai.kbe.songsRX.service;

import java.security.SecureRandom;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.songsRX.storage.UserI;

@Path("/auth")
public class AuthentificationService {

	private UserI useri;
	
	@Inject
	public AuthentificationService(UserI useri){
		this.useri = useri;
	}
	
	@GET
	public Response getUsers(@QueryParam("userId") String userId){
		if(useri.userExists(userId)){
			String token = useri.getToken(userId);
			if(token == null) {
				SecureRandom rnd = new SecureRandom();
				byte bytes[] = new byte[20];
				rnd.nextBytes(bytes);
				token = bytes.toString();
				useri.saveToken(userId, token);
			}
			
			return Response.status(Response.Status.ACCEPTED).entity("Token for userID " + userId + ": " + token).build();
		}
		else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
}
