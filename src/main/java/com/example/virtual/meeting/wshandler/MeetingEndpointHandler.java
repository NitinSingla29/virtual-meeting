package com.example.virtual.meeting.wshandler;

import com.example.virtual.meeting.component.JsonSerializer;
import com.example.virtual.meeting.domain.MeetingRoom;
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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class MeetingEndpointHandler extends AbstractWebSocketHandler {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Autowired
    private JsonSerializer jsonSerializer;

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            final String payload = message.getPayload();
            final MeetingRoomRequest meetingRoomRequest = jsonSerializer.read(payload, MeetingRoomRequest.class);
            final String webSocketSessionId = session.getId();
            MeetingRoom meetingRoom = null
            switch (meetingRoomRequest.getMeetingRequestType()) {
                case GETROOM :
                    int roomNumber = generateRoomNumber();
                    meetingRoom = meetingRoomRepository.getMeetingRoom(roomNumber);
                    if(meetingRoom==null) {
                        meetingRoom = new MeetingRoom(roomNumber,"MeetingRomm"+ roomNumber);
                    }
                    meetingRoomRepository.addAssociation(meetingRoom,webSocketSessionId);
                    session.sendMessage(new TextMessage(jsonSerializer.writeObject(meetingRoom)));
                    break;
                case ENTER_ROOM :
                    meetingRoomRepository.addAssociation(meetingRoomRequest.getRoomNumber(),webSocketSessionId);
                    break;
                default:
                    meetingRoom = this.meetingRoomRepository.getMeetingRoom(meetingRoomRequest.getRoomNumber());
                    final Set<String> associateSessionIds = meetingRoom.getWebSocketSessionIds();
                    associateSessionIds.remove(webSocketSessionId);
                    sendToAll(associateSessionIds,meetingRoomRequest.getMessage());
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private int generateRoomNumber() {
        return new Random(System.currentTimeMillis()).nextInt();
    }

    private void sendToAll(Collection<String> sessionIds, String message) throws  IOException{
        for (String sessionId: sessionIds) {
            final WebSocketSession webSocketSession = this.webSocketSessionRepository.get(sessionId);
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }

}