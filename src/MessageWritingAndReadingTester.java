import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Collection;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MessageWritingAndReadingTester {
    private static final String IMG_PATH = "testPic1.png"; // path to original test image

    @Parameterized.Parameter(0)
    public String mssgIn; // message to hide in the picture (with truncations)

    @Parameterized.Parameter(1)
    public String expectedMessageOut; // message written by MessageWriter

    @Parameterized.Parameters(name = "{index}: Test with mssgIn={0}, " +
            "expectedMessageOut={1}")
    public static Collection<Object[]> data() throws IOException {
        // Make sure that image IMG_PATH has expected dimensions
        BufferedImage img = ImageIO.read(new File(IMG_PATH));
        assertEquals(216/3, img.getHeight() * img.getWidth());

        Object[][] data = new Object[][]{
                {"I", "I"},
                {"I love you!", "I love you!"},
                {"The man does", "The man does"},
                {"The man doses", "The man dose"},
        };

        return Arrays.asList(data);
    }

    @Test
    public void TestMessageWriterAndMessageReader() throws IOException {
        String destinationPath = "fakePhoto.png";
        MessageWriter mw = new MessageWriter(mssgIn, IMG_PATH,
                destinationPath);
        mw.writeMessage();

        MessageReader mr = new MessageReader(destinationPath);
        String hiddenMessage = mr.readMessage();

        assertEquals(expectedMessageOut, hiddenMessage.substring(0,
                hiddenMessage.length()-1)); // substringing needed to avoid trimming
    }
}
