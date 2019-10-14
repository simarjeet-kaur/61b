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
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        String _notches = notches;
    }

    // FIXME?

    @Override
    void advance() {
        if (atNotch()) { //check something about notches too
            _setting++;
        }
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
