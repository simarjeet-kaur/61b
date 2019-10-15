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

    //what is the point of notches? if it is at a notch does this change the setting?

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
            _setting++;
    }

    // fixme: addl fields as needed?

}