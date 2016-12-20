var username = prompt("What is your name?", "Anonymous" + Math.floor(Math.random() * 100));
var Chat = {};

var socketServerUrl = location.protocol + "//" + location.hostname + ":3000";
var nspChat = socketServerUrl + "/chat";

var socket = io.connect(nspChat);

socket.on('connect', function(){
	console.log('connected!');
	socket.emit('new user', username);
});

socket.on('message', function(chat){
	$('#chatBox').append(Chat.render(chat));
	
	
});

socket.on('new user', function(username){
	Chat.renderJoin(username);
});

$("#txtMessage").keyup(function(event){
    if(event.keyCode == 13){
    	socket.emit('message', {username: username, message: $('#txtMessage').val()});
    	$('#txtMessage').val("");
    }
});

Chat.render = function(chat){
	return `<p>${chat.username}: ${chat.message}</p>`;
};

Chat.renderJoin = function(username){
	$('#chatBox').append(`<center><p class="join">${username} joined!</p></center>`);
};