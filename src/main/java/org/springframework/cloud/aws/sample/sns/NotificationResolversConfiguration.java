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

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientConfigurationUtils;
import org.springframework.cloud.aws.messaging.endpoint.NotificationMessageHandlerMethodArgumentResolver;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatusHandlerMethodArgumentResolver;
import org.springframework.cloud.aws.messaging.endpoint.NotificationSubjectHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Alain Sahli
 */
@Component
public class NotificationResolversConfiguration implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        List<BeanDefinition> methodArgumentResolvers = new ManagedList<>(3);

        BeanDefinitionBuilder notificationMessageHandler = BeanDefinitionBuilder.rootBeanDefinition(NotificationMessageHandlerMethodArgumentResolver.class);
        methodArgumentResolvers.add(notificationMessageHandler.getBeanDefinition());

        BeanDefinitionBuilder notificationSubjectHandler = BeanDefinitionBuilder.rootBeanDefinition(NotificationSubjectHandlerMethodArgumentResolver.class);
        methodArgumentResolvers.add(notificationSubjectHandler.getBeanDefinition());

        BeanDefinitionBuilder notificationStatusBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(NotificationStatusHandlerMethodArgumentResolver.class);
        notificationStatusBeanDefinition.addConstructorArgReference(AmazonWebserviceClientConfigurationUtils.getBeanName(AmazonSNS.class.getName()));
        methodArgumentResolvers.add(notificationStatusBeanDefinition.getBeanDefinition());

        BeanDefinition requestMappingHandlerAdapter = beanFactory.getBeanDefinition("requestMappingHandlerAdapter");

        requestMappingHandlerAdapter.getPropertyValues().addPropertyValue("customArgumentResolvers", methodArgumentResolvers);
    }
}
