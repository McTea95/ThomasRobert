package de.htw.ai.kbe.songsRX.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.songsRX.bean.Song;

@Singleton
public class DBSongs implements SongsI {

	private EntityManagerFactory emf;
	
	//private static Map<Integer,Song> storage;
	
	/*public DBSongs(){
		storage = new ConcurrentHashMap<Integer,Song>();
		initSongsDB("songs.json");
	}*/
	
	@Inject
	private DBSongs(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	/*private void initSongsDB(String filename){
		List<Song> songList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream input = getClass().getClassLoader().getResourceAsStream("/" + filename);
		try {
			songList = (List<Song>) objectMapper.readValue(input, new TypeReference<List<Song>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i=0; i<songList.size();i++){
			storage.put(songList.get(i).getId(), songList.get(i));
		}
	}*/
	
	@Override
	public Song getSong(Integer id) {
		//Song song = storage.get(id);
		EntityManager em = emf.createEntityManager();
		Song song = em.find(Song.class, id);
		
		return song;
	}

	@Override
	public Collection<Song> getAllSongs() {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("SELECT s FROM song s");
		Collection<Song> songs = q.getResultList();
		
		return songs;
	}

	@Override
	public Integer addSong(Song song) {
		//song.setId((int)storage.keySet().stream().count() + 1);
		//storage.put(song.getId(), song);
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			
			em.persist(song);
			em.getTransaction().commit();
		}
		catch (Exception e){
			e.printStackTrace();
			return -1;
		}
		finally {
			em.close();
		}
		
		return 1;
	}
	
   /*@Override
	public boolean updateSong(Song song){
		if(storage.containsKey(song.getId())){
			storage.put(song.getId(), song);
			return true;
		}
		else {
			return false;
		}
	}*/

	@Override
	public boolean deleteSong(Integer id) {
		EntityManager em = emf.createEntityManager();
		Song song = em.find(Song.class, id);
		if(em.contains(song)){
			em.getTransaction().begin();
			em.remove(song);
			em.getTransaction().commit();
			return true;
		}
		return false;
	}
	

}
