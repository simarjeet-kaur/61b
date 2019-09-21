package signpost;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;

import static signpost.Place.*;
import static signpost.Utils.*;

/** The state of a Signpost puzzle.  Each cell has coordinates (x, y),
 *  where 0 <= x < width(),  0 <= y < height().  The upper-left corner of
 *  the puzzle has coordinates (0, height() - 1), and the lower-right corner
 *  is at (width() - 1, 0).
 *
 *  A constructor initializes the squares according to a particular
 *  solution.  A solution is an assignment of sequence numbers from 1 to
 *  size() == width() * height() to square positions so that squares with
 *  adjacent numbers are separated by queen moves. A queen move is a move from
 *  one square to another horizontally, vertically, or diagonally. The effect
 *  is to give each square whose number in the solution is less than
 *  size() an <i>arrow direction</i>, 1 <= d <= 8, indicating the direction
 *  of the next higher numbered square in the solution: d * 45 degrees clockwise
 *  from straight up (i.e., toward higher y coordinates).  The highest-numbered
 *  square has direction 0.  Certain squares can have their values fixed to
 *  those in the solution. Initially, the only two squares with fixed values
 *  are those with the lowest and highest sequence numbers in the solution.
 *
 *  At any given time after initialization, a square whose value is not fixed
 *  may have an unknown value, represented as 0, or a tentative number (not
 *  necessarily that of the solution) between 1 and size(). Squares may be
 *  connected together, indicating that their sequence numbers (unknown or not)
 *  are consecutive.
 *
 *  When square S0 is connected to S1, we say that S1 is the <i>successor</i> of
 *  S0, and S0 is the <i>predecessor</i> of S1.  Sequences of connected squares
 *  with unknown (0) values form a <i>group</i>, identified by a unique
 *  <i>group number</i>.  Numbered cells (whether linked or not) are in group 0.
 *  Unnumbered, unlinked cells are in group -1.
 *
 *  Squares are represented as objects of the inner class Sq (Model.Sq).  A
 *  Model object is itself iterable, yielding its squares in unspecified order.
 *
 *  The puzzle is solved when all cells are contained in a single sequence
 *  of consecutively numbered cells (therefore all in group 0) and all cells
 *  with fixed sequence numbers appear at the corresponding position
 *  in that sequence.
 *
 *  @author
 */
class Model implements Iterable<Model.Sq> {

