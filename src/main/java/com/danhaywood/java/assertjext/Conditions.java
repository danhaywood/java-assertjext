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

import org.assertj.core.api.Condition;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

public class Conditions {

    public static <T> Condition<T> matchedBy(final Matcher<T> matcher) {

        return new Condition<T>() {
            @Override public boolean matches(final T t) {
                final boolean matches = matcher.matches(t);
                if(!(matches)) {
                    String value = valueOf(t);
                    final String reason = reason();
                    as(String.format("not matched as %s is not %s", value, reason));
                }
                return matches;
            }

            private String reason() {
                final StringDescription reasonDescription = new StringDescription();
                matcher.describeTo(reasonDescription);
                return reasonDescription.toString();
            }

            private String valueOf(final T t) {
                String value = null;
                if(matcher instanceof TypeSafeMatcher) {
                    final TypeSafeMatcher typeSafeMatcher = (TypeSafeMatcher) matcher;
                    final StringDescription valueDescription = new StringDescription();
                    typeSafeMatcher.describeMismatch(t, valueDescription);
                    final String valueDescr = valueDescription.toString();
                    if(valueDescr.startsWith("was ")) {
                        // most seem to
                        value = valueDescr.substring(4);
                    }
                }
                if(value == null) {
                    value = "<" + t + ">";
                }
                return value;
            }
        };
    }
}
