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

//    /** A dummy test as a placeholder for real ones. */
//    @Test
//    public void dummyTest() {
//        assertTrue("There are no unit tests!", false);
//    }


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
        assertEquals(true, _testBoard.isUnblockedMove(sq(5), sq(8)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(4), sq(8)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(8), sq(80)));
        assertEquals(false, _testBoard.isUnblockedMove(sq(8), sq(9)));
        assertEquals(true, _testBoard.isUnblockedMove(sq(5), sq(8)));
        assertEquals(true, _testBoard.isUnblockedMove(sq(3), sq(12)));
    }
//
//    @Test
//    public void TestIsLegal() {
//        Board _testBoard;
//        _testBoard = new Board();
//        _testBoard.setMoveLimit(5);
//        assertEquals(true, _testBoard.isLegal(sq(5), sq(8)));
//        assertEquals(false, _testBoard.isLegal(sq(3), sq(5)));
//        assertEquals(true, _testBoard.isLegal(sq(3), sq(12)));
//        assertEquals(true, _testBoard.isLegal(sq(42), sq(78)));
//        assertEquals(true, _testBoard.isLegal(sq(43), sq(52)));
//        //   assertEquals(false,)
//    }
////
//    @Test
//    public void testMakeMove() {
//        Board _testBoard;
//        _testBoard = new Board();
//        _testBoard.setMoveLimit(5);
//        _testBoard.makeMove(sq(3), sq(2));
//        Piece taken = _testBoard.get(sq(2));
//        assertEquals(taken, Piece.EMPTY);
//        assertEquals(_testBoard.get(sq(3)), taken);
//    }

    @Test
    public void testPut() {
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.put(Piece.EMPTY, sq(4, 0));
        assertEquals(Piece.EMPTY, _testBoard.get(4, 0));
        _testBoard.put(Piece.BLACK, sq(0, 0));
        assertEquals(Piece.BLACK, _testBoard.get(0, 0));
    }

