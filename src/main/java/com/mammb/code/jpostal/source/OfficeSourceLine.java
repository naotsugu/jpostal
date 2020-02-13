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

import com.mammb.code.jpostal.PostalCode;
import com.mammb.code.jpostal.Address;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * OfficeSourceLine.
 * <p>
 * <pre>
 * 0. 大口事業所の所在地のJISコード（5バイト）
 * 1. 大口事業所名（カナ）（100バイト）
 * 2. 大口事業所名（漢字）（160バイト）
 * 3. 都道府県名（漢字）（8バイト）
 * 4. 市区町村名（漢字）（24バイト）
 * 5. 町域名（漢字）（24バイト）
 * 6. 小字名、丁目、番地等（漢字）（124バイト）
 * 7. 大口事業所個別番号（7バイト）
 * 8. 旧郵便番号（5バイト）
 * 9. 取扱局（漢字）（40バイト）
 * 10.個別番号の種別の表示（1バイト）「0」大口事業所 「1」私書箱
 * 11.複数番号の有無（1バイト）
 * 「0」複数番号無し
 * 「1」複数番号を設定している場合の個別番号の1
 * 「2」複数番号を設定している場合の個別番号の2
 * 「3」複数番号を設定している場合の個別番号の3
 * 12.修正コード（1バイト）「0」修正なし「1」新規追加「5」廃止
 * </pre>
 */
public class OfficeSourceLine implements SourceLine {

    private String code;

    private String pref;
    private String city;
    private String town;
    private String street;

    private OfficeSourceLine(String str) {

        if (Objects.isNull(str) || str.length() == 0) {
            return;
        }

        List<String> line = Strings.split(str, ',');

        if (line.size() != 13) {
            throw new RuntimeException("Illegal format. [" + str + "]");
        }

        code = Strings.strip(line.get(7), '"');

        pref = Strings.strip(line.get(3), '"');
        city = Strings.strip(line.get(4), '"');
        town = Strings.strip(line.get(5), '"');
        street = Strings.strip(line.get(6), '"');
    }


    /**
     * Create the {@code OfficeSourceLine} by the given record.
     * @param record the record
     * @return the {@code OfficeSourceLine}
     */
    public static OfficeSourceLine of(String record) {
        return new OfficeSourceLine(record);
    }


    @Override
    public String toString() {
        return String.join(",", code, pref, city, town, "");
    }


    @Override
    public List<Address> getAddress() {
        return Arrays.asList(Address.of(PostalCode.of(code), pref, city, town, street));
    }

}
