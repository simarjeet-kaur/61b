package enigma;

import java.util.ArrayList;
import java.util.HashMap;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Simarjeet Kaur
 */
class Permutation {

    /** Cycles. */
    private String _cycles;
    /** List of Cycles. */
    private ArrayList<String> _listOfCycles;
    /** Permuted alphabet. */
    private HashMap<Integer, Character> _permutedAlphabet;
    /** Inverted alphabet. */
    private HashMap<Integer, Character> _invertedAlphabet;
    /** Split cycles. */
    private String[] _splitCycles;


    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _cycles = cycles;
        _alphabet = alphabet;
        _listOfCycles = new ArrayList<>();
        _permutedAlphabet = new HashMap<>();
        _invertedAlphabet = new HashMap<>();
        _splitCycles = splitCycles(_cycles);
        for (int i = 0; i < _alphabet.size(); i++) {
            _permutedAlphabet.put(i, findPermute(_alphabet.toChar(i)));
            _invertedAlphabet.put(i, findInvert(_alphabet.toChar(i)));
        }
        for (String cycle : splitCycles(_cycles)) {
            addCycle(cycle);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _listOfCycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int pMod = wrap(p);
        return _alphabet.toInt(_permutedAlphabet.get(pMod));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int cMod = wrap(c);
        return _alphabet.toInt(_invertedAlphabet.get(cMod));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _permutedAlphabet.get(_alphabet.toInt(p));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _invertedAlphabet.get(_alphabet.toInt(c));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 1; i < _alphabet.size(); i++) {
            if (_alphabet.toChar(i) == _permutedAlphabet.get(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /**split cycles here.
     * @param cycles the strings passed in.
     * @return an array of strings.
     * */
    String[] splitCycles(String cycles) {
        String cycles1 = cycles.replace(")(", " ");
        String cycles2 = cycles1.replace(") (", " ");
        String cycles3 = cycles2.replace("(", "");
        String cycles4 = cycles3.replace(")", "");
        _splitCycles = cycles4.split(" ");
        return _splitCycles;
    }

    /** finding the cycle that char c is in, returns itself
     * as a string if it's not in any cycle.
     * @param c the character passed in.
     * @return the cycle it is in.
     * */
    String findCycle(char c) {
        for (int i = 0; i < splitCycles(_cycles).length; i++) {
            if (splitCycles(_cycles)[i].indexOf(c) > -1) {
                return splitCycles(_cycles)[i];
            }
        }
        return String.valueOf(c);
    }

    /** finding the right mapped letter from the permutation.
     * @param c the character passed in.
     * @return the permuted letter.
     * */
    char findPermute(char c) {
        if (findCycle(c).length() <= 1) {
            return c;
        } else {
            String correctCycle = findCycle(c);
            int index = correctCycle.indexOf(c) + 1;
            if (index < correctCycle.length()) {
                return correctCycle.charAt(correctCycle.indexOf(c) + 1);
            } else {
                return correctCycle.charAt(0);
            }
        }
    }

    /**find the inverted character.
     * @param c the character passed in.
     * @return the inverted character.
     * */
    char findInvert(char c) {
        if (findCycle(c).length() <= 1) {
            return c;
        } else {
            String correctCycle = findCycle(c);
            int index = correctCycle.indexOf(c) - 1;
            if (index >= 0) {
                return correctCycle.charAt(index);
            } else {
                return correctCycle.charAt(correctCycle.length() - 1);
            }
        }
    }
}
