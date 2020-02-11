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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case of {@link StandardSourceLine}.
 *
 * @author naotsugu
 */
class StandardSourceLineTest {

    private String str0 = "13103,\"105  \",\"1050022\",\"ﾄｳｷｮｳﾄ\",\"ﾐﾅﾄｸ\",\"ｶｲｶﾞﾝ(1､2ﾁｮｳﾒ)\",\"東京都\",\"港区\",\"海岸（１、２丁目）\",1,0,1,0,0,0";
    private String str1 = "17201,\"920  \",\"9200946\",\"ｲｼｶﾜｹﾝ\",\"ｶﾅｻﾞﾜｼ\",\"ｵｵｸﾜﾏﾁ(ｶ､ｺ､ｻｵﾂ､ｻｺｳ､ｿ､ﾀ､ﾂｺｳ､ﾅ､ﾒ､ﾕ､ﾖ､\",\"石川県\",\"金沢市\",\"大桑町（カ、コ、サ乙、サ甲、ソ、タ、ツ甲、ナ、メ、ユ、ヨ、\",1,0,0,0,0,0";
    private String str2 = "17201,\"920  \",\"9200946\",\"ｲｼｶﾜｹﾝ\",\"ｶﾅｻﾞﾜｼ\",\"ﾗ､ﾜ､ｱﾅｶﾞｹ､ｱﾅﾌﾞﾁｶﾞｹ､ﾈ､ｺｳ､ﾖｼｼﾞﾏ､ｼﾓﾖｼｼﾞﾏｶﾞｹ)\",\"石川県\",\"金沢市\",\"ラ、ワ、穴欠、穴淵欠、子、庚、葭島、下葭島欠）\",1,0,0,0,0,0";

    @Test
    void of() {
        Address address = StandardSourceLine.of(str0).getAddress().get(0);
        assertThat(address.getCode()).isEqualTo(PostalCode.of("1050022"));
        assertThat(address.getPrefecture()).isEqualTo("東京都");
        assertThat(address.getCity()).isEqualTo("港区");
        assertThat(address.getTown()).isEqualTo("海岸（１、２丁目）");
        assertThat(address.getStreet()).isEqualTo("");
    }

    @Test
    void canMarge() {
        StandardSourceLine line0 = StandardSourceLine.of(str0);
        StandardSourceLine line1 = StandardSourceLine.of(str1);
        StandardSourceLine line2 = StandardSourceLine.of(str2);

        assertThat(line0.canMarge(line1)).isEqualTo(false);
        assertThat(line1.canMarge(line2)).isEqualTo(true);
        assertThat(line2.canMarge(line1)).isEqualTo(false);
    }

    @Test
    void marge() {
        StandardSourceLine line1 = StandardSourceLine.of(str1);
        StandardSourceLine line2 = StandardSourceLine.of(str2);
        Address address = line1.marge(line2).getAddress().get(0);
        assertThat(address.getCode()).isEqualTo(PostalCode.of("9200946"));
        assertThat(address.getPrefecture()).isEqualTo("石川県");
        assertThat(address.getCity()).isEqualTo("金沢市");
        assertThat(address.getTown()).isEqualTo("大桑町（カ、コ、サ乙、サ甲、ソ、タ、ツ甲、ナ、メ、ユ、ヨ、ラ、ワ、穴欠、穴淵欠、子、庚、葭島、下葭島欠）");
        assertThat(address.getStreet()).isEqualTo("");
    }
}