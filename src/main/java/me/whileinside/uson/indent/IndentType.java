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
