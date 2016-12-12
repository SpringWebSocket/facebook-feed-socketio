package com.phearun.controller.socket.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.phearun.model.Feed;

@Component
public class FeedSocketController {
	
	private SocketIONamespace nspFeed;
	
	@Autowired
	public FeedSocketController(SocketIOServer server) {
		
		this.nspFeed = server.addNamespace("/feed");
		
		//TODO: onConnect event listener
		this.nspFeed.addConnectListener(onConnect);
		
		//TODO: onDisconnect event listener
		this.nspFeed.addDisconnectListener(onDisconnect);
		
		//TODO: onCustom event listener
		this.nspFeed.addEventListener("message", Feed.class, onFeed);
		
		//TODO: client join room
		this.nspFeed.addEventListener("join", String.class, onJoinRoom);
		
		//TODO: client join room
		this.nspFeed.addEventListener("leave", String.class, onLeaveRoom);

		//TODO: client message to room
		this.nspFeed.addEventListener("toroom", String.class, onSendMessageToRoom);
		
	}
	
	private ConnectListener onConnect = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			System.out.println("getSessionId: " + client.getSessionId());
		}
	};
	
	private DisconnectListener onDisconnect = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			System.out.println("getSessionId: " + client.getSessionId());
		}
	};
	
	private DataListener<Feed> onFeed = new DataListener<Feed>() {
		@Override
		public void onData(SocketIOClient client, Feed feed, AckRequest ackSender) throws Exception {
			System.out.println("getAllRooms: " + client.getAllRooms());
			System.out.println("getNamespace: " + client.getNamespace().getName());
			System.out.println("getTransport: " + client.getTransport());
			System.out.println(feed);
		}
	};
	
	private DataListener<String> onJoinRoom = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String room, AckRequest ackSender) throws Exception {
			System.out.println("Client joining room: " + room);
			client.joinRoom(room);
			client.getNamespace().getRoomOperations(room).sendEvent("joined", "somebody joined room!");
			ackSender.sendAckData("Joined!");
		}
	};
	
	private DataListener<String> onSendMessageToRoom = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String message, AckRequest ackSender) throws Exception {
			System.out.println("Sending message to [Android Room]...");
			client.getNamespace().getRoomOperations("Android Room").sendEvent("room-message", message);
			ackSender.sendAckData("Sent!");
		}
	};
	
	private DataListener<String> onLeaveRoom = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String room, AckRequest ackSender) throws Exception {
			System.out.println("Leaving [Android Room]...");
			client.leaveRoom(room);
			client.getNamespace().getRoomOperations(room).sendEvent("left", "somebody left room!");
			ackSender.sendAckData("Left!");
		}
	};
}
