import java.util.LinkedList;
import java.util.Iterator;

/** A binary search tree with arbitrary Objects as keys.
 *  @author
 */
public class BST {
    /** Root of tree. */
    private BSTNode root;

    /** A BST containing the elements in the sorted list LIST. */
    public BST(LinkedList list) {
        root = linkedListToTree(list.iterator(), list.size());
    }

    /**
     * Converts the linkedList to a tree.
     */
    private BSTNode linkedListToTree(Iterator iter, int n) {
        BSTNode root = new BSTNode(); //create a new BSTnode
        if (n == 0) {
            return null; //if there is nothing n length is 0 you have an empty BST
        } else if (n == 1) {
            root.item = iter.next(); //if n == 1, this is the first real base case,
        } else {
            root.left = linkedListToTree(iter, n / 2);
            root.item = iter.next();
            root.right = linkedListToTree(iter, (n - 1) / 2);
        }
        return root;
    }

    /**
     * Prints the tree to the console.
     */
    private void print() {
        print(root, 0);
    }

    /** Print NODE and its subtrees, indented D levels.  */
    private void print(BSTNode node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    /**
     * Node for BST.
     */
    static class BSTNode {

        /** Item. */
        protected Object item;

        /** Left child. */
        protected BSTNode left;

        /** Right child. */
        protected BSTNode right;
    }
}
