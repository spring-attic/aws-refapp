/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.aws.sample.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Alain Sahli
 */
public class SendingTextWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    private CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.sessions.add(session);
    }

    public <T> void broadcastToSessions(DataWithTimestamp<T> dataToSend) throws IOException {
        String payload = this.jsonMapper.writeValueAsString(dataToSend);
        sendTestMessageToAllSessions(payload);
    }

    private void sendTestMessageToAllSessions(String payload) throws IOException {
        List<WebSocketSession> sessionsToRemove = new ArrayList<>();
        for (WebSocketSession session : this.sessions) {
            if (!session.isOpen()) {
                sessionsToRemove.add(session);
            }
        }
        this.sessions.removeAll(sessionsToRemove);

        for (WebSocketSession session : this.sessions) {
            session.sendMessage(new TextMessage(payload));
        }
    }

}
