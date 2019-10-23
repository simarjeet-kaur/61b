package enigma;

import java.util.HashMap;
import java.util.Map;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Simarjeet Kaur
 */
class Alphabet {

    /** Letters in the alphabet. */
    private String _letters;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */

    Alphabet(String chars) {
        char[] newChars = chars.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        for (char c : newChars) {
            if (map.containsKey(c)) {
                int counter = map.get(c);
                map.put(c, ++counter);
            } else {
                map.put(c, 1);
            }
        }

        for (char c : map.keySet()) {
            if (map.get(c) > 1) {
                throw new EnigmaException("Duplicates Found");
            } else {
                _letters = chars;
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _letters.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _letters.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _letters.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _letters.indexOf(ch);
    }
}
