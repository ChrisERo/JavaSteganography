import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Traverser {
    public static final int DIMENSIONS = 3;
    public static final int BITS_PER_DIMENSION = 8;

    private int x;
    private int y;
    private int dimensions;

    protected BufferedImage targetImage;  // Image on which to hide our hidden message

    public Traverser(int colors, String imagePathName) throws IOException {
        this.targetImage = ImageIO.read(new File(imagePathName));
        assert(this.targetImage.getType() == BufferedImage.TYPE_3BYTE_BGR);

        this.x = 0;
        this.y = 0;
        this.dimensions = colors;
    }

    /**
     * Returns bitmask for color corresponding to c (R=2,G=1,B=0)
     *
     * @param c number in range [0, 2]
     * @return a bit mask where the lsb of either the R, G, or B part of a pixel is 1 and all other values are 0
     */
    public int makeMask(int c) {
        assert(c > 0 && c <=2);
        return 1 << (c * BITS_PER_DIMENSION);
    }

    /**
     * Performs some operation (determined by child classes) based on the current pixel/color we are on and
     * internal state of Traverser object. Then indicates whether traversal should end.
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
     *
     * @param image
     */
    public void traverse(BufferedImage image) {
        for(int c = 0; c < this.dimensions; c++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    boolean should_stop = injectedFunction(c, x, y);
                    if (should_stop)
                        return;
                }
            }
        }

    }
}
