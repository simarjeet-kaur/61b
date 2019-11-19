import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        int[] starting = new int[intervals.size()]; //make a int array that is the length of the list taken in
        int[] ending = new int[intervals.size()]; //make an int array that is the length of the list taken in
        int[] everything = new int[intervals.size() * 2]; //everything that is a list of them times too
        //this is done to help with sorting
        for (int i = 0; i < intervals.size(); i++) {
            starting[i] = intervals.get(i)[0];
            ending[i] = intervals.get(i)[1];
            everything[2 * i] = intervals.get(i)[0];
            everything[2 * i + 1] = intervals.get(i)[1];
        }
        MySortingAlgorithms.QuickSort sa = new MySortingAlgorithms.QuickSort();
        sa.sort(starting, intervals.size());
        sa.sort(ending, intervals.size());
        sa.sort(everything, intervals.size() * 2);
        int i, j;
        i = j = 0;
        int startIdx = 0;
        int length = 0;
        int counter = 0;
        for (int k = 0; k < everything.length; k++) {
            if (i >= intervals.size()) {
                k = everything.length;
                length += ending[intervals.size() - 1] - starting[startIdx];
                break;
            } else if (everything[k] == starting[i]) {
                i += 1;
                counter += 1;
            } else if (everything[k] == ending[j]) {
                j += 1;
                counter -= 1;
            }
            if (counter == 0) {
                length += everything[k] - starting[startIdx];
                startIdx = i;
            }
        }
        return length;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
