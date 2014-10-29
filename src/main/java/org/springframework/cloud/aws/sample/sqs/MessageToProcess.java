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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alain Sahli
 */
public class MessageToProcess {

    private final String message;

    private final int priority;

    @JsonCreator
    public MessageToProcess(@JsonProperty("message") String message, @JsonProperty("priority") int priority) {
        this.message = message;
        this.priority = priority;
    }

    public String getMessage() {
        return this.message;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return "MessageToProcess{" + "message='" + message + '\'' + ", priority=" + priority + '}';
    }
}
