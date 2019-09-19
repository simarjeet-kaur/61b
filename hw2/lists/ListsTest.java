package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {
    /** FIXME
     */

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    @Test
    public void naturalRunsTest() {
                IntList test = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);

                int[][] expectedOutput = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
                IntListList expected = IntListList.list(expectedOutput);
                IntListList result = Lists.naturalRuns(test);
                boolean isTrue = expected.equals(result);
                assertEquals(isTrue, true);
            }


    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
