package de.htw.ai.kbe.songsRX.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="songList")
public class SongList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	User owner;
	
	@Column(name="access")
	private String access;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "SongList_Song",
	joinColumns = {@JoinColumn(name = "songList_id", referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
	Set<Song> songs;

	public SongList() {}
	
	public SongList(String access){
		this(null, access);
	}
	
	public SongList(User owner, String access){
		this.owner = owner;
		this.access = access;
	}
	
	@JsonIgnore
	public User getUser() {
		return owner;
	}
	
	public void setUser(User owner) {
        this.owner = owner;
    }
	
	public Integer getId() {
		return id;
	}
	public User getOwner() {
		return owner;
	}
	public String getAccess() {
		return access;
	}
	
	@JsonIgnore
	public Set<Song> getSongListSet() {
        if(songs == null) {
        	songs = new HashSet<>();
        }
        return songs;
    }

    public void setSongList(Set<Song> songs) {
        this.songs = songs;
        
    }
    
    public void addSongList(Song song) {
        if(songs == null) {
            songs = new HashSet<>();
        }
        this.songs.add(song);
    }
	
}
