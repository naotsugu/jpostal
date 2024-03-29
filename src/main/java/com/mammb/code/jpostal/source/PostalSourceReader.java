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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * PostalSourceReader.
 *
 * @author naotsugu
 */
public abstract class PostalSourceReader implements Closeable {

    private final BufferedReader bufferedReader;

    /**
     * Constructor.
     * @param path path of file
     * @param charset the charset of source
     */
    protected PostalSourceReader(Path path, Charset charset) {
        Objects.requireNonNull(path);
        try {
            this.bufferedReader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(path), charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the next line as {@code SourceLine}.
     * @return the {@code SourceLine}
     */
    public abstract SourceLine readNext();

    /**
     * Read line.
     * @return the line of string
     */
    protected String readLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }

}
