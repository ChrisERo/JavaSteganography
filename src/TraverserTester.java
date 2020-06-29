import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Tests for Traverser abstract class' non-abstract, public functions,
 * excluding traverse()
 */
public class TraverserTester {
    @Test
    public void TestMakeMask() throws IOException {
        Traverser tr = new MessageReader("testImage.png");
        int[][] colorChannels = new int[][]{{0, 1}, {1, 256}, {2, 65536}};
        for(int[] data: colorChannels) {
            int channel = data[0];
            int mask = tr.makeMask(channel);
            int maskValueExpected = data[1];
            assertEquals(maskValueExpected, mask);
        }
    }

    @Test
    public void TestGetPixelBit() throws IOException {
        Traverser tr = new MessageReader("testImage.png");

        int[] pixelValues = new int[]{
                32 + (0xfb << 8) + (0xaf << 16),
                121 + (0xfc << 8) + (0xae << 16),
                121 + (121 << 8) + (121 << 16),
                144 + (220 << 8) + (46 << 16),
                0,
                255 + (255 << 8) + (255 << 16),
        };
        int [][] bitExpected = new int[][] {
                {0, 1, 1},
                {1, 0, 0},
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0},
                {1, 1, 1},
        };
        for (int i = 0; i < pixelValues.length; i++) {
            int pixelValue = pixelValues[i];
            for (int c = 0; c <= 2; c++) {
                int bit = tr.getPixelBit(pixelValue, c);
                assertEquals(bitExpected[i][c], bit);
            }
        }
    }
}
