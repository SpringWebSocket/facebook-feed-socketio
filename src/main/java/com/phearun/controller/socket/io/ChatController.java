package com.phearun.controller.socket.io;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.phearun.model.Chat;

@Component
public class ChatController {

	private SocketIONamespace nspChat;
	private Map<SocketIOClient, String> chatUsers = new HashMap<>();
	
	@Autowired
	public ChatController(SocketIOServer server){
		this.nspChat = server.addNamespace("/chat");
		
		this.nspChat.addConnectListener(onConnectEvent);
		this.nspChat.addDisconnectListener(onDisconnectEvent);
		this.nspChat.addEventListener("message", Chat.class, onChatEvent);
		this.nspChat.addEventListener("join", String.class, onUserJoinEvent);
		this.nspChat.addEventListener("typing", String.class, onUserTypingEvent);
		this.nspChat.addEventListener("stop typing", String.class, onUserStopTypingEvent);
	}
	
	private ConnectListener onConnectEvent = new ConnectListener() {
		@Override
		public void onConnect(SocketIOClient client) {
			System.out.println("Connected to /chat namespace! " + client.getSessionId());
		}
	};
	
	private DisconnectListener onDisconnectEvent = new DisconnectListener() {
		@Override
		public void onDisconnect(SocketIOClient client) {
			nspChat.getBroadcastOperations().sendEvent("leave", chatUsers.get(client));
			chatUsers.remove(client);			
			System.out.println("Disconnected from /chat namespace! " + client.getSessionId());
		}
	};
	
	private DataListener<Chat> onChatEvent = new DataListener<Chat>() {
		@Override
		public void onData(SocketIOClient client, Chat chat, AckRequest ackSender) throws Exception {
			nspChat.getBroadcastOperations().sendEvent("message", client, chat);
			System.out.println("Chat /chat namespace: " + chat);
			ackSender.sendAckData("Message sent!");
		}
	};
	
	private DataListener<String> onUserJoinEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			chatUsers.put(client, username);
			nspChat.getBroadcastOperations().sendEvent("join", username);
		}
	};
	
	private DataListener<String> onUserTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			nspChat.getBroadcastOperations().sendEvent("typing", client, username);
		}
	};

	private DataListener<String> onUserStopTypingEvent = new DataListener<String>() {
		@Override
		public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
			nspChat.getBroadcastOperations().sendEvent("stop typing", client, username);
		}
	};
	
	
}
