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

package me.whileinside.uson.util;

/**
 * @author Unidentified Person
 */
public final class NumberParser {

    private NumberParser() {
        throw new UnsupportedOperationException();
    }

    public static long parseLong(char[] buf, int start, int end) {
        boolean negate = false;
        long result = 0;

        switch (buf[start]) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = buf[i];

            if (c == 'E' || c == 'e') {
                int exp = parseExp(buf, i + 1, end);

                if (exp > 0) {
                    result *= exp;
                } else {
                    result /= -exp;
                }

                break;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static int parseInt(char[] buf, int start, int end) {
        boolean negate = false;
        int result = 0;

        switch (buf[start]) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = buf[i];

            if (c == 'E' || c == 'e') {
                int exp = parseExp(buf, i + 1, end);

                if (exp > 0) {
                    result *= exp;
                } else {
                    result /= -exp;
                }

                break;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static double parseDouble(char[] buf, int start, int end) {
        boolean negate = false;

        double result = 0;
        double fraction = 0;
        double scale = 1;

        switch (buf[start]) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = buf[i];

            if (c == 'E' || c == 'e') {
                int exp = parseExp(buf, i + 1, end);

                if (exp > 0) {
                    result *= exp;
                    fraction *= exp;
                } else {
                    exp = -exp;

                    result /= exp;
                    fraction /= exp;
                }

                break;
            }

            if (c == '.') {
                for (int j = i + 1; j < end; j++, i++) {
                    c = buf[j];

                    if (c == 'E' || c == 'e') {
                        break;
                    }

                    if (c < '0' || c > '9') {
                        throw new NumberFormatException();
                    }

                    fraction = fraction * 10 + (c & 0xF);
                    scale *= 10;
                }

                continue;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        result = result + fraction / scale;
        return negate ? -result : result;
    }

    private static int parseExp(char[] buf, int start, int end) {
        int exp = parseInt(buf, start, end);
        boolean negate = exp < 0;

        if (negate) {
            exp = -exp;
        }

        int result = 1;
        int base = 10;

        while (exp != 0) {
            if ((exp & 1) == 1)
                result *= base;

            exp >>= 1;
            base *= base;
        }

        return negate ? -result : result;
    }

    public static float parseFloat(char[] buf, int start, int end) {
        boolean negate = false;

        float result = 0;
        float fraction = 0;
        float scale = 1;

        switch (buf[start]) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = buf[i];

            if (c == 'E' || c == 'e') {
                int exp = parseExp(buf, i + 1, end);

                if (exp > 0) {
                    result *= exp;
                    fraction *= exp;
                } else {
                    exp = -exp;

                    result /= exp;
                    fraction /= exp;
                }

                break;
            }

            if (c == '.') {
                for (int j = i + 1; j < end; j++, i++) {
                    c = buf[j];

                    if (c == 'E' || c == 'e') {
                        break;
                    }

                    if (c < '0' || c > '9') {
                        throw new NumberFormatException();
                    }

                    fraction = fraction * 10 + (c & 0xF);
                    scale *= 10;
                }
                continue;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        result = result + fraction / scale;
        return negate ? -result : result;
    }

}
