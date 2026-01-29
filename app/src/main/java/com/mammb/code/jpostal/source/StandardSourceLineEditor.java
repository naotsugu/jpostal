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

import com.mammb.code.jpostal.Address;
import com.mammb.code.jpostal.MunicipalId;
import com.mammb.code.jpostal.PostalCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StandardSourceLineEditor.
 *
 * @author naotsugu
 */
public class StandardSourceLineEditor implements SourceLine {

    private static final System.Logger log = System.getLogger(StandardSourceLineEditor.class.getName());

    private final StandardSourceLine peer;
    private final List<TownEditor> rules;


    private StandardSourceLineEditor(StandardSourceLine peer, List<TownEditor> rules) {
        this.peer = peer;
        this.rules = rules;
    }


    /**
     * Create a {@link StandardSourceLineEditor}.
     * @param origin the SourceLine
     * @param rules the rules
     * @return a {@link StandardSourceLineEditor}
     */
    public static StandardSourceLineEditor of(StandardSourceLine origin, List<TownEditor> rules) {
        return new StandardSourceLineEditor(origin, rules);
    }


    private List<String> towns() {
        return towns(rules.iterator(), Collections.singletonList(peer.town));
    }


    private List<String> towns(Iterator<TownEditor> iterator, List<String> list) {
        if (iterator.hasNext()) {

            TownEditor editor = iterator.next();

            List<String> edited = list.stream()
                    .map(town -> editor.apply(town, peer))
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
                        PostalCode.of(peer.code), MunicipalId.of(peer.mccd), peer.pref, peer.city,
                        town.substring(0, town.indexOf('（')),
                        town.substring(town.indexOf('（') + 1, town.indexOf('）'))));
            } else {
                list.add(Address.of(
                        PostalCode.of(peer.code), MunicipalId.of(peer.mccd), peer.pref, peer.city, town, ""));
            }
        }
        return list;
    }

}
