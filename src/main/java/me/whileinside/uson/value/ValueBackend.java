/*
 *    Copyright 2021 Unidentified Person
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.whileinside.uson.value;

import org.jetbrains.annotations.ApiStatus;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
@ApiStatus.Internal
public abstract class ValueBackend {

    public CharSequence getRaw() {
        return getString();
    }

    public abstract String getString();

    protected boolean checkRawValues() {
        return false;
    }

    public String getUnescapedString() {
        return getString();
    }

    public String getEscapedString() {
        return getString();
    }

    public abstract byte getByte();

    public abstract short getShort();

    public abstract int getInt();

    public abstract long getLong();

    public abstract double getDouble();

    public abstract float getFloat();

    public abstract BigDecimal getBigDecimal();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ValueBackend)) return false;

        ValueBackend that = (ValueBackend) obj;

        return checkRawValues() || that.checkRawValues()
                ? getRaw().equals(that.getRaw())
                : getUnescapedString().equals(that.getUnescapedString());
    }
}
