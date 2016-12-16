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
import com.phearun.model.Chat;

@Component
public class ChatController {

	private SocketIONamespace nspChat;
	
	@Autowired
	public ChatController(SocketIOServer server){
		this.nspChat = server.addNamespace("/chat");
		
		this.nspChat.addConnectListener(onConnectEvent);
		this.nspChat.addDisconnectListener(onDisconnectEvent);
		this.nspChat.addEventListener("message", Chat.class, onChatEvent);
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
			System.out.println("Disconnected from /chat namespace! " + client.getSessionId());
		}
	};
	private DataListener<Chat> onChatEvent = new DataListener<Chat>() {
		@Override
		public void onData(SocketIOClient client, Chat chat, AckRequest ackSender) throws Exception {
			nspChat.getBroadcastOperations().sendEvent("message", chat);
			System.out.println("Chat /chat namespace: " + chat);
			ackSender.sendAckData("Message sent!");
		}
	};
	
}
