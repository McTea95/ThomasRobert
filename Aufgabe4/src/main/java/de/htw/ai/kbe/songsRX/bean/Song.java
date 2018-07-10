package de.htw.ai.kbe.songsRX.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="song")
@XmlRootElement(name = "song")
public class Song {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="title")
	private String title;
	@Column(name="artist")
	private String artist;
	@Column(name="album")
	private String album;
	@Column(name="released")
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
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@XmlElement
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	@XmlElement
	public String getAlbum() {
		return album;
	}
	
	public void setAlbum(String album){
		this.album = album;
	}
	
	@XmlElement
	public Integer getReleased() {
		return released;
	}
	
	public void setReleased(Integer released){
		this.released = released;
	}
}
