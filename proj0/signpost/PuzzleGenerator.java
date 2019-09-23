package signpost;

import java.util.Collections;
import java.util.Random;

import signpost.Model.Sq;
import static signpost.Place.PlaceList;
import static signpost.Utils.*;

/** A creator of random Signpost puzzles.
 *  @author
 */
class PuzzleGenerator implements PuzzleSource {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    @Override
    public Model getPuzzle(int width, int height, boolean allowFreeEnds) {
        Model model =
            new Model(makePuzzleSolution(width, height, allowFreeEnds));
        // FIXME: Remove the "//" on the following two lines.
        makeSolutionUnique(model);
        model.autoconnect();
        return model;
    }

    /** Return an array representing a WIDTH x HEIGHT Signpost puzzle.
     *  The first array index indicates x-coordinates (column numbers) on
     *  the board, and the second index represents y-coordinates (row numbers).
     *  Its values will be the sequence numbers (1 to WIDTH x HEIGHT)
     *  appearing in a sequence queen moves on the resulting board.
     *  Unless ALLOWFREEENDS, the first and last sequence numbers will
     *  appear in the upper-left and lower-right corners, respectively. */
    private int[][] makePuzzleSolution(int width, int height,
                                       boolean allowFreeEnds) {
        _vals = new int[width][height];
        _successorCells = Place.successorCells(width, height);
        int last = width * height;
        int x0, y0, x1, y1;
        if (allowFreeEnds) {
            int r0 = _random.nextInt(last),
                r1 = (r0 + 1 + _random.nextInt(last - 1)) % (last);
            x0 = r0 / height; y0 = r0 % height;
            x1 = r1 / height; y1 = r1 % height;
        } else {
            x0 = 0; y0 = height - 1;
            x1 = width - 1; y1 = 0;
        }
        _vals[x0][y0] = 1;
        _vals[x1][y1] = last;
        // FIXME: Remove the following return statement and uncomment the
        //        next three lines.
//        return new int[][] {
//            { 14, 9, 8, 1 },
//            { 15, 10, 7, 2 },
//            { 13, 11, 6, 3 },
//            { 16, 12, 5, 4 }
//        };
        boolean ok = findSolutionPathFrom(x0, y0);
        assert ok;
        return _vals;
    }

    /** Try to find a random path of queen moves through VALS from (X0, Y0)
     *  to the cell with number LAST.  Assumes that
     *    + The dimensions of VALS conforms to those of MODEL;
     *    + There are cells (separated by queen moves) numbered from 1 up to
     *      and including the number in (X0, Y0);
     *    + There is a cell numbered LAST;
     *    + All other cells in VALS contain 0.
     *  Does not change the contents of any non-zero cell in VALS.
     *  Returns true and leaves the path that is found in VALS.  Otherwise
     *  returns false and leaves VALS unchanged. Does not change MODEL. */
    private boolean findSolutionPathFrom(int x0, int y0) {
        int w = _vals.length, h = _vals[0].length;
        int v;
        int start = _vals[x0][y0] + 1;
        PlaceList moves = _successorCells[x0][y0][0];
        Collections.shuffle(moves, _random);
        for (Place p : moves) {
            v = _vals[p.x][p.y];
            if (v == 0) {
                _vals[p.x][p.y] = start;
                if (findSolutionPathFrom(p.x, p.y)) {
                    return true;
                }
                _vals[p.x][p.y] = 0;
            } else if (v == start && start == w * h) {
                return true;
            }
        }
        return false;
    }

    /** Extend unambiguous paths in MODEL (add all connections where there is
     *  a single possible successor or predecessor). Return 2 if any change
     *  was made, 1 if no change was made, 0 if unconnectable
     *  square encountered. */
    private int extendSimple(Model model) {
        int result;
        result = 1;
        while (true) {
            int cf = makeForwardConnections(model);
            if (cf == 0) {
                return 0;
            }
            int cb = makeBackwardConnections(model);
            if (cb == 0) {
                return 0;
            } else if (cb == 1 && cf == 1) {
                return result;
            }
            result = 2;
        }
    }

