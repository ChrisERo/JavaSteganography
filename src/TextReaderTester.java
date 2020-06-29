import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Collection;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;
/**
 * Tests for TextReader class
 */
@RunWith(Parameterized.class)
public class TextReaderTester {

    @Parameterized.Parameter(0)
    public String mssg; // message from which to read bits from
    @Parameterized.Parameter(1)
    public int bitsToRead; // number of bits to read
    @Parameterized.Parameter(2)
    public int resultingBit;
    @Parameterized.Parameter(3)
    public boolean shouldEnd;

    @Parameterized.Parameters(name = "{index}: Test with mssg={0}, bitsToRead={1}, resultingBit is:{2} ")
    public static Collection<Object[]> data() {

        Object[][] data = new Object[][]{
                {"I", 1, 0, false},
                {"I", 10, 1, false},
                {"I", 16, 1, false},
        };
        List<Object[]> result = new ArrayList<>(Arrays.asList(data));
        for (int i = 17; i <= 32; i++) {
            result.add(new Object[] {"I", i, 0, i == 32});
        }

        int [] is = new int[] {
                0,0,0,0,0,0,0,0,
                0,1,0,0,1,0,0,1,
                0,0,0,0,0,0,0,0,
                0,1,1,1,0,0,1,1,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };
        for (int i = 1; i <= 48; i++) {
            result.add(new Object[] {"Is", i, is[i-1], i == 48});
        }
        return result;
    }


    @Test
    public void TestReadBit() {
        TextReader tr = new TextReader(mssg);
        int result = 0;
        for(int i = 0; i < bitsToRead; i++) {
            result = tr.readBit();
        }
        assertEquals(resultingBit, result);
    }

    @Test
    public void TestShouldEnd() {
        TextReader tr = new TextReader(mssg);
        for(int i = 0; i < bitsToRead; i++) {
            tr.readBit();
        }
        boolean shouldEndRead = tr.shouldEnd();
        assertEquals(shouldEnd, shouldEndRead);
    }
}
