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

import com.mammb.code.jpostal.Address;
import com.mammb.code.jpostal.MunicipalId;
import com.mammb.code.jpostal.PostalCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * StandardSourceLineEditor.
 *
 * @author naotsugu
 */
public class StandardSourceLineEditor implements SourceLine {

    private static final Logger log = Logger.getLogger(StandardSourceLineEditor.class.getName());

    private final StandardSourceLine pear;
    private final List<TownEditor> rules;


    private StandardSourceLineEditor(StandardSourceLine pear, List<TownEditor> rules) {
        this.pear = pear;
        this.rules = rules;
    }


    public static StandardSourceLineEditor of(StandardSourceLine origin, List<TownEditor> rules) {
        return new StandardSourceLineEditor(origin, rules);
    }


    private List<String> towns() {
        return towns(rules.iterator(), Arrays.asList(pear.town));
    }


    private List<String> towns(Iterator<TownEditor> iterator, List<String> list) {
        if (iterator.hasNext()) {

            TownEditor editor = iterator.next();

            List<String> edited = list.stream()
                    .map(town -> editor.apply(town, pear))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (edited.isEmpty()) {
                return edited;
            }
            return towns(iterator, edited);
        }
        return list;
    }


    @Override
    public List<Address> getAddress() {
        List<Address> list = new ArrayList<>();
        for (String town : towns()) {
            if (town.contains("（") && town.contains("）")) {
                list.add(Address.of(
                        PostalCode.of(pear.code), MunicipalId.of(pear.mccd), pear.pref, pear.city,
                        town.substring(0, town.indexOf('（')),
                        town.substring(town.indexOf('（') + 1, town.indexOf('）'))));
            } else {
                list.add(Address.of(
                        PostalCode.of(pear.code), MunicipalId.of(pear.mccd), pear.pref, pear.city, town, ""));
            }
        }
        return list;
    }

}
