/*
 * Copyright 2002-2016 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * String utility.
 *
 * @author naotsugu
 */
public class Strings {

    public static String strip(final String str, final char separatorChar) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.charAt(0) == separatorChar && str.charAt(str.length() - 1) == separatorChar) {
            return str.substring(1, str.length() - 1);
        } else {
            return str;
        }
    }

    public static List<String> split(final String str, final char separatorChar) {

        if (isEmpty(str)) {
            return Collections.emptyList();
        }

        final List<String> list = new ArrayList<>();
        int i = 0, start = 0, len = str.length();
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list;
    }


    public static int countMatches(final String str, final char ch) {
        if (isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public static boolean isEmpty(final String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

}