    /** Make all unique forward connections in MODEL (those in which there is
     *  a single possible successor).  Return 2 if changes made, 1 if no
     *  changes made, 0 if a non-final square with no possible connections
     *  encountered. */
    private int makeForwardConnections(Model model) {
        int w = model.width(), h = model.height();
        int result;
        result = 1;
        for (Sq sq : model) {
            Sq found;
            found = null;
            int nFound;
            nFound = 0;
            if (sq.successor() == null && sq.direction() != 0) {
                // FIXME: Set nFound to the number of squares in the
                //        direction sq.direction() from sq that can
                //        be connected to it and set found to one of those
                //        squares.  If sq is numbered and can be connected to
                //        a numbered square, then set nFound to 1 and found
                //        to that numbered square.

                PlaceList successors = sq.successors();

                for (int i = 0; i < successors.size(); i ++) {
                    Place successor = successors.get(i);
                    if (sq.connectable(model.get(successor.x, successor.y))) {
                        nFound += 1;
                    }
                    found = model.get(successor.x, successor.y);
                }


                if (sq.sequenceNum() != 0) {
                    for (Place place : successors) {
                        if (model.get(place.x, place.y).sequenceNum() != 0) {
                            if (sq.connectable(model.get(place.x, place.y))) {
                                nFound = 1;
                                found = model.get(place.x, place.y);
                            }
                        }
                    }
                }

                //if you have a square and want to make forward connections, you need to look
                //at successors - you can't make connections to what it doesn't have a possibility to connect to
                //get those by doing ._successors - this is specific to a cell, so you need to use this, not allsuccessors
                //to make forward connections,
                //if we know what square and the directin, we set nfound to the number of squares that it can be connected to in
                //the right direction
                //how do we make sure nfound is only being set to the number of things in the right way? use connectable
                //nfound should be the number of squares in the direction sq.direction
                //we are either counting it or not counting based on if it is connectable or not
                //if you are allowed to connect, add it - you get your total from that
                //also check sequence number -
                //highest it can be is _successors - nfound can only increment every time we have a successor, but that's
                //not what we do because sometimes it's not connectable
                //if we only have one we can conect
                //go through successors, if you can connect, increase nfound
                //doesn't matter which it's connected to, can just be whichever it's connectable to (that's all the statement is saying)
                //now that we know the number of squares it can be connected to, you just connect it to one, doesn't matter whcih one
                //setting found to any one of these squares
                //FIXME: If sq is numbered and can be connected to
                // a numbered square, then set nFound to 1 and found
                // to that numbered square.


               // if (sq.sequenceNum() != 0 && sq.successor() == null); {
                 //   nFound = 1;
                   // found =
               // }

                //next sentence
                //say you have some sort of board -
                //only connectable if the sequence num is sequential
                //check if they are connectable and the sequence num, if sequence num is +1, then don't
                //keep looking
                //nfound should be 1 because there is one best option
                //before you have three options, now you only have one
                //if they both have sequence numbers, then
                //fixed means its for sure set (1, 16) are definitely set
                //sequence num might come from picking the number after 1 to a block, that is now set to 2
                //as soon as you connect something, it gets a sequence num, but you can always change it, it is
                //unfixed
                //when does it become fixed? -
                //think about autoconnect and disconnect methods too, but don't need to use it
                //just need to change nfound and found and just check the sequencenums and if it's connectable
                //get throughout successors to see if it's connectable
                //pick last unless there is one

                if (nFound == 0) {
                    return 0;
                } else if (nFound == 1) {
                    sq.connect(found);
                    result = 2;
                }
            }
        }
        return result;
    }

    /** Make all unique backward connections in MODEL (those in which there is
     *  a single possible predecessor).  Return 2 if changes made, 1 if no
     *  changes made, 0 if a non-final square with no possible connections
     *  encountered. */
    private int makeBackwardConnections(Model model) {
        int w = model.width(), h = model.height();
        int result;
        result = 1;
        for (Sq sq : model) {
            Sq found;
            int nFound;
            found = null;
            nFound = 0;
            if (sq.predecessor() == null && sq.sequenceNum() != 1) {
                // FIXME: Set nFound to the number of squares that are
                //        possible predecessors of sq and connectable to it,
                //        and set found to one of those squares.  If sq is
                //        numbered and one of these connectable predecessors
                //        is numbered, then set nFound to 1 and found
                //        to that numbered predecessor.
                //should we use a while loop here again and test connectable on it
                //
                PlaceList predecessors = sq.predecessors();

                for (int i = 0; i < predecessors.size(); i ++) {
                    Place predecessor = predecessors.get(i);
                    if (sq.connectable(model.get(predecessor.x, predecessor.y))) {
                        nFound += 1;
                    }
                    found = model.get(predecessor.x, predecessor.y);
                }

                if (sq.sequenceNum() != 0) {
                    for (Place place : predecessors) {
                        if (model.get(place.x, place.y).sequenceNum() != 0) {
                            if (sq.connectable(model.get(place.x, place.y))) {
                                nFound = 1;
                                found = model.get(place.x, place.y);
                            }
                        }
                    }
                }


                if (nFound == 0) {
                    return 0;
                } else if (nFound == 1) {
                    found.connect(sq);
                    result = 2;
                }
            }
        }
        return result;
    }

    /** Remove all links in MODEL and unfix numbers (other than the first and
     *  last) that do not affect solvability.  Not all such numbers are
     *  necessarily removed. */
    private void trimFixed(Model model) {
        int w = model.width(), h = model.height();
        boolean changed;
        do {
            changed = false;
            for (Sq sq : model) {
                if (sq.hasFixedNum() && sq.sequenceNum() != 1
                    && sq.direction() != 0) {
                    model.restart();
                    int n = sq.sequenceNum();
                    sq.unfixNum();
                    extendSimple(model);
                    if (model.solved()) {
                        changed = true;
                    } else {
                        sq.setFixedNum(n);
                    }
                }
            }
        } while (changed);
    }

    /** Fix additional numbers in MODEL to make the solution from which
     *  it was formed unique.  Need not result in a minimal set of
     *  fixed numbers. */
    private void makeSolutionUnique(Model model) {
        model.restart();
        AddNum:
        while (true) {
            extendSimple(model);
            if (model.solved()) {
                trimFixed(model);
                model.restart();
                return;
            }
            PlaceList unnumbered = new PlaceList();
            for (Sq sq : model) {
                if (sq.sequenceNum() == 0) {
                    unnumbered.add(sq.pl);
                }
            }
            Collections.shuffle(unnumbered, _random);
            for (Place p : unnumbered) {
                Model model1 = new Model(model);
                model1.get(p).setFixedNum(model.solution()[p.x][p.y]);
                if (extendSimple(model1) == 2) {
                    model.get(p).setFixedNum(model1.get(p).sequenceNum());
                    continue AddNum;
                }
            }
            throw badArgs("no solution found");
        }
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Solution board currently being filled in by findSolutionPathFrom. */
    private int[][] _vals;
    /** Mapping of positions and directions to lists of queen moves on _vals. */
    private PlaceList[][][] _successorCells;

    /** My PNRG. */
    private Random _random;

}
