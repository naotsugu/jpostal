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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case of {@link StandardSourceLineEditor}.
 *
 * @author naotsugu
 */
class StandardSourceLineEditorTest {

    private final List<TownEditor> stdRules = TownEditor.standardEditors();
    private final List<TownEditor> simpleRules = TownEditor.simpleEditors();

    @Test
    void getAddress01() {
        StandardSourceLine line = StandardSourceLine.of("22131,\"433  \",\"4320000\",\"ｼｽﾞｵｶｹﾝ\",\"ﾊﾏﾏﾂｼﾅｶｸ\",\"ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ\",\"静岡県\",\"浜松市中区\",\"以下に掲載がない場合\",0,0,0,1,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getCode()).isEqualTo(PostalCode.of("4320000"));
        assertThat(address.getPrefecture()).isEqualTo("静岡県");
        assertThat(address.getCity()).isEqualTo("浜松市中区");
        assertThat(address.getTown()).isEqualTo("");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress02() {
        StandardSourceLine line = StandardSourceLine.of("22219,\"415  \",\"4150001\",\"ｼｽﾞｵｶｹﾝ\",\"ｼﾓﾀﾞｼ\",\"ｼﾓﾀﾞｼﾉﾂｷﾞﾆﾊﾞﾝﾁｶﾞｸﾙﾊﾞｱｲ\",\"静岡県\",\"下田市\",\"下田市の次に番地がくる場合\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress03() {
        StandardSourceLine line = StandardSourceLine.of("20482,\"39985\",\"3998501\",\"ﾅｶﾞﾉｹﾝ\",\"ｷﾀｱﾂﾞﾐｸﾞﾝﾏﾂｶﾜﾑﾗ\",\"ﾏﾂｶﾜﾑﾗｲﾁｴﾝ\",\"長野県\",\"北安曇郡松川村\",\"松川村一円\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("");
        assertThat(address.getStreet()).isEqualTo("");

        line = StandardSourceLine.of("25443,\"52203\",\"5220317\",\"ｼｶﾞｹﾝ\",\"ｲﾇｶﾐｸﾞﾝﾀｶﾞﾁｮｳ\",\"ｲﾁｴﾝ\",\"滋賀県\",\"犬上郡多賀町\",\"一円\",0,0,0,0,0,0");
        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("一円");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress04() {
        StandardSourceLine line = StandardSourceLine.of("01649,\"08958\",\"0895865\",\"ﾎｯｶｲﾄﾞｳ\",\"ﾄｶﾁｸﾞﾝｳﾗﾎﾛﾁｮｳ\",\"ｱﾂﾅｲ(ｾﾞﾝｲｷ)\",\"北海道\",\"十勝郡浦幌町\",\"厚内（全域）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("厚内");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress05() {
        StandardSourceLine line = StandardSourceLine.of("01663,\"08816\",\"0881646\",\"ﾎｯｶｲﾄﾞｳ\",\"ｱｯｹｼｸﾞﾝﾊﾏﾅｶﾁｮｳ\",\"ｱﾈﾍﾞﾂ(ﾁｮｳﾒ)\",\"北海道\",\"厚岸郡浜中町\",\"姉別（丁目）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("姉別");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress06() {
        StandardSourceLine line = StandardSourceLine.of("26103,\"60111\",\"6011101\",\"ｷｮｳﾄﾌ\",\"ｷｮｳﾄｼｻｷｮｳｸ\",\"ﾋﾛｶﾞﾜﾗ(ｶｸﾏﾁ)\",\"京都府\",\"京都市左京区\",\"広河原（各町）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("広河原");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress07() {
        StandardSourceLine line = StandardSourceLine.of("27229,\"575  \",\"5750004\",\"ｵｵｻｶﾌ\",\"ｼｼﾞｮｳﾅﾜﾃｼ\",\"ｵｶﾔﾏ(ﾊﾞﾝﾁ)\",\"大阪府\",\"四條畷市\",\"岡山（番地）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("岡山");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress08() {
        StandardSourceLine line = StandardSourceLine.of("23201,\"440  \",\"4400075\",\"ｱｲﾁｹﾝ\",\"ﾄﾖﾊｼｼ\",\"ﾊﾅﾀﾞﾁｮｳ(ﾑﾊﾞﾝﾁ)\",\"愛知県\",\"豊橋市\",\"花田町（無番地）\",1,0,0,1,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("花田町");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress09() {
        StandardSourceLine line = StandardSourceLine.of("07204,\"97401\",\"9740152\",\"ﾌｸｼﾏｹﾝ\",\"ｲﾜｷｼ\",\"ﾀﾋﾞﾄﾏﾁﾀﾋﾞｳﾄ(ｿﾉﾀ)\",\"福島県\",\"いわき市\",\"田人町旅人（その他）\",1,1,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("田人町旅人");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress10() {
        StandardSourceLine line = StandardSourceLine.of("23221,\"44113\",\"4411336\",\"ｱｲﾁｹﾝ\",\"ｼﾝｼﾛｼ\",\"ﾄﾐｵｶ(ﾔｼｷﾁｸ)\",\"愛知県\",\"新城市\",\"富岡（○○屋敷）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("富岡");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress11() {
        StandardSourceLine line = StandardSourceLine.of("14103,\"220  \",\"2208190\",\"ｶﾅｶﾞﾜｹﾝ\",\"ﾖｺﾊﾏｼﾆｼｸ\",\"ﾐﾅﾄﾐﾗｲﾗﾝﾄﾞﾏｰｸﾀﾜｰ(ﾁｶｲ･ｶｲｿｳﾌﾒｲ)\",\"神奈川県\",\"横浜市西区\",\"みなとみらいランドマークタワー（地階・階層不明）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("みなとみらいランドマークタワー");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress12() {
        StandardSourceLine line = StandardSourceLine.of("13113,\"150  \",\"1506147\",\"ﾄｳｷｮｳﾄ\",\"ｼﾌﾞﾔｸ\",\"ｼﾌﾞﾔｼﾌﾞﾔｽｸﾗﾝﾌﾞﾙｽｸｴｱ(47ｶｲ)\",\"東京都\",\"渋谷区\",\"渋谷渋谷スクランブルスクエア（４７階）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("渋谷渋谷スクランブルスクエア４７階");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress13() {
        StandardSourceLine line = StandardSourceLine.of("23105,\"450  \",\"4506210\",\"ｱｲﾁｹﾝ\",\"ﾅｺﾞﾔｼﾅｶﾑﾗｸ\",\"ﾒｲｴｷﾐｯﾄﾞﾗﾝﾄﾞｽｸｴｱ(ｺｳｿｳﾄｳ)(10ｶｲ)\",\"愛知県\",\"名古屋市中村区\",\"名駅ミッドランドスクエア（高層棟）（１０階）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("名駅ミッドランドスクエア１０階");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress14() {
        StandardSourceLine line = StandardSourceLine.of("27119,\"545  \",\"5450052\",\"ｵｵｻｶﾌ\",\"ｵｵｻｶｼｱﾍﾞﾉｸ\",\"ｱﾍﾞﾉｽｼﾞ(ﾂｷﾞﾉﾋﾞﾙｦﾉｿﾞｸ)\",\"大阪府\",\"大阪市阿倍野区\",\"阿倍野筋（次のビルを除く）\",0,0,1,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("阿倍野筋");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress15() {
        StandardSourceLine line = StandardSourceLine.of("38205,\"792  \",\"7920846\",\"ｴﾋﾒｹﾝ\",\"ﾆｲﾊﾏｼ\",\"ﾀﾂｶﾜﾁｮｳ(ﾀﾂｶﾜﾔﾏｦﾌｸﾑ)\",\"愛媛県\",\"新居浜市\",\"立川町（立川山を含む）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("立川町");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress16() {
        StandardSourceLine line = StandardSourceLine.of("39412,\"78603\",\"7860301\",\"ｺｳﾁｹﾝ\",\"ﾀｶｵｶｸﾞﾝｼﾏﾝﾄﾁｮｳ\",\"ﾀｲｼｮｳ(ﾂﾂﾞﾗｶﾞﾜ､ﾄﾄﾞﾛｷｻﾞｷｦﾌｸﾑ)\",\"高知県\",\"高岡郡四万十町\",\"大正（葛籠川、轟崎を含む）\",0,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("大正");
        assertThat(address.getStreet()).isEqualTo("葛籠川");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("大正");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress17() {
        StandardSourceLine line = StandardSourceLine.of("39428,\"78917\",\"7891720\",\"ｺｳﾁｹﾝ\",\"ﾊﾀｸﾞﾝｸﾛｼｵﾁｮｳ\",\"ｻｶﾞ(ｿﾉﾀ)\",\"高知県\",\"幡多郡黒潮町\",\"佐賀（その他）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("佐賀");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress18() {
        StandardSourceLine line = StandardSourceLine.of("23201,\"440  \",\"4400845\",\"ｱｲﾁｹﾝ\",\"ﾄﾖﾊｼｼ\",\"ﾀｶｼﾁｮｳ(ｷﾀﾊﾗ､ｿﾉﾀ)\",\"愛知県\",\"豊橋市\",\"高師町（北原、その他）\",1,0,0,1,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("高師町");
        assertThat(address.getStreet()).isEqualTo("北原");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("高師町");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress19() {
        StandardSourceLine line = StandardSourceLine.of("30208,\"64964\",\"6496413\",\"ﾜｶﾔﾏｹﾝ\",\"ｷﾉｶﾜｼ\",\"ﾀｹﾌﾞｻ(450ﾊﾞﾝﾁｲｶ)\",\"和歌山県\",\"紀の川市\",\"竹房（４５０番地以下）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("竹房");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress20() {
        StandardSourceLine line = StandardSourceLine.of("04205,\"988  \",\"9880927\",\"ﾐﾔｷﾞｹﾝ\",\"ｹｾﾝﾇﾏｼ\",\"ｶﾗｸﾜﾁｮｳﾆｼﾓｳﾈ(200ﾊﾞﾝｲｼﾞｮｳ)\",\"宮城県\",\"気仙沼市\",\"唐桑町西舞根（２００番以上）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("唐桑町西舞根");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress21() {
        StandardSourceLine line = StandardSourceLine.of("12219,\"29001\",\"2900156\",\"ﾁﾊﾞｹﾝ\",\"ｲﾁﾊﾗｼ\",\"ｸｻｶﾘ(1656-1999)\",\"千葉県\",\"市原市\",\"草刈（１６５６～１９９９）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("草刈");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress22() {
        StandardSourceLine line = StandardSourceLine.of("20202,\"39982\",\"3998251\",\"ﾅｶﾞﾉｹﾝ\",\"ﾏﾂﾓﾄｼ\",\"ｼﾏｳﾁ(9820､9821､9823-9830､9864ﾊﾞﾝﾁｲｼﾞｮｳ)\",\"長野県\",\"松本市\",\"島内（９８２０、９８２１、９８２３～９８３０、９８６４番地以上）\",1,0,0,0,0,0\n");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("島内");
        assertThat(address.getStreet()).isEqualTo("９８２０");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("島内");
        assertThat(address.getStreet()).isEqualTo("９８２１");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(2);
        assertThat(address.getTown()).isEqualTo("島内");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress23() {
        StandardSourceLine line = StandardSourceLine.of("45206,\"88301\",\"8830104\",\"ﾐﾔｻﾞｷｹﾝ\",\"ﾋｭｳｶﾞｼ\",\"ﾄｳｺﾞｳﾁｮｳﾔﾏｹﾞﾎﾞ(513ﾉ1ｲﾅｲ)\",\"宮崎県\",\"日向市\",\"東郷町山陰戊（５１３の１以内）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("東郷町山陰戊");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress24() {
        StandardSourceLine line = StandardSourceLine.of("46201,\"89112\",\"8911274\",\"ｶｺﾞｼﾏｹﾝ\",\"ｶｺﾞｼﾏｼ\",\"ﾐﾄﾞﾘｶﾞｵｶﾁｮｳ(35ﾊﾞﾝｲｺｳ)\",\"鹿児島県\",\"鹿児島市\",\"緑ヶ丘町（３５番以降）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("緑ヶ丘町");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress25() {
        StandardSourceLine line = StandardSourceLine.of("20201,\"38122\",\"3812241\",\"ﾅｶﾞﾉｹﾝ\",\"ﾅｶﾞﾉｼ\",\"ｱｵｷｼﾞﾏﾏﾁｱｵｷｼﾞﾏｵﾂ(956ﾊﾞﾝﾁｲｶﾞｲ)\",\"長野県\",\"長野市\",\"青木島町青木島乙（９５６番地以外）\",1,0,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("青木島町青木島乙");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress26() {
        StandardSourceLine line = StandardSourceLine.of("01407,\"04824\",\"0482402\",\"ﾎｯｶｲﾄﾞｳ\",\"ﾖｲﾁｸﾞﾝﾆｷﾁｮｳ\",\"ｵｵｴ(1ﾁｮｳﾒ､2ﾁｮｳﾒ<651､662､668ﾊﾞﾝﾁ>ｲｶﾞｲ､3ﾁｮｳﾒ5､1\",\"北海道\",\"余市郡仁木町\",\"大江（１丁目、２丁目「６５１、６６２、６６８番地」以外、３丁目５、１\",1,0,1,0,0,0\n")
                           .marge(StandardSourceLine.of("01407,\"04824\",\"0482402\",\"ﾎｯｶｲﾄﾞｳ\",\"ﾖｲﾁｸﾞﾝﾆｷﾁｮｳ\",\"3-4､20､678､687ﾊﾞﾝﾁ)\",\"北海道\",\"余市郡仁木町\",\"３－４、２０、６７８、６８７番地）\",1,0,1,0,0,0"));
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("１丁目");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(2);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("３丁目５番地");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(3);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("１３－４番地");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(4);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("２０番地");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(5);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("６７８番地");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(6);
        assertThat(address.getTown()).isEqualTo("大江");
        assertThat(address.getStreet()).isEqualTo("６８７番地");
    }

    @Test
    void getAddress27() {
        StandardSourceLine line = StandardSourceLine.of("06365,\"99602\",\"9960301\",\"ﾔﾏｶﾞﾀｹﾝ\",\"ﾓｶﾞﾐｸﾞﾝｵｵｸﾗﾑﾗ\",\"ﾐﾅﾐﾔﾏ(430ﾊﾞﾝﾁｲｼﾞｮｳ<1770-1-2､1862-42､\",\"山形県\",\"最上郡大蔵村\",\"南山（４３０番地以上「１７７０－１～２、１８６２－４２、\",1,1,0,0,0,0")
                           .marge(StandardSourceLine.of("06365,\"99602\",\"9960301\",\"ﾔﾏｶﾞﾀｹﾝ\",\"ﾓｶﾞﾐｸﾞﾝｵｵｸﾗﾑﾗ\",\"1923-5ｦﾉｿﾞｸ>､ｵｵﾔﾁ､ｵﾘﾜﾀﾘ､ｶﾝｶﾈﾉ､ｷﾝｻﾞﾝ､ﾀｷﾉｻﾜ､ﾄﾖﾏｷ､ﾇﾏﾉﾀﾞｲ､\",\"山形県\",\"最上郡大蔵村\",\"１９２３－５を除く」、大谷地、折渡、鍵金野、金山、滝ノ沢、豊牧、沼の台、\",1,1,0,0,0,0"))
                           .marge(StandardSourceLine.of("06365,\"99602\",\"9960301\",\"ﾔﾏｶﾞﾀｹﾝ\",\"ﾓｶﾞﾐｸﾞﾝｵｵｸﾗﾑﾗ\",\"ﾋｼﾞｵﾘ､ﾋﾗﾊﾞﾔｼ)\",\"山形県\",\"最上郡大蔵村\",\"肘折、平林）\",1,1,0,0,0,0"));
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("大谷地");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(2);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("折渡");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(3);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("鍵金野");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(4);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("金山");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(5);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("滝ノ沢");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(6);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("豊牧");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(7);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("沼の台");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(8);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("肘折");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(9);
        assertThat(address.getTown()).isEqualTo("南山");
        assertThat(address.getStreet()).isEqualTo("平林");
    }

    @Test
    void getAddress28() {
        StandardSourceLine line = StandardSourceLine.of("37322,\"76141\",\"7614103\",\"ｶｶﾞﾜｹﾝ\",\"ｼｮｳｽﾞｸﾞﾝﾄﾉｼｮｳﾁｮｳ\",\"ｺｳ､ｵﾂ(ｵｵｷﾄﾞ)\",\"香川県\",\"小豆郡土庄町\",\"甲、乙（大木戸）\",1,0,0,0,0,0");

        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("甲");
        assertThat(address.getStreet()).isEqualTo("");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("乙");
        assertThat(address.getStreet()).isEqualTo("大木戸");
    }

    @Test
    void getAddress29() {
        StandardSourceLine line = StandardSourceLine.of("03366,\"02955\",\"0295503\",\"ｲﾜﾃｹﾝ\",\"ﾜｶﾞｸﾞﾝﾆｼﾜｶﾞﾏﾁ\",\"ｱﾅｱｹ22ﾁﾜﾘ､ｱﾅｱｹ23ﾁﾜﾘ\",\"岩手県\",\"和賀郡西和賀町\",\"穴明２２地割、穴明２３地割\",0,0,0,1,0,0");

        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("穴明２２地割");
        assertThat(address.getStreet()).isEqualTo("");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("穴明２３地割");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress30() {
        StandardSourceLine line = StandardSourceLine.of("03366,\"02955\",\"0295523\",\"ｲﾜﾃｹﾝ\",\"ﾜｶﾞｸﾞﾝﾆｼﾜｶﾞﾏﾁ\",\"ｴｯﾁｭｳﾊﾀ64ﾁﾜﾘ-ｴｯﾁｭｳﾊﾀ66ﾁﾜﾘ\",\"岩手県\",\"和賀郡西和賀町\",\"越中畑６４地割～越中畑６６地割\",0,0,0,1,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("越中畑");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void getAddress31() {
        StandardSourceLine line = StandardSourceLine.of("03507,\"02879\",\"0287917\",\"ｲﾜﾃｹﾝ\",\"ｸﾉﾍｸﾞﾝﾋﾛﾉﾁｮｳ\",\"ﾀﾈｲﾁﾀﾞｲ50ﾁﾜﾘ-ﾀﾞｲ70ﾁﾜﾘ(ｵｵｻﾜ､ｼﾞｮｳﾅｲ､ﾀｷｻﾜ)\",\"岩手県\",\"九戸郡洋野町\",\"種市第５０地割～第７０地割（大沢、城内、滝沢）\",0,1,0,0,0,0");
        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
        assertThat(address.getTown()).isEqualTo("種市");
        assertThat(address.getStreet()).isEqualTo("大沢");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(1);
        assertThat(address.getTown()).isEqualTo("種市");
        assertThat(address.getStreet()).isEqualTo("城内");

        address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(2);
        assertThat(address.getTown()).isEqualTo("種市");
        assertThat(address.getStreet()).isEqualTo("滝沢");
    }

//    @Test
//    void getAddress12() {
//        StandardSourceLine line = StandardSourceLine.of("");
//        Address address = StandardSourceLineEditor.of(line, stdRules).getAddress().get(0);
//        assertThat(address.getTown()).isEqualTo("");
//        assertThat(address.getStreet()).isEqualTo("");
//    }

}