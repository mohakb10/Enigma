package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author MohakBuch
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    private Permutation _perm;

    /**
     *
     * @param name of reflector
     * @param perm is the permutation
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _perm = perm;

    }

    /**
     *
     * @return reflecting
     */
    @Override
    boolean reflecting() {
        return true;
    }

    /**
     *
     * @param posn
     */
    @Override
    void set(int posn) {
        if (posn != 0) {

            throw new EnigmaException("reflector has only one position");
        }

    }

}
