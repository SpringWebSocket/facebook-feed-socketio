var username = prompt("What is your name?", "Anonymous" + Math.floor(Math.random() * 100));
var Chat = {};

var typingTimer;

var socketServerUrl = location.protocol + "//" + location.hostname + ":3000";
var nspChat = socketServerUrl + "/chat";

var socket = io.connect(nspChat);

socket.on('connect', function(){
	console.log('connected!');
	socket.emit('join', username, function(chatHistory){
		console.log(chatHistory);
	});
});

socket.on('message', function(chat){
	Chat.other(chat);
});

socket.on('join', function(username){
	Chat.join(username);
});

$("#txtMessage").keyup(function(event){
	
	//TODO: on typing event
	socket.emit('typing', username);
	clearTimeout(typingTimer);
	typingTimer = setTimeout(function () {
  		socket.emit('stop typing', username);
	}, 1000);
	
	//TODO: on enter key
    if(event.keyCode == 13){
    	socket.emit('stop typing', username);
    	var chat = {username: username, message: $('#txtMessage').val()};
    	socket.emit('message', chat, function(){
    		$('#txtMessage').val("");
    	});
    	Chat.self(chat);
    }
});

socket.on('leave', function(username){
	Chat.leave(username);
});

socket.on('typing', function(username){
	Chat.typing(username);
});

socket.on('stop typing', function(username){
	Chat.stopTyping(username);
});

Chat.other = function(chat){
	Chat.render(`<div class="other"><p>${chat.username}: ${chat.message}</p></div>`);
};

Chat.self = function(chat){
	Chat.render(`<div class="self"><p>${chat.message}</p></div>`);
};

Chat.join = function(username){
	Chat.render(`<center><p class="join">${username} Joined!</p></center>`);
};

Chat.leave = function(username){
	Chat.render(`<center><p class="left">${username} Left!</p></center>`);
};

Chat.typing = function(username){
	Chat.replaceRender(`<center><p class="typing">${username} is typing...</p></center>`);
};

Chat.stopTyping = function(username){
	$('.typing').parent().remove();
};

Chat.render = function(template){
	$('#chat').append(template);
	Chat.scrollBottom();
};

Chat.replaceRender = function(template){
	$('#typing').html(template);
	Chat.scrollBottom();
};

Chat.scrollBottom = function(){
	$('#chatBox').scrollTop($('#chatBox').prop('scrollHeight'));
};

