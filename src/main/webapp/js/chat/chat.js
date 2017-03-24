$(document).ready(function(){
    var user = $.url().param('u');
    $("#useName").html("Logged in User: " + user)


    var wsConnection;
    var channelReady = false;

    $("#endChatButton").click(function() {
        if(channelReady && wsConnection.readyState === WebSocket.OPEN) {
            wsConnection.close();
        }
    });
    initRtc("ws://localhost:8080/vmr")

    function initRtc(sURL) {
        openChannel(sURL);
    };

    function openChannel(signalingURL) {
        channelReady = false;
        wsConnection = new WebSocket(signalingURL);
        wsConnection.onopen = onChannelOpened;
        wsConnection.onmessage = onChannelMessage;
        wsConnection.onclose = onChannelClosed;
    };

    function onChannelOpened() {
        channelReady = true;
        console.log("Connnected to Signalling server")
    };

    function onChannelMessage(message) {
        processSignalingMessage(message.data);
    };

    function onChannelClosed() {
        channelReady = false;
        console.log("Disconnnected from Signalling server")
    };

    function sendMessageToSignalServer(message) {
        var msgString = JSON.stringify(message);
        wsConnection.send(msgString);
    };


//    var chatForm = $("#chat_form")
//    $("#chat_submit_btn").click(function( event ) {
//      var message = $("#chat_input").val();
//      var msg = JSON.stringify({"type" : "chatmessage", "txt" : message});
//      sendDataMessage(msg);
//    });
//
//    function onRoomReceived(room) {
//        var st = $("#status")
//        st.html("Now, if somebody wants to join you, should use this link: <a href=\""+window.location.href+"?room="+room+"\">"+window.location.href+"?room="+room+"</a>");
//    };
//
//    function onPrivateMessageReceived(txt) {
//        var t = $("#chat").html();
//        t += "<br>" + txt;
//        $("#chat").html(t);
//    }
});

