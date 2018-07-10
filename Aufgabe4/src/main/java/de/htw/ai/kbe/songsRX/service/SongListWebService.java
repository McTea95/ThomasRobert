package de.htw.ai.kbe.songsRX.service;


import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.songsRX.bean.SongList;
import de.htw.ai.kbe.songsRX.storage.SongListI;
import de.htw.ai.kbe.songsRX.storage.UserI;

@Path("/userId")
public class SongListWebService {

	private SongListI songListI;
	private UserI userI;
	
	@Inject
	public SongListWebService(SongListI slI, UserI userI) {
		this.songListI = slI;
		this.userI = userI;
	}
	
	@GET
	@Path("/{userid}/songLists/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getSongListId(@PathParam("userid") String userId, @PathParam("id") int id) {
		if(userI.userExists(userId)) {
			return Response.ok(songListI.getSongList(userId, id)).build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).entity("User: " + userId + " wurde nicht gefunden!").build();
		}
	}
	
	@GET
	@Path("/{userid}/songLists")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getSongLists(@PathParam("userid") String userId){
		if(userI.userExists(userId)) {
			return Response.ok(songListI.getSongLists(userId)).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("User: " + userId + " wurde nicht gefunden!").build();
		}
	}
	
	@DELETE
	@Path("/{userid}/songLists/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteSongListId(@PathParam("userId") String userId, @PathParam("id") int id){
		if(userI.userExists(userId)){
			SongList sg = songListI.deleteSongList(id);
			return Response.ok("SongList mit der ID " + sg.getId() + " wurde entfernt.").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("User: " + userId + " wurde nicht gefunden!").build();
		}
	}
}
