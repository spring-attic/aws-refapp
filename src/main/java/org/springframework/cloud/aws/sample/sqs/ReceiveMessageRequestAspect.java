package org.springframework.cloud.aws.sample.sqs;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Aspect
@Component
public class ReceiveMessageRequestAspect {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Before("execution(* com.amazonaws.services.sqs.AmazonSQS*.receiveMessage*(com.amazonaws.services.sqs.model.ReceiveMessageRequest)) && args(request)")
    public void onReceiveMessage(ReceiveMessageRequest request) throws Throwable {
        LOG.info("{} - {}", request.getMessageAttributeNames().size(), request.getAttributeNames().size());
    }

}
