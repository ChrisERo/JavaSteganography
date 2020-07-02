import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

class JavaMessageHiderTest {
    private static Arguments[] checkInputHelperData() {
        return new Arguments[] {
                Arguments.arguments("-w myImg.png".split(" "), null),
                Arguments.arguments("-w d/myImg.png t.txt".split(" "), null),
                Arguments.arguments("-write myImg.png".split(" "), null),
                Arguments.arguments("-read myImg.png".split(" "), null),
                Arguments.arguments("-r the/myImg.png".split(" "), null),
                Arguments.arguments("-r the/myImg.png t.txt".split(" "), null),

                Arguments.arguments("-r".split(" "), "INVALID NUMBER OF ARGUMENTS"),
                Arguments.arguments(null, "INVALID NUMBER OF ARGUMENTS"),
                Arguments.arguments("-w d/myImg.png t.txt t.txt".split(" "),
                        "INVALID NUMBER OF ARGUMENTS"),

                Arguments.arguments("-a the/myImg.png t.txt".split(" "),
                        "INVALID COMMAND -a"),
                Arguments.arguments("-wrrite the/myImg.png t.txt".split(" "),
                        "INVALID COMMAND -wrrite"),

                Arguments.arguments("-r the/myImg.jpeg".split(" "),
                        "INVALID IMAGE TYPE [the/myImg.jpeg], MUST BE .png"),
                Arguments.arguments("-w the/myImg.txt".split(" "),
                        "INVALID IMAGE TYPE [the/myImg.txt], MUST BE .png"),

                Arguments.arguments("-r myImg.png t.jpeg".split(" "),
                        "INVALID MESSAGE SOURCE/DESTINATION TYPE [t.jpeg], MUST BE .txt"),
                Arguments.arguments("-w myImg.png t.png".split(" "),
                        "INVALID MESSAGE SOURCE/DESTINATION TYPE [t.png], MUST BE .txt"),
        };
    }

    @ParameterizedTest
    @MethodSource("checkInputHelperData")
    public void TestCheckInputHelper(String[] args, String out) {
        assertEquals(out, JavaMessageHider.checkInputHelper(args));
    }
}
