package enigma;

import java.util.Arrays;

/** Class that represents a complete enigma machine.
 *  @author Simarjeet Kaur
 */
class Machine {
    /**List of sorted rotors.*/
    private Rotor[] _sortedRotors;
    /**Number of rotors.*/
    private int _numRotors;
    /**Number of pawls.*/
    private int _pawls;
    /**Plugboard.*/
    private Permutation _plugboard;
    /** List of all rotors.*/
    private Rotor[] _allRotors;
    /** Boolean array of if the rotor advances.*/
    private Boolean [] _booleanArray;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */

    Machine(Alphabet alpha, int numRotors, int pawls,
            Rotor[] allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = new Permutation("", _alphabet);
        _sortedRotors = new Rotor [numRotors()];
        _booleanArray = new Boolean[numRotors];

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
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor rotor : _allRotors) {
                if (rotor.name().equals(rotors[i])) {
                    _sortedRotors[i] = rotor;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Setting is incorrect length");
        }
        if (_sortedRotors.length != numRotors()) {
            throw new EnigmaException("Rotors are not named correctly");
        }
        for (int i = 1; i < numRotors(); i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException("Initial positions not in alphabet");
            } else {
                _sortedRotors[i].set(setting.charAt(i - 1));
            }
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
        int converted = c;
        advanceAllRotors();
        if (_plugboard != null) {
            converted = _plugboard.permute(c);
        }
        for (int i = _sortedRotors.length - 1; i >= 0; i--) {
            converted = _sortedRotors[i].convertForward(converted);
        }
        for (int k = 1; k < _sortedRotors.length; k++) {
            converted = _sortedRotors[k].convertBackward(converted);
        }
        if (_plugboard != null) {
            converted = _plugboard.permute(converted);
        }
        return converted;
    }

    /**Function to advance all rotors.*/
    private void advanceAllRotors() {
        Arrays.fill(_booleanArray, false);
        for (int i = 1; i < _sortedRotors.length; i++) {
            if (i == _booleanArray.length - 1) {
                _booleanArray[i] = true;
            } else if ((_sortedRotors[i].rotates()
                    && _sortedRotors[i + 1].atNotch())) {
                _booleanArray[i] = true;
                _booleanArray[i + 1] = true;
            }
        }
        for (int i = 0; i < _sortedRotors.length; i++) {
            if (_booleanArray[i]) {
                _sortedRotors[i].advance();
            }
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char current = msg.charAt(i);
            if (current != ' ') {
                int a = convert(_alphabet.toInt(current));
                result.append(_alphabet.toChar(a));
            }
        }
        return result.toString();
    }

    /**Getting sorted rotors.
     * @return the sorted rotors.
     * */
    Rotor[] getSorted() {
        return _sortedRotors;
    }

    /**Getting plugboard.
     * @return the plugboard permutation.
     * */
    Permutation getPlugboard() {
        return _plugboard;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

}