    /** A Model whose solution is SOLUTION, initialized to its starting,
     *  unsolved state (where only cells with fixed numbers currently
     *  have sequence numbers and no unnumbered cells are connected).
     *  SOLUTION must be a proper solution:
     *      1. It must have dimensions w x h such that w * h >= 2.
     *      2. There must be a sequence of chess-queen moves such that
     *         the sequence of values in the cells reached is 1, 2, ... w * h.
     *  The contents of SOLUTION are copied into this Model, so that subsequent
     *  changes to it have no effect on the Model.
     */
    Model(int[][] solution) {
        /** int [][] takes in a matrix   */
        if (solution.length == 0 || solution.length * solution[0].length < 2) {
            throw badArgs("must have at least 2 squares");
        }
        _width = solution.length;
        _height = solution[0].length;
        /** setting width and length values */
        int last = _width * _height;
        /** last is the last value int you should be getting
         * which you get by multiplyng the width by the lenght*/
        BitSet allNums = new BitSet();
        /** BitSet is a set that grows as needed, each
         * element has a boolean value, indexed by nonnegative numbers
         * by default are all False
         * so allNums is a BitSet of all False boolean values*/
        _allSuccessors = Place.successorCells(_width, _height);
        /** getting the sucessor cells*/
        _solution = new int[_width][_height];
        /** solution is the new integer */
        deepCopy(solution, _solution);

        // DUMMY SETUP
        // FIXME: Remove everything down "// END DUMMY SETUP".
       // _board = new Sq[][]{
//                {new Sq(0, 0, 0, false, 2, -1), new Sq(0, 1, 0, false, 2, -1),
//                        new Sq(0, 2, 0, false, 4, -1), new Sq(0, 3, 1, true, 2, 0)},
//                {new Sq(1, 0, 0, false, 2, -1), new Sq(1, 1, 0, false, 2, -1),
//                        new Sq(1, 2, 0, false, 6, -1), new Sq(1, 3, 0, false, 2, -1)},
//                {new Sq(2, 0, 0, false, 6, -1), new Sq(2, 1, 0, false, 2, -1),
//                        new Sq(2, 2, 0, false, 6, -1), new Sq(2, 3, 0, false, 2, -1)},
//                {new Sq(3, 0, 16, true, 0, 0), new Sq(3, 1, 0, false, 5, -1),
//                        new Sq(3, 2, 0, false, 6, -1), new Sq(3, 3, 0, false, 4, -1)}
//        };
//        for (Sq[] col : _board) {
//            for (Sq sq : col) {
//                _allSquares.add(sq);
//            }
//        }
        /** need to make a for loop in a for loop
         * sq[][] takes in two arrays, but putting in one list here*/
        // END DUMMY SETUP


        //make for loop with general m and n and use those to
        //make a general board

    //}

        //void init(int width, int height) {
            //if (not all numbers from 1-last appear) {
            //    throw badArgs("must have at least 2 squares");
           // }
        // FIXME: Initialize _board so that _board[x][y] contains the Sq object
        //        representing the contents at cell (x, y), _allSquares
        //        contains the list of all Sq objects on the board, and
        //        _solnNumToPlace[k] contains the Place in _solution that
        //        contains sequence number k.  Check that all numbers from
        //        1 - last appear; else throw IllegalArgumentException (see
        //        badArgs utility).

        _solnNumToPlace = new Place[_width * _height + 1]; //start at 1 not at 0
         for (int i = 0; i < _width; i ++) {
             for (int j = 0; j < _height; j++) {
                     _solnNumToPlace[solution[i][j]] = pl(i, j);
                 }
          }
//
//        _solnNumToPlace = new Place[last + 1]; //want to start at 1, not 0
//        for (int i = 0; i < _width; i ++) {
//            for (int j = 0; j < _height; j ++) {
//                //for (int k = 0; k < last + 1; k ++) {
//                _solnNumToPlace[solution[i][j]] = _board[i][j].pl;
//            }
//        }


        //Sq(int x0, int y0, int sequenceNum, boolean fixed, int dir, int group) {
    _board = new Sq[_width][_height]; //make an initial board that is a square with width and height
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++)
              //  for (int k = 1; k < _width * _height; k ++) - don't need this because this will be
                //the solnnumtoplace list
                    if (solution[i][j] == last) {
                        _board[i][j] = new Sq(i, j, last, true, 0, 0);
                    }
                    else if (solution[i][j] == 1) {
                        _board[i][j] = new Sq(i, j, 1, true, pl(i, j).dirOf(_solnNumToPlace[solution[i][j] + 1]), 0);
                        //something wrong with dirOf
                        //what is x0 and x1
                        //solnNumToPlace = 1
                    }
                    else {
                        _board[i][j] = new Sq(i, j, 0, false, pl(i, j).dirOf(_solnNumToPlace[solution[i][j] + 1]), -1);
                    }

        }

        //_allSquares = new IntList _board;
        //how to do allSquares

        //group is whether or not it's connected - for the first and last it will be 0 because
        // they are solved, for the rest it'll be -1
        //boolean is fixed true for first and last, for the rest it is false because they aren't fixed yet
        _allSquares = new ArrayList();
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++) {
                _allSquares.add(_board[i][j]);
            }
        }


        //_allSquares = a list of all the values in the board - use deepcopy maybe

        // FIXME: For each Sq object on the board, set its _successors and
        //        _predecessor lists to the lists of locations of all cells
        //        that it might connect to (i.e., all cells that are a queen
        //        move away in the direction of its arrow, and of all cells
        //        that might connect to it.
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++) {
                _board[i][j]._successors = _allSuccessors[i][j][arrowDirection(i, j)];
                //update arrowdirection
                //dirOf is the direction from one place to another
                for (int i1 = 0; i1 < _width; i1 ++) {
                    for (int j1 = 0; j1 < _height; j1 ++) {
                        _board[i1][j1]._predecessors = new PlaceList();
                        if (_board[i][j]._successors.contains(pl(i1, j1))) {
                            _board[i1][j1]._predecessors.add(pl(i, j));
                        }
                    }
                }
                //

                /** should this be the same as the predecessors? */
                //you know all the successors for the sq are queen moves from the suare
                //write something that given some location gives you all the queen moves from the
                //square
                //what gos in the x1 and y1 prts of dirof
                //if pl(i, j)
                //_board[i][j]._predecessor = _board[i][j].predecessor();
                /** list of all locations of cells that
                 it might connect to (something that is a queen move away in the direction of its arrow
                 and all the cells that might connect to it*/
                //might error later, go back and check

            }
        }
        _unconnected = last - 1;
        //tells you how many are unconnected - once it equal
        //don't use this now, but use it later
        //every time something is unconnected, you subtract one
    }

    //for this, grab a place and look through all the i, j successors and see if this place is in any successor squares
    //if it is, add this i, j to the predecessor list for place

    /** Initializes a copy of MODEL. */
    Model(Model model) {
        _width = model.width(); _height = model.height();
        //modifying instance variables
        _unconnected = model._unconnected;
        _solnNumToPlace = model._solnNumToPlace;
        _solution = model._solution;
        _usedGroups.addAll(model._usedGroups);
        _allSuccessors = model._allSuccessors;

        //trying to get rid of a lot of the extra information in this model
        //model.board is a pointer - can't make a copy of it, you want a copy, not a pointer
        //do a foor loop through the previous board - for sq in model.board make a new square
        //transfr previous variables to this new sq
        //you want the board to consist of the sq - copy everything except the sq and selectively choose which
        //variables to copy from one to another
        //model.board/sq.x = previous.model.board.x
        //go through every single sq (for loop) in the
        //Model takes in a Model and model is old model
        //what do you return? - save new model you create by
        //setting attributes of your model to the model you're passing in
        //assign instance variable to the new body you make
        //use Sq(sq other) - it changes the square you have to make it how you want it without all the extra stuff
        //to find predecessors look at all the other cells successors and if the cell is in the successors
        //then it is a predecessor for that cell and add it to the list

        // FIXME: Initialize _board and _allSquares to contain copies of the
        //        the Sq objects in MODEL other than their _successor,
        //        _predecessor, and _head fields (which can't necessarily be
        //        set until all the necessary Sq objects are first created.)
        _allSquares = new ArrayList(); //do we need to reinitialize allSquares
        _board = new Sq[_width][_height]; //2D array of width and height with Sq inside
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++) {
                _board[i][j] = new Sq(model._board[i][j]);
                _allSquares.add(_board[i][j]); //new Sq(model._board[i][j])
            }
        }

       // for (int i = 0; i < _width; i ++) {
       //     for (int j = 0; j < _height; j ++) {
         //       _board[i][j] = model[i][j];
        //                _allSquares = model;
       //     }
      //  }

        _solnNumToPlace = new Place[_width * _height + 1]; //start at 1 not at 0
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++)
                _solnNumToPlace[_solution[i][j]] = pl(i, j);
        }

        // FIXME: Fill in the _successor, _predecessor, and _head fields of the
        //        copied Sq objects.
        //_successor is only one - it'll be whatever is already set
        //this is what is happening in the middle of the board
        //if there is no successor, it's null
        //otherwise you're looking at what was already

        //is this only for the solution

        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j++) {

                if (model._board[i][j]._predecessor != null) {
                    _board[i][j]._predecessor = get((model._board[i][j].predecessor().pl.x), (model._board[i][j].predecessor().pl.y));
                    //_board[i][j]._successor = get((model._board[i][j].successor().pl.x), (model._board[i][j].successor().pl.y));
                } else {
                    _board[i][j]._predecessor = null;
                   // _board[i][j]._successor = get((model._board[i][j].successor().pl.x), (model._board[i][j].successor().pl.y));
                   // _board[i][j]._head = get((model._board[i][j].head().pl.x), (model._board[i][j].head().pl.y));
                }
                if (model._board[i][j]._successor != null) {
                   // _board[i][j]._predecessor = get((model._board[i][j].predecessor().pl.x), (model._board[i][j].predecessor().pl.y));
                    _board[i][j]._successor = get((model._board[i][j].successor().pl.x), (model._board[i][j].successor().pl.y));
                   // _board[i][j]._head = get((model._board[i][j].head().pl.x), (model._board[i][j].head().pl.y));
                }

                else {
                    //_board[i][j]._predecessor = get((model._board[i][j].predecessor().pl.x), (model._board[i][j].predecessor().pl.y));
                    _board[i][j]._successor = null;
                }
                _board[i][j]._head = get((model._board[i][j].head().pl.x), (model._board[i][j].head().pl.y));
                //get is working on this.board so you don't need to work on anything else
                //it gives you the square of the coords you're looking for

            }
        }


