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
public class DegreeInitTest {

    private String param1;
    private String param2;

    public DegreeInitTest(String params1, String params2) {
        this.param1 = params1;
        this.param2 = params2;
    }

    @Parameters
    public static Collection getParameters() {
        return Arrays.asList(new String[][]{
                {"-0°31,1'", "-31,1'"},     //  0
                {"-0°31'1''", "-31'1''"},   //  1
                {"-0°0,1'", "-0,1'"},       //  2
                {"2°10,1''", "2°0'10,1''"}, //  3
                {"-2°10,1'", "-2°10,1'"},   //  4
                {"-2°0'1''", "-2°0'1''"},   //  5
                {"-2°1'", "-2°1'"},         //  6
                {"-2°1''", "-2°0'1''"},     //  7
                {"1''", "1''"},             //  8
                {"-1''", "-1''"},           //  9
                {"1'", "1'"},               // 10
                {"1,2'", "1,2'"},           // 11
                {"1,2°", "1,2°"},           // 12
        });
    }

    @Test
    public void mainTest() throws Exception {
        assertEquals(param2, new DegreeNumber(param1).toString());
    }
}