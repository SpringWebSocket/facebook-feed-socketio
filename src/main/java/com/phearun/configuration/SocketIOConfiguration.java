package com.phearun.configuration;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

@Configuration
public class SocketIOConfiguration {
	
	@Value("${socket.io.host}")
	public String SOCKET_IO_HOST;
	
	@Value("${socket.io.port}")
	public Integer SOCKET_IO_PORT;
	
	@Bean
	public com.corundumstudio.socketio.Configuration socketConfig(){
		com.corundumstudio.socketio.Configuration socketConfig = new com.corundumstudio.socketio.Configuration();
		
		if(System.getenv("PORT")!=null){
			System.out.println("PORT: " + System.getenv("PORT"));
			SOCKET_IO_PORT = Integer.valueOf(System.getenv("PORT"));
			SOCKET_IO_HOST = "spring-pagination.herokuapp.com";
		}
		
	    socketConfig.setHostname(SOCKET_IO_HOST);
	    socketConfig.setPort(SOCKET_IO_PORT);
	    
	    socketConfig.getTransports().forEach(transport->{
	    	System.out.println("Supported Transport: " + transport.name());
	    });
	    //TODO: set the maximum payload data 
	    socketConfig.setMaxFramePayloadLength(1*1024*1024); // megabytes * kilobytes * bytes
	    
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

