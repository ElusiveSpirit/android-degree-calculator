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
                {"2°10'1''", "2°10'1''", "4°20'2''"},       //  0
                {"2°10'1''", "0°50'0''", "3°0'1''"},        //  1
                {"1°0,1'", "0°0,59'", "1°0,69'"},           //  2
                {"2°10,59'", "0°50,1'", "3°0,69'"},         //  3
                {"2°0'0''", "-0°0'1''", "1°59'59''"},       //  4
                {"2°0'0''", "-0°1'0''", "1°59'"},           //  5
                {"-2°0'0''", "-1°1'0''", "-3°1'"},          //  6
                {"-2°0'1''", "-1°59'59''", "-4°"},          //  7
                {"-2°10'1''", "-0°50'0''", "-3°0'1''"},     //  8
                {"2°59'2''", "0°00'58''", "3°"},            //  9
                {"0°0'1''", "-0°00'2''", "-1''"},           // 10
                {"-0°0'2''", "0°00'1''", "-1''"},           // 11
                {"0°0'2''", "0°00'1''", "3''"},             // 12
                {"-0°0'2''", "-0°00'1''", "-3''"},          // 13
                {"0°0'59''", "0°00'1''", "1'"},             // 14
                {"-0°0'2''", "-0°00'58''", "-1'"},          // 15
                {"-0°0'05''", "0°01'10''", "1'5''"},        // 16
                {"0°0'2''", "-1°0'2''", "-1°"},             // 17
                {"1°0'0''", "-1°5'2''", "-5'2''"},          // 18
                {"2°0'0''", "-0°5'5''", "1°54'55''"},       // 19
                {"2°55'55''", "1°55'55''", "4°51'50''"},    // 20
                {"101°56'0''", "-0°0'2''", "101°55'58''"},  // 21
                {"87°19,4'", "-0°0,2'", "87°19,2'"},        // 22
                {"95°48,7'", "-0°0,3'", "95°48,4'"},        // 23
                {"74°56,8'", "-0°0,2'", "74°56,6'"},        // 24

        });
    }

    @Test
    public void mainTest() throws Exception {
        assertEquals(result, new DegreeNumber(param1).add(new DegreeNumber(param2)).toString());
    }
}