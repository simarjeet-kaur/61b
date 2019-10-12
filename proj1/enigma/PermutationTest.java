package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Simarjeet Kaur
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* testing the Permutation::invert method */
    @Test
    public void testInvertChar() {
        //FIXME get rid of // below
        //Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));
            //b will map to A because of the () given
        //assertEquals(p.invert('B'), 'A');
            //g is not mapped to anything so it will return itself
        //assertEquals(p.invert('G'), 'G');
    }

    @Test
    public void testSplitCycles() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String testCycles2 = "(QRS)(TUV) (WXY)";
        assertEquals(perm.splitCycles(testCycles1)[0], "ABCD");
        assertEquals(perm.splitCycles(testCycles2)[0], "QRS");
    }

    @Test
    public void testFindCycle() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String [] testCycles1Array = new String[4];
        testCycles1Array[0] = "ABCD";
        testCycles1Array[1] = "EFG";
        testCycles1Array[2] = "HIJK";
        testCycles1Array[3] = "LMNOP";
        assertEquals(perm.findCycle('A'), "ABCD");
        assertEquals(perm.findCycle('P'), "LMNOP");
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

}
