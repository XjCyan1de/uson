package me.whileinside.uson.reader;

import java.io.IOException;

/**
 * @author Unidentified Person
 */
public interface JsonReader {

    boolean isFinished();
    void finish();

    int getPosition();

    int read();
    void rollback();

    char[] getBuffer();

    CharSequence getChars(int start, int end);
    int getInt(int start, int end);
    long getLong(int start, int end);
    float getFloat(int start, int end);
    double getDouble(int start, int end);
    char getChar(int idx);

    IOException getReadCause();

}
