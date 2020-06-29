public class MessageReader {
    public String message;  // message to read out bit by bit
    private int index = 0;  // indicates number of characters that have been completely read
    private byte cIndex = 0; // indicates number of bits in a given character that have been read

    public MessageReader(String mssg) {
        this.message = mssg;
        this.index = 0;
        this.cIndex = 0;
    }

    public boolean shouldEnd() {
        return this.index > this.message.length();
    }

    /**
     *
     * @return
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
