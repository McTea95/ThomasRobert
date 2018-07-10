package de.htw.ai.kbe.songsRX.service;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htw.ai.kbe.songsRX.bean.Song;
import de.htw.ai.kbe.songsRX.storage.SongsI;

@Path("/songs")
public class SongWebService {
	
	private SongsI songsI;
	
	@Context
	private UriInfo uriInfo;
	
	
	@Inject
	public SongWebService(SongsI sI){
		this.songsI = sI;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Collection<Song> getAllSongs() {
		return songsI.getAllSongs();
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getSong(@PathParam("id") Integer id) {
		
		Song song = songsI.getSong(id);
		if(song != null){
			return Response.ok(song).build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Song mit der ID " + id + "wurde nicht gefunden.").build();
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public Response addSong(Song song){
		if(song == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Die Payload ist leer.").build();
		}
		Integer newID = songsI.addSong(song);
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		uriBuilder.path(Integer.toString(newID));
		return Response.created(uriBuilder.build()).build();
	}
	
	/*@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateSong(@PathParam("id") Integer id, Song song){
		if(song == null){
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Die Payload ist leer.").build();
		}
		if(song.getId() != id){
			return Response.status(Response.Status.BAD_REQUEST).entity("Song-IDs stimmen nicht ueberein.").build();
		}
		boolean result = songsI.updateSong(song);
		if(result){
			return Response.status(Response.Status.NO_CONTENT).entity("Song mit der ID " + id + " wurde erfolgreich aktualisiert.").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Song mit der ID " + id + " existiert nicht. Vorgang wurde nicht ausgefuehrt.").build();
		}
		
	}
	*/
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteSong(@PathParam("id") Integer id){
		boolean song = songsI.deleteSong(id);
		
		if(song){
			return Response.status(Response.Status.NO_CONTENT).entity("Song mit der ID " + id + " wurde erfolgreich gelöscht.").build();
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Song mit der ID " + id + " existiert nicht.").build();
		}
	}
}
