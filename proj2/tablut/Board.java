package tablut;

import java.util.*;

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
        _limit = Integer.MAX_VALUE; //fix
        _winner = null;
        _moveCount = _limit; //fix
        _repeated = false; //fix
        _undoMoves = new Stack<Move>();
        _gameStates = new HashSet<String>();
        _undoPieces = new Stack<ArrayList<Piece>>();
        _undoSquares = new Stack<ArrayList<Square>>();


        for (Square s : INITIAL_DEFENDERS) {
            put(WHITE, s);
        }
        for (Square s : INITIAL_ATTACKERS) {
            put(BLACK, s);
        }
        for (int i = 0; i < SIZE; i ++) {
            for (int j = 0; j < SIZE; j ++) {
                if (get(i, j) == null) {
                    put(EMPTY, sq(i, j));
                }
            }
        }
        put(KING, sq(THRONE.col(), THRONE.row()));
        //take a snapshot here in your hashset
        _gameStates.add(encodedBoard());
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
        if (_gameStates.contains(encodedBoard())) {
            _repeated = true;
            if (_turn == BLACK) {
                _winner = WHITE;
            } else {
                _winner = BLACK;
            }
            //FIXME: how do you end the game here
        }
        // fixme
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        int _row = THRONE.row();
        int _col = THRONE.col();
        return sq(_col, _row); //_board[THRONE.row()]; // _board[THRONE.row()][THRONE.col()]; // fixme
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
        return _board[col][row];
                //_board[row][col]; // fixme - CHANGED
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _board[s.col()][s.row()] = p; //fixme
    }
    //_board[s.row()][s.col()] = p;

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
       //how to record - record the move or the changing?
        //is this really necessary to implement?
        //_previous.push(mv(get(p), s));
        //save the record and then call put
        //don't really need this
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
            if (get(to) != EMPTY) {
                return false;
            } else if (from.row() == to.row() && to.col() > from.col()) {
                for (int i = from.col() + 1; i < to.col(); i++) {
                    if (_board[i][to.row()] != EMPTY) { //FIXME - SWITCHED
                        return false;
                    }
                }
                return true;
            } else if (from.row() == to.row() && to.col() < from.col()) {
                for (int i = to.col() + 1; i < from.col(); i++) {
                    if (_board[i][to.row()] != EMPTY) { //FIXME - SWITCHED
                        return false;
                    }
                }
                return true;
            } else if (from.col() == to.col() && to.row() > from.row()) {
                for (int i = from.row() + 1; i < to.row(); i++) {
                    if (_board[to.col()][i] != EMPTY) { //FIXME- SWITCHED
                        return false;
                    }
                }
                return true;
            } else if (from.col() == to.col() && to.row() < from.row()) {
                for (int i = to.row() + 1; i < from.row(); i++) {
                    if (_board[to.col()][i] != EMPTY) { //FIXME- SWITCHEd
                        return false;
                    }
                }
                return true;
            }
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
       if (_turn != get(from) && get(from) != KING) {
           return false;
       } else if (isUnblockedMove(from, to) && to == THRONE && get(from) == KING) {
            return _turn == WHITE;
        } else if (isUnblockedMove(from, to) && to != THRONE) {
            return true;
            //if you're going to a throne, you need to check if you're starting as a king going to a throne's spot
            //if it's blocked, return false
        } else return isUnblockedMove(from, to);
