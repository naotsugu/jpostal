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
import java.util.Arrays;
import java.util.List;

/**
 * TownEditor.
 *
 * @author naotsugu
 */
public interface TownEditor {

    List<String> empty = Arrays.asList("");

    List<String> apply(String town, StandardSourceLine org);


    static List<TownEditor> standardEditors() {
        return Arrays.asList(

            // 1874 records
            (town, org) -> town.contains("以下に掲載がない場合") ? empty : keep(town),

            // 17 records
            (town, org) -> town.contains("の次に番地がくる場合") ? empty : keep(town),

            // 23 records (exclude 滋賀県犬上郡多賀町一円)
            (town, org) -> endsWithExact(town, "一円" ,org.city) ? empty : keep(town),

            // 1 records
            (town, org) -> town.endsWith("（全域）") ? removeParen(town) : keep(town),

            // 48 records 名駅ミッドランドスクエア
            (town, org) -> town.contains("（高層棟）") ? remove(town, "（高層棟）") : keep(town),

            // 81 records
            (town, org) -> town.endsWith("（地階・階層不明）") ? removeParen(town) : keep(town),

            // 32 records
            (town, org) -> town.endsWith("（次のビルを除く）") ? removeParen(town) : keep(town),

            // 3170 records ex) 霞が関霞が関ビル（１０階）-> 霞が関霞が関ビル１０階
            (town, org) -> town.matches(".*（[０-９]+階）") ? extractParen(town) : keep(town),

            // 59 records
            (town, org) -> town.endsWith("（丁目）") ? removeParen(town) : keep(town),

            // 1 records
            (town, org) -> town.endsWith("（各町）") ? removeParen(town) : keep(town),

            // 36 records
            (town, org) -> town.endsWith("（番地）") ? removeParen(town) : keep(town),

            // 1 records
            (town, org) -> town.endsWith("（無番地）") ? removeParen(town) : keep(town),

            // 564 records
            (town, org) -> town.endsWith("（その他）") ? removeParen(town) : keep(town),

            // 36 records
            (town, org) -> town.endsWith("を除く）") ? removeParen(town) : keep(town),

            // 1 records
            (town, org) -> town.endsWith("以下）") ? removeParen(town) : keep(town),

            // 1 records
            (town, org) -> town.endsWith("以内）") ? removeParen(town) : keep(town),

            // 2 records
            (town, org) -> town.endsWith("以降）") ? removeParen(town) : keep(town),

            // 1 records
            (town, org) -> town.endsWith("以外）") ? removeParen(town) : keep(town),

            // 1 records. 愛知県新城市
            (town, org) -> town.endsWith("○○屋敷）") ? removeParen(town) : keep(town),

            // ex) AA（BB、CC「XX、YY」） -> AA（BB）, AA（CC「XX、YY」）
            (town, org) -> split(town),

            // 2 records
            (town, org) -> town.endsWith("を含む）") ? removeParen(town) : keep(town),

            // 2 records
            (town, org) -> town.endsWith("その他）") ? removeParen(town) : keep(town),

            // 6 records
            (town, org) -> town.endsWith("以上）") ? removeParen(town) : keep(town),

            // 1 records ex) 大江（１丁目、２丁目「６５１、６６２、６６８番地」以外、
            (town, org) -> town.endsWith("」以外）") ? removeParen(town) : keep(town),

            // 1 records ex) 南山（４３０番地以上「１７７０－１～２、
            (town, org) -> town.contains("番地以上") ? removeParen(town) : keep(town),

            // 13 records
            (town, org) -> town.contains("「") && town.contains("」") ? removeSqBrackets(town) : keep(town),

            // ex) 西瑞江（４丁目１～２番・１０～２７番、５丁目）-> 西瑞江
            (town, org) -> town.matches(".*（.*・.*）.*") ? removeParen(town) : keep(town),

            // ex) 中央本町（３～５丁目）-> 中央本町
            (town, org) -> town.matches(".*（.*～.*）.*") ? removeParen(town) : keep(town),

            // 29 records 岩手県 地割
            (town, org) -> town.matches(".*第.*地割～第.*地割.*") ? Arrays.asList(town.replaceAll("第.*地割～第.*地割", "")) : keep(town),
            //  9 records 岩手県 地割
            (town, org) -> town.matches(".*地割～.*地割.*") ? Arrays.asList(town.replaceAll("[０-９]+地割～.*", "")) : keep(town),
            // 10 records 岩手県 地割
            (town, org) -> town.contains("地割、") ? Arrays.asList(town.split("、")) : keep(town),

            // 11 records 香川県小豆郡土庄町
            (town, org) -> town.startsWith("甲、乙") ? Arrays.asList(town.split("、")) : keep(town)
        );
    }


