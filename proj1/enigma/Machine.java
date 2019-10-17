package enigma;

import javax.management.AttributeList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

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

    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = (Rotor[]) allRotors.toArray();
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
        Rotor [] _sortedRotors = new Rotor [_allRotors.length];
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor rotor : _allRotors) {
                if (rotor.name() == rotors[i]) {
                    _sortedRotors[i] = rotor;
                }
            }
        }
        _allRotors = _sortedRotors; //is this right? doesn't return anything but should resort them
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        //check the length of settings to make sure it is numrotors-1 and also check
        //to make sure the first rotor is in fact a reflector
        //dont change the setting of the relfector
        int i = 0;
        Rotor [] _allRotorsNoReflector = new Rotor[_allRotors.length - 1];
        System.arraycopy(_allRotors, 1, _allRotorsNoReflector, _allRotors.length, _allRotors.length - 1);
        for (Rotor rotor : _allRotorsNoReflector) {
            rotor.set(setting.charAt(i));
            i++;
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

        return 0; // FIXME
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        return ""; // FIXME
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
