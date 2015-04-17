/*
 *  Copyright 2015 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.danhaywood.java.assertjext;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.danhaywood.java.assertjext.Conditions.matchedBy;
import static org.assertj.core.api.Assertions.assertThat;

public class ConditionsTest {

    @Test
    public void happyCase_forIntegers() throws Exception {
        final int actual = 3;

        assertThat(actual).is(matchedBy(Matchers.equalTo(3)));
        assertThat(actual).is(matchedBy(Matchers.greaterThan(2)));
    }

    @Test
    public void happyCase_forString() throws Exception {
        final String actual = "how now brown cow";

        assertThat(actual).is(matchedBy(Matchers.containsString("now brown")));
        assertThat(actual).is(matchedBy(Matchers.endsWith("cow")));
    }

    @Test
    public void happyCase_forList() throws Exception {
        final List<String> actual = Arrays.asList("how","now","brown","cow");

        assertThat(actual).is(matchedBy(Matchers.contains("how", "now", "brown", "cow")));
        assertThat(actual).is(matchedBy(Matchers.containsInAnyOrder("brown", "now", "cow", "how")));
    }

    @Test
    public void whenAssertionFailsTheErrorMessageIsCorrectTest_forEquals() throws Exception {
        try {
            assertThat(3).is(matchedBy(Matchers.equalTo(2)));
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("not matched as <3> is not <2>");
        }
    }

    @Test
    public void whenAssertionFailsTheErrorMessageIsCorrectTest_forGreaterThan() throws Exception {
        try {
            assertThat(3).is(matchedBy(Matchers.greaterThan(3)));
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("not matched as <3> is not a value greater than <3>");
        }
    }

    @Test
    public void whenAssertionFailsTheErrorMessageIsCorrectTest_forString() throws Exception {
        try {
            assertThat("how now brown cow").is(matchedBy(Matchers.endsWith("dog")));
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("not matched as \"how now brown cow\" is not a string ending with \"dog\"");
        }
    }

    @Test
    public void whenAssertionFailsTheErrorMessageIsCorrectTest_forList() throws Exception {
        final List<String> actual = Arrays.asList("how","now","brown","cow");

        try {
            assertThat(actual).is(matchedBy(Matchers.contains("how", "now", "black", "cat")));
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("not matched as <[how, now, brown, cow]> is not iterable containing [\"how\", \"now\", \"black\", \"cat\"]");
        }
    }


}