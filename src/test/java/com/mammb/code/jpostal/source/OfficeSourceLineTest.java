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
 * Test case of {@link OfficeSourceLine}.
 *
 * @author naotsugu
 */
class OfficeSourceLineTest {

    @Test
    void of() {
        String str = "13105,\"ﾄｳｷﾖｳﾀﾞｲｶﾞｸ ｺｳｶﾞｸﾌﾞ\",\"東京大学　工学部\",\"東京都\",\"文京区\",\"本郷\",\"７丁目３－１\",\"1138656\",\"113  \",\"本郷\",0,0,0";
        Address address = OfficeSourceLine.of(str).getAddress().get(0);
        assertThat(address.getCode()).isEqualTo(PostalCode.of("1138656"));
        assertThat(address.getPrefecture()).isEqualTo("東京都");
        assertThat(address.getCity()).isEqualTo("文京区");
        assertThat(address.getTown()).isEqualTo("本郷");
        assertThat(address.getStreet()).isEqualTo("７丁目３－１");

    }
}