package com.phearun.controller.socket.io;

import java.util.ArrayList;
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
import com.phearun.model.User;
import com.phearun.service.FeedService;

@Component
public class RealtimeFeedController {

	private SocketIONamespace nspPost;
	
	private ArrayList<User> allUsers = new ArrayList<>();
	
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
		
		//TODO: listening on "typing " event 
		this.nspPost.addEventListener("typing", String.class, onTypingEvent);
		
		//TODO: listening on "typing " event 
		this.nspPost.addEventListener("stop typing", String.class, onStopTypingEvent );
		
		this.nspPost.addEventListener("new user", User.class, onNewUserEvent );
	}
	
	//TODO: connect handler
	private ConnectListener onConnect = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			System.out.println("Connected to /post namespace : " + client.getSessionId());
			
			List<Feed> feeds = feedService.findAll();
			if(!feeds.isEmpty()){
				//TODO: send data back to sender
				client.sendEvent("all posts", feeds);
				//nspPost.getClient(client.getSessionId()).sendEvent("all posts", feeds);				
			}
			System.out.println("onConnect - getTransport: "+ client.getTransport());
		}
	};
	
	//TODO: post status handler
	private DataListener<Feed> onPostEvent = new DataListener<Feed>() {
		@Override
		public void onData(SocketIOClient client, Feed feed, AckRequest ackSender) throws Exception {
			
			//TODO: save to database
			if(feedService.save(feed)){
				
				//TODO: broadcast "new post" to all connected client
				nspPost.getBroadcastOperations().sendEvent("new post", feed);
				
				//TODO: response back to sender
				ackSender.sendAckData("Status Posted!");
			}
			
			System.out.println("Feed /post : " + feed);
			System.out.println("onPost - getTransport: "+ client.getTransport());
		}
	};
	
	//TODO: remove post handler
	private DataListener<String> onRemovePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			if(feedService.remove(id)){
				//nspPost.getBroadcastOperations().sendEvent("all posts", feedService.findAll());
				nspPost.getBroadcastOperations().sendEvent("removed post", id);
			}
		}
	};

	//TODO: like post handler
	private DataListener<String> onLikePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			nspPost.getBroadcastOperations().sendEvent("update like", feedService.updateLike(id), id);
		}
	};
	
	//TODO: disconnect handler
	private DisconnectListener onDisconnect = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			allUsers.remove(User.findById(allUsers, client.getSessionId()));
			
			nspPost.getBroadcastOperations().sendEvent("user offline", client, client.getSessionId());
			
			System.out.println("Disconnected to /post namespace : " + client.getSessionId());
		}
	};
	
	//TODO: typing handler
	private DataListener<String> onTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			//TODO: send to all connected namespace user exclude sender
			nspPost.getBroadcastOperations().sendEvent("typing", client, username);
			
			//nspPost.getBroadcastOperations().sendEvent("typing", username);
		}
	};
	
	//TODO: stop typing handler
	private DataListener<String> onStopTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			nspPost.getBroadcastOperations().sendEvent("stop typing", username);
		}
	};
	
	//TODO: new user
	private DataListener<User> onNewUserEvent = new DataListener<User>() {
		@Override
		public void onData(SocketIOClient client, User user, AckRequest ackSender) throws Exception {
			allUsers.add(0, user);
			nspPost.getBroadcastOperations().sendEvent("new user", client, user);				
			ackSender.sendAckData(allUsers);
		}
	};
}
