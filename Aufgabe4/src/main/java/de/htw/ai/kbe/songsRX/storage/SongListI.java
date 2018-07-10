package de.htw.ai.kbe.songsRX.storage;

import java.util.ArrayList;

import de.htw.ai.kbe.songsRX.bean.SongList;
import de.htw.ai.kbe.songsRX.bean.User;

public interface SongListI {

	public SongList getSongList(String userId, int songListId);
	
	public boolean addSongList(User userId, String songListId);
	public ArrayList<SongList> getSongLists(String userId);
	public void makePrivate();
	public void makePublic();
	public SongList deleteSongList(Integer id);
}
