/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mammb.code.jpostal.source;

import java.nio.file.Path;

/**
 * Reader of {@code OfficeSourceLine}.
 *
 * @author naotsugu
 */
public class OfficeSourceLineReader extends PostalSourceReader {

    private OfficeSourceLineReader(Path path) {
        super(path);
    }


    /**
     * Create the {@code OfficeSourceLineReader} instance.
     * @param path the source path
     * @return {@code OfficeSourceLineReader} instance
     */
    public static OfficeSourceLineReader of(Path path) {
        return new OfficeSourceLineReader(path);
    }


    @Override
    public SourceLine readNext() {
        return OfficeSourceLine.of(readLine());
    }

}
