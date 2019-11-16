package tablut;

import java.util.List;

import static java.lang.Math.*;

import static tablut.Square.sq;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Simarjeet Kaur
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /**A weight for the static score.*/
    public static final int RANDOM_UPPER_LIMIT = 100;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (b.turn() == WHITE) {
            findMove(b, maxDepth(b), true, 1,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            findMove(b, maxDepth(b), true, -1,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int bestSoFar;
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        } else {
            if (sense == 1) {
                bestSoFar = -INFTY;
                List<Move> whiteMoves = board.legalMoves(Piece.WHITE);
                for (Move m : whiteMoves) {
                    board.makeMove(m);
                    int response = findMove(board, depth - 1, false,
                            -1, alpha, beta);
                    board.undo();
                    if (response >= bestSoFar) {
                        bestSoFar = response;
                        alpha = max(alpha, response);
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
                return bestSoFar;
            } else {
                bestSoFar = INFTY;
                List<Move> blackMoves = board.legalMoves(Piece.BLACK);
                for (Move m : blackMoves) {
                    board.makeMove(m);
                    int response = findMove(board, depth - 1, false,
                            1, alpha, beta);
                    board.undo();
                    if (response <= bestSoFar) {
                        bestSoFar = response;
                        beta = min(beta, response);
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestSoFar;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == WHITE) {
            return WINNING_VALUE;
        } else if (winner == BLACK) {
            return -WINNING_VALUE;
        }
        int counter = 0;
        int oppCounter = 0;
        int staticCounter;
        for (Move ignored: board.legalMoves(board.turn())) {
            counter++;
        }
        for (Move ignored : board.legalMoves(board.turn().opponent())) {
            oppCounter++;
        }
        staticCounter = counter - oppCounter;
        int distance = 0;
        int surrounding = 0;
        for (int i = 0; i < board.getSize(board); i++) {
            for (int j = 0; j < board.getSize(board); j++) {
                if (board.retBoard()[i][j] == KING) {
                    Piece king = board.retBoard()[i][j];
                    Square kingS = sq(i, j);
                    distance = max(i, j);

                    if (i <= j) {
                        Square edge = sq(0, j);
                        if (board.isUnblockedMove(kingS, edge)) {
                            distance = distance * RANDOM_UPPER_LIMIT;
                        }
                    } else {
                        Square edge = sq(i, 0);
                        if (board.isUnblockedMove(kingS, edge)) {
                            distance = distance * RANDOM_UPPER_LIMIT;
                        }
                    }
                    Square north = kingS.rookMove(0, 1);
                    Square east = kingS.rookMove(1, 1);
                    Square south = kingS.rookMove(2, 1);
                    Square west = kingS.rookMove(3, 1);
                    if (north != null && board.get(north) == Piece.BLACK) {
                        surrounding++;
                    } else if (east != null && board.get(east) == Piece.BLACK) {
                        surrounding++;
                    } else if (west != null && board.get(west) == Piece.BLACK) {
                        surrounding++;
                    } else if (south != null
                            && board.get(south) == Piece.BLACK) {
                        surrounding++;
                    }
                }
            }
        }
        int score = distance + staticCounter - surrounding;
        return score;
    }
}
