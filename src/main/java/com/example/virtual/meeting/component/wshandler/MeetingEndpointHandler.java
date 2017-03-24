package com.example.virtual.meeting.component.wshandler;

import com.example.virtual.meeting.component.JsonSerializer;
import com.example.virtual.meeting.domain.MeetingRoom;
import com.example.virtual.meeting.dto.BaseRequest;
import com.example.virtual.meeting.dto.MeetingRoomRequest;
import com.example.virtual.meeting.dto.MeetingRoomResponse;
import com.example.virtual.meeting.dto.RequestType;
import com.example.virtual.meeting.repository.MeetingRoomRepository;
import com.example.virtual.meeting.repository.WebSocketSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class MeetingEndpointHandler extends AbstractWebSocketHandler {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Autowired
    private JsonSerializer jsonSerializer;

    private AtomicInteger roomCounter = new AtomicInteger();

    public void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        try {
            final String payload = message.getPayload();
            final BaseRequest request = jsonSerializer.read(payload, BaseRequest.class);
            MeetingRoomRequest meetingRoomRequest = null;
            final String webSocketSessionId = session.getId();
            MeetingRoom meetingRoom = null;
            switch(request.getRequestType()) {
                case JOIN_ROOM:
                    meetingRoomRequest = jsonSerializer.read(payload, MeetingRoomRequest.class);
                    meetingRoom = this.createOrGetRoom(meetingRoomRequest);
                    meetingRoomRepository.addAssociation(meetingRoom, meetingRoomRequest.getUserName(),
                            webSocketSessionId);
                    sendToAll(getSessions(meetingRoom), new
                            MeetingRoomResponse
                            (RequestType.JOIN_ROOM, meetingRoom));
                    break;
                default:
                    break;
            }
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getSessions(final MeetingRoom meetingRoom) {
        return meetingRoom.getUserSessions().stream().map(u -> u.getSessionId()).collect(Collectors.toList());
    }

    private MeetingRoom createOrGetRoom(final MeetingRoomRequest meetingRoomRequest) {
        MeetingRoom meetingRoom = meetingRoomRepository.getMeetingRoom(meetingRoomRequest.getRoomNumber());
        if(meetingRoom == null) {
            int roomNumber = meetingRoomRequest.getRoomNumber();
            roomNumber = roomNumber == 0 ? roomCounter.incrementAndGet() : roomNumber;
            final String roomName = meetingRoomRequest.getName() != null ? meetingRoom.getRoomName() : "MeetingRomm" +
                    roomNumber;
            meetingRoom = new MeetingRoom(roomNumber, roomName);
        }
        return meetingRoom;
    }

    private int generateRoomNumber() {
        return new Random(System.currentTimeMillis()).nextInt();
    }

    private void sendToAll(final Collection<String> sessionIds, final Object message) throws IOException {
        final TextMessage textMessage = new TextMessage(jsonSerializer.writeObject(message));
        for(final String sessionId : sessionIds) {
            final WebSocketSession webSocketSession = this.webSocketSessionRepository.get(sessionId);
            webSocketSession.sendMessage(textMessage);
        }
    }

}