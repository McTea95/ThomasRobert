package de.htw.ai.kbe.songsRX.storage;

import java.util.Collection;

import de.htw.ai.kbe.songsRX.bean.Song;

public interface SongsI {

	public Song getSong(Integer id);
	public Collection<Song> getAllSongs();
	public Integer addSong(Song song);
	//public boolean updateSong(Song song);
	public boolean deleteSong(Integer id);
}
