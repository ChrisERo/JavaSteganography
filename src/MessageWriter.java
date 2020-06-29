import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * This class handles writing messages onto an chosen jpeg image
 *
 * Assumes the following:
 */
public class MessageWriter extends Traverser{
    private MessageReader messageReader;
    private String destinationPath;

    /**
     *
     * @param mssg
     * @param imagePathName
     * @param destinationPath
     * @throws IOException
     */
    public MessageWriter(String mssg, String imagePathName, String destinationPath) throws IOException{
        super(DIMENSIONS, imagePathName);
        assert(mssg != null && mssg.length() > 0);
        this.messageReader = new MessageReader(mssg);
        this.destinationPath = destinationPath;
    }


    /**
     *
     * Assumes BufferedImage.getRGB() returns pixel value in format ARGB per
     * https://stackoverflow.com/questions/6001211/format-of-type-int-rgb-and-type-int-argb
     *
     * @param c color/dimension we are currently traversing through. in range [0, 2]
     * @param x horizontal coordinate of current piexel of traverse()
     * @param y vertical coordinate of current piexel of traverse()
     * @return
     */
    public boolean injectedFunction(int c, int x, int y) {
        if (this.messageReader.shouldEnd()) {
            return true;
        }

        int nextBit = this.messageReader.readBit();
        assert(nextBit == 0 || nextBit == 1);
        int pixelValue = this.targetImage.getRGB(x, y);

        // Make sure that pixelValue's lsb at color c differs from nextBit of message before modifying
        if (pixelValue >> (c * BITS_PER_DIMENSION) % 2 != nextBit) {
            pixelValue = pixelValue ^ this.makeMask(c);
            this.targetImage.setRGB(x, y, pixelValue);
        }

        return false;
    }

    /**
     *
     * @return
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
     * Writes MessageWriter's image to file this.destinationPath
     */
    private void writeNewImage() throws IOException {
        File outputfile = new File(this.destinationPath);
        String fileType = this.destinationPath.substring(this.destinationPath.lastIndexOf('.')+1);
        ImageIO.write(this.targetImage, fileType, outputfile);
    }

    /**
     *
     */
    public void writeMessage() throws IOException {
        // Make message fit in image
        int maxLength = this.getMaxLength(); // max length of any message in the writer's image
        if (this.messageReader.message.length() >= maxLength) {
            this.messageReader.message = this.messageReader.message.substring(0, maxLength - 1);
        }

        this.traverse(this.targetImage);
        this.writeNewImage();
    }

    public static void main(String[] args) throws IOException {
        MessageWriter mw = new MessageWriter("I love you very very very very very very very " +
                "much", "IMG_2485.jpeg", "fakeImage.jpeg");
        mw.writeMessage();

    }
}
