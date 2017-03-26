var data_channel = null;
var initiator = false;

function createPeerConnection() {
    var pc_config = {"iceServers": [{url:'stun:stun.l.google.com:19302'}]};
    try {
        peerConnection = new RTCPeerConnection(pc_config, null);
        console.log("Created Peer connection");
        peerConnection.onicecandidate = onIceCandidate;
        peerConnection.ondatachannel = onDataChannel;
    } catch (e) {
        console.log(e);
        peerConnection = null;
        return;
    }
}

function onIceCandidate(event) {
    console.log("Event received: onIceCandidate")
    if (event.candidate) {
        sendMessageToSignalServer({type: 'candidate', label: event.candidate.sdpMLineIndex, id: event.candidate.sdpMid, candidate: event.candidate.candidate});
    }
}


function onDataChannel(evt) {
    console.log('Received data channel creating request');
    data_channel = evt.channel;
    initDataChannel();
}

function initDataChannel() {
    data_channel.onopen = onChannelStateChange;
    data_channel.onclose = onChannelStateChange;
    data_channel.onmessage = onReceiveMessageCallback;
}

function createDataChannel(room, role) {
     try {
        data_channel = peerConnection.createDataChannel("datachannel_" + room + role, null);
        console.log("Created data channel: " + JSON.stringify(data_channel))
     } catch (e) {
         console.log('error creating data sserverWsConnetion ' + e);
         return;
     }
     initDataChannel();
}

function doCall(room) {
    createDataChannel("caller");
    console.log("Making an offer call for peer connection.")
    peerConnection.createOffer(setLocalAndSendMessage, failureCallback, null);
};

function doAnswer() {
    console.log("Answering an offer call from peer")
    peerConnection.createAnswer(setLocalAndSendMessage, failureCallback, null);
};


function setLocalAndSendMessage(sessionDescription) {
    console.log("Set local session description: " + JSON.stringify(sessionDescription))
    peerConnection.setLocalDescription(sessionDescription);
    console.log("Sending session description to Signal server")
    sendMessageToSignalServer(sessionDescription);
};

function sendDataMessage(data) {
    data_channel.send(data);
};

function onChannelStateChange() {
    console.log('Data peer connection data channel state is: ' + data_channel.readyState);
}

function onReceiveMessageCallback(event) {
    console.log("Message received from RTC peer: " + event);
    try {
        var msg = JSON.parse(event.data);
        if (msg.type === 'chatmessage') onChatMessageReceive(msgusername, msg.txt);
    }
    catch (e) {}
};

function failureCallback(e) {
    console.log("failure callback "+ e.message);
}