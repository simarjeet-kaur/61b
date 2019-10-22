package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Simarjeet Kaur
 */

class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    String _notches;
    Alphabet _rotorAlphabet;
    Permutation _permutation;

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
        _rotorAlphabet = perm.alphabet();
    }

    @Override
    boolean atNotch() {
            if (_notches.indexOf(_rotorAlphabet.toChar(permutation().wrap(this.setting()))) != -1) {
                return true;
        }
        return false;
    }

    @Override
    void advance() {
        this.set(permutation().wrap(this.setting() + 1));
    }

    // fixme: addl fields as needed?

}
