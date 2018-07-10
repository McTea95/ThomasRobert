package de.htw.ai.kbe.songsRX.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user")
@XmlRootElement(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="user_id")
	private String userId;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	
	@OneToMany(mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER) 
	Set<SongList> songList;
	
	public User() {}
	
	public User (Integer id, String userId, String firstName, String lastName){
		this.id = id;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@XmlElement
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	@XmlElement
	public String getUserId(){
		return userId;
	}
	
	public void setUserId(String uid){
		this.userId = uid;
	}
	
	@XmlElement
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String fn){
		this.firstName = fn;
	}
	
	@XmlElement
	public String getLastName(){
		return lastName;
	}
	
	public void setLastName(String ln){
		this.lastName = ln;
	}
	
	@JsonIgnore
	public Set<SongList> getSongListSet() {
        if(songList == null) {
        	songList = new HashSet<>();
        }
        return songList;
    }
	@JsonIgnore
    public void setSongList(Set<SongList> songList) {
        this.songList = songList;
        
        if(songList != null) {
            this.songList.forEach(a-> a.setUser(this));
        }
    }
    
    public void addSongList(SongList list) {
        if(songList == null) {
            songList = new HashSet<>();
        }
        list.setUser(this);
        this.songList.add(list);
    }
}
