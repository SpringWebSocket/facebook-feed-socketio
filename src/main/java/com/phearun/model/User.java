package com.phearun.model;

import java.util.List;
import java.util.UUID;

public class User {
	private UUID id;
	private String username;
	
	public User() {
		super();
	}
	public User(UUID id, String username) {
		super();
		this.id = id;
		this.username = username;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}
	
	public static User findById(List<User> users, UUID id){
		for(User user: users)
			if(user.getId().equals(id))
				return user;
		return null;
	}
}
