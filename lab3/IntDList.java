/**
 * Scheme-like pairs that can be used to form a list of integers.
 * @author P. N. Hilfinger or unknown TA
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int s = 0;
        DNode example = _front;
        while (example != null) {
            example = example._next;
            s ++;
        }
        return s;   // Your code here
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size
     *          for positive indices and -size <= i < 0 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        DNode current;
        if (i >= 0) {
            current = _front;
            while (i > 0) {
                current = current._next;
                i--;
            }
        } else {
            current = _back;
            while (i < -1) {
                current = current._prev;
                i++;
            }
        }
        return current._val;   // Your code here
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        DNode example = _front;
        _front = new DNode(null, d, example);
        example._prev = _front;
        // Your code here
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if (_front == null && _back == null) {
            _front = new DNode(null, d, null);
            _back = _front;
        } else {
            DNode temporary = _back; //need to keep making new dnodes because you need to keep track of
            //what is already there
            _back = new DNode(temporary, d, null);
            temporary._next = _back;
            // Your code here
        }
    }
    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        DNode temporary = _back;
        if (_front == _back) {
            _front = _back = null;
        } else {
            _back = temporary._prev; //use temporary, not the actual back because this messes it up
            //you need to keep the back
           // Your code here
        }
        return temporary._val;
    }
    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        String result = "["; //start with this, declare it as a string, keep adding to it later
        DNode temporary = _front; //initialize tmemporary to be _front, then edit temporary
        while (temporary != null) {
            if (temporary._next == null) {
                result += temporary._val; //add the value of temporary's front now, 
            } else {
                result = result + temporary._val + ", ";
            }
            temporary = temporary._next;
        }
        return result + "]";   // Your code here
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    protected static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
