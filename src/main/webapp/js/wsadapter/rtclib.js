$(document).ready(function(){
    var RTCPeerConnection = null;
    var room = null;
    var initiator;
    var peerConnection = null;

    var data_channel = null;
    var channelReady;
    var sserverWsConnetion;


    function initRtc(sURL) {
        openChannel(sURL);
    };

    function openChannel(sURL) {
        channelReady = false;
        singalServerWebSocketConnetion = new WebSocket(signalingURL);
        sserverWsConnetion.onopen = onChannelOpened;
        sserverWsConnetion.onmessage = onChannelMessage;
        sserverWsConnetion.onclose = onChannelClosed;
    };

    function onChannelOpened() {
        channelReady = true;
        createPeerConnection();

        if(location.search.substring(1,5) == "room") {
            room = location.search.substring(6);
            sendMessageToSignalServer({"type" : "ENTERROOM", "value" : room * 1});
            initiator = true;
            doCall();
        } else {
            sendMessageToSignalServer({"type" : "GETROOM", "value" : ""});
            initiator = false;
        }
    };

    function onChannelMessage(message) {
        processSignalingMessage(message.data);
    };

    function onChannelClosed() {
        channelReady = false;
    };

    function sendMessageToSignalServer(message) {
        var msgString = JSON.stringify(message);
        sserverWsConnetion.send(msgString);
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
        } else if (msg.type === 'GETROOM') {
            room = msg.value;
            onRoomReceived(room);
        } else if (msg.type === 'WRONGROOM') {
            window.location.href = "/";
        }
    };

    function createPeerConnection() {
        var pc_config = {"iceServers": [{url:'stun:stun.l.google.com:19302'}]};
        try {
            peerConnection = new RTCPeerConnection(pc_config, null);
            peerConnection.onicecandidate = onIceCandidate;
            peerConnection.ondatachannel = onDataChannel;
        } catch (e) {
            console.log(e);
            peerConnection = null;
            return;
        }
    };

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

    function createDataChannel(role) {
         try {
            data_channel = peerConnection.createDataChannel("datachannel_"+room+role, null);
         } catch (e) {
             console.log('error creating data sserverWsConnetion ' + e);
             return;
         }
         initDataChannel();
    }

    function onIceCandidate(event) {
     if (event.candidate)
        sendMessageToSignalServer({type: 'candidate', label: event.candidate.sdpMLineIndex, id: event.candidate.sdpMid, candidate: event.candidate.candidate});
    };

    function failureCallback(e) {
        console.log("failure callback "+ e.message);
    }

    function doCall() {
        createDataChannel("caller");
        peerConnection.createOffer(setLocalAndSendMessage, failureCallback, null);
    };

    function doAnswer() {
        peerConnection.createAnswer(setLocalAndSendMessage, failureCallback, null);
    };

    function setLocalAndSendMessage(sessionDescription) {
        peerConnection.setLocalDescription(sessionDescription);
        sendMessageToSignalServer(sessionDescription);
    };

    function sendDataMessage(data) {
        data_channel.send(data);
    };

    function onChannelStateChange() {
        console.log('Data sserverWsConnetion state is: ' + data_channel.readyState);
    }

    function onReceiveMessageCallback(event) {
        console.log(event);
        try {
            var msg = JSON.parse(event.data);
            if (msg.type === 'chatmessage') onPrivateMessageReceived(msg.txt);
        }
        catch (e) {}
    };
});