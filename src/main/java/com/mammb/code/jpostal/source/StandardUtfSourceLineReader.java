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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * StandardUtfSourceLineReader.
 *
 * @author naotsugu
 */
public class StandardUtfSourceLineReader extends PostalSourceReader {

    private final List<TownEditor> editors;


    private StandardUtfSourceLineReader(Path path, List<TownEditor> editors) {
        super(Objects.requireNonNull(path), StandardCharsets.UTF_8);
        this.editors = (Objects.isNull(editors) || editors.isEmpty())
            ? TownEditor.standardEditors() : editors;
    }


    /**
     * Create the {@code StandardSourceLineReader} instance.
     * @param path the source path
     * @param editors the list of {@code TownEditor}
     * @return the {@code StandardSourceLineReader} instance
     */
    public static StandardUtfSourceLineReader of(Path path, List<TownEditor> editors) {
        return new StandardUtfSourceLineReader(path, editors);
    }


    /**
     * Create the {@code StandardSourceLineReader} instance.
     * @param path the source path
     * @return the {@code StandardSourceLineReader} instance
     */
    public static StandardUtfSourceLineReader of(Path path) {
        return new StandardUtfSourceLineReader(path, null);
    }


    @Override
    public SourceLine readNext() {
        String line = readLine();
        return Objects.isNull(line)
            ? null
            : StandardSourceLineEditor.of(StandardSourceLine.of(line), editors);
    }

}