//    @Test
//    public void testCapture() {
//        Board _testBoard;
//        _testBoard = new Board();
//        _testBoard.put(Piece.WHITE, sq(2, 0));
//        _testBoard.put(Piece.BLACK, sq(1, 0));
//        _testBoard.capture(sq(3, 0), sq(1, 0));
//        assertEquals(Piece.BLACK, _testBoard.get(sq(2, 0)));
//        _testBoard.put(Piece.WHITE, sq(0, 0));
//        _testBoard.put(Piece.BLACK, sq(0, 1));
//        _testBoard.put(Piece.BLACK, sq(0, 2));
//        _testBoard.capture(sq(0, 0), sq(0, 2));
//        assertEquals(Piece.BLACK, _testBoard.get(sq(0, 1)));
//    }

    @Test
    public void testMakeMove() { //regular capture
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 4), sq(4, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(1, 3), sq(3, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(0, 5), sq(3, 5));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(2, 4), sq(2, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(8, 5), sq(5, 5));
        System.out.print(_testBoard);
        //assertEquals(Piece.WHITE, _testBoard.get(sq(4, 3)));
        //assertEquals(Piece.KING, _testBoard.get(sq(4, 3)));
    }

    @Test
    public void testMakeMove1() { //capture with hostile king and black piece
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(7, 6), sq(7, 7));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 2), sq(1, 2));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(4, 1), sq(1, 1));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 4), sq(4, 1));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(1, 1), sq(0, 1));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(1, 2), sq(0, 2));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(0, 2), sq(4, 2));
        System.out.print(_testBoard);

    }

    @Test
    public void testMakeMove2() { //king throne capture
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.put(Piece.BLACK, sq(4, 5));
        _testBoard.put(Piece.BLACK, sq(4, 3));
        _testBoard.put(Piece.BLACK, sq(3, 4));
        _testBoard.put(Piece.EMPTY, sq(5, 4));
        _testBoard.put(Piece.EMPTY, sq(6, 4));
        _testBoard.put(Piece.EMPTY, sq(7, 4));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(8, 4), sq(5, 4));

        System.out.print(_testBoard);
        System.out.print(_testBoard.winner());

    }

    @Test
    public void testMakeMove3() { //king not throne captured
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 8), sq(0, 8));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 2), sq(0, 2));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(0, 0), sq(1, 0));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 4), sq(4, 2));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(_testBoard);
    }

    @Test
    public void testMakeMove4() { //two captured
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.put(Piece.EMPTY, sq(4, 1));
        _testBoard.put(Piece.WHITE, sq(5, 1));
        _testBoard.put(Piece.BLACK, sq(6, 1));
        _testBoard.put(Piece.WHITE, sq(3, 1));
        _testBoard.put(Piece.BLACK, sq(2, 1));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(_testBoard);

    }


    @Test
    public void testMakeMove5() { //three captured
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.put(Piece.EMPTY, sq(4, 1));
        _testBoard.put(Piece.WHITE, sq(5, 1));
        _testBoard.put(Piece.BLACK, sq(6, 1));
        _testBoard.put(Piece.WHITE, sq(3, 1));
        _testBoard.put(Piece.BLACK, sq(2, 1));
        _testBoard.put(Piece.BLACK, sq(4, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(_testBoard);
    }
//    @Test
//    public void testIsLegal() {
//        Board _testBoard;
//        _testBoard = new Board();
//        _testBoard.makeMove(sq(7, 4), sq(7, 6));
//        //assertEquals(Piece.BLACK, _testBoard.get(sq(7, 6)));
//        _testBoard.makeMove(sq(4, 3), sq(1, 3));
//        assertTrue(_testBoard.isLegal(sq(4, 4), sq(4, 3)));
//        //_testBoard.makeMove(sq(4, 4), sq(4, 3));
//        //assertEquals(Piece.KING, _testBoard.get(sq(4, 3)));
//    }

    @Test
    public void testCaptured() {
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.makeMove(sq(8, 5), sq(5, 5));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(6, 4), sq(6, 5));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 8), sq(3, 5));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(2, 4), sq(2, 5));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(0, 3), sq(3, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 2), sq(3, 2));
        System.out.print(_testBoard);
        System.out.print(_testBoard);
        System.out.print(_testBoard);

    }

    @Test
    public void testUndo() { //noncapture
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(_testBoard);
        _testBoard.undo();
        System.out.print(_testBoard);
    }

    @Test
    public void testUndo1() { //capture one piece
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(7, 4), sq(7, 6));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(7, 6), sq(7, 7));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 2), sq(1, 2));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(4, 1), sq(1, 1));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(4, 4), sq(4, 1));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(1, 1), sq(0, 1));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(1, 2), sq(0, 2));
        System.out.print(_testBoard);
        //black
        _testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(_testBoard);
        //white
        _testBoard.makeMove(sq(0, 2), sq(4, 2));
        System.out.print(_testBoard);
        //undo
        _testBoard.undo();
        System.out.print(_testBoard);
    }

    @Test
    public void testUndo2() { //capturing and uncapturing the throne
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.put(Piece.BLACK, sq(4, 5));
        _testBoard.put(Piece.BLACK, sq(4, 3));
        _testBoard.put(Piece.BLACK, sq(3, 4));
        _testBoard.put(Piece.EMPTY, sq(5, 4));
        _testBoard.put(Piece.EMPTY, sq(6, 4));
        _testBoard.put(Piece.EMPTY, sq(7, 4));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(8, 4), sq(5, 4));

        System.out.print(_testBoard);
        System.out.print(_testBoard.winner());

        _testBoard.undo();
        System.out.print(_testBoard);
        System.out.print(_testBoard.winner());

    }

    @Test
    public void testUndo3() { //undoing capturing the king outside of the throne spots
        Board _testBoard;
        _testBoard = new Board();
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 8), sq(0, 8));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 2), sq(0, 2));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(3, 0), sq(0, 0));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 3), sq(1, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(0, 0), sq(1, 0));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 4), sq(4, 2));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(8, 3), sq(4, 3));
        System.out.print(_testBoard);
        System.out.print(_testBoard.winner());
        _testBoard.undo();
        System.out.print(_testBoard);
        System.out.print(_testBoard.winner());

    }

    @Test
    public void testUndo4() { //capturing two pieces and undoing it
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.put(Piece.EMPTY, sq(4, 1));
        _testBoard.put(Piece.WHITE, sq(5, 1));
        _testBoard.put(Piece.BLACK, sq(6, 1));
        _testBoard.put(Piece.WHITE, sq(3, 1));
        _testBoard.put(Piece.BLACK, sq(2, 1));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(_testBoard);
        _testBoard.undo();
        System.out.print(_testBoard);
    }

    @Test
    public void testUndo5() { //capturing three pieces and undoing it
        Board _testBoard;
        _testBoard = new Board();
        _testBoard.put(Piece.EMPTY, sq(4, 1));
        _testBoard.put(Piece.WHITE, sq(5, 1));
        _testBoard.put(Piece.BLACK, sq(6, 1));
        _testBoard.put(Piece.WHITE, sq(3, 1));
        _testBoard.put(Piece.BLACK, sq(2, 1));
        _testBoard.put(Piece.BLACK, sq(4, 3));
        System.out.print(_testBoard);
        _testBoard.makeMove(sq(4, 0), sq(4, 1));
        System.out.print(_testBoard);
        _testBoard.undo();
        System.out.print(_testBoard);

    }
}



