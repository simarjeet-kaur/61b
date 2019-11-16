package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import static tablut.Square.sq;

import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author Simarjeet Kaur
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
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
        Board testBoard;
        testBoard = new Board();
        assertEquals(true, testBoard.isUnblockedMove(sq(5), sq(8)));
        assertEquals(false, testBoard.isUnblockedMove(sq(4), sq(8)));
        assertEquals(false, testBoard.isUnblockedMove(sq(8), sq(80)));
        assertEquals(false, testBoard.isUnblockedMove(sq(8), sq(9)));
        assertEquals(true, testBoard.isUnblockedMove(sq(5), sq(8)));
        assertEquals(true, testBoard.isUnblockedMove(sq(3), sq(12)));
    }

    @Test
    public void testPut() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(4, 0));
        assertEquals(Piece.EMPTY, testBoard.get(4, 0));
        testBoard.put(Piece.BLACK, sq(0, 0));
        assertEquals(Piece.BLACK, testBoard.get(0, 0));
    }

    @Test
    public void testMakeMove() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 4), sq(4, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(1, 3), sq(3, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 5), sq(3, 5));
        System.out.print(testBoard);
        testBoard.makeMove(sq(2, 4), sq(2, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 5), sq(5, 5));
        System.out.print(testBoard);
    }

    @Test
    public void testMakeMove1() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 6), sq(7, 7));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 2), sq(1, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 1), sq(1, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 4), sq(4, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(1, 1), sq(0, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(1, 2), sq(0, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 2), sq(4, 2));
        System.out.print(testBoard);
    }

    @Test
    public void testMakeMove2() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.put(Piece.BLACK, sq(4, 5));
        testBoard.put(Piece.BLACK, sq(4, 3));
        testBoard.put(Piece.BLACK, sq(3, 4));
        testBoard.put(Piece.EMPTY, sq(5, 4));
        testBoard.put(Piece.EMPTY, sq(6, 4));
        testBoard.put(Piece.EMPTY, sq(7, 4));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 4), sq(5, 4));

        System.out.print(testBoard);
        System.out.print(testBoard.winner());

    }

    @Test
    public void testMakeMove3() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 8), sq(0, 8));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 2), sq(0, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 0), sq(1, 0));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 4), sq(4, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(testBoard);
    }

    @Test
    public void testMakeMove4() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(4, 1));
        testBoard.put(Piece.WHITE, sq(5, 1));
        testBoard.put(Piece.BLACK, sq(6, 1));
        testBoard.put(Piece.WHITE, sq(3, 1));
        testBoard.put(Piece.BLACK, sq(2, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(testBoard);

    }


    @Test
    public void testMakeMove5() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(4, 1));
        testBoard.put(Piece.WHITE, sq(5, 1));
        testBoard.put(Piece.BLACK, sq(6, 1));
        testBoard.put(Piece.WHITE, sq(3, 1));
        testBoard.put(Piece.BLACK, sq(2, 1));
        testBoard.put(Piece.BLACK, sq(4, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(testBoard);
    }

    @Test
    public void testCaptured() {
        Board testBoard;
        testBoard = new Board();
        testBoard.makeMove(sq(8, 5), sq(5, 5));
        System.out.print(testBoard);
        testBoard.makeMove(sq(6, 4), sq(6, 5));
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 8), sq(3, 5));
        System.out.print(testBoard);
        testBoard.makeMove(sq(2, 4), sq(2, 5));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 3), sq(3, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 2), sq(3, 2));
        System.out.print(testBoard);
        System.out.print(testBoard);
        System.out.print(testBoard);
    }

    @Test
    public void testUndo() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(testBoard);
        testBoard.undo();
        System.out.print(testBoard);
    }

    @Test
    public void testUndo1() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 6), sq(7, 7));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 2), sq(1, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 1), sq(1, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 4), sq(4, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(1, 1), sq(0, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(1, 2), sq(0, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 2), sq(4, 2));
        System.out.print(testBoard);
        testBoard.undo();
        System.out.print(testBoard);
    }

    @Test
    public void testUndo2() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.put(Piece.BLACK, sq(4, 5));
        testBoard.put(Piece.BLACK, sq(4, 3));
        testBoard.put(Piece.BLACK, sq(3, 4));
        testBoard.put(Piece.EMPTY, sq(5, 4));
        testBoard.put(Piece.EMPTY, sq(6, 4));
        testBoard.put(Piece.EMPTY, sq(7, 4));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 4), sq(5, 4));
        System.out.print(testBoard);
        System.out.print(testBoard.winner());
        testBoard.undo();
        System.out.print(testBoard);
        System.out.print(testBoard.winner());

    }

    @Test
    public void testUndo3() {
        Board testBoard;
        testBoard = new Board();
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 8), sq(0, 8));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 2), sq(0, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(0, 0), sq(1, 0));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 4), sq(4, 2));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(testBoard);
        System.out.print(testBoard.winner());
        testBoard.undo();
        System.out.print(testBoard);
        System.out.print(testBoard.winner());
    }

    @Test
    public void testUndo4() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(4, 1));
        testBoard.put(Piece.WHITE, sq(5, 1));
        testBoard.put(Piece.BLACK, sq(6, 1));
        testBoard.put(Piece.WHITE, sq(3, 1));
        testBoard.put(Piece.BLACK, sq(2, 1));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(testBoard);
        testBoard.undo();
        System.out.print(testBoard);
    }

    @Test
    public void testUndo5() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(4, 1));
        testBoard.put(Piece.WHITE, sq(5, 1));
        testBoard.put(Piece.BLACK, sq(6, 1));
        testBoard.put(Piece.WHITE, sq(3, 1));
        testBoard.put(Piece.BLACK, sq(2, 1));
        testBoard.put(Piece.BLACK, sq(4, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(testBoard);
        testBoard.undo();
        System.out.print(testBoard);
    }

    @Test
    public void capture5() {
        Board testBoard;
        testBoard = new Board();
        testBoard.put(Piece.EMPTY, sq(5, 4));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 5), sq(8, 6));
        testBoard.makeMove(sq(6, 4), sq(6, 5));
        System.out.print(testBoard);
        testBoard.put(Piece.BLACK, sq(4, 3));
        testBoard.put(Piece.WHITE, sq(5, 3));
        System.out.print(testBoard);
        testBoard.makeMove(sq(8, 3), sq(8, 2));
        testBoard.makeMove(sq(4, 4), sq(5, 4));
        System.out.print(testBoard);
        testBoard.makeMove(sq(7, 4), sq(6, 4));
        System.out.print(testBoard);
    }
}



