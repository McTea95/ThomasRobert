package de.htw.ai.kbe.songsRX.di;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htw.ai.kbe.songsRX.storage.DBSongList;
import de.htw.ai.kbe.songsRX.storage.DBSongs;
import de.htw.ai.kbe.songsRX.storage.DBUser;
import de.htw.ai.kbe.songsRX.storage.SongListI;
import de.htw.ai.kbe.songsRX.storage.SongsI;
import de.htw.ai.kbe.songsRX.storage.UserI;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(Persistence.createEntityManagerFactory("songDB-PU")).to(EntityManagerFactory.class);
		bind(DBSongs.class).to(SongsI.class).in(Singleton.class);
		bind(DBUser.class).to(UserI.class).in(Singleton.class);
		bind(DBSongList.class).to(SongListI.class).in(Singleton.class);
	}
}
