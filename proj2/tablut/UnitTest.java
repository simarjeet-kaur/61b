package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import static tablut.Square.sq;

import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void dummyTest() {
        assertTrue("There are no unit tests!", false);
    }


    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
            NTHRONE = sq(4, 5),
            STHRONE = sq(4, 3),
            WTHRONE = sq(3, 4),
            ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
            sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
            sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
            sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
            sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
            NTHRONE, ETHRONE, STHRONE, WTHRONE,
            sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    @Test
    public void testIsUnblocked() {
        Board _testBoard;
        _testBoard = new Board();
        assertEquals(true, _testBoard.isUnblockedMove(sq(0), sq(2)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(0), sq(9)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(8), sq(80)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(8), sq(9)));
    }

    @Test
    public void TestIsLegal() {
        Board _testBoard;
        _testBoard = new Board();
        //assertEquals(true, _testBoard.isLegal());
    }

}


