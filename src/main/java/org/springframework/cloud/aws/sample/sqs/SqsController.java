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

package org.springframework.cloud.aws.sample.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.cloud.aws.sample.websocket.DataWithTimestamp;
import org.springframework.cloud.aws.sample.websocket.SendingTextWebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Alain Sahli
 */
@RestController
@RequestMapping("/sqs")
public class SqsController {

    private static final Logger LOG = LoggerFactory.getLogger(SqsController.class);

    private static final String QUEUE_NAME = "MessageProcessingQueue";

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final SendingTextWebSocketHandler sqsSendingTextWebSocketHandler;

    @Autowired
    public SqsController(QueueMessagingTemplate queueMessagingTemplate,
                         @Qualifier("sqsWebSocketHandler") SendingTextWebSocketHandler sqsSendingTextWebSocketHandler) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.sqsSendingTextWebSocketHandler = sqsSendingTextWebSocketHandler;
    }

    @RequestMapping(value = "/message-processing-queue", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToMessageProcessingQueue(@RequestBody MessageToProcess message) {
        LOG.debug("Going to send message {} over SQS", message);

        this.queueMessagingTemplate.convertAndSend(QUEUE_NAME, message);
    }

    @SqsListener(QUEUE_NAME)
    private void receiveMessage(MessageToProcess message, @Header("ApproximateFirstReceiveTimestamp") String approximateFirstReceiveTimestamp) {
        LOG.debug("Received SQS message {}", message);

        try {
            this.sqsSendingTextWebSocketHandler.broadcastToSessions(new DataWithTimestamp<>(message, approximateFirstReceiveTimestamp));
        } catch (IOException e) {
            LOG.error("Was not able to push the message to the client.", e);
        }
    }

}
