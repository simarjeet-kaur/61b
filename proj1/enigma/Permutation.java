package enigma;

import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */

    String _cycles;
    ArrayList<String> _listOfCycles;
    HashMap<String, Integer> _permutedAlphabet;

    Permutation(String cycles, Alphabet alphabet) {
        _cycles = cycles;
        _listOfCycles = new ArrayList<>();
        _permutedAlphabet = new HashMap<>();
        // FIXME
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {



        //adding to the instance of the permutation
        //what data structure are you using to represent cycles - multiple ways to do this
        //want to represent how these cycles enter the java program - you could do array lists
        //could use a list of strings (a lazy approach)
        //cycle itself is the letters you're using
        //"ABCD", "EF"
        //how would you check if Z maps to anything?
            //check every single letter with a double for loop (discussion)
        //could store these as lists
            //ABCD in a list

//        for (String cycle : _cycles) {
//
//        }
        // FIXME
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
        return _alphabet.size(); // fixme
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        //use cycle.length() mod the permutation length to find out which index to
        String[] _splitCycles = new String[];
        _splitCycles = splitCycles(_cycles);
        int pMod = wrap(p);
        for (int i = 0; i < _alphabet.size(); i ++) {
            _permutedAlphabet.put(_alphabet.toChar(i), i);
        }
        return 0;  // FIXME
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return 0;  // FIXME
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return 0;  // FIXME
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return 0;  // FIXME
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        //check _permutedAlphabet if any index matches up
        for (key : _permutedAlphabet)
        return true;  // FIXME
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /**split cycles here */
    String[] splitCycles(String cycles) {
        return cycles.split(")");

        //fixme
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
