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

package me.whileinside.uson.indent;

/**
 * @author Unidentified Person
 */
public enum IndentType {

    ONE_SPACE(' ', 1),
    TWO_SPACES(' ', 2),
    FOUR_SPACES(' ', 4),
    EIGHT_SPACES(' ', 8),
    ONE_TAB('\t', 1),
    TWO_TABS('\t', 2);

    private final char value;
    private final int length;

    IndentType(char value, int length) {
        this.value = value;
        this.length = length;
    }

    public char getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }

}
