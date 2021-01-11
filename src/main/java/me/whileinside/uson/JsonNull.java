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

package me.whileinside.uson;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Unidentified Person
 */
public class JsonNull extends JsonNode {

    public static final JsonNull INSTANCE = new JsonNull();

    @ApiStatus.Internal
    JsonNull() {
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "Null";
    }

    @Override
    public int hashCode() {
        return INSTANCE == this ? super.hashCode() : INSTANCE.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonNull;
    }
}
