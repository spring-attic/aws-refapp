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

package org.springframework.cloud.aws.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain Sahli
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableWebSocket
@EnableCaching
@ImportResource("classpath:org/springframework/cloud/aws/sample/aws-config.xml")
public class ReferenceApplication implements EmbeddedServletContainerCustomizer {

    public static void main(String[] args) {
        SpringApplication.run(ReferenceApplication.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }

    @Bean
    @Profile("local")
    public CacheManager createSimpleCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>(2);
        caches.add(new ConcurrentMapCache("CacheCluster"));
        caches.add(new ConcurrentMapCache("GitHubSourceCode"));
        simpleCacheManager.setCaches(caches);

        return simpleCacheManager;
    }
}