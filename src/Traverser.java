import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Abstract class for Objects that traverse the pixels of some image and perform some
 * action on them. The traversal is done by color channel, height, and finally width.
 */
public abstract class Traverser {
    public static final int DIMENSIONS = 3;  // Number of color channels
    public static final int BITS_PER_DIMENSION = 8;  // Number of bits representing chanel

    private int dimensions; // number of color channels in targetImage;
    protected BufferedImage targetImage;  // Image on which to hide our hidden message

    public Traverser(int colors, String imagePathName) throws IOException {
        this.targetImage = ImageIO.read(new File(imagePathName));
        assert(this.targetImage.getType() == BufferedImage.TYPE_3BYTE_BGR);

        this.dimensions = colors;
    }

    /**
     * Returns bitmask for color corresponding to c (R=2,G=1,B=0)
     *
     * @param c number in range [0, 2]
     * @return a bit mask where the lsb of either the R, G, or B part of a pixel is 1 and
     *         all other values are 0
     */
    public int makeMask(int c) {
        assert(c > 0 && c <=2);
        return 1 << (c * BITS_PER_DIMENSION);
    }

    /**
     * Performs some operation (determined by child classes) based on the current
     * pixel/color we are on and internal state of Traverser object. Then indicates
     * whether traversal should end.
     *
     * Only executed in Traverser.traverse()
     *
     * @param c color/dimension we are currently traversing through
     * @param x horizontal coordinate of current piexel of traverse()
     * @param y vertical coordinate of current piexel of traverse()
     * @return true if traverse should terminate and false otherwise
     */
    public abstract boolean injectedFunction(int c, int x, int y);

    /**
     * Traverses each pixel of targetImage one color channel, performing injectedFunction
     * and checking to see whether injectedFunction indicates that the function should
     * terminate. The traversal starts from top to bottom, left to right.
     */
    public void traverse() {
        for(int c = 0; c < this.dimensions; c++) {
            for (int y = 0; y < this.targetImage.getHeight(); y++) {
                for (int x = 0; x < this.targetImage.getWidth(); x++) {
                    boolean should_stop = injectedFunction(c, x, y);
                    if (should_stop)
                        return;
                }
            }
        }

    }
}
