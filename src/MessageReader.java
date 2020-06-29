import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/**
 * Takes image written to by MessageWriter and extracts the hidden text
 */
public class MessageReader extends Traverser {
    private List<Character> hiddenText; // List of characters found while reading message
    private String sourcePathName;
    private char buffer; // stores
    private byte currentBit;

    public MessageReader (String imagePathName) throws IOException {
        super(DIMENSIONS, imagePathName);
        this.sourcePathName = imagePathName;
        this.hiddenText = new LinkedList<>();
        // Set primitives to 0 for style
        buffer = 0;
        currentBit = 0;
    }

    /**
     * Stores bit of pixel[y,x] at color channel c in the appropriate location in buffer,
     * adding buffer to hiddenText and 0ing out character buffer if full.
     *
     * Assumes image passed through imagePathName was written to by MessageWriter
     * Only executed in Traverser.traverse()
     *
     * @param c color/dimension we are currently traversing through
     * @param x horizontal coordinate of current piexel of traverse()
     * @param y vertical coordinate of current piexel of traverse()
     * @return true if traverse should terminate and false otherwise
     */
    public boolean injectedFunction(int c, int x, int y) {
        // Extract bit value in question and add to buffer
        int pixelValue = this.targetImage.getRGB(x, y);

        char pixelBit = (char)this.getPixelBit(pixelValue, c);
        this.buffer += pixelBit << (Character.SIZE - 1 - this.currentBit);

        this.currentBit += 1;
        // if char buffer full, append to hiddenText list
        if (this.currentBit == Character.SIZE) {
            this.hiddenText.add(this.buffer);
            if (this.buffer == 0) { // reached end of message
                return true;
            } else { // reset buffer and currentBit index
                this.buffer = 0;
                this.currentBit = 0;
            }
        }

        return false;
    }

    /**
     * Returns String representation of hiddenText;
     * Inspired by
     * https://www.geeksforgeeks.org/convert-list-of-characters-to-string-in-java/
     *
     * @return String representation of text in hiddenText
     */
    public String getMessageFromList() {
        StringBuilder sb = new StringBuilder();
        for (Character c: this.hiddenText) {
            sb.append(c.charValue());
        }
        return sb.toString();
    }

    /**
     * Reads message from image that was written to by MessageWriter.writeMessage
     *
     * @return message hidden inside image of sourcePathname
     * @throws IOException
     */
    public String readMessage() throws IOException {
        this.traverse();
        return this.getMessageFromList();
    }
}
