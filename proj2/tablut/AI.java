package tablut;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

import static tablut.Square.sq;
import static tablut.Board.THRONE;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author
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

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    //using a minimax game tree
    //how is this implemented?
        //use findmove - this is where your minimax starts
        //static score: your heuristic: determining how good your move is
    //say you're trying to search a depth 2 - wanna analyze the nodes and see your depth
        //look at your first one, input an int called depth, your base case is checking your depth compared to the one you're given
        //you'll start at findmax, recurse into findmin, then recurse into find simplemin at the depth you want to searfch into
        //that's where you use the static
        //copy the pseudocode for this - in the game trees lecture
        //apply static score to your whole bottom layer

    //findmin - redcurses down one level and finds the max
        //findmax does not call static score, that's your bottom score

    //mutual recursion - find max and find min
        //simple find max and simple find min
        //you need to implement these but the pseudo code is there, you just need to copy it
        //

    //say you want to generate a game tree of depth 2 - you want to search fro 2 - start at find max
        //dpeth is 2, not 0, so call find min from find max and decrement the depth by 1
        //depth is now 1, stll not 0, so call
        //simple find min/max are like the base cases for the regular find max and find min

    //say ou have depth 4
        //do max, that calls a min, depth is now 3
        //do a min, that calls a max, depth is now 2
        //do a min, depth is now 1 (stop at 1 or 0, 0 calculate the score, or at 1 calculae the min at that point)
        //depth is now 0, NOW you call simple max that will propogate up now

    //look at the best for black, and then negate it for your white

    //could always maximize and assume youre making the best move for white,
        //or fundamentally diff scores for black and white


    //satic score
        //figure out what the best move is by simulating each move and assign values to every possible outcome
        //in a very simple case, where you're trying to capture on black, you can get an alpha and beta
        //make alpha worth how many pieces you'll capture
        //take the max alpha and use that move

    //not very expressive because if you capture = good, if you don't = bad, that's not comprehensive
        //make alpha 2 - what puts you one move from a capture - now you have a better way to analyze it
        //could be like far from hostile pieces
    //alphas are the heristics
        //you would check this by your game tree
        //don't necessarily implement this, it's just an example, tricky to implement

    //general idea: take all these heuristics, assign weights to them, and decide how important these attributes are
        //a1w1 + a2w2 + a3w3 = static score
        //at the btotom of the tree

        //one could be how likely you are to block the king too

    //for the other side
        //whatever if best for the white is worst for the black so just negate it
        //pick the lowest there
        //or have a diff weight vector called B for black scores
        // if it's a white turn base it off of one value, and if it's black turn, make another set to do that
        //make the move for white that is the best

    //don't call legal moves every time - should be relatively quick - use an iterator?
        //make it as simple as possible
        //the weighted thing is complex, but if you can boil down what it means to be a good move to one thing, go with that
        //you can still iterate through every possible move you an make - iterate over a subset of that
        //analyze the board differently

    //you just need to make sure you're getting to the winning spot
        //testing info in the spec or set up a board you know it's solvable in four moves and then make sure your
        //AI picks the right winner

    //according to the spec, you need to check up to four moves
        //your search depth affects this
        //have a lower search depth at the beginning of the game, increase your search depth as your game goes on
        //eventually go to four
        //you have to test it out
        //don't wait ten thousand moves before you increase it to four
        //depends on num moves

    //if you write a good AI, search depth 1 would be really good - you get diminishing returns by going farther down



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
        return findMove().toString(); // fixme
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        //will start your game tree - use the pseudo code and call that from findmove
        //copy functons that hilfinger wrote - should be four
            //find min find max simple finds
        //call them from find move, whichever you want to start from
        Board b = new Board(board());
        _lastFoundMove = null;
        // FIXME
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
        //keep changing alpha and beta and call it recursively until you're done
        //keep changing sense depending on what's going on
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        } else {
            if (sense == 1) {
                int bestSoFar = -INFTY;
                List<Move> whiteMoves = board.legalMoves(Piece.WHITE);
                //find max goes here
                for (Move m : whiteMoves) {
                    board.makeMove(m);
                    //what is alpha, beta, saveMove
                    //int saved = staticScore(board);
                    //response is the next level, what you see while traversing the tree
                    int response = findMove(board, depth-1, saveMove, -1, alpha, beta);
                    if (response >= bestSoFar) {
                        bestSoFar = response;
                        //board.setValue(response);
                        //response = 0; //giving the board a value //FIXME
                        alpha = max(alpha, response);
                        _lastFoundMove = m;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                    board.undo();
                }
                return 0;
            } else {
            // if (sense == -1) - find min here
                int bestSoFar = INFTY;
                List<Move> blackMoves = board.legalMoves(Piece.BLACK);
                for (Move m : blackMoves) {
                    board.makeMove(m);
                    int response = findMove(board, depth-1, saveMove, 1, alpha, beta);
                    if (response <= bestSoFar) {
                        bestSoFar = response;
                        beta = min(beta, response);
                        _lastFoundMove = m;
                    }
                }
            }
        return 0; // FIXME
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4; // fixme?
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        //like simplefindmax
        //black wins if king is capture - check to see how many blacks are on the board, if king surrounded
        //white wins if king is on an edge, see how close it is to the edge - see if it has a clear way to edge, any black
        //pieces blocking it, how less black pieces there are
        return 0;  // FIXME
    }

//    /**Finding the simpMaxMove.*/
//    Move simpleFindMax(Square square, double alpha, double beta) {
//        if (square.maxPlayerWon()) {
//            return "arificial move with value +inifinity";
//        } else if (square.minPlayerWon()) {
//            return "arificial "Move" with value -infinity"
//        }
//    }

    // FIXME: More here.

}
