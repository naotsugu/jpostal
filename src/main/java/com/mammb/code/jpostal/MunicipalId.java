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
package com.mammb.code.jpostal;

import java.io.Serializable;
import java.util.Objects;

/**
 * MunicipalId.
 * @author naotsugu
 */
public class MunicipalId implements Serializable {

    /** Prefecture code. */
    private String prefCode;

    /** Municipality code. */
    private String municipalCode;


    private MunicipalId(String prefCode, String municipalCode) {
        Objects.requireNonNull(prefCode);
        Objects.requireNonNull(municipalCode);
        if (prefCode.length() != 2) {
            throw new IllegalArgumentException("Illegal length of prefecture code");
        }
        if (municipalCode.length() != 3) {
            throw new IllegalArgumentException("Illegal length of municipality code");
        }
        this.prefCode = prefCode;
        this.municipalCode = municipalCode;
    }


    /**
     * Create the MunicipalId.
     * @param prefCode the prefecture code
     * @param municipalCode the municipality code
     * @return MunicipalId
     */
    public static MunicipalId of(String prefCode, String municipalCode) {
        return new MunicipalId(prefCode, municipalCode);
    }


    /**
     * Create the MunicipalId.
     * @param code prefecture code + municipality code + [check digit]
     * @return MunicipalId
     */
    public static MunicipalId of(String code) {
        Objects.requireNonNull(code);
        if (code.length() == 5) {
            return new MunicipalId(code.substring(0, 2), code.substring(2));
        } else if (code.length() == 6) {
            MunicipalId id = new MunicipalId(code.substring(0, 2), code.substring(2, 5));
            if (id.getCheckDigit() != Integer.parseInt(code.substring(5))) {
                throw new IllegalArgumentException("Illegal check digit");
            }
            return id;
        }
        throw new IllegalArgumentException("Illegal format");
    }


    /**
     * Get the check digit.
     * @return the check digit
     */
    public int getCheckDigit() {
        int a = Integer.parseInt(prefCode.substring(0, 1));
        int b = Integer.parseInt(prefCode.substring(1, 2));
        int c = Integer.parseInt(municipalCode.substring(0, 1));
        int d = Integer.parseInt(municipalCode.substring(1, 2));
        int e = Integer.parseInt(municipalCode.substring(2, 3));
        return ((a * 6) + (b * 5) + (c * 4) + (d * 3) + (e * 2)) / 11 - 11;
    }


    /**
     * Get the code with check digit.
     * @return the code with check digit
     */
    public String getCode() {
        return prefCode + municipalCode + getCheckDigit();
    }


    /**
     * Get the prefecture code.
     * @return the prefecture code
     */
    public String getPrefCode() {
        return prefCode;
    }


    /**
     * Get the municipality code.
     * @return the municipality code
     */
    public String getMunicipalCode() {
        return municipalCode;
    }


    @Override
    public String toString() {
        return "MunicipalId{" +
                "prefCode='" + prefCode + '\'' +
                ", municipalCode='" + municipalCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipalId that = (MunicipalId) o;
        return prefCode.equals(that.prefCode) &&
                municipalCode.equals(that.municipalCode);
    }


    @Override
    public int hashCode() {
        return Objects.hash(prefCode, municipalCode);
    }

}
