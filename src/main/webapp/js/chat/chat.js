$(document).ready(function(){
    var user = $.url().param('u');

    var wsConnection;
    var channelReady = false;

    $("#endChatButton").click(function() {
        if(channelReady && wsConnection.readyState === WebSocket.OPEN) {
            wsConnection.close();
        }
    });

    $("#chat_submit_btn").click(function() {
        var message = $("#chat_input").val();
        onChatMessageReceive("Me", message)

        var msg = JSON.stringify({"type" : "chatmessage", "username" : user ,"txt" : txt});
        sendDataMessage(msg);
    });

    function onChatMessageReceive(user, message) {
        $("#messageList").append('<li>' + user + ': ' + message + '</li>');
    }


    function sendMessageToSignalServer(message) {
        var msgString = JSON.stringify(message);
        wsConnection.send(msgString);
    };

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
        meetingRoomRequest["requestType"] = "JOIN_ROOM";
        meetingRoomRequest["userName"] = user
        meetingRoomRequest["roomNumber"] = 1;
        console.log(JSON.stringify(meetingRoomRequest));
        sendMessageToSignalServer(meetingRoomRequest);
    }

    function onChannelClosed() {
        channelReady = false;
        $("#useName").html("Logged in User: ")
        clearUserList();
        console.log("Disconnnected from Signalling server")
    };

    function onChannelMessage(message) {
        processSignalingMessage(message.data);
    };

    function processSignalingMessage(message) {
        var msg = JSON.parse(message);
        if (msg.type === 'offer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
            doAnswer();
        } else if (msg.type === 'answer') {
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg));
        } else if (msg.type === 'candidate') {
            var candidate = new RTCIceCandidate({sdpMLineIndex:msg.label, candidate:msg.candidate});
            peerConnection.addIceCandidate(candidate);
        } else if (msg.type === 'JOIN_ROOM') {
            var room = msg.meetingRoom;
            onRoomReceived(room);
        } else if (msg.type === 'LEFT_ROOM') {
            var room = msg.meetingRoom;
            onRoomLeft(room);
        }else if (msg.type === 'WRONGROOM') {
            window.location.href = "/";
        }
    };

    function onRoomReceived(room) {
        refreshUserList(room)
        var sessions = room.userSessions;
        if(sessions.length >= 2) {
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
    }

    function onRoomLeft(room) {
        refreshUserList(room)
    }

    function clearUserList() {
        $("#userList").html("");
    }
});