    static List<TownEditor> simpleEditors() {
        return Arrays.asList(
                (town, org) -> town.contains("以下に掲載がない場合") ? empty : keep(town),
                (town, org) -> town.contains("の次に番地がくる場合") ? empty : keep(town),
                (town, org) -> endsWithExact(town, "一円" ,org.city) ? empty : keep(town),
                (town, org) -> town.matches(".*（[０-９]+階）") ? extractParen(town) : keep(town),
                (town, org) -> town.matches(".*第.*地割～第.*地割.*") ? Arrays.asList(town.replaceAll("第.*地割～第.*地割", "")) : keep(town),
                (town, org) -> town.matches(".*地割～.*地割.*") ? Arrays.asList(town.replaceAll("[０-９]+地割～.*", "")) : keep(town),
                (town, org) -> town.contains("地割、") ? Arrays.asList(town.split("、")) : keep(town),
                (town, org) -> town.startsWith("甲、乙") ? Arrays.asList(town.split("、")) : keep(town),
                (town, org) -> hasParen(town) ? removeParen(town) : keep(town)
        );
    }


    private static List<String> keep(String input) {
        return Arrays.asList(input);
    }


    private static List<String> split(String input) {

        if (!hasParen(input)) {
            return Arrays.asList(input);
        }

        String content = input.substring(input.indexOf('（'));
        if (!content.contains("、")) {
            return Arrays.asList(input);
        }

        List<String> list = new ArrayList<>();

        for (String str : splitExcludeSqBrackets(content)) {
            // ex) 栄（１４５、１６９番地）-> 栄（１４５番地）, 栄（１６９番地）
            String prefix1 = input.substring(0, input.indexOf('（'));
            String prefix2 = str.startsWith("（") ? "" : "（";
            String suffix1 = (input.endsWith("番地）") && str.matches(".*[０-９]$")) ? "番地" : "";
                   suffix1 = (input.endsWith("丁目）") && str.matches(".*[０-９]$")) ? "丁目" : suffix1;
            String suffix2 = str.endsWith("）") ? "" : "）";
            list.add(prefix1 + prefix2 + str + suffix1 + suffix2);
        }
        return list;
    }


    private static boolean hasParen(String input) {
        return input.contains("（") && input.contains("）");
    }


    private static boolean endsWithExact(String input, String str, String parent) {
        return input.endsWith(str) && !input.equals(str) && parent.endsWith(input.replace(str, ""));
    }


    private static List<String> extractParen(String input) {
        return Arrays.asList(input.replace("（", "").replace("）", ""));
    }


    private static List<String> removeSqBrackets(String input) {
        return Arrays.asList(input.replaceFirst("「.*」", ""));
    }


    private static List<String> removeParen(String input) {
        return Arrays.asList(input.substring(0, input.lastIndexOf("（")));
    }


    private static List<String> remove(String input, String str) {
        return Arrays.asList(input.replace(str, ""));
    }


    private static List<String> splitExcludeSqBrackets(String str) {
        // ex) （１丁目、２丁目「６５１、６６２、６６８番地」以外、６７８、６８７番地）
        //   -> [（１丁目], [２丁目「６５１、６６２、６６８番地」以外], [６７８], [６８７番地）]
        final List<String> list = new ArrayList<>();
        int i = 0, start = 0, len = str.length();
        boolean match = false;
        boolean inSqb = false;
        while (i < len) {
            char chr = str.charAt(i);
            if (chr == '「') {
                inSqb = true;
            } else if (inSqb && chr == '」') {
                inSqb = false;
            }
            if (!inSqb && chr == '、') {
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
}
