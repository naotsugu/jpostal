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
import com.mammb.code.jpostal.PostalCode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * StandardSourceLine.
 * <p>
 * <pre>
 * 0. 全国地方公共団体コード
 * 1. 旧）郵便番号(5桁)
 * 2. 郵便番号7桁
 * 3. 都道府県名(カナ)
 * 4. 市区町村名(カナ)
 * 5. 町域名(カナ)
 * 6. 都道府県名
 * 7. 市区町村名
 * 8. 町域名
 * 9. 一町域が二以上の郵便番号で表される場合の表示(「1」は該当、「0」は該当せず)
 * 10.小字毎に番地が起番されている町域の表示(「1」は該当、「0」は該当せず)
 * 11.丁目を有する町域の場合の表示(「1」は該当、「0」は該当せず)
 * 12.一つの郵便番号で二以上の町域を表す場合の表示(「1」は該当、「0」は該当せず)
 * 13.更新の表示(「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用))
 * 14.変更理由(「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用))
 * </pre>
 */
class StandardSourceLine implements SourceLine {

    String code;

    String pref;
    String city;
    String town;

    private int openCount;
    private int closeCount;


    private StandardSourceLine(String str) {

        if (Objects.isNull(str) || str.length() == 0) {
            return;
        }

        List<String> line = Strings.split(str, ',');

        if (line.size() != 15) {
            throw new RuntimeException("Illegal format. [" + str + "]");
        }

        code = Strings.strip(line.get(2), '"');

        pref = Strings.strip(line.get(6), '"');
        city = Strings.strip(line.get(7), '"');
        town = Strings.strip(line.get(8), '"');

        openCount  = Strings.countMatches(town, '（');
        closeCount = Strings.countMatches(town, '）');

    }


    /**
     * Create the {@code StandardSourceLine} by the given record.
     * @param record the recode
     * @return the {@code StandardSourceLine} instance
     */
    public static StandardSourceLine of(String record) {
        return new StandardSourceLine(record);
    }


    public boolean canMarge(StandardSourceLine that) {
        return this.code.equals(that.code)
                && this.pref.equals(that.pref)
                && this.city.equals(that.city)
                && openCount > closeCount;
    }


    public StandardSourceLine marge(StandardSourceLine that) {
        if (!this.town.equals(that.town)) {
            this.town += that.town;
        }
        this.openCount  += that.openCount;
        this.closeCount += that.closeCount;
        return this;
    }


    @Override
    public List<Address> getAddress() {
        return Arrays.asList(Address.of(PostalCode.of(code), pref, city, town, ""));
    }

}


