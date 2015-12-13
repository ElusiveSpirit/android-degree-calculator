package android.solleks.degreecalculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(value = Parameterized.class)
public class DegreeCalculateTest {

    private String param1;
    private String param2;
    private String result;

    public DegreeCalculateTest(String params1, String params2, String result) {
        this.param1 = params1;
        this.param2 = params2;
        this.result = result;
    }

    @Parameters
    public static Collection getParameters() {
        return Arrays.asList(new String[][]{
                {"2°10,1'", "2°10'1''", "4°20'2''"},
                {"2°10,1'", "0°50'0''", "3°0'1''"},
        });
    }

    @Test
    public void mainTest() throws Exception {
        assertEquals(result, new DegreeNumber(param1).add(new DegreeNumber(param2)).toString());
    }
}