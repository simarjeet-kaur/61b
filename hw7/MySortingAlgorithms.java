import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Integer.min(k, array.length); //need to keep reassigning k because you'll call this again probably
            //make a the min between k and the length of the array - this is the number you're comparing up until
            for (int i = 0; i < k; i++) {
                //compare up until a
                for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                    //compare from each pair, see if they are ordered, if not swap
                    //this is the basis of insertion sort
                    swap(array, j, j - 1);
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Integer.min(k, array.length); //need to keep reassigning k because you'll probably use it later
            for (int i = 0; i < k; i++) {
                int min = i; //set min to be the i and you can check from j = 1 to j = k if they are comparable
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < array[min]) {
                        min = j; //as soon as the left is greater than the one min you are checking, change the j and min
                    }
                }
                swap(array, i, min); //then swap these too
                //this is essentially iterating through the whole thing to see when you need to make a swap
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {

        //first make a merge method that does all the work.  this will do the merging and comparing, but not the
        //sorting which the later methods will do
        private void merge(int[] array, int[] temp, int low, int middle, int high) {
            for (int i = low; i <= high; i++) { //go from low to high
                temp[i] = array[i]; //for each value here, take the ith value in temp and put it into the temp array
                //at the same location
            }
            int i = low; //now set low to i
            int j = middle + 1; //and j to middle + 1
            for (int k = low; k <= high ; k++) { //starting at low, up until high, continue merging
                if (i > middle) { //if you're at a spot that is past the middle
                    array[k] = temp[j++]; //set this index in the array to be the next value
                } else if (j > high) { //if j, which is the middle + 1, is greater than your highest value,
                    array[k] = temp[i++]; //set the kth value in the array to be the temp +1 of the low
                } else if (temp[j] < temp[i]) { //if temp[j] < temp[i]
                    array[k] = temp[j++]; //array[k] will become temp[j++] again
                } else {
                    array[k] = temp[i++]; //otherwise, just set it equal to the temp i ++
                }
            }
        }

        @Override
        public void sort(int[] array, int k) { //this sorting method will
            int[] aux = new int[array.length];
            int hi = Integer.min(k - 1, array.length - 1);
            sort(array, aux, 0, hi);
            //this first sort
        }

        private void sort(int[] array, int[] aux, int lo, int hi) {
            if (hi <= lo) {
                return;
            }
            int mid = lo + (hi - lo) / 2;
            sort(array, aux, lo, mid);
            sort(array, aux, mid + 1, hi);
            merge(array, aux, lo, mid, hi);
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
