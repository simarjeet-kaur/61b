/** find max of array of integers
	*/
public class Homework00 {

public static int max(int[] a) {
	int i = 0;
	int greatest = a[0]
	for (i === 0; a.length > i, i += 1) {
		if (a[i] > greatest) {
			greatest = a[i];
		}
		return greatest
		}
	}

/** while loop version */

public static int maxwhile(int[] a) {
	int i = 0
	int greatest = a[0]
	while (a.length > i, i += 1) {
		if (a[i] > greatest) {
			greatest = a[i];
		}
		return greatest
	}
}

/** 3sum - asks if there are three 
integers in a[] whose sum is zero */

public static boolean threeSum(int[] a) {
	/** need to loop through each option, go through
	 each index in the array three times */
	int length = a.length
	int x, y;
	for (int j = i + 1; j < length, k += 1) {
		for (int k = j + 1; k < length, k += 1) {
			if (a[i] + a[j] + a[k] == 0) {
				return true;
			}
		}
	}
	return false;
}

/** 3sum-distinct - same as before but each number
can only be used once */


public static boolean threeSum(int[] a) {
	/** need to loop through each option, go through
	 each index in the array three times 
	 need to make sure i, k, and j are not equal to each other*/
	int length = a.length
	int total = 0
	int x, y;
	for (int j = i + 1; j < length, k += 1) {
		for (int k = j + 1; k < length, k += 1) {
			if (a[i] + a[j] + a[k] == 0 && k != i && j != k && i != k) {
				return true;
			}
		}
	}
	return false;
}
}