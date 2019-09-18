package signpost;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/** An (X, Y) position on a Signpost puzzle board.  We require that
 *  X, Y >= 0.  Each Place object is unique; no other has the same x and y
 *  values.  As a result, "==" may be used for comparisons.
 *  @author
 */
class Place {

    /** Convenience list-of-Place class.  (Defining this allows one to create
     *  arrays of lists without compiler warnings.) */
    static class PlaceList extends ArrayList<Place> {
        /** Initialize empty PlaceList. */
        PlaceList() {
        }

        /** Initialze PlaceList from a copy of INIT. */
        PlaceList(List<Place> init) {
            super(init);
        }
    }

    /** The position (X0, Y0), where X0, Y0 >= 0. */
    private Place(int x0, int y0) {
        x = x0; y = y0;
    }

    /** Return the position (X, Y).  This is a factory method that
     *  creates a new Place only if needed by caching those that are
     *  created. */
    static Place pl(int x, int y) {
        assert x >= 0 && y >= 0;
        int s = max(x, y);
        if (s >= _places.length) {
            Place[][] newPlaces = new Place[s + 1][s + 1];
            for (int i = 0; i < _places.length; i += 1) {
                System.arraycopy(_places[i], 0, newPlaces[i], 0,
                                 _places.length);
            }
            _places = newPlaces;
        }
        if (_places[x][y] == null) {
            _places[x][y] = new Place(x, y);
        }
        /** if you're at the end of the row, move on*/
        return _places[x][y];
        /** returns the next place*/
    }

    /** Returns the direction from (X0, Y0) to (X1, Y1), if we are a queen
     *  move apart.  If not, returns 0. The direction returned (if not 0)
     *  will be an integer 1 <= dir <= 8 corresponding to the definitions
     *  in Model.java */
    static int dirOf(int x0, int y0, int x1, int y1) {
        int dx = x1 < x0 ? -1 : x0 == x1 ? 0 : 1;
        int dy = y1 < y0 ? -1 : y0 == y1 ? 0 : 1;
        if (dx == 0 && dy == 0) {
            return 0;
        }
        if (dx != 0 && dy != 0 && Math.abs(x0 - x1) != Math.abs(y0 - y1)) {
            return 0;
        }

        return dx > 0 ? 2 - dy : dx == 0 ? 6 + 2 * dy : 6 + dy;
    }

    /** dirOF Returns the direction from me to PLACE, if we are a queen
     *  move apart.  If not, returns 0. */
    int dirOf(Place place) {
        return dirOf(x, y, place.x, place.y);
    }
    //what does me mean

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  x1 - x.  */
    static int dx(int dir) {
        return DX[dir];
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  y1 - y.  */
    static int dy(int dir) {
        return DY[dir];
    }

    /** Return an array, M, such that M[x][y][dir] is a list of Places that are
     *  one queen move away from square (x, y) in direction dir on a
     *  WIDTH x HEIGHT board.  Additionally, M[x][y][0] is a list of all Places
     *  that are a queen move away from (x, y) in any direction (the union of
     *  the lists of queen moves in directions 1-8). */
    static PlaceList[][][] successorCells(int width, int height) {
        PlaceList[][][] M = new PlaceList[width][height][9];
        int lim = Math.max(width, height);
        // FIXME
        //always start with a double for loop because you'll be going through the whole board
        for (int i = 0; i < width; i ++){
            for (int j = 0; j < height; j ++) {
                M[i][j][0] = new PlaceList(); //make a new placelist with dir 0 where it is  alist of all places
                //in any direction, that is why dirOf is 0 here in the third []
                for (int dir = 1; dir <= 8; dir ++) {
                    //keep adding one to dir - check all the directions for that one place i, j
                    M[i][j][dir] = new PlaceList();
                    //now M[i][j][in this dir] is a new placelist that we can add to depending on if the new place
                    //is a successor or not in this specific direction
                    //we are making a list of all the successors for each i, j in each direction
                    int a = i + dx(dir); //dx takes in a direction and returns the sq in that direction
                    int b = j + dy(dir); //same thing but in y
                    //need to account for if this is still in the board or not, so we can add it depending on
                    //how it relates to the width and height of the board
                    //starting a and b at the immediate sq coming after, use a while loop to keep going until
                    //you reach a point outside of the board
                    while (a < width && b < height && a > 0 && b > 0) {
                        M[i][j][dir].add(pl(a, b));
                        M[i][j][0].add(pl(a, b)); // should have all values anyway
                        //need to increment b and a somehow - do this by changing them to be the next successor cell
                        //essentially now you are finding the successor in that specific direction for the
                        //successor cell
                        a += dx(dir);
                        b += dy(dir);
                    }

                }
            }
        }
        return M;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Place)) {
            return false;
        }
        Place other = (Place) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return (x << 16) + y;
    }
    /** puts it in string format */
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public static void main(String[] args) {
        PlaceList[][][] solution = Place.successorCells(3, 3);
        System.out.println();
    }

    /** X displacement of adjacent squares, indexed by direction. */
    static final int[] DX = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };

    /** Y displacement of adjacent squares, indexed by direction. */
    static final int[] DY = { 0, 1, 0, -1, -1, -1, 0, 1, 1 };

    /** Coordinates of this Place. */
    protected final int x, y;

    /** Places already generated. */
    private static Place[][] _places = new Place[10][10];


}
