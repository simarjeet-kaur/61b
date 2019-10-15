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
    private Permutation perm2;
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
       // Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));
      //  assertEquals(p.invert('B'), 'A');
      //  assertEquals(p.invert('G'), 'G');
    }

    @Test
    public void testSplitCycles() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String testCycles2 = "(QRS)(TUV) (WXY)";
        Alphabet alphabet1 = new Alphabet("ABCDEFGHIJKLMNOP");
        Alphabet alphabet2 = new Alphabet("QRSTUVWXY");
        perm = new Permutation(testCycles1, alphabet1);
        perm2 = new Permutation(testCycles2, alphabet2);
        assertEquals("ABCD", perm.splitCycles(testCycles1)[0]);
        assertEquals("HIJK", perm.splitCycles(testCycles1)[2]);
        assertEquals("QRS", perm2.splitCycles(testCycles2)[0]);
        assertEquals("TUV", perm2.splitCycles(testCycles2)[1]);
        assertEquals("WXY", perm2.splitCycles(testCycles2)[2]);
    }

    @Test
    public void testFindCycle() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String testCycles2 = "(QRS)(TUV) (WXY)";
        Alphabet alphabet1 = new Alphabet("ABCDEFGHIJKLMNOP");
        Alphabet alphabet2 = new Alphabet("QRSTUVWXY");
        perm = new Permutation(testCycles1, alphabet1);
        perm2 = new Permutation(testCycles2, alphabet2);
        assertEquals("ABCD", perm.findCycle('A'));
        assertEquals("LMNOP", perm.findCycle('P'));
        assertEquals("QRS", perm2.findCycle('S'));
        assertEquals("QRS", perm2.findCycle('R'));
        assertEquals("TUV", perm2.findCycle('V'));
    }

    @Test
    public void testFindPermute() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String testCycles2 = "(QRS)(TUV) (Y)";
        Alphabet alphabet1 = new Alphabet("ABCDEFGHIJKLMNOP");
        Alphabet alphabet2 = new Alphabet("QRSTUVWXY");
        perm = new Permutation(testCycles1, alphabet1);
        perm2 = new Permutation(testCycles2, alphabet2);
        assertEquals('B', perm.findPermute('A'));
        assertEquals('A', perm.findPermute('D'));
        assertEquals('Z', perm.findPermute('Z'));
        assertEquals('S', perm2.findPermute('R'));
        assertEquals('Y', perm2.findPermute('Y'));
        assertEquals('A', perm2.findPermute('A'));
    }

    @Test
    public void testFindInvert() {
        String testCycles1 = "(ABCD) (EFG) (HIJK) (LMNOP)";
        String testCycles2 = "(QRS)(TUV) (Y)";
        Alphabet alphabet1 = new Alphabet("ABCDEFGHIJKLMNOPZ");
        Alphabet alphabet2 = new Alphabet("QRSTUVWXYA");
        perm = new Permutation(testCycles1, alphabet1);
        perm2 = new Permutation(testCycles2, alphabet2);
        assertEquals('D', perm.findInvert('A'));
        assertEquals('C', perm.findInvert('D'));
        assertEquals('Z', perm.findInvert('Z'));
        //assertEquals('Q', perm2.findInvert('R'));
        assertEquals('Y', perm2.findInvert('Y'));
        assertEquals('A', perm2.findInvert('A'));
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

}
