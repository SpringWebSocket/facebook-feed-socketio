package com.phearun.controller.socket.io;

import java.util.List;

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
import com.phearun.service.FeedService;

@Component
public class RealtimeFeedController {

	private SocketIONamespace nspPost;
	
	@Autowired
	private FeedService feedService;

	@Autowired
	public RealtimeFeedController(SocketIOServer server) {
		
		this.nspPost = server.addNamespace("/post");
		
		this.nspPost.addConnectListener(onConnect);
		this.nspPost.addDisconnectListener(onDisconnect);
		
		//TODO: listening on "new post" event
		this.nspPost.addEventListener("new post", Feed.class, onPostEvent);
		
		//TODO: listening on "remove post" event
		this.nspPost.addEventListener("remove post", String.class, onRemovePostEvent);
		
		//TODO: listening on "like post" event
		this.nspPost.addEventListener("like post", String.class, onLikePostEvent);
	}
	
	private ConnectListener onConnect = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			System.out.println("Client connected! " + client.getSessionId());
			
			List<Feed> feeds = feedService.findAll();
			nspPost.getClient(client.getSessionId()).sendEvent("all posts", feeds);
			
			System.out.println("onConnect - getTransport: "+ client.getTransport());
			
		}
	};
	
	private DataListener<Feed> onPostEvent = new DataListener<Feed>() {
		@Override
		public void onData(SocketIOClient client, Feed feed, AckRequest ackSender) throws Exception {
			//TODO: broadcast "new post" to all connected client
			nspPost.getBroadcastOperations().sendEvent("new post", feed);
			
			//TODO: 
			ackSender.sendAckData("Message Send!");
			
			//TODO: save to database
			feedService.save(feed);
			
			System.out.println("onPost - getTransport: "+ client.getTransport());
		}
	};
	
	private DataListener<String> onRemovePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			if(feedService.remove(id)){
				//nspPost.getBroadcastOperations().sendEvent("all posts", feedService.findAll());
				nspPost.getBroadcastOperations().sendEvent("removed post", id);
			}
		}
	};

	private DataListener<String> onLikePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			
			nspPost.getBroadcastOperations().sendEvent("update like", feedService.updateLike(id), id);
		}
	};
	
	private DisconnectListener onDisconnect = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			System.out.println("Client Disconnected! " + client.getSessionId());
		}
	};
}
