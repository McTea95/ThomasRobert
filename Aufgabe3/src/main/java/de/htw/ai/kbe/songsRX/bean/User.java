package de.htw.ai.kbe.songsRX.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class User {

	private Integer id;
	private String userId;
	private String firstName;
	private String lastName;
	
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
	
	@XmlElement
	public String getFirstName(){
		return firstName;
	}
	
	@XmlElement
	public String getLastName(){
		return lastName;
	}
}
