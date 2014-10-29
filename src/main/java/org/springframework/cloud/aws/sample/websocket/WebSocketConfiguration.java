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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Alain Sahli
 */
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(sqsReceivingMessageHandler(), "/sqs-messages").withSockJS();
        registry.addHandler(snsReceivingMessageHandler(), "/sns-messages").withSockJS();
    }

    @Bean(name = "sqsWebSocketHandler")
    public SendingTextWebSocketHandler sqsReceivingMessageHandler() {
        return new SendingTextWebSocketHandler();
    }

    @Bean(name = "snsWebSocketHandler")
    public SendingTextWebSocketHandler snsReceivingMessageHandler() {
        return new SendingTextWebSocketHandler();
    }
}
