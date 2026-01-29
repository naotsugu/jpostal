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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case of {@link Strings}.
 * @author naotsugu
 */
class StringsTest {

    @Test
    void strip() {
        assertThat(Strings.strip("abc", ':')).isEqualTo("abc");
        assertThat(Strings.strip(":abc:", ':')).isEqualTo("abc");
        assertThat(Strings.strip(":abc", ':')).isEqualTo(":abc");
        assertThat(Strings.strip("abc:", ':')).isEqualTo("abc:");
        assertThat(Strings.strip(":a:b:c:", ':')).isEqualTo("a:b:c");
    }

    @Test
    void split() {
        assertThat(Strings.split("abc", ',')).containsExactly("abc");
        assertThat(Strings.split("a,b,c", ',')).containsExactly("a", "b", "c");
    }

    @Test
    void countMatches() {
        assertThat(Strings.countMatches("abc", ',')).isEqualTo(0);
        assertThat(Strings.countMatches("a,bc", ',')).isEqualTo(1);
        assertThat(Strings.countMatches(",a,b,c,", ',')).isEqualTo(4);
    }

    @Test
    void isEmpty() {
        assertThat(Strings.isEmpty(null)).isEqualTo(true);
        assertThat(Strings.isEmpty("")).isEqualTo(true);
        assertThat(Strings.isEmpty("a")).isEqualTo(false);
    }
}
