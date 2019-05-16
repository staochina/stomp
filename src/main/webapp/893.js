function rnd(n, m){
        var random = Math.floor(Math.random()*(m-n+1)+n);
        return random;
    }

var stompClient = null;
/**
 设置用户信息
 */
var uid = 893;
var name = 'user_'+uid;
var to_id =1099;

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
        //var userChannel ='/user/'+ uid +'/queue';
        var userChannel ='/user/queue';
        console.info('subscribe channel :'+userChannel);
        stompClient.subscribe(userChannel, function (greeting) {
            var receiveMsg =JSON.parse(greeting.body);
            console.info('get msg :'+ receiveMsg);
            appendMessage(receiveMsg);
            var back ={id: receiveMsg.id,toId:receiveMsg.toId};
            callBack(back);
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
}

function sendUser() {
    stompClient.send("/app/sendMsg", {} , JSON.stringify({ 'toId':to_id , 'content':getContent(),'type':"STATIC" }) );
}

function callBack(message) {
//  {id: receiveMsg.id,toId:receiveMsg.toId}
    stompClient.send("/app/callBack", {} , JSON.stringify(message) );
}

function appendMessage(message) {
    $("#messages").append("<tr><td>" + JSON.stringify(message) + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
    $( "#sendUser" ).text("Send to user: " + to_id );
    $( "#sendUser" ).click(function() { sendUser(); });
});