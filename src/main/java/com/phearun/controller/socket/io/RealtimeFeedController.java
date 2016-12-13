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
public class RealtimeFeedController {

	private SocketIONamespace nspPost;


	@Autowired
	public RealtimeFeedController(SocketIOServer server) {
		
		this.nspPost = server.addNamespace("/post");
		
		this.nspPost.addConnectListener(onConnect);
		this.nspPost.addDisconnectListener(onDisconnect);
		
		//TODO: listening on "new post" event
		this.nspPost.addEventListener("new post", Feed.class, onPostEvent);
		
		//TODO: listening on "remove post" event
		this.nspPost.addEventListener("remove post", String.class, onRemovePostEvent);
	}
	
	private ConnectListener onConnect = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			System.out.println("Client connected! " + client.getSessionId());
		}
	};
	
	private DataListener<Feed> onPostEvent = new DataListener<Feed>() {
		@Override
		public void onData(SocketIOClient client, Feed data, AckRequest ackSender) throws Exception {
			//TODO: broadcast "new post" to all connected client
			nspPost.getBroadcastOperations().sendEvent("new post", data);
			
			//TODO: 
			ackSender.sendAckData("Message Send!");
		}
	};
	
	private DataListener<String> onRemovePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			
		}
	};
	
	private DisconnectListener onDisconnect = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			System.out.println("Client Disconnected! " + client.getSessionId());
		}
	};
	
	
}
