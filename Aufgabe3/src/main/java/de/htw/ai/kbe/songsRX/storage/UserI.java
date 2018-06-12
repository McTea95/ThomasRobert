package de.htw.ai.kbe.songsRX.storage;

import de.htw.ai.kbe.songsRX.bean.User;

public interface UserI {

	public boolean tokenExists(String token);
	public String getToken(String userId);
	
	public String getUserId(String token);
	public boolean userExists(String userId);
	public User test(String userId);
	public void saveToken(String userId, String token);
}
