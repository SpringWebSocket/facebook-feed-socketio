package com.phearun.controller.socket.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;

@Configuration
public class RootNamespaceController {

	private SocketIOServer server;
	
	@Autowired
	public RootNamespaceController(SocketIOServer server) {
		this.server = server;
	}
	
	@OnConnect
	public void onConnect(SocketIOClient client){
		System.out.println("Connected to / namespace");
	}	
	
	@OnConnect
	public void onDisconnect(SocketIOClient client){
		System.out.println("Disconnected from / namespace");
	}	
	
}