//        for (int i = 0; i < _width; i ++) {
//     //       for (int j = 0; j < _height; j ++) {
//                _board[i][j]._successor = _board[i][j]._successors();
//                _board[i][j]._predecessor = _board[i][j]._predecessor();
//                _allSquares = model[i][j];
//            }
//        }
//
//
    }

    /** Returns the width (number of columns of cells) of the board. */
    final int width() {
        return _width;
    }

    /** Returns the height (number of rows of cells) of the board. */
    /** final means you can't modify the variable later=*/
    final int height() {
        return _height;
    }

    /** Returns the number of cells (and thus, the sequence number of the
     *  final cell). */
    final int size() {
        return _width * _height;
    }

    /** Returns true iff (X, Y) is a valid cell location. */
    final boolean isCell(int x, int y) {
        return 0 <= x && x < width() && 0 <= y && y < height();
    }

    /** Returns true iff P is a valid cell location. */
    final boolean isCell(Place p) {
        return isCell(p.x, p.y);
    }

    /** Returns all cell locations that are a queen move from (X, Y)
     *  in direction DIR, or all queen moves in any direction if DIR = 0. */
    final PlaceList allSuccessors(int x, int y, int dir) {
        return _allSuccessors[x][y][dir];
    }

    /** Returns all cell locations that are a queen move from P in direction
     *  DIR, or all queen moves in any direction if DIR = 0. */
    final PlaceList allSuccessors(Place p, int dir) {
        return _allSuccessors[p.x][p.y][dir];
    }

    /** Initialize MODEL to an empty WIDTH x HEIGHT board with a null solution.
     */
    void init(int width, int height) {
        if (width <= 0 || width * height < 2) {
            throw badArgs("must have at least 2 squares");
        }
        _width = width; _height = height;
        _unconnected = _width * _height - 1;
        _solution = null;
        _usedGroups.clear();
        // FIXME: Initialize _board to contain nulls and clear all objects from
        //        _allSquares.
        _board = new Sq[_width][_height];
//        for (int i = 0; i < _width; i ++) {
//            for (int j = 0; j < _height; j ++) {
//                _board[i][j] = null;
//            }
//        }
        _allSquares.clear();

        // FIXME: Initialize _allSuccessors so that _allSuccessors[x][y][dir]
        //        is a list of all the Places on the board that are a queen
        //        move in direction DIR from (x, y) and _allSuccessors[x][y][0]
        //        is a list of all Places that are one queen move from in
        //        direction from (x, y).

        //use this with

        _allSuccessors = Place.successorCells(_width, _height);

    }

    /** Remove all connections and non-fixed sequence numbers. */
    void restart() {
        for (Sq sq : this) {
            sq.disconnect();
        }
        assert _unconnected == _width * _height - 1;
    }

    /** Return the number array that solves the current puzzle (the argument
     *  the constructor.  The result must not be subsequently modified.  */
    final int[][] solution() {
        return _solution;
    }

    /** Return the position of the cell with sequence number N in my
     *  solution. */
    Place solnNumToPlace(int n) {
        return _solnNumToPlace[n];
    }

    /** Return the current number of unconnected cells. */
    final int unconnected() {
        return _unconnected;
    }

    /** Returns true iff the puzzle is solved. */
    final boolean solved() {
        return _unconnected == 0;
    }

    /** Return the cell at (X, Y). */
    final Sq get(int x, int y) {
        return _board[x][y];
    }

    /** Return the cell at P. */
    final Sq get(Place p) {
        return p == null ? null : _board[p.x][p.y];
    }

    /** Return the cell at the same position as SQ (generally from another
     *  board), or null if SQ is null. */
    final Sq get(Sq sq) {
        return sq == null ? null : _board[sq.x][sq.y];
    }

    /** Connect all numbered cells with successive numbers that as yet are
     *  unconnected and are separated by a queen move.  Returns true iff
     *  any changes were made. */
    boolean autoconnect() {
        return false; // FIXME
    }

    /** Sets the numbers in my squares to the solution from which I was
     *  last initialized by the constructor. */
    void solve() {
       //set each sequence num to the solution
        //solving the board
        for (int i = 0; i < _width; i ++) {
            for (int j = 0; j < _height; j ++) {
                _board[i][j]._sequenceNum = _solution[i][j];
            }
        }
        //initialize the board with all the solution numbers

        _unconnected = 0;
    }

    /** Return the direction from cell (X, Y) in the solution to its
     *  successor, or 0 if it has none. */
    private int arrowDirection(int x, int y) {
        int seq0 = _solution[x][y]; //seq0 is the number this is in the sequence
        //if you add one to this, you will get it's successor
        // FIXME
        /** need an if else statement here
         * return direction using code from above*/
        if (_board[x][y]._successor != null) {
            return dirOf(x, y, _board[x][y]._successor.x, _board[x][y]._successor.y);
        }
        return 0;
    }

    /** Return a new, currently unused group number > 0.  Selects the
     *  lowest not currently in used. */
    private int newGroup() {
        for (int i = 1; true; i += 1) {
            if (_usedGroups.add(i)) {
                return i;
            }
        }
    }

    /** Indicate that group number GROUP is no longer in use. */
    private void releaseGroup(int group) {
        _usedGroups.remove(group);
    }

    /** Combine the groups G1 and G2, returning the resulting group. Assumes
     *  G1 != 0 != G2 and G1 != G2. */
    private int joinGroups(int g1, int g2) {
        assert (g1 != 0 && g2 != 0);
        if (g1 == -1 && g2 == -1) {
            return newGroup();
        } else if (g1 == -1) {
            return g2;
        } else if (g2 == -1) {
            return g1;
        } else if (g1 < g2) {
            releaseGroup(g2);
            return g1;
        } else {
            releaseGroup(g1);
            return g2;
        }
    }

    @Override
    public Iterator<Sq> iterator() {
        return _allSquares.iterator();
    }

    @Override
    public String toString() {
        String hline;
        hline = "+";
        for (int x = 0; x < _width; x += 1) {
            hline += "------+";
        }

        Formatter out = new Formatter();
        for (int y = _height - 1; y >= 0; y -= 1) {
            out.format("%s%n", hline);
            out.format("|");
            for (int x = 0; x < _width; x += 1) {
                Sq sq = get(x, y);
                if (sq.hasFixedNum()) {
                    out.format("+%-5s|", sq.seqText());
                } else {
                    out.format("%-6s|", sq.seqText());
                }
            }
            out.format("%n|");
            for (int x = 0; x < _width; x += 1) {
                Sq sq = get(x, y);
                if (sq.predecessor() == null && sq.sequenceNum() != 1) {
                    out.format(".");
                } else {
                    out.format(" ");
                }
                if (sq.successor() == null
                    && sq.sequenceNum() != size()) {
                    out.format("o ");
                } else {
                    out.format("  ");
                }
                out.format("%s |", ARROWS[sq.direction()]);
            }
            out.format("%n");
        }
        out.format(hline);
        return out.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Model model = (Model) obj;
        return (_unconnected == model._unconnected
                && _width == model._width && _height == model._height
                && Arrays.deepEquals(_solution, model._solution)
                && Arrays.deepEquals(_board, model._board));
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_solution) * Arrays.deepHashCode(_board);
    }

    /** Represents a square on the board. */
    final class Sq {
        /** A square at (X0, Y0) with arrow in direction DIR (0 if not
         *  set), group number GROUP, sequence number SEQUENCENUM (0
         *  if none initially assigned), which is fixed iff FIXED. */
        Sq(int x0, int y0, int sequenceNum, boolean fixed, int dir, int group) {
            x = x0; y = y0;
            pl = pl(x, y);
            _hasFixedNum = fixed;
            _sequenceNum = sequenceNum;
            _dir = dir;
            _head = this; //what does this mean
            _group = group;
        }

        /** A copy of OTHER, excluding head, successor, and predecessor. */
        Sq(Sq other) {
            this(other.x, other.y, other._sequenceNum, other._hasFixedNum,
                 other._dir, other._group);
            _successor = _predecessor = null;
            _head = this;
            _successors = other._successors;
            _predecessors = other._predecessors;
        }

        /** Return my current sequence number, or 0 if none assigned. */
        int sequenceNum() {
            return _sequenceNum;
        }

        /** Fix my current sequence number at N>0.  It is an error if my number
         *  is not initially 0 or N. */
        void setFixedNum(int n) {
            if (n == 0 || (_sequenceNum != 0 && _sequenceNum != n)) {
                throw badArgs("sequence number may not be fixed");
            }
            _hasFixedNum = true;
            if (_sequenceNum == n) {
                return;
            } else {
                releaseGroup(_head._group);
            }
            _sequenceNum = n;
            for (Sq sq = this; sq._successor != null; sq = sq._successor) {
                sq._successor._sequenceNum = sq._sequenceNum + 1;
            }
            for (Sq sq = this; sq._predecessor != null; sq = sq._predecessor) {
                sq._predecessor._sequenceNum = sq._sequenceNum - 1;
            }
        }

        /** Unfix my sequence number if it is currently fixed; otherwise do
         *  nothing. */
        void unfixNum() {
            Sq next = _successor, pred = _predecessor;
            _hasFixedNum = false;
            disconnect();
            if (pred != null) {
                pred.disconnect();
            }
            _sequenceNum = 0;
            if (next != null) {
                connect(next);
            }
            if (pred != null) {
                pred.connect(this);
            }
        }

        /** Return true iff my sequence number is fixed. */
        boolean hasFixedNum() {
            return _hasFixedNum;
        }

        /** Returns direction of my arrow (0 if no arrow). */
        int direction() {
            return _dir;
        }

        /** Return my current predecessor. */
        Sq predecessor() {
            return _predecessor;
        }

        /** Return my current successor. */
        Sq successor() {
            return _successor;
        }

        /** Return the head of the connected sequence I am currently in. */
        Sq head() {
            return _head;
        }

        /** Return the group number of my group.  It is 0 if I am numbered, and
         *  -1 if I am alone in my group. */
        int group() {
            if (_sequenceNum != 0) {
                return 0;
            } else {
                return _head._group;
            }
        }

        /** Size of alphabet. */
        static final int ALPHA_SIZE = 26;

        /** Return a textual representation of my sequence number or
         *  group/position. */
        String seqText() {
            if (_sequenceNum != 0) {
                return String.format("%d", _sequenceNum);
            }
            int g = group() - 1;
            if (g < 0) {
                return "";
            }

            String groupName =
                String.format("%s%s",
                              g < ALPHA_SIZE ? ""
                              : Character.toString((char) (g / ALPHA_SIZE
                                                           + 'a')),
                              Character.toString((char) (g % ALPHA_SIZE
                                                         + 'a')));
            if (this == _head) {
                return groupName;
            }
            int n;
            n = 0;
            for (Sq p = this; p != _head; p = p._predecessor) {
                n += 1;
            }
            return String.format("%s%+d", groupName, n);
        }

        /** Return locations of my potential successors. */
        PlaceList successors() {
            return _successors;
        }

        /** Return locations of my potential predecessors. */
        PlaceList predecessors() {
            return _predecessors;
        }

        /** Returns true iff I may be connected to cell S1, that is:
         *  + S1 is in the correct direction from me. √
         *  + S1 does not have a current predecessor, and I do not have a
         *    current successor. √
         *  + If S1 and I both have sequence numbers, then mine is
         *    sequenceNum() == S1.sequenceNum() - 1. √
         *  + If neither S1 nor I have sequence numbers, then we are not part
         *    of the same connected sequence.
         */
        //is using this the right idea?
        //
        boolean connectable(Sq s1) {
            /** needs to return T/F based on whether or not it is connection to S1*/
            if (this._successors.contains(s1) && //check this direction thing
                    s1._predecessor == null &&
                    this._successor == null) { //finish this
                if (this.sequenceNum() > 0 &&
                        s1.sequenceNum() > 0) {
                    if (this.sequenceNum() == s1.sequenceNum() - 1) {
                        return true;
                }
                    if (this.sequenceNum() == 0 && s1.sequenceNum() == 0) {
                        return true;
                    }
            }

            } else {
                return false;
            }
    }

        /** Connect me to S1, if we are connectable; (chang the pred and succ)
         *  otherwise do nothing.
         *  Returns true iff we were connectable.  Assumes S1 is in the proper
         *  arrow direction from me. */
        boolean connect(Sq s1) {
            if (!connectable(s1)) {
                return false;
            }
            int sgroup = s1.group();
            _unconnected -= 1;
            this._group = sgroup;

            //        + Set my _successor field and S1's _predecessor field.

            this._successor = s1;
            s1._predecessor = this;
            //Sq sqs1 = s1;
            Sq sqthis = this;

            //        + If I have a number, number all my successors
            //          accordingly (if needed).

            if (this._sequenceNum != 0) {
                while (sqthis != null) {
                    sqthis._successor._sequenceNum = this._sequenceNum + 1;
                    sqthis = sqthis._successor;
                }
            }

            //        + If S1 is numbered, number me and my predecessors
            //          accordingly (if needed).

            if (s1._sequenceNum != 0) {
                while (sqthis != null) { // FIXME look into this - Jackson
                    sqthis._predecessor._sequenceNum = sqthis._sequenceNum - 1;
                    sqthis = sqthis._predecessor;
                }
            }

            // + Set the _head fields of my successors to my _head.

            while (sqthis != null) {
                sqthis._successor._head = this._head;
                sqthis = sqthis._successor;
            }

            //        + If either of this or S1 used to be unnumbered and is
            //          now numbered, release its group of whichever was
            //          unnumbered, so that it can be reused.

            if (s1.sequenceNum() != 0) {
                releaseGroup(s1._group);
                releaseGroup(this._group);
            }

            //        + If both this and S1 are unnumbered, set the group of
            //          my head to the result of joining the two groups.

            if (this._sequenceNum == 0 && s1._sequenceNum == 0) {
                this._head._group = joinGroups(this._group, s1._group);
            }

            // FIXME: Connect me to my successor:
            //        + Set my _successor field and S1's _predecessor field. √
            //        + If I have a number, number all my successors
            //          accordingly (if needed). √
            //        + If S1 is numbered, number me and my predecessors
            //          accordingly (if needed). √
            //        + Set the _head fields of my successors to my _head. √
            //        + If either of this or S1 used to be unnumbered and is
            //          now numbered, release its group of whichever was
            //          unnumbered, so that it can be reused. √
            //        + If both this and S1 are unnumbered, set the group of
            //          my head to the result of joining the two groups. √

            return true;
        }

        /** Disconnect me from my current successor, if any. */
        void disconnect() {
            Sq next = _successor;
            Sq sqthis = this;
            if (next == null) {
                return;
            }
            _unconnected += 1;
            next._predecessor = _successor = null;
            if (this.predecessor() == null && this.successor() == null && next.predecessor() == null && next.successor() == null) {
                releaseGroup(this._group);
                releaseGroup(next._group);
                this._group = -1;
                next._group = -1;
            }
            if (this.predecessor() == null && this.successor() == null &&  next.predecessor() != null && next.successor() != null)
            {
                this._group = -1;
            }
            if (next.predecessor() == null && next.successor() == null &&  this.predecessor() != null && this.successor() != null) {
                next._group = -1;
            }
                // FIXME: If both this and next are now one-element groups,
                //        release their former group and set both group
                //        numbers to -1. √
                //        Otherwise, if either is now a one-element group, set
                //        its group number to -1 without releasing the group
                //        number. √
                //        Otherwise, the group has been split into two multi-
                //        element groups.  Create a new group for next. √
             else { //should this just be else? or another if?
                next._group = newGroup();
            }
                // FIXME: If neither this nor any square in its group that
                //        precedes it has a fixed sequence number, set all
                //        their sequence numbers to 0 and create a new group
                //        for them if this has a current predecessor (other
                //        set group to -1).
            int counter = 0;
            while (!sqthis.hasFixedNum()) {
                sqthis = sqthis._predecessor; //how to check
                counter ++;
                }

            if (counter == # of preds) {
                for (int i = 0; i < counter; i++) {
                    sqthis._sequenceNum = 0;
                }
            }

                // FIXME: If neither next nor any square in its group that
                //        follows it has a fixed sequence number, set all
                //        their sequence numbers to 0 and create a new
                //        group for them if next has a current successor
                //        (otherwise set next's group to -1.)
            // FIXME: Set the _head of next and all squares in its group to
            //        next.
        }

        @Override
        public boolean equals(Object obj) {
            Sq sq = (Sq) obj;
            return sq != null
                && pl == sq.pl
                && _hasFixedNum == sq._hasFixedNum
                && _sequenceNum == sq._sequenceNum
                && _dir == sq._dir
                && (_predecessor == null) == (sq._predecessor == null)
                && (_predecessor == null
                    || _predecessor.pl == sq._predecessor.pl)
                && (_successor == null || _successor.pl == sq._successor.pl);
        }

        @Override
        public int hashCode() {
            return (x + 1) * (y + 1) * (_dir + 1)
                * (_hasFixedNum ? 3 : 1) * (_sequenceNum + 1);
        }

        /** The coordinates of this square in the board. */
        protected final int x, y;
        /** My coordinates as a Place. */
        protected final Place pl;
        /** The first in the currently connected sequence of cells ("group")
         *  that includes this one. */
        private Sq _head;
        /** If _head == this, then the group number of the group of which this
         *  is a member.  Numbered sequences have a group number of 0,
         *  regardless of the value of _group. Unnumbered one-member groups
         *  have a group number of -1.   */
        private int _group;
        /** True iff assigned a fixed sequence number. */
        private boolean _hasFixedNum;
        /** The current imputed or fixed sequenxce number,
         *  numbering from 1, or 0 if there currently is none. */
        private int _sequenceNum;
        /** The arrow direction. The possible values are 0 (for unset),
         *  1 for northeast, 2 for east, 3 for southeast, 4 for south,
         *  5 for southwest, 6 for west, 7 for northwest, and 8 for north. */
        private int _dir;
        /** The current predecessor of this square, or null if there is
         *  currently no predecessor. */
        private Sq _predecessor;
        /** The current successor of this square, or null if there is
         *  currently no successor. */
        private Sq _successor;
        /** Locations of my possible predecessors. */
        private PlaceList _predecessors;
        /** Locations of my possible successor. */
        private PlaceList _successors;
    }

    /** ASCII denotations of arrows, indexed by direction. */
    private static final String[] ARROWS = {
        " *", "NE", "E ", "SE", "S ", "SW", "W ", "NW", "N "
    };

    /** Number of squares that haven't been connected. */
    private int _unconnected;
    /** Dimensions of board. */
    private int _width, _height;
    /** Contents of board, indexed by position. */
    private Sq[][] _board;
    /** Contents of board as a sequence of squares for convenient iteration. */
    private ArrayList<Sq> _allSquares = new ArrayList<>();
    /** _allSuccessors[x][y][dir] is a sequence of all queen moves possible
     *  on the board of in direction dir from (x, y).  If dir == 0,
     *  this is all places that are a queen move from (x, y) in any
     *  direction. */
    private PlaceList[][][] _allSuccessors;
    /** The solution from which this Model was built. */
    private int[][] _solution;
    /** Inverse mapping from sequence numbers to board positions. */
    private Place[] _solnNumToPlace;
    /** The set of positive group numbers currently in use. */
    private HashSet<Integer> _usedGroups = new HashSet<>();

}
