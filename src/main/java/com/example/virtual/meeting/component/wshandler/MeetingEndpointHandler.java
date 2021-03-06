package com.example.virtual.meeting.component.wshandler;

import com.example.virtual.meeting.component.JsonSerializer;
import com.example.virtual.meeting.domain.MeetingRoom;
import com.example.virtual.meeting.dto.BaseRequest;
import com.example.virtual.meeting.dto.MeetingRoomRequest;
import com.example.virtual.meeting.dto.MeetingRoomResponse;
import com.example.virtual.meeting.dto.ResponseType;
import com.example.virtual.meeting.repository.WebSocketSessionRepository;
import com.example.virtual.meeting.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeetingEndpointHandler extends AbstractWebSocketHandler {

    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private JsonSerializer jsonSerializer;

    public void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        try {
            this.webSocketSessionRepository.add(session);
            final String payload = message.getPayload();
            final BaseRequest request = jsonSerializer.read(payload, BaseRequest.class);
            MeetingRoomRequest meetingRoomRequest = null;
            final String webSocketSessionId = session.getId();
            switch(request.getRequestType()) {
                case JOIN_ROOM:
                    meetingRoomRequest = jsonSerializer.read(payload, MeetingRoomRequest.class);
                    final MeetingRoom meetingRoom = this.meetingRoomService.joinMeetingRoon(meetingRoomRequest, webSocketSessionId);

                    sendReply(new MeetingRoomResponse(ResponseType.JOIN_ROOM, meetingRoom), webSocketSessionId);
                    handleMeetingRoomUpdate(ResponseType.ROOM_UPDATED, meetingRoom, session.getId());
                    break;
                default:
                    final Collection<String> allLinkedConnections = getAllLinkedConnections(webSocketSessionId);
                    allLinkedConnections.remove(webSocketSessionId);
                    sendReply(message, allLinkedConnections.toArray(new String[0]));
            }
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMeetingRoomUpdate(ResponseType type, MeetingRoom meetingRoom, String excludingSessionId)
            throws
            IOException {
        final List<String> allMeetingRoomSessions = getSessions(meetingRoom);
        if(excludingSessionId != null) {
            allMeetingRoomSessions.remove(excludingSessionId);
        }
        sendReply(new MeetingRoomResponse(type, meetingRoom), allMeetingRoomSessions.toArray(new String[0]));
    }

    private List<String> getSessions(final MeetingRoom meetingRoom) {
        return meetingRoom.getUserSessions().stream().map(u -> u.getSessionId()).collect(Collectors.toList());
    }

    private Collection<String> getAllLinkedConnections(String sessionId) {
        final MeetingRoom meetingRoom = this.meetingRoomService.getMeetingRoom(sessionId);
        if(meetingRoom != null) {
            return getSessions(meetingRoom);
        }
        return new ArrayList<>();
    }

    private void sendReply(final Object message, String... sessionIds) throws IOException {
        final TextMessage textMessage = new TextMessage(jsonSerializer.writeObject(message));
        for(final String sessionId : sessionIds) {
            final WebSocketSession webSocketSession = this.webSocketSessionRepository.get(sessionId);
            webSocketSession.sendMessage(textMessage);
        }
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSessionRepository.remove(session);
        final MeetingRoom meetingRoom = this.meetingRoomService.leaveMeetingRoom(session.getId());
        this.handleMeetingRoomUpdate(ResponseType.ROOM_UPDATED, meetingRoom, session.getId());
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        this.webSocketSessionRepository.add(session);
        System.out.println("Added new session " + session.getId());
    }
}