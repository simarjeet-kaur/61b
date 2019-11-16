package tablut;

import java.util.Formatter;
import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author Simarjeet Kaur
 */
class Board {

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
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board.length; j++) {
                _board[i][j] = model._board[i][j];
            }
        }
        _turn = model._turn;
        _winner = model._winner;
        _moveCount = model._moveCount;
        _repeated = model._repeated;
        _undoPieces = (Stack<ArrayList<Piece>>) model._undoPieces.clone();
        _undoSquares = (Stack<ArrayList<Square>>) model._undoSquares.clone();
        _undoMoves = (Stack<Move>) model._undoMoves.clone();
        _gameStates = (HashSet<String>) model._gameStates.clone();
    }

    /** Clears the board to the initial position. */
    void init() {
        _board = new Piece[SIZE][SIZE];
        _turn = BLACK;
        _limit = Integer.MAX_VALUE;
        _winner = null;
        _moveCount = 0;
        _repeated = false;
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
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (get(i, j) == null) {
                    put(EMPTY, sq(i, j));
                }
            }
        }
        put(KING, sq(THRONE.col(), THRONE.row()));
        _gameStates.add(encodedBoard());
    }

    /** Set the move limit to n.  It is an error if 2*n <= moveCount().
     * @param n is the move limit*/
    void setMoveLimit(int n) {
        if (2 * n <= moveCount()) {
            throw new IllegalArgumentException("Limit input is incorrect");
        }
        _limit = n;
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
            _winner = _turn.opponent();
        }
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        int row = THRONE.row();
        int col = THRONE.col();
        return sq(col, row);
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        if (col < 0 || row > 9) {
            throw new ExceptionInInitializerError("Invalid input");
        }
        return _board[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _board[s.col()][s.row()] = p;
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
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
                    if (_board[i][to.row()] != EMPTY) {
                        return false;
                    }
                }
                return true;
            } else if (from.row() == to.row() && to.col() < from.col()) {
                for (int i = to.col() + 1; i < from.col(); i++) {
                    if (_board[i][to.row()] != EMPTY) {
                        return false;
                    }
                }
                return true;
            } else if (from.col() == to.col() && to.row() > from.row()) {
                for (int i = from.row() + 1; i < to.row(); i++) {
                    if (_board[to.col()][i] != EMPTY) {
                        return false;
                    }
                }
                return true;
            } else if (from.col() == to.col() && to.row() < from.row()) {
                for (int i = to.row() + 1; i < from.row(); i++) {
                    if (_board[to.col()][i] != EMPTY) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (_turn != get(from) && get(from) != KING) {
            return false;
        } else if (isUnblockedMove(from, to) && to == THRONE
                && get(from) == KING) {
            return _turn == WHITE;
        } else if (isUnblockedMove(from, to) && to != THRONE) {
            return true;
        } else {
            return isUnblockedMove(from, to);
        }
    }


    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /**Checks if a square is surrounded by hostile squares
     * according to the square it is trying to capture.
     * @param middle refers to the square it's taking in
     * @param compare refers to the square it's comparing to
     * @return is a boolean*/
    boolean isHostile(Square middle, Square compare) {
        if (middle == null || compare == null) {
            return false;
        } else if ((get(middle) == WHITE || get(middle) == KING)
                && get(compare) == BLACK) {
            return true;
        } else if ((get(middle) == BLACK && get(compare) == WHITE)
                || (get(middle) == BLACK && get(compare) == KING)) {
            return true;
        } else if (compare == THRONE && get(compare) == EMPTY) {
            return true;

        } else if (compare == THRONE && get(compare) == WHITE) {
            Square north = compare.rookMove(0, 1);
            Square east = compare.rookMove(1, 1);
            Square south = compare.rookMove(2, 1);
            Square west = compare.rookMove(3, 1);
            int check = 0;
            for (int i = 0; i < 4; i++) {
                if (get(compare.rookMove(i, 1)) == BLACK) {
                    check++;
                }
            }
            return check == 3;
        } else if (compare == null) {
            return false;
        }
        return false;
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        if (_moveCount <= _limit) {
            if (get(from) == KING && to.isEdge()) {
                _winner = WHITE;
            }
            _undoMoves.push(mv(from, to));
            put(get(from), to);
            put(EMPTY, from);
            ArrayList<Piece> capturedPiecesList = new ArrayList<Piece>();
            ArrayList<Square> capturedSquaresList = new ArrayList<Square>();
            Square toNorth = to.rookMove(0, 2);
            Square toEast = to.rookMove(1, 2);
            Square toSouth = to.rookMove(2, 2);
            Square toWest = to.rookMove(3, 2);
            if (toNorth != null) {
                Square capturedNorth = to.between(toNorth);
                Square north = capturedNorth.rookMove(0, 1);
                Square east = capturedNorth.rookMove(1, 1);
                Square south = capturedNorth.rookMove(2, 1);
                Square west = capturedNorth.rookMove(3, 1);
                if (get(capturedNorth) == KING && (capturedNorth == THRONE
                        || capturedNorth == NTHRONE || capturedNorth == STHRONE
                        || capturedNorth == WTHRONE
                        || capturedNorth == ETHRONE)) {
                    if (isHostile(capturedNorth, north)
                            && isHostile(capturedNorth, south)
                            && isHostile(capturedNorth, east)
                            && isHostile(capturedNorth, west)) {
                        capturedSquaresList.add(capturedNorth);
                        capturedPiecesList.add(get(capturedNorth));
                        capture(south, north);
                        _winner = BLACK;
                    }
                } else {
                    if (isHostile(capturedNorth, north)
                            && isHostile(capturedNorth, south)) {
                        capturedSquaresList.add(capturedNorth);
                        capturedPiecesList.add(get(capturedNorth));
                        capture(south, north);
                    } else if (isHostile(capturedNorth, east)
                            && isHostile(capturedNorth, west)) {
                        capturedSquaresList.add(capturedNorth);
                        capturedPiecesList.add(get(capturedNorth));
                        capture(east, west);
                    }
                }
            }
            if (toEast != null) {
                Square capturedEast = to.between(toEast);
                Square north = capturedEast.rookMove(0, 1);
                Square east = capturedEast.rookMove(1, 1);
                Square south = capturedEast.rookMove(2, 1);
                Square west = capturedEast.rookMove(3, 1);
                if (get(capturedEast) == KING && (capturedEast == THRONE
                        || capturedEast == NTHRONE
                        || capturedEast == STHRONE
                        || capturedEast == WTHRONE
                        || capturedEast == ETHRONE)) {
                    if (isHostile(capturedEast, north)
                            && isHostile(capturedEast, south)
                            && isHostile(capturedEast, east)
                            && isHostile(capturedEast, west)) {
                        capturedSquaresList.add(capturedEast);
                        capturedPiecesList.add(get(capturedEast));
                        capture(south, north);
                        _winner = BLACK;
                    }
                } else {
                    if (isHostile(capturedEast, north)
                            && isHostile(capturedEast, south)) {
                        capturedSquaresList.add(capturedEast);
                        capturedPiecesList.add(get(capturedEast));
                        capture(south, north);
                    } else if (isHostile(capturedEast, east)
                            && isHostile(capturedEast, west)) {
                        capturedSquaresList.add(capturedEast);
                        capturedPiecesList.add(get(capturedEast));
                        capture(east, west);
                    }
                }
            }
            if (toSouth != null) {
                Square capturedSouth = to.between(toSouth);
                Square north = capturedSouth.rookMove(0, 1);
                Square east = capturedSouth.rookMove(1, 1);
                Square south = capturedSouth.rookMove(2, 1);
                Square west = capturedSouth.rookMove(3, 1);
                if (get(capturedSouth) == KING && (capturedSouth == THRONE
                        || capturedSouth == NTHRONE
                        || capturedSouth == STHRONE
                        || capturedSouth == WTHRONE
                        || capturedSouth == ETHRONE)) {
                    if (isHostile(capturedSouth, north)
                            && isHostile(capturedSouth, south)
                            && isHostile(capturedSouth, east)
                            && isHostile(capturedSouth, west)) {
                        capturedSquaresList.add(capturedSouth);
                        capturedPiecesList.add(get(capturedSouth));
                        capture(south, north);
                        _winner = BLACK;
                    }
                } else {
                    if (isHostile(capturedSouth, north)
                            && isHostile(capturedSouth, south)) {
                        capturedSquaresList.add(capturedSouth);
                        capturedPiecesList.add(get(capturedSouth));
                        capture(south, north);
                    } else if (isHostile(capturedSouth, east)
                            && isHostile(capturedSouth, west)) {
                        capturedSquaresList.add(capturedSouth);
                        capturedPiecesList.add(get(capturedSouth));
                        capture(east, west);
                    }
                }
            }
            if (toWest != null) {
                Square capturedWest = to.between(toWest);
                Square north = capturedWest.rookMove(0, 1);
                Square east = capturedWest.rookMove(1, 1);
                Square south = capturedWest.rookMove(2, 1);
                Square west = capturedWest.rookMove(3, 1);
                if (get(capturedWest) == KING && (capturedWest == THRONE
                        || capturedWest == NTHRONE
                        || capturedWest == STHRONE
                        || capturedWest == WTHRONE
                        || capturedWest == ETHRONE)) {
                    if (isHostile(capturedWest, north)
                            && isHostile(capturedWest, south)
                            && isHostile(capturedWest, east)
                            && isHostile(capturedWest, west)) {
                        capturedSquaresList.add(capturedWest);
                        capturedPiecesList.add(get(capturedWest));
                        capture(south, north);
                        _winner = BLACK;
                    }
                } else {
                    if (isHostile(capturedWest, north)
                            && isHostile(capturedWest, south)) {
                        capturedSquaresList.add(capturedWest);
                        capturedPiecesList.add(get(capturedWest));
                        capture(south, north);
                    } else if (isHostile(capturedWest, east)
                            && isHostile(capturedWest, west)) {
                        capturedSquaresList.add(capturedWest);
                        capturedPiecesList.add(get(capturedWest));
                        capture(east, west);
                    }
                }
            }
            _undoSquares.add(capturedSquaresList);
            _undoPieces.add(capturedPiecesList);
            checkRepeated();
            _gameStates.add(encodedBoard());
            _moveCount = _moveCount + 1;
            if (_turn == BLACK) {
                _turn = WHITE;
            } else {
                _turn = BLACK;
            }
            boolean kingCheck = false;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (_board[i][j] == KING) {
                        kingCheck = true;
                        break;
                    }
                }
            }
            if (!kingCheck) {
                _winner = BLACK;
            }
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    void capture(Square sq0, Square sq2) {
        Square between = sq0.between(sq2);
        _board[between.col()][between.row()] = Piece.EMPTY;
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            Move undone = _undoMoves.pop();
            Square squareFrom = undone.from();
            Square squareTo = undone.to();
            Piece pieceFrom = get(undone.from());
            Piece pieceTo = get(undone.to());
            put(pieceFrom, sq(squareTo.col(), squareTo.row()));
            put(pieceTo, sq(squareFrom.col(), squareFrom.row()));
            ArrayList<Piece> capturedPieces = _undoPieces.pop();
            ArrayList<Square> capturedSquares = _undoSquares.pop();
            for (int i = 0; i < capturedPieces.size(); i++) {
                if (capturedPieces.get(i) != EMPTY
                        && capturedSquares.get(i) != null) {
                    Piece piece = capturedPieces.get(i);
                    Square square = capturedSquares.get(i);
                    put(piece, square);
                }
            }
            if (_winner != null) {
                _winner = null;
            }
            _moveCount--;
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        if (!_repeated) {
            _gameStates.remove(encodedBoard());
        }
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        _undoMoves.clear();
        _gameStates.clear();
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        HashSet<Square> squares = pieceLocations(side);
        for (Square s : squares) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isLegal(s, sq(i, j))) {
                        legalMoves.add(mv(s, sq(i, j)));
                    }
                }
            }
        }
        return legalMoves;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return legalMoves(side).size() > 0;
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
    HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        HashSet<Square> pieceLocs = new HashSet<>();
        if (side == BLACK) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (_board[i][j] == side) {
                        pieceLocs.add(sq(i, j));
                    }
                }
            }
        } else {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (_board[i][j] == WHITE || _board[i][j] == KING) {
                        pieceLocs.add(sq(i, j));
                    }
                }
            }
        }
        return pieceLocs;
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

    /**Returns the _board instance.*/
    Piece[][] retBoard() {
        return _board;
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or null if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;
    /**Limit.*/
    private int _limit;
    /**Board initialization.*/
    private Piece[][] _board;
    /**Current positions.*/
    private Square _currentPosition;
    /**Stack that represents the past moves.*/
    private Stack<Move> _undoMoves;
    /**Stack that represents undo Pieces.*/
    private Stack<ArrayList<Piece>> _undoPieces;
    /**Stack that represents undo Squares.*/
    private Stack<ArrayList<Square>> _undoSquares;
    /**List of valid moves.*/
    private ArrayList<Move> _legalMoves;
    /**HashSet of GameStates.*/
    private HashSet<String> _gameStates;
    /** Getting the board size.
     * @param board is the board
     * @return is the int size*/
    int getSize(Board board) {
        return SIZE;
    }

}
