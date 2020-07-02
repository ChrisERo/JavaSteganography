import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class UtilTest {

    private static Arguments[] containsStringData() {
       return new Arguments[]{
                Arguments.arguments("I", new String[0], false),
                Arguments.arguments(null, new String[0], false),
                Arguments.arguments("I", new String [] {"I"}, true),
                Arguments.arguments("I", new String [] {"he", "I", "she"}, true),
                Arguments.arguments("I", new String [] {"he", "she", "I"}, true),
                Arguments.arguments("I", new String [] {"he", "she"}, false),
                Arguments.arguments(null, new String [] {"he", "she"}, false),
        };
    }

    @ParameterizedTest
    @MethodSource("containsStringData")
    public void TestContainsString(String value, String[] values, boolean isPresent) {
        assertEquals(Util.containsString(value, values), isPresent);
    }

    public static Arguments[] hasRightFileFormatData() {
        return new Arguments[]{
                Arguments.arguments("imgpng", "png", false),
                Arguments.arguments("img_png", "png", false),
                Arguments.arguments("img.png", "png", true),
                Arguments.arguments("img.pngg", "png", false),
                Arguments.arguments("mrbean/img.png", "png", true),
                Arguments.arguments("mrbean/img.txt", "png", false),
                Arguments.arguments("mrbean/img.txt", "txt", true),
                Arguments.arguments("img.png", "txt", false),
                Arguments.arguments("img.png", "pngpngpng", false),
                Arguments.arguments("img.txt", "txt", true),
                Arguments.arguments(null, "png", false),
                Arguments.arguments(null, null, false),
                Arguments.arguments("img.png", null, false),
        };
    }

    @ParameterizedTest
    @MethodSource("hasRightFileFormatData")
    public void TestHasRightFileFormat(String filePath,
                                       String fileType, boolean isRightFileFormat) {
        assertEquals(isRightFileFormat, Util.hasRightFileFormat(filePath, fileType));
    }
}