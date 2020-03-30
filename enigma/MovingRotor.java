package enigma;


import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author MohakBuch
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     *
     */

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _perm = perm;
        _notches = notches;
    }



    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }
    @Override
    /**
     * @return whether or not rotor is at a notch
     */
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (alphabet().toInt(_notches.charAt(i)) == setting()) {
                return true;
            }
        }
        return false;
    }
    @Override
    /**
     * @return true if rotor will rotate.
     */
    boolean rotates() {
        return true;
    }

    /** *
     * @return notches
     */
    private String _notches;
    /**
     * @return permutation
     */
    private Permutation _perm;

}
