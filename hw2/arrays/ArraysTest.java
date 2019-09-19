package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /**
     */
    @Test
    public void catenateTest() {
        int[] example = {1, 2};
        int[] example2 = {3, 4};
        int[] expected = {1, 2, 3, 4};
        int[] result = Arrays.catenate(example, example2);
        boolean isTrue = Utils.equals(result, expected);
        assertEquals(isTrue, true);
    }
//    ArrayList example = ArrayList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
//    IntList example2 = IntList.list(1);
//    assertEquals(Lists.naturalRuns(example), new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}});
//    assertEquals(Lists.naturalRuns(example2), example2);
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
