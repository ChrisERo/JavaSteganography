/**
 * Takes in a String and reads out each bit, one at a time in same order as they appear
 */
public class TextReader {
    public String message;  // message to read out bit by bit
    private int index = 0;  // indicates number of characters that have been completely read
    private byte cIndex = 0; // indicates number of bits in a given character that have been read

    /**
     * Constructor for TextReader object
     *
     * @param mssg text that must be read one bit at a time. Must have at least one
     *             character
     */
    public TextReader(String mssg) {
        this.message = mssg;
        this.index = 0;
        this.cIndex = 0;
    }

    /**
     * Checks to see whether there are any more bits left to read
     *
     * @return true if there are not more bits to read, false otherwise
     */
    public boolean shouldEnd() {
        return this.index > this.message.length();
    }

    /**
     * Reads bit indicated by index and cIndex and increments these counters accordingly
     * Once all chars have been read, readBit starts reading the "phantom" 0 char to
     * indicate end of text.
     *
     * @return the next bit of message
     */
    public int readBit() {
        int result;
        if (this.index == this.message.length()) {
            result = 0;
        } else {
            char currentChar = this.message.charAt(this.index);
            int movement = Character.SIZE - 1 - this.cIndex;
            char filter = (char)(1 << movement);
            result = currentChar & filter >> movement;
        }

        this.cIndex += 1;
        if (this.cIndex == Character.SIZE) {
            this.cIndex = 0;
            this.index += 1;
        }

        return result;
    }
}
