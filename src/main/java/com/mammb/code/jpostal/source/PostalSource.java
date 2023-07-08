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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * PostalSource.
 *
 * @author naotsugu
 */
public interface PostalSource {

    /**
     * Get the source url.
     * @return the source url
     */
    String url();

    /**
     * Get the {@code PostalSourceReader}.
     * @param path the path of source csv file
     * @return the {@code PostalSourceReader}
     */
    PostalSourceReader reader(Path path);


    /**
     * Add {@code TownEditor}.
     * @param editors the list of {@code TownEditor}
     */
    void with(List<TownEditor> editors);


    /**
     * Get the standard source.
     * @return standard source
     */
    static PostalSource standardSource() {

        return new PostalSource() {

            private final List<TownEditor> editors = new ArrayList<>();

            @Override
            public String url() {
                return "https://www.post.japanpost.jp/zipcode/dl/kogaki/zip/ken_all.zip";
            }

            @Override
            public PostalSourceReader reader(Path path) {
                return StandardSourceLineReader.of(path, Charset.forName("Shift_JIS"), editors);
            }

            @Override
            public void with(List<TownEditor> editors) {
                this.editors.addAll(editors);
            }
        };
    }

    /**
     * Get the standard utf source.
     * @return standard source
     */
    static PostalSource standardUtfSource() {

        return new PostalSource() {

            private final List<TownEditor> editors = new ArrayList<>();

            @Override
            public String url() {
                return "https://www.post.japanpost.jp/zipcode/utf_all.csv";
            }

            @Override
            public PostalSourceReader reader(Path path) {
                return StandardSourceLineReader.of(path, StandardCharsets.UTF_8, editors);
            }

            @Override
            public void with(List<TownEditor> editors) {
                this.editors.addAll(editors);
            }
        };
    }

    /**
     * Get the office source.
     * @return office source
     */
    static PostalSource officeSource() {

        return new PostalSource() {

            @Override
            public String url() {
                return "https://www.post.japanpost.jp/zipcode/dl/jigyosyo/zip/jigyosyo.zip";
            }

            @Override
            public PostalSourceReader reader(Path path) {
                return OfficeSourceLineReader.of(path, Charset.forName("Shift_JIS"));
            }

            @Override
            public void with(List<TownEditor> editors) {
                // Unsupported
            }
        };
    }

}
