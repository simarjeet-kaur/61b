package enigma;

import javax.management.AttributeList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.ListIterator;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */

    int _numRotors;
    int _pawls;
    Permutation _plugboard;
    Rotor[] _allRotors;
    Rotor [] _sortedRotors;
    //Alphabet _alphabet;

    Machine(Alphabet alpha, int numRotors, int pawls,
            Rotor[] allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = new Permutation("", _alphabet);
        _sortedRotors = new Rotor [numRotors()];

        if (pawls < 0 || pawls > numRotors) {
            throw new EnigmaException("Incompatible number of pawls");
        }
        if (numRotors < 1) {
            throw new EnigmaException("Incompatible number of rotors");
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        //what is a rotor slot
        //this gives a stringof the correct order of rotors, and you need to reorder the collection properly
        //make a new variable to store them in any other data strucutre, get all of them and check if the string is valid
        //make sure it's a string that exists
       // Collections(_allRotors);
        //create a new array of the sorted ones that is emprt
        //iterate through the list to find which one matches up first , add it to the new list, then move on
        if (_sortedRotors.length != numRotors()) {
            throw new EnigmaException("Rotors are not named correctly");
        }

        for (int i = 0; i < rotors.length; i++) {
            for (Rotor rotor : _allRotors) {
                if (rotor.name() == rotors[i]) {
                    _sortedRotors[i] = rotor;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        //check the length of settings to make sure it is numrotors-1 and also check
        //to make sure the first rotor is in fact a reflector
        //dont change the setting of the relfector
        if (setting.length() != numRotors()-1) {
            throw new EnigmaException("Setting is incorrect length");
        }
        for (int i = 1; i < numRotors(); i++) {
            if (!_alphabet.contains(setting.charAt(i-1))) {
                throw new EnigmaException("Initial positions string not in alphabet");
            }
            _sortedRotors[i].set(setting.charAt(i-1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        //is this using permute with a new permutation?
        //write easy test cases first
        //jumping from rotor to rotor using convert
        //next rotor next cycle that it's in
        //advancing machine = next rotor
        //make all rotors an iterator so you can iterate through it properly
        ///check if there is a plugboard - if there is a plug board, then it translates immediately
        int converted = c;
        advanceAllRotors();
//        if (_plugboard != null) {
//            converted = _plugboard.permute(c);
//        }
        for (int i = _sortedRotors.length - 1; i >= 0; i--) {
            converted = _sortedRotors[i].convertForward(converted);
        }
        for (int k = 1; k < _sortedRotors.length; k++) {
            converted = _sortedRotors[k].convertBackward(converted);
        }
//        if (_plugboard != null) {
//            converted = _plugboard.permute(converted);
//        }
        return converted;
    }

    void advanceAllRotors() {
        for (int i = 1; i < _sortedRotors.length - 1; i++) {
            if (_sortedRotors[i].rotates()
                    && (_sortedRotors[i+1].atNotch()
                    || ((i + 1) == (_sortedRotors.length - 1))
                    && (_sortedRotors[i].atNotch()
                    && _sortedRotors[i - 1].rotates()))) {
                boolean a = true;
                for (int j = i + 1; j < _sortedRotors.length - 1; j++) {
                    if (!_sortedRotors[i].atNotch()) {
                        a = false;
                    }
                }
                if (a) {
                    _sortedRotors[i].advance();
                }
            }
        }
        _sortedRotors[_sortedRotors.length - 1].advance();
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            char current = msg.charAt(i);
            if (current != ' ') {
                int a = convert(_plugboard.permute(_alphabet.toInt(current)));
                result = result + _alphabet.toChar(_plugboard.permute(a));
            }
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

}
