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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * StandardSourceLineReader.
 *
 * @author naotsugu
 */
public class StandardSourceLineReader extends PostalSourceReader {

    private StandardSourceLine buffered;
    private final List<TownEditor> editors;


    private StandardSourceLineReader(Path path, Charset charset, List<TownEditor> editors) {
        super(Objects.requireNonNull(path), Objects.requireNonNull(charset));
        this.buffered = null;
        this.editors = (Objects.isNull(editors) || editors.isEmpty())
                ? TownEditor.standardEditors() : editors;
    }

    /**
     * Create the {@code StandardSourceLineReader} instance.
     * @param path the source path
     * @param charset the charset of source
     * @param editors the list of {@code TownEditor}
     * @return the {@code StandardSourceLineReader} instance
     */
    public static StandardSourceLineReader of(Path path, Charset charset, List<TownEditor> editors) {
        return new StandardSourceLineReader(path, charset, editors);
    }


    /**
     * Create the {@code StandardSourceLineReader} instance.
     * @param path the source path
     * @param charset the charset of source
     * @return the {@code StandardSourceLineReader} instance
     */
    public static StandardSourceLineReader of(Path path, Charset charset) {
        return new StandardSourceLineReader(path, charset, null);
    }


    @Override
    public SourceLine readNext() {

        for (;;) {

            String line = readLine();
            if (Objects.isNull(line)) {
                StandardSourceLine ret = buffered;
                buffered = null;
                return Objects.nonNull(ret) ? StandardSourceLineEditor.of(ret, editors) : null;
            }

            StandardSourceLine current = StandardSourceLine.of(line);
            if (Objects.isNull(buffered)) {
                buffered = current;
                continue;
            }

            if (buffered.canMarge(current)) {
                buffered.marge(current);
            } else {
                StandardSourceLine ret = buffered;
                buffered = current;
                return StandardSourceLineEditor.of(ret, editors);
            }
        }
    }
}
