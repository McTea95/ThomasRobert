package de.htw.ai.kbe.songsRX.storage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import de.htw.ai.kbe.songsRX.bean.SongList;
import de.htw.ai.kbe.songsRX.bean.User;

@Singleton
public class DBSongList implements SongListI{

	private boolean access;
	private EntityManagerFactory emf;
	Query q;
	
	@Inject
	private DBSongList(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	@Override
	public SongList getSongList(String userId, int songListId) {
		EntityManager em = emf.createEntityManager();
		
		SongList songlist = em.find(SongList.class, songListId);
		em.close();
		if(songlist != null){
			if(songlist.getAccess().equals("public")||access){
				return songlist;
			}
			else {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		}
		else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@Override
	public ArrayList<SongList> getSongLists(String userId){
		EntityManager em = emf.createEntityManager();
		ArrayList<SongList> sgList = new ArrayList<SongList>();
		
		q = em.createQuery("SELECT sg FROM SongList sg");
		List<SongList>songlistList = q.getResultList();
		
		for(int i=0; i<songlistList.size(); i++){
			if(songlistList.get(i).getOwner().getUserId().equals(userId)) {
				if(songlistList.get(i).getAccess().equals("public")||access){
					sgList.add(songlistList.get(i));
				}
			}
		}
		
		em.close();
		return sgList;
	}
	
	public void makePrivate() {
		access=false;
	}
	
	public void makePublic() {
		access = true;
	}
	
	@Override
	public boolean addSongList(User user, String access){
		return false;
	}
	
	@Override
	public SongList deleteSongList(Integer id) {
		EntityManager em = emf.createEntityManager();
		SongList songList = em.find(SongList.class, id);
		if(em.contains(songList)) {
			if(access) {
				em.getTransaction().begin();
				em.remove(songList);
				em.getTransaction().commit();
				return songList;
			}
			else {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		}
		return null;
	}
}
