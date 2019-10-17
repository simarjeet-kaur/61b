package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Simarjeet Kaur
 */

//finished??

class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    String _notches;
    Alphabet _rotorAlphabet;

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _rotorAlphabet = new Alphabet();
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (_rotorAlphabet.toChar(_setting) == _notches.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        if (atNotch()) {
            _setting++;
        }
        //doesn't this need to be the rotor to its right not this rotor
    }

    // fixme: addl fields as needed?

}