//
//        if (_winner != null) {
//            return false;
//        } else if (get(from) != KING && to == THRONE) {
//            return false;
//        } else {
//            return isLegal(from) && _board[to.row()][to.col()] == EMPTY && isUnblockedMove(from, to);
//        }

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
        //assert that it is a legal move
        assert isLegal(from, to);
        //check the movecount before you make a move (check if this is
        //already in isLegal
        if (_moveCount <= _limit) {
            //move the from and to using put, from becomes empty after you move it
            if (get(from) == KING && to.isEdge()) {
                _winner = WHITE;
            }
            put(get(from), to);
            put(EMPTY, from);
            //set these arraylists to new empty array lists so you can add them to the stacks
            ArrayList<Piece> _capturedPieces = new ArrayList<Piece>();
            ArrayList<Square> _capturedSquares = new ArrayList<Square>();
            for (int i = 0; i < 4; i++) {
                Square s0 = to;
                Square s2 = to.rookMove(i, 2);
                Square between;
                if (s2 == null) {
                    between = null;
                } else {
                    between = s0.between(s2);
                }
                if (between == null) {
                    _capturedSquares.add(null);
                    _capturedPieces.add(null);
                } else if (get(s0) == get(s2) && get(between) == EMPTY || get(s0) != get(s2)) {
                    _capturedSquares.add(null);
                    _capturedPieces.add(null);
                } else if (get(s0) == get(s2) && get(between) == KING && between != THRONE) {
                    //FIXME throne case
                    //THRONE is the square the piece KING is in
                    //Square between = s0.between(s2);
                    capture(s0, s2);
                    _capturedSquares.add(between);
                    _capturedPieces.add(get(between));
                } else if (get(s0) == get(s2) && get(between) == KING && between == THRONE) {
                    boolean check = true;
                    for (int j = 0; j < 4; j++) {
                        Square sA = between;
                        Square sC = to.rookMove(i, 2);
                        if (get(sA) != get(sC)) {
                            check = false;
                        }
                    }
                    if (check) {
                        _capturedSquares.add(THRONE);
                        _capturedPieces.add(KING);
                        _winner = BLACK;
                    } else {
                        _capturedSquares.add(null);
                        _capturedPieces.add(null);
                    }
                }

 //                   Captures result only as a result of enemy moves;
//                    a piece may move so as to land between two enemy
//                    pieces without being captured. A single move can
//                    capture up to three pieces.

//                      //this is taken care of in the if statements,
                        //capture is not checked from when something moves into a captured position,
                        //only checked when it is the s0 and s2 moving
//
//                    The king is captured like other pieces except when
//                    he is on the throne square or on one of the four
//                    squares orthogonally adjacent to the throne. In
//                    that case, the king is captured only when surrounded
//                    on all four sides by hostile squares (of which
//                    the empty throne may be one).
            }
            //adding the arraylists to the stacks you've made
            _undoSquares.add(_capturedSquares);
            _undoPieces.add(_capturedPieces);
            //also check the captured around it before you check repeated
            //check repeated before you add it to the stack - maybe add to the stack in
            //check repeated so you don't add it to the stack if it is repeated
            checkRepeated();
            //figure out how to end the game if the check repeated is called and goes through
            //maybe do some sort of if statement to see if the winner changed - how do you end
            //the game basically FIXME QUESTION ABOUT ENDING THE GAME
            //add to moves stack
            _undoMoves.push(mv(from, to)); //stack of moves
            //add to gameStates hashset
            _gameStates.add(encodedBoard()); //hashSet of gameStates
            //decrement the move count here too
            _moveCount = _moveCount - 1;
            if (_turn == BLACK) {
                _turn = WHITE;
            } else {
                _turn = BLACK;
            }
        }
        // FIXME

        //check capture for all of its neighbors - check neighbors in the capture

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
    void capture(Square sq0, Square sq2) {
        Square between = sq0.between(sq2);
        if (get(sq0) == get(sq2)) {
            _board[between.col()][between.row()] = get(sq0);
        }

       // if (sq0.isRookMove(sq2) && null) {

        //capture - store the piece that was capture and it's location
            //switch it when you do undo
            //store it as a square and the piece seperately
            //create a class that
            //always pop from both stacks
            //capture might capture multiple or just one
            //you can't capture diagonally
            //stack of array list to keep track of how many and which got captured
            //iterate through this list and switch them back
            //add something to your capture stack whether there's something or nothing
            //iterate through each square and add each piece to it
            //keep three stacks - one piece, one square, one moves

            //throne case end up being a bunch of if cases
            //in order to capture something, it needs to be surrounded by the same color
            //this means it should be wrapped by a black and black or a white and white
            // FIXME
       // }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            //how do you revert to the previous board
            //use move.from move.to to switch back
            //capture - store the piece that was capture and it's location
                //
            // FIXME
        }
        //if you're in the first board, don't do anything - movecount checks this
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
        //if repeated, make sure you don't get rid of the most recent encoded board, but if
        //it is not repeated, you can get rid of it, at the end change repeated = false
        //remove(encodedboard)
        if (!_repeated) {
            _gameStates.remove(encodedBoard());
        }
        // FIXME
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        //what is a board position account
        //clear stack
        _undoMoves.clear();
        //clear board position counts
        //_moveCount = 0; FIXME don't do this
        //get rid of gamestates too?
        //FIXME get rid of all the gamestates - board positions
        //fixme
        //make sure current position and win status stay the same
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        Square compared = null;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (_board[i][j] == side) {
                    compared = sq(i, j);
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)  {
                if (isLegal(compared, sq(i, j))) {
                    //isLegal takes in two squares
                    _legalMoves.add(mv(compared, sq(i, j)));
                }
            }
        }
        return _legalMoves;  // fixme
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return _legalMoves.size() > 0; // fixme
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
    private Stack<Move> _undoMoves;
    /**Stack that represents undo Pieces.*/
    private Stack<ArrayList<Piece>> _undoPieces;
    /**Stack that represents undo Squares*/
    private Stack<ArrayList<Square>> _undoSquares;
    /**List of valid moves.*/
    private List<Move> _legalMoves;
    /**HashSet of GameStates.*/
    private HashSet<String> _gameStates;
    // FIXME: Other state?

}
