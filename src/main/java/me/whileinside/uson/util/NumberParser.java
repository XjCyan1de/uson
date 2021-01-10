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

    public static long parseLong(CharSequence sequence) {
        return parseLong(sequence, 0, sequence.length());
    }

    public static long parseLong(CharSequence sequence, int start, int end) {
        boolean negate = false;
        long result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == 'E' || c == 'e') {
                result *= Math.pow(10, parseInt(sequence, i + 1, end));
                break;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static int parseInt(CharSequence sequence) {
        return parseInt(sequence, 0, sequence.length());
    }

    public static int parseInt(CharSequence sequence, int start, int end) {
        boolean negate = false;
        int result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == 'E' || c == 'e') {
                result *= Math.pow(10, parseInt(sequence, i + 1, end));
                break;
            }

            if (c < '0' || c > '9') {
                throw new NumberFormatException();
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static double parseDouble(CharSequence sequence) {
        return parseDouble(sequence, 0, sequence.length());
    }

    public static double parseDouble(CharSequence sequence, int start, int end) {
        boolean negate = false;

        double result = 0;
        double fraction = 0;
        double scale = 1;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == 'E' || c == 'e') {
                double exp = Math.pow(10, parseInt(sequence, i + 1, end));
                result *= exp;
                fraction *= exp;

                break;
            }

            if (c == '.') {
                for (int j = i + 1; j < end; j++, i++) {
                    c = sequence.charAt(j);

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

    public static float parseFloat(CharSequence sequence) {
        return parseFloat(sequence, 0, sequence.length());
    }

    public static float parseFloat(CharSequence sequence, int start, int end) {
        boolean negate = false;

        float result = 0;
        float fraction = 0;
        float scale = 1;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == 'E' || c == 'e') {
                float exp = (float) Math.pow(10, parseInt(sequence, i + 1, end));
                result *= exp;
                fraction *= exp;

                break;
            }

            if (c == '.') {
                for (int j = i + 1; j < end; j++, i++) {
                    c = sequence.charAt(j);

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
