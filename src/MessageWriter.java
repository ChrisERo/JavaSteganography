import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Handles writing messages onto an chosen jpeg image
 */
public class MessageWriter extends Traverser{
    private TextReader textReader;  // Handles getting next bit of message
    private String destinationPath;  // Where to save steganographic image

    /**
     * Constructor for MessageWriter
     *
     * @param mssg message to hide in image
     * @param imagePathName path to image in which to perform steganography
     * @param destinationPath where to save steganographic image
     * @throws IOException
     */
    public MessageWriter(String mssg, String imagePathName,
                         String destinationPath) throws IOException{
        super(DIMENSIONS, imagePathName);
        assert(mssg != null && mssg.length() > 0);
        this.textReader = new TextReader(mssg);
        this.destinationPath = destinationPath;
    }


    /**
     * Reads the next bit from textReader, if there is one, and places in color channel
     * c of targetImage at pixel[x, y]
     *
     * Assumes BufferedImage.getRGB() returns pixel value in format ARGB per
     * https://stackoverflow.com/questions/6001211/format-of-type-int-rgb-and-type-int-argb
     *
     * @param c color/dimension we are currently traversing through. in range [0, 2]
     * @param x horizontal coordinate of current piexel of traverse()
     * @param y vertical coordinate of current piexel of traverse()
     * @return true if traverse() should terminate and false otherwise
     */
    public boolean injectedFunction(int c, int x, int y) {
        if (this.textReader.shouldEnd()) {
            return true;
        }

        int nextBit = this.textReader.readBit();
        assert(nextBit == 0 || nextBit == 1);
        int pixelValue = this.targetImage.getRGB(x, y);

        // Make sure pixelValue's lsb at color c differs from nextBit of message
        if (pixelValue >> (c * BITS_PER_DIMENSION) % 2 != nextBit) {
            pixelValue = pixelValue ^ this.makeMask(c);
            this.targetImage.setRGB(x, y, pixelValue);
        }

        return false;
    }

    /**
     * Calculates how many characters can be hidden inside targetImage, where targetImage
     * is an RGB image.
     *
     * @return number of characters that can fill lsbs of targetImage
     */
    private int getMaxLength() {
        if (this.targetImage == null)
            return 0;
        int result = this.targetImage.getHeight() * this.targetImage.getWidth() / BITS_PER_DIMENSION;
        int imgType = this.targetImage.getType();
        if (imgType == BufferedImage.TYPE_3BYTE_BGR) { // TODO: figure out if others need this treatment
            result *= 3;
        }
        return result;
    }

    /**
     * Writes MessageWriter's image to file this.destinationPath. The algorithm used goes
     * through each color channel and for ever pixel (top down, left right) flips the
     * lsb of the pixel.
     *
     * @throws IOException
     */
    private void writeNewImage() throws IOException {
        File outputfile = new File(this.destinationPath);
        String fileType = this.destinationPath.substring(this.destinationPath.lastIndexOf('.')+1);
        ImageIO.write(this.targetImage, fileType, outputfile);
    }

    /**
     * Writes message stored in textReader onto targetImage
     *
     * @throws IOException
     */
    public void writeMessage() throws IOException {
        // Make message fit in image
        int maxLength = this.getMaxLength(); // max length of any message in the writer's image
        if (this.textReader.message.length() >= maxLength) {
            this.textReader.message = this.textReader.message.substring(0, maxLength - 1);
        }

        this.traverse();
        this.writeNewImage();
    }

    /**
     * Test
     */
    public static void main(String[] args) throws IOException {
        MessageWriter mw = new MessageWriter("I love you very very very very very very very " +
                "much", "IMG_2485.jpeg", "fakeImage.jpeg");
        mw.writeMessage();

    }
}
