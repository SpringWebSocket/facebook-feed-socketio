package com.phearun.controller.socket.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
import com.phearun.model.UploadFile;

@Component
public class FeedSocketController {
	
	private SocketIONamespace nspFeed;
	
	@Autowired
	public FeedSocketController(SocketIOServer server) {
		//TODO: create feed name space
		this.nspFeed = server.addNamespace("/feed");
		
		//TODO: onConnect event listener
		this.nspFeed.addConnectListener(onConnect);
		
		//TODO: onDisconnect event listener
		this.nspFeed.addDisconnectListener(onDisconnect);
		
		//TODO: onCustom event listener
		this.nspFeed.addEventListener("message", Feed.class, onFeed);
		
		//TODO: handle client join room
		this.nspFeed.addEventListener("join", String.class, onJoinRoom);
		
		//TODO: handle client leave room
		this.nspFeed.addEventListener("leave", String.class, onLeaveRoom);

		//TODO: client message to room
		this.nspFeed.addEventListener("toroom", String.class, onSendMessageToRoom);
	
		//TODO: handle binary data from client
		this.nspFeed.addEventListener("binary", byte[].class, onBinaryHandler);
		
		//TODO: handle binary data from client
		this.nspFeed.addEventListener("object", UploadFile.class, onBinaryObjectHandler);
		
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
	
	private DataListener<byte[]> onBinaryHandler = new DataListener<byte[]>() {
		@Override
		public void onData(SocketIOClient client, byte[] data, AckRequest ackSender) throws Exception {
			System.out.println("binary data: " + data);
			
			/*FileOutputStream fos = new FileOutputStream("test.html");
			fos.write(data);
			fos.close();*/
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("test.png")));
			bos.write(data);
			bos.close();
		}
			
	};
	
	private DataListener<UploadFile> onBinaryObjectHandler = new DataListener<UploadFile>() {
		@Override
		public void onData(SocketIOClient client, UploadFile uploadFile, AckRequest ackSender) throws Exception {
			System.out.println("binary data: " + uploadFile);
		}
			
	};
}
