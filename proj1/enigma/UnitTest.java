package enigma;

import org.junit.Test;
import ucb.junit.textui;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author Simarjeet Kaur
 */

public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

}


//    @Test
//    public void testInvertChar() {
//        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));
//        //b will map to A because of the () given
//        assertEquals(p.invert('B'), 'A');
//        //g is not mapped to anything so it will return itself
//        assertEquals(p.invert('G'), 'G');
//    }

