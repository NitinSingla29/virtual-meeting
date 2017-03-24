$(document).ready(function(){
    var user = $.url().param('u');

    var wsConnection;
    var channelReady = false;

    $("#endChatButton").click(function() {
        if(channelReady && wsConnection.readyState === WebSocket.OPEN) {
            wsConnection.close();
        }
    });


    function sendMessageToSignalServer(message) {
        var msgString = JSON.stringify(message);
        wsConnection.send(msgString);
    };

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
        $("#useName").html("Hi! " + user)
        joinChatRoom();
    };

    function joinChatRoom() {
        var meetingRoomRequest = {};
        meetingRoomRequest["requestType"] = "JOIN_ROOM";
        meetingRoomRequest["userName"] = user
        console.log(JSON.stringify(meetingRoomRequest));
        sendMessageToSignalServer(meetingRoomRequest);
    }

    function onChannelClosed() {
        channelReady = false;
        $("#useName").html("Logged in User: ")
        console.log("Disconnnected from Signalling server")
    };

    function onChannelMessage(message) {
        processSignalingMessage(message.data);
    };

    function processSignalingMessage(message) {
        var msg = JSON.parse(message);

        if (msg.requestType === 'offer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
            doAnswer();
        } else if (msg.requestType === 'answer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
        } else if (msg.requestType === 'candidate') {
            var candidate = new RTCIceCandidate({sdpMLineIndex:msg.label, candidate:msg.candidate});
            peerConnection.addIceCandidate(candidate);
        } else if (msg.requestType === 'JOIN_ROOM') {
            var room = msg.meetingRoom;
            onRoomReceived(room);
        } else if (msg.requestType === 'WRONGROOM') {
            window.location.href = "/";
        }
    };

    function onRoomReceived(room) {
        var st = $("#status")
        st.html("Now, if somebody wants to join you, should use this link: <a href=\""+window.location.href+"?room="+room+"\">"+window.location.href+"?room="+room+"</a>");
    };


//    var chatForm = $("#chat_form")
//    $("#chat_submit_btn").click(function( event ) {
//      var message = $("#chat_input").val();
//      var msg = JSON.stringify({"type" : "chatmessage", "txt" : message});
//      sendDataMessage(msg);
//    });



//    function onPrivateMessageReceived(txt) {
//        var t = $("#chat").html();
//        t += "<br>" + txt;
//        $("#chat").html(t);
//    }
});

