import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests for TextReader class
 */
public class TextReaderTester {

    public static Collection<Arguments> data() {

        Arguments[] data = new Arguments[]{
                Arguments.arguments("I", 1, 0, false),
                Arguments.arguments("I", 10, 1, false),
                Arguments.arguments("I", 16, 1, false),
        };
        List<Arguments> result = new ArrayList<Arguments>(Arrays.asList(data));
        for (int i = 17; i <= 32; i++) {
            result.add(Arguments.arguments("I", i, 0, i == 32));
        }

        int [] is = new int[] {
                0,0,0,0,0,0,0,0,
                0,1,0,0,1,0,0,1,
                0,0,0,0,0,0,0,0,
                0,1,1,1,0,0,1,1,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };
        for (int i = 1; i <= 48; i++) {
            result.add(Arguments.arguments("Is", i, is[i-1], i == 48));
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("data")
    public void TestReadBit(String mssg, int bitsToRead, int resultingBit,
                            boolean shouldEnd) {
        TextReader tr = new TextReader(mssg);
        int result = 0;
        for(int i = 0; i < bitsToRead; i++) {
            result = tr.readBit();
        }
        assertEquals(resultingBit, result);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void TestShouldEnd(String mssg, int bitsToRead, int resultingBit,
                              boolean shouldEnd) {
        TextReader tr = new TextReader(mssg);
        for(int i = 0; i < bitsToRead; i++) {
            tr.readBit();
        }
        boolean shouldEndRead = tr.shouldEnd();
        assertEquals(shouldEnd, shouldEndRead);
    }
}
