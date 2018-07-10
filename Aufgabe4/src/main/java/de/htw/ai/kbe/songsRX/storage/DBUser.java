package de.htw.ai.kbe.songsRX.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.songsRX.bean.User;

@Singleton
public class DBUser implements UserI{

	private static Map<Integer, User> storage;
	private static Map<String, String> tokenStorage;
	
	private EntityManagerFactory emf;
	Query q;
	
	@Inject
	public DBUser(EntityManagerFactory emf){
		storage = new ConcurrentHashMap<Integer,User>();
		tokenStorage = new ConcurrentHashMap<String, String>();
		//initUserDB("users.json");
		this.emf = emf;
	}
	
	/*private void initUserDB(String filename){
		List<User> userList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream input = getClass().getClassLoader().getResourceAsStream("/" + filename);
		try {
			userList = (List<User>) objectMapper.readValue(input, new TypeReference<List<User>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i=0; i<userList.size();i++){
			storage.put(userList.get(i).getId(), userList.get(i));
		}
	}*/

	@Override
	public boolean tokenExists(String token) {
		return tokenStorage.containsKey(token);
	}

	@Override
	public String getToken(String userId) {
		if(tokenStorage.containsValue(userId)){
			for(String key : tokenStorage.keySet()){
				if(tokenStorage.get(key).equals(userId))
					return key;
			}
		}
		return null;
	}

	@Override
	public String getUserId(String token) {
		if(tokenStorage.containsKey(token)){
			return tokenStorage.get(token);
		}
		else {
			return null;
		}
	}

	@Override
	public boolean userExists(String userId) {
		/*for(Map.Entry<Integer, User> entry : storage.entrySet()) {
			//Integer key = entry.getKey();
			User value = entry.getValue();
			
			if(value.getUserId().equals(userId)) {
				return true;
			}
			
		}*/
		
		EntityManager em = emf.createEntityManager();
		q = em.createQuery("SELECT u FROM User u");
		@SuppressWarnings("unchecked")
		List<User> userList = q.getResultList();
		
		for(int i=0; i<userList.size(); i++) {
			if(userList.get(i).getUserId().equals(userId)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public User test(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveToken(String userId, String token) {
		int id = 0;
		EntityManager em = emf.createEntityManager();
		q = em.createQuery("SELECT u FROM User u");
		
		List<User>userList = q.getResultList();
		
		for(int i=0; i<userList.size(); i++) {
			if(userList.get(i).getUserId().equals(userId)) {
				id = i;
			}
		}
		tokenStorage.put(token, userList.get(id).getUserId());
	}
}
