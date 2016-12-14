package com.phearun.model;

public class Chat {
	private String username;
	private String message;
	
	public Chat(){}
	
	public Chat(String username, String message) {
		super();
		this.username = username;
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Chat [username=" + username + ", message=" + message + "]";
	}
}
