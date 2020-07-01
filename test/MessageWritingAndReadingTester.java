import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Collection;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.Arrays;

public class MessageWritingAndReadingTester {
    private static final String IMG_PATH = "testPic1.png"; // path to original test image



    public static Arguments[] data() {
        return new Arguments[] {
                Arguments.arguments("I", "I"),
                Arguments.arguments("I love you!", "I love you!"),
                Arguments.arguments("The man does", "The man does"),
                Arguments.arguments("The man doses", "The man dose"),
        };
    }

    /**
     * Make sure that image IMG_PATH has expected dimensions
     */
    @BeforeAll
    public static void sanityCheck() throws IOException {
        BufferedImage img = ImageIO.read(new File(IMG_PATH));
        assertEquals(216/3, img.getHeight() * img.getWidth());
    }

    @ParameterizedTest
    @MethodSource("data")
    public void TestMessageWriterAndMessageReader(String mssgIn,
                                                  String expectedMessageOut)
            throws IOException {
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
