package enigma;

import java.util.HashMap;
import java.util.Map;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Simarjeet Kaur
 */
class Alphabet {

    //check for duplicates and associate each letters with numerical values
        //in the constructor
        //if there is a duplicate, you want to throw the enigma exception

    //trying to calculate size of intlist use an instance varibale that can store it for you
    //another method in alphabet class is contains
    //you need to store the alphabet to begin with before you check if something is in it
    //think about what variables you need to keep track of
    //how can you store the string - copy it over probably
    //look at animal example from discussion on how to set variables equal to soemthing
    //instance variable
    //safe to assume your alphabet is immutable

    //how to traverse through a string - what do we do?
        //checking for duplicates - traversing or data structures - easiest is whatever makes most sense to you
        //traversing meaning for loops
        //tips for looking through strings in general
            //say we have Stirng S;
            //something you can do is s.indexOf(char c) which returns the indexOf the first occurance of this char, if it's not there
            //it'll return a negative number
            //would be helpful for
            //google Java String
            //something else that could also be helpful is s.charAt(int i) - indexing into the string to find what is at
            //this index
    //ex: alphabet is ABCDE - how do you check for duplicates
        //
    String _letters;

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
        // fixme

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
    //calls its own default constructor - one that takes in no parameters, that will call it's constructor on an alphabet
    //calling above constructor

    /** Returns the size of the alphabet. */
    int size() {
        return _letters.length(); // fixme
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _letters.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {

        return _letters.charAt(index); // fixme
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {

        return _letters.indexOf(ch); // fixme
    }

}
