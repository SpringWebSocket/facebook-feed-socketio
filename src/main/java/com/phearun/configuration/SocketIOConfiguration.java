package com.phearun.configuration;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

@Configuration
public class SocketIOConfiguration {
	
	@Bean
	public com.corundumstudio.socketio.Configuration socketConfig(){
		com.corundumstudio.socketio.Configuration socketConfig = new com.corundumstudio.socketio.Configuration();
	    socketConfig.setHostname("localhost");
	    socketConfig.setPort(3000);
	    return socketConfig;
	}
	
	@Bean
	public SocketIOServer socketIOServer(){
	    SocketIOServer server = new SocketIOServer(socketConfig());
	    System.out.println("Starting SocketIO Server...");
		server.start();
		//server.startAsync();
		return server;
	}
	
	//For enable socket.io annotation ( @onConnect, @onEvent,...)
	@Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }

	@PreDestroy
	public void stopSocketIOServer(){
		System.out.println("Stopping SocketIO Server...");
		socketIOServer().stop();
	}
	
}

