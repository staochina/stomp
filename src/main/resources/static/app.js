function rnd(n, m){
        var random = Math.floor(Math.random()*(m-n+1)+n);
        return random;
    }

var stompClient = null;
/**
 设置用户信息
 */
var uid = rnd(1,3);
var name = 'user_'+uid;

var t = setTimeout(function(){
   console.info($("#uid").html());
   $("#uid").append("<h2>" + uid +"</h2>");
 },500)

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/message-service');
    stompClient = Stomp.over(socket);
    var headers ={'uid':uid,'name':name};
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        var publicChannel ="/topic/public";
        stompClient.subscribe('/topic/public', function (greeting) {
            console.info('subscribe url :'+publicChannel);
            showGreeting(JSON.parse(greeting.body).content);
        });
        var userChannel ='/user/'+ uid +'/topic';
        stompClient.subscribe(userChannel, function (greeting) {
            console.info('subscribe url :'+userChannel);
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function getContent(){
   return $("#content").val();
}

function sendMsg() {
    var ak = new Date().getTime() +"";
    stompClient.send("/app/sendMsg", {} , JSON.stringify({'ak':ak,'senderId': uid,'content': getContent() }) );
}

function sendUser() {
    var ak = new Date().getTime() +"";
    stompClient.send("/app/sendMsg", {} , JSON.stringify({'ak':ak,'senderId': uid,'receiverId': 3 - uid , 'content':getContent() }) );
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
    $( "#sendUser" ).text("Send to user: " + (3 - uid));
    $( "#sendUser" ).click(function() { sendUser(); });
});