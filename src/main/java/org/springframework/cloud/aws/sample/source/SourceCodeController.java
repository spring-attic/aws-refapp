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

package org.springframework.cloud.aws.sample.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alain Sahli
 */
@RestController
public class SourceCodeController {

    private final SourceCodeProvider sourceCodeProvider;

    @Autowired
    public SourceCodeController(SourceCodeProvider sourceCodeProvider) {
        this.sourceCodeProvider = sourceCodeProvider;
    }

    @RequestMapping(value = "/source", method = RequestMethod.GET)
    public SourceFile getSourceFile(@RequestParam String path) {
        Assert.isTrue(StringUtils.hasText(path), "path cannot be empty");

        return this.sourceCodeProvider.getSourceFileContent(path);
    }

}
