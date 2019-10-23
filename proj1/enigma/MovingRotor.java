package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Simarjeet Kaur
 */

class MovingRotor extends Rotor {

    /** Notches of the Rotor.*/
    private String _notches;
    /** Rotor's alphabet.*/
    private Alphabet _rotorAlphabet;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _rotorAlphabet = perm.alphabet();
    }

    @Override
    boolean atNotch() {
        char c = _rotorAlphabet.toChar(permutation().wrap(this.setting()));
        return _notches.indexOf(c) != -1;
    }

    @Override
    void advance() {
        this.set(permutation().wrap(this.setting() + 1));
    }

}
