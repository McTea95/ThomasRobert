package de.htw.ai.kbe.songsRX.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "song")
public class Song {

	private Integer id;
	private String title;
	private String artist;
	private String album;
	private Integer released;
	
	public Song () {}
	
	public Song (Integer id, String title, String artist, String album, Integer released) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.released = released;
	}
	public Song (String title, String artist, String album, Integer released) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.released = released;
	}
	
	@XmlElement
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}
	
	@XmlElement
	public String getArtist() {
		return artist;
	}
	
	@XmlElement
	public String getAlbum() {
		return album;
	}
	
	@XmlElement
	public Integer getReleased() {
		return released;
	}
}
