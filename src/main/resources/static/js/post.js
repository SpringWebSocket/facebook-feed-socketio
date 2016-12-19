"use strict";
// $(document).ready(function(){
	var username = prompt("What is your name?", "Anonymous" + Math.floor(Math.random() * 100));
	var Post = {};
	var User = {};
	
	var socketServerUrl = location.protocol + "//" + location.hostname + ":23456";
	var nspPost = socketServerUrl + "/post";
	
	var socketOptions = {
		upgrade: false,
		transports: ['websocket', 'flashsocket', 'htmlfile', 'xhr-polling', 'jsonp-polling']
	};
	var socket = io.connect(nspPost/* , socketOptions */);
	var typingTimer;
	
	socket.on('connect', function () {
	  	console.log('connected to server', socket.id);
	  	socket.emit('new user', { id: socket.id, username: username}, function(users){
	  		console.log('all users:', users);
	  		var htmlUsers = ``;
			users.forEach(function(user){
				 htmlUsers += User.render(user);
			});
			$('div.online-user').html(htmlUsers);
	  	});
	
	  	$('button#btnPost').on('click', function () {
	  		if($('#statusText').val().trim()=='') return;
	        socket.emit('new post', { username: username, text: $('#statusText').val() });
	    });
	
	 	$('textarea#statusText').on('keyup', function () {
	    	socket.emit('typing', username);
	    	clearTimeout(typingTimer);
	    
	    	typingTimer = setTimeout(function () {
	      		socket.emit('stop typing', username);
	    	}, 1000);
	  	});
	});
	
	socket.on('all posts', function (datas) {
		console.log(arguments);
	  	var post = "";
	  	datas.forEach(function (data, index) {
	    	post += Post.render(data);
	  	});
	  	$('div#posts').html(post);
	});
	
	socket.on('new post', function (data) {
		$('div#posts').prepend(Post.render(data));
	  	$('textarea#statusText').val('');
	});
	
	socket.on('update like', function (like, id) {
		console.log(arguments);
	  	$("#like" + id).html(like);
	});
	
	socket.on('removed post', function (id) {
		$("#" + id).parent().remove();
	});
	
	socket.on('typing', function (username) {
	    console.log(username, 'is typing...!');
	    var html = '<div class="typingStatus"><img src="/static/images/comment-pink.svg"></div>';
	    $('div#typing').html(html);
	});
	
	socket.on('stop typing', function (username) {
		console.log(username, 'stop typing...!');
		$('div#typing').find('div').remove();
	});
	
	Post.render = function (data) {
	    return "<div class=\"post\"><p>" + data.username + "</p><hr><h3>" + data.text + "</h3><button id=\"" + data.id + "\" onClick=\"Post.like(this)\" class=\"btn btn-primary\"><span id=\"like" + data.id + "\">" + data.like + "</span> Like</button><button id=\"" + data.id + "\" onClick=\"Post.remove(this)\" class=\"btn btn-danger\">Remove</button></div>";
	};
	
	Post.remove = function (button) {
	  	socket.emit('remove post', button.id);
	};
	
	Post.like = function (button) {
	  	socket.emit('like post', button.id);
	};
	
	// TODO: user's block
	socket.on('new user', function(user){
		console.log('new user:', user);
		$('div.online-user').prepend(User.render(user));
	});
	
	socket.on('user offline', function(userId){
		$(`span#${userId}`).parent().remove();
	});
	
	User.render = function(user){
		return `<div class="user">
					<img class="profile" src="/static/images/img.svg">
					<span id="${user.id}">${user.username}</span>
					<img class="online-status" src="/static/images/online-status.svg">
				</div>`;
	};
// });
