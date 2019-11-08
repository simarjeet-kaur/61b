package tablut;

import jdk.jshell.spi.ExecutionControl;

import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author Simarjeet Kaur
 */
class  Board {

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

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        _limit = model._limit;
        _board = model._board ;
        _turn = model._turn;
        _winner = model._winner;
        _moveCount = model._moveCount;
        _repeated = model._repeated;
    }

    /** Clears the board to the initial position. */
    void init() {
        _board = new Piece[SIZE][SIZE];
        _turn = BLACK;
        _limit = 0; //fix
        _winner = null;
        _moveCount = 0; //fix
        _repeated = false; //fix

        for (Square s : INITIAL_DEFENDERS) {
            put(WHITE, s);
        }
        for (Square s : INITIAL_ATTACKERS) {
            put(BLACK, s);
        }
        for (int i = 0; i < SIZE; i ++) {
            for (int j = 0; j < SIZE; j ++) {
                if (_board[j][i] == null) {
                    put(EMPTY, sq(i, j));
                }
            }
        }
        put(KING, sq(THRONE.row(), THRONE.col()));
        //take a snapshot here in your hashset
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int LIM) {
        if (2*LIM <= moveCount()) {
            throw new IllegalArgumentException("Limit input is incorrect");
        }
        _limit = LIM;
        // fixme
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {
        //ask about this

        // FIXME
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        return null; //_board[THRONE.row()]; // _board[THRONE.row()][THRONE.col()]; // FIXME
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        if (col < 0 || row > 9) {
            throw new ExceptionInInitializerError("Invalid input"); //fixme - what
            //should go in the exception name
        }
        return _board[row][col]; // fixme
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _board[s.row()][s.col()] = p; //fixme
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
       //how to record - record the move or the changing?
        //is this really necessary to implement?
        put(p, s);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        if (from.isRookMove(to)) {
            if (from.row() == to.row() && to.col() > from.col()) {
                for (int i = from.col(); i < to.col(); i++) {
                    if (_board[to.row()][i] != EMPTY) {
                        return false;
                    }
                }
                return true;
            }
        } else if (from.row() == to.row() && to.col() < from.col()) {
            for (int i = to.col(); i < from.col(); i++) {
                if (_board[to.row()][i] != EMPTY) {
                    return false;
                }
            }
            return true;
        } else if (from.col() == to.col() && to.row() > from.row()) {
            for (int i = from.row(); i < to.row(); i++) {
                if (_board[i][to.col()] != EMPTY) {
                    return false;
                }
            }
            return true;
        } else if (from.col() == to.col() && to.row() < from.row()) {
            for (int i = to.row(); i < from.row(); i++) {
                if (_board[i][to.col()] != EMPTY) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
            //for loop between each sqaure in between from and to and see if it's empty
            //iterate through all the squares in between from and to
            //make sure their pieces are empty
           // if (piece != empty) {
            //    break;
            //}
            //is there a chance they'll give you the same piece, as in like no moving
            //?
        // FIXME


    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
            //limit count is over
        if (_moveCount <= 0) {
            return false;
        } else if (isUnblockedMove(from, to) && to != THRONE) {
            return true;
            //if you're going to a throne, you need to check if you're starting as a king going to a throne's spot
        } else if (isUnblockedMove(from, to) && to == THRONE) {
            return get(from) == KING;
            //if it's blocked, return false
        } else return isUnblockedMove(from, to);

//        All pieces move like chess rooks: any number of
//        squares orthogonally (horizontally or vertically).
//        Pieces may not jump over each other or land on top
//        of another piece. No piece other than the king may
//        land on the throne, although any piece may pass through
//        it when it is empty.
        //check if there are no more moves too - move count limit

    }


    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        if (_moveCount <= _limit) {
            put(get(from), to);
            put(EMPTY, from);
            _previous.push(mv(from, to)); //stack of moves
            _gameStates.add(encodedBoard()); //hashSet of gameStates
            //decrement the move count here too
            _moveCount = _moveCount - 1;
            if (_turn == BLACK) {
                _turn = WHITE;
            } else {
                _turn = BLACK;
            }
        }
        //make from empty
        // FIXME

        //add to your hashset of game states
        //check to see if the previous board is the same as this one according to
        //spec change the winner
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        // FIXME
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            // FIXME
        }
        //removing the game state from the hashset
        //changing the instance variable
        //get rid of the last move from the stack
        //how to control moving a space back -
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        //check all the things a board records and check what you need to change
        //there are a lot of things it stores each time that you need to change
        //remove the record of the current position unless
        // FIXME
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        //what is a board position account
        //clear stack
        _previous.clear();
        //clear board position counts
        _moveCount = 0;
        //get rid of gamestates too?
        //fixme
        //make sure current position and win status stay the same
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        Square compared = null;
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board.length; j++) {
                if (_board[i][j] == side) {
                    compared = sq(i, j);
                }
            }
        }

        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board.length; j++)  {
                if (isLegal(compared, sq(i, j))) {
                    //isLegal takes in two squares
                    _moves.add(mv(compared, sq(i, j)));
                }
            }
        }
        return _moves;  // fixme
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return _moves.size() > 0; // fixme
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        return null; // FIXME
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;
    /**Limit*/
    private int _limit;
    /**Board initialization.*/
    private Piece[][] _board;
    /**Current positions.*/
    private Square _currentPosition;
    /**Stack that represents the past moves.*/
    private Stack<Move> _previous;
    /**List of valid moves.*/
    private List<Move> _moves;
    /**HashSet of GameStates.*/
    private HashSet<String> _gameStates;
    // FIXME: Other state?

}
