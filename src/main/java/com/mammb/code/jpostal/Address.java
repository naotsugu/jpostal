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
 * Address.
 *
 * @author naotsugu
 */
public class Address implements Serializable {

    /** PostalCode. */
    private final PostalCode code;
    /** MunicipalId. */
    private final MunicipalId municipalId;
    /** prefecture. */
    private final String prefecture;
    /** city. */
    private final String city;
    /** town. */
    private final String town;
    /** street. */
    private final String street;


    private Address(PostalCode code, MunicipalId municipalId, String prefecture, String city, String town, String street) {
        this.code = Objects.requireNonNull(code);
        this.municipalId = Objects.requireNonNull(municipalId);
        this.prefecture = Objects.isNull(prefecture) ? "" : prefecture;
        this.city = Objects.isNull(city) ? "" : city;
        this.town = Objects.isNull(town) ? "" : town;
        this.street = Objects.isNull(street) ? "" : street;
    }


    /**
     * Create the {@code Address}.
     * @param code the postal code
     * @param municipalId the MunicipalId
     * @param prefecture the prefecture name
     * @param city the city name
     * @param town the town name
     * @param street the street name
     * @return the {@code Address}
     */
    public static Address of(PostalCode code, MunicipalId municipalId, String prefecture, String city, String town, String street) {
        return new Address(code, municipalId, prefecture, city, town, street);
    }


    /**
     * Get the postal code.
     * @return the postal code
     */
    public PostalCode getCode() {
        return code;
    }

    /**
     * Get the municipalId.
     * @return municipalId
     */
    public MunicipalId getMunicipalId() {
        return municipalId;
    }

    /**
     * Get the prefecture name.
     * @return the prefecture name
     */
    public String getPrefecture() {
        return prefecture;
    }


    /**
     * Get the city name.
     * @return the city name
     */
    public String getCity() {
        return city;
    }


    /**
     * Get the town name.
     * @return the town name
     */
    public String getTown() {
        return town;
    }


    /**
     * Get the street name.
     * @return the street name
     */
    public String getStreet() {
        return street;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return code.equals(address.code) &&
                municipalId.equals(address.municipalId) &&
                prefecture.equals(address.prefecture) &&
                city.equals(address.city) &&
                town.equals(address.town) &&
                street.equals(address.street);
    }


    @Override
    public int hashCode() {
        return Objects.hash(code, municipalId, prefecture, city, town, street);
    }


    @Override
    public String toString() {
        return "Address{" +
                "code=" + code +
                ", municipalId='" + municipalId + '\'' +
                ", prefecture='" + prefecture + '\'' +
                ", city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                '}';
    }


    /**
     * Get the json string.
     * @return the json string
     */
    public String toJsonString() {
        return String.format(
                "{'code': '%s', 'prefectureCode': '%s', 'municipalityCode': '%s', 'prefecture': '%s', 'city': '%s', 'town': '%s', 'street': '%s'}".replace("'", "\""),
                code.getCode(), municipalId.getPrefCode(), municipalId.getMunicipalCode(), prefecture, city, town, street);
    }

}
