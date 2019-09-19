package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /**
     * Return the list of lists formed by breaking up L into "natural runs":
     * that is, maximal strictly ascending sublists, in the same order as
     * the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     * then result is the four-item list
     * ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     * Destructive: creates no new IntList items, and may modify the
     * original list pointed to by L.
     */
    static IntListList naturalRuns(IntList L) {
        IntListList temp = new IntListList(L, null); //makes a new intlistlist that starts with one element, that is L
        //because you can only edit the original list pointed to by L, use what we made above and edit that L
        //grab L by using .head
        IntList end = temp.head; //this will be a normal list instead because L is a regular list
        //you don't want to edit temp directly, so you need to pick it out again and get the pointer
        IntListList result = temp;
        while (end.tail != null) { //check here now iterating through the L that was passed in, seeing if it's empty
            //while it's not empty, add something to the temporary list (after L)
            //you need to do these steps because you NEED an intlistlist, can't make an empty intListList
            if (end.head >= end.tail.head) {
                result.tail = new IntListList(end.tail, null);
                end.tail = null; //new IntListList because there could be sub lists
                //now update end to be the rest of temp
                //edit becomes the rest of the head of end, which is the rest of L
                end = result.tail.head;
                result = result.tail;
            } else {
                end = end.tail;
            }
        }
            return temp;
        }
    }
