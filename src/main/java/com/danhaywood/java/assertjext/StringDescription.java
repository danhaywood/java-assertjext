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

import java.io.IOException;

import org.hamcrest.BaseDescription;

public class StringDescription extends BaseDescription {
    private final Appendable out;

    public StringDescription() {
        this(new StringBuilder());
    }

    public StringDescription(Appendable out) {
        this.out = out;
    }

    protected void append(String str) {
        try {
            this.out.append(str);
        } catch (IOException var3) {
            throw new RuntimeException("Could not write description", var3);
        }
    }

    protected void append(char c) {
        try {
            this.out.append(c);
        } catch (IOException var3) {
            throw new RuntimeException("Could not write description", var3);
        }
    }

    public String toString() {
        return this.out.toString();
    }
}
