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

package org.springframework.cloud.aws.sample.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.sample.websocket.DataWithTimestamp;
import org.springframework.cloud.aws.sample.websocket.SendingTextWebSocketHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Alain Sahli
 */
@RestController
@RequestMapping("/sns/receive")
public class SnsEndpointController {

    private static Logger LOG = LoggerFactory.getLogger(SnsEndpointController.class);

    private final SendingTextWebSocketHandler snsSendingTextWebSocketHandler;

    @Autowired
    public SnsEndpointController(@Qualifier("snsWebSocketHandler") SendingTextWebSocketHandler snsSendingTextWebSocketHandler) {
        this.snsSendingTextWebSocketHandler = snsSendingTextWebSocketHandler;
    }

    @NotificationSubscriptionMapping
    public void confirmSubscription(NotificationStatus notificationStatus) {
        notificationStatus.confirmSubscription();
    }

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message, @NotificationSubject String subject) {
        LOG.debug("Received SNS message {} with subject {}", message, subject);

        try {
            this.snsSendingTextWebSocketHandler.broadcastToSessions(new DataWithTimestamp<>(new SnsNotification(subject, message)));
        } catch (IOException e) {
            LOG.error("Was not able to push the message to the client.", e);
        }
    }

}
