package com.phearun.controller.socket.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
	
	//TODO: store all connected user on memory
	private Map<SocketIOClient, User> allUsers = new HashMap<>();
	
	@Autowired
	private FeedService feedService;

	@Autowired
	public RealtimeFeedController(SocketIOServer server) {
		
		//TODO: let server listen to /post namespace
		this.nspPost = server.addNamespace("/post");
		
		//TODO: fired after the client established the connection
		this.nspPost.addConnectListener(onConnect);
		
		//TODO: fired after the client disconnect from the server
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
		
		//TODO: listening on "new user" event
		this.nspPost.addEventListener("new user", User.class, onNewUserEvent );
	}
	
	//TODO: connect handler
	private ConnectListener onConnect = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			
			System.out.println("Connected to /post namespace : " + client.getSessionId());
			
			List<Feed> feeds = feedService.findAll();
			if(!feeds.isEmpty()){
				System.out.println("Sending!!!");
				
				//TODO: send data back to sender
				//client.sendEvent("all posts", feeds);
				//or nspPost.getClient(client.getSessionId()).sendEvent("all posts", feeds);		
				
				//TODO: problem with iOS, can't receive event immediately. for android use above code, it works fine
				//delay 1 second before send back to the current connected client
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						//send to current connected client only
						//or client.sendEvent("all posts", feeds);
						nspPost.getClient(client.getSessionId()).sendEvent("all posts", feeds);
					}
				}, 1000);
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
				//send to all connected client
				nspPost.getBroadcastOperations().sendEvent("removed post", id);
			}
		}
	};

	//TODO: like post handler
	private DataListener<String> onLikePostEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String id, AckRequest ackSender) throws Exception {
			//TODO send multiple payload to client (like and id)
			nspPost.getBroadcastOperations().sendEvent("update like", feedService.updateLike(id), id);
		}
	};
	
	//TODO: disconnect handler
	private DisconnectListener onDisconnect = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			
			//TODO: remove user
			allUsers.remove(client);
			
			//send to all connected client
			nspPost.getBroadcastOperations().sendEvent("user offline", client.getSessionId());
			
			System.out.println("Disconnected to /post namespace : " + client.getSessionId());
		}
	};
	
	//TODO: typing handler
	private DataListener<String> onTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			
			//TODO: send to all connected namespace user exclude sender
			nspPost.getBroadcastOperations().sendEvent("typing", client, username);
		}
	};
	
	//TODO: stop typing handler
	private DataListener<String> onStopTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			//TODO: send to all connected client exclude sender
			nspPost.getBroadcastOperations().sendEvent("stop typing", client, username);
		}
	};
	
	//TODO: new user event handler
	private DataListener<User> onNewUserEvent = new DataListener<User>() {
		@Override
		public void onData(SocketIOClient client, User user, AckRequest ackSender) throws Exception {
			
			//add new user to map
			allUsers.put(client, user);
			
			//send to all connected client exclude sender
			nspPost.getBroadcastOperations().sendEvent("new user", client, user);
			
			//TODO: send acknowledgement back to the sender ( response back to the sender)
			ackSender.sendAckData(allUsers.values());
		}
	};
}
