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
package com.mammb.code.jpostal;

import java.io.Serializable;
import java.util.Objects;

/**
 * PostalCode.
 *
 * @author naotsugu
 */
public class PostalCode implements Serializable {

    private final String code;


    private PostalCode(String code) {
        Objects.requireNonNull(code);
        this.code = code.contains("-") ? code.replace("-", "") : code;
        if (!this.code.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException(code);
        }
    }


    /**
     * Create the {@code PostalCode}.
     * @param code the postal code string
     * @return the {@code PostalCode}
     */
    public static PostalCode of(String code) {
        return new PostalCode(code);
    }


    /**
     * Get the code string.
     * @return the code string
     */
    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostalCode that = (PostalCode) o;
        return code.equals(that.code);
    }


    @Override
    public int hashCode() {
        return Objects.hash(code);
    }


    @Override
    public String toString() {
        return "PostalCode{" +
                "code='" + code + '\'' +
                '}';
    }

}
