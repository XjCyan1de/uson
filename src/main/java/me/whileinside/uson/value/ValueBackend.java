package me.whileinside.uson.value;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public interface ValueBackend {

    CharSequence getRaw();

    String getString(boolean escaped);

    String getUnescapedString();
    String getEscapedString();

    byte getByte();
    short getShort();
    int getInt();
    long getLong();
    double getDouble();
    float getFloat();
    boolean getBoolean();
    BigDecimal getBigDecimal();

}
