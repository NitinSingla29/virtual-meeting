package com.example.virtual.meeting.component.wshandler;

import com.example.virtual.meeting.component.JsonSerializer;
import com.example.virtual.meeting.domain.MeetingRoom;
import com.example.virtual.meeting.model.BaseRequest;
import com.example.virtual.meeting.model.MeetingRoomRequest;
import com.example.virtual.meeting.repository.MeetingRoomRepository;
import com.example.virtual.meeting.repository.WebSocketSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

@Component
public class MeetingEndpointHandler extends AbstractWebSocketHandler {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Autowired
    private JsonSerializer jsonSerializer;

    public void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        try {
            final String payload = message.getPayload();
            final BaseRequest request = jsonSerializer.read(payload, BaseRequest.class);
            MeetingRoomRequest meetingRoomRequest = null;
            final String webSocketSessionId = session.getId();
            MeetingRoom meetingRoom = null;
            switch(request.getRequestType()) {
                case JOIN_ROOM:
                    meetingRoomRequest = (MeetingRoomRequest) request;
                    meetingRoom = this.createOrGetRoom(meetingRoomRequest);
                    meetingRoomRepository.addAssociation(meetingRoom, webSocketSessionId);
                    session.sendMessage(new TextMessage(jsonSerializer.writeObject(meetingRoom)));
                    break;
                default:
//                    meetingRoom = this.meetingRoomRepository.getMeetingRoom(request.getRoomNumber());
//                    final Set<String> associateSessionIds = meetingRoom.getWebSocketSessionIds();
//                    associateSessionIds.remove(webSocketSessionId);
//                    sendToAll(associateSessionIds,"Hi");
                    break;
            }
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }

    private MeetingRoom createOrGetRoom(final MeetingRoomRequest meetingRoomRequest) {
        MeetingRoom meetingRoom = meetingRoomRepository.getMeetingRoom(meetingRoomRequest.getRoomNumber());
        if(meetingRoom == null) {
            final int roomNumber = generateRoomNumber();
            final String roomName = meetingRoomRequest.getName()!=null? meetingRoom.getRoomName() : "MeetingRomm" +
                    roomNumber;
            meetingRoom = new MeetingRoom(roomNumber, roomName);
        }
        return meetingRoom;
    }

    private int generateRoomNumber() {
        return new Random(System.currentTimeMillis()).nextInt();
    }

    private void sendToAll(final Collection<String> sessionIds, final String message) throws IOException {
        for(final String sessionId : sessionIds) {
            final WebSocketSession webSocketSession = this.webSocketSessionRepository.get(sessionId);
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }

}