var wsConnection;
var channelReady = false;

function sendMessageToSignalServer(message) {
    var msgString = JSON.stringify(message);
    wsConnection.send(msgString);
};

function onChatMessageReceive(user, message) {
    $("#messageList").append('<li>' + user + ': ' + message + '</li>');
}

$(document).ready(function(){
    var user = $.url().param('u');

    $("#endChatButton").click(function() {
        if(channelReady && wsConnection.readyState === WebSocket.OPEN) {
            wsConnection.close();
        }
    });

    $("#chat_input").keypress(function(event) {
        if (event.which == 13) {
            var message = $("#chat_input").val();
            $("#chat_input").val("");
            onChatMessageReceive("Me", message)

            var msg = JSON.stringify({"type" : "chatmessage", "username" : user ,"txt" : message});
            sendDataMessage(msg);
        }
    });

    initSignalServerConnection("ws://localhost:8080/vmr")

    function initSignalServerConnection(sURL) {
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
        createPeerConnection();
    };

    function joinChatRoom() {
        var meetingRoomRequest = {};
        meetingRoomRequest["type"] = "JOIN_ROOM";
        meetingRoomRequest["userName"] = user
        meetingRoomRequest["roomNumber"] = 1;
        console.log(JSON.stringify(meetingRoomRequest));
        console.log("Sent message to signal server to join chat room");
        sendMessageToSignalServer(meetingRoomRequest);
    }

    function onChannelClosed() {
        channelReady = false;
        $("#useName").html("Logged in User: ")
        clearUserList();
        console.log("Disconnnected from Signalling server")
    };

    function onChannelMessage(message) {
        console.log("Message received from signal server. " + JSON.stringify(message.data))
        processSignalingMessage(message.data);
    };

    function processSignalingMessage(message) {
        var msg = JSON.parse(message);
        var messageType;
        if(msg.payload) {
            msg = JSON.parse(msg.payload)
        }
        messageType = msg.type;
        if (messageType === 'offer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
            doAnswer();
        } else if (messageType === 'answer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
        } else if (messageType === 'candidate') {
            var candidate = new RTCIceCandidate({sdpMLineIndex:msg.label, candidate:msg.candidate});
            peerConnection.addIceCandidate(candidate);
        } else if (messageType === 'JOIN_ROOM') {
            var room = msg.meetingRoom;
            onRoomReceived(room);
        } else if (messageType === 'ROOM_UPDATED') {
            var room = msg.meetingRoom;
            onRoomUpdate(room);
        } else if (messageType === 'WRONGROOM') {
            window.location.href = "/";
        }
    };

    function onRoomReceived(room) {
        console.log("Event received : JOIN_ROOM for room: " + JSON.stringify(room))
        refreshUserList(room)
        var sessions = room.userSessions;
        if(sessions.length >= 2) {
            console.log("Initiating RTC call.")
            doCall(room);
        }
    };
    function refreshUserList(room) {
        var sessions = room.userSessions;
        clearUserList()
        for (i = 0; i < sessions.length; i++) {
            $("#userList").append('<li>' + sessions[i].userName  +  '</li>')
            var userList = $("#userList");
        }
        console.log("Refreshed user list.")
    }

    function onRoomUpdate(room) {
        console.log("Event received: ROOM_UPDATED")
        refreshUserList(room)
    }

    function clearUserList() {
        $("#userList").html("");
    }
});

