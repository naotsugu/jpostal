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

import java.util.Objects;

/**
 * Address.
 *
 * @author naotsugu
 */
public class Address {

    private final PostalCode code;
    private final String prefecture;
    private final String city;
    private final String town;
    private final String street;


    private Address(PostalCode code, String prefecture, String city, String town, String street) {
        this.code = Objects.requireNonNull(code);
        this.prefecture = Objects.isNull(prefecture) ? "" : prefecture;
        this.city = Objects.isNull(city) ? "" : city;
        this.town = Objects.isNull(town) ? "" : town;
        this.street = Objects.isNull(street) ? "" : street;
    }


    public static Address of(PostalCode code, String prefecture, String city, String town, String street) {
        return new Address(code, prefecture, city, town, street);
    }

    public PostalCode getCode() {
        return code;
    }


    public String getPrefecture() {
        return prefecture;
    }


    public String getCity() {
        return city;
    }


    public String getTown() {
        return town;
    }


    public String getStreet() {
        return street;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return code.equals(address.code) &&
                prefecture.equals(address.prefecture) &&
                city.equals(address.city) &&
                town.equals(address.town) &&
                street.equals(address.street);
    }


    @Override
    public int hashCode() {
        return Objects.hash(code, prefecture, city, town, street);
    }


    @Override
    public String toString() {
        return "Address{" +
                "code=" + code +
                ", prefecture='" + prefecture + '\'' +
                ", city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                '}';
    }


    public String toJsonString() {
        return String.format(
                "{'code': '%s', 'prefecture': '%s', 'city': '%s', 'town': '%s', 'street': '%s'}".replace("'", "\""),
                code.getCode(), prefecture, city, town, street);
    }

}
