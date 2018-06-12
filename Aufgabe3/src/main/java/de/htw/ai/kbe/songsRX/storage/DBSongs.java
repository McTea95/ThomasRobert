package de.htw.ai.kbe.songsRX.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.songsRX.bean.Song;


public class DBSongs implements SongsI {

	private static Map<Integer,Song> storage;
	
	public DBSongs(){
		storage = new ConcurrentHashMap<Integer,Song>();
		initSongsDB("songs.json");
	}
	
	private void initSongsDB(String filename){
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
	}
	
	@Override
	public Song getSong(Integer id) {
		Song song = storage.get(id);
		
		return song;
	}

	@Override
	public Collection<Song> getAllSongs() {
		return storage.values();
	}

	@Override
	public Integer addSong(Song song) {
		song.setId((int)storage.keySet().stream().count() + 1);
		storage.put(song.getId(), song);
		return song.getId();
	}
	
	@Override
	public boolean updateSong(Song song){
		if(storage.containsKey(song.getId())){
			storage.put(song.getId(), song);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean deleteSong(Integer id) {
		if(storage.containsKey(id)){
			storage.remove(id);
			return true;
		}
		else {
			return false;
		}
	}
	

}
