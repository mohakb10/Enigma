package enigma;

import net.sf.saxon.functions.ConstantFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Mohak Buch
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors.
     *  */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _numRotors = numRotors;
        _alphabet = alpha;
        _numPawls = pawls;
        _allRotors = allRotors;
        _rotors = new HashMap<>();


    }

    /** Return the number of rotor slots I have.
     * @return num rotors
     * */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have.
     * @return number pawls
     * */
    int numPawls() {
        return _numPawls;

    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */

    void insertRotors(String[] rotors) {
        orderedRotors = new ArrayList<>();
        HashMap<String, Rotor> map = new HashMap<>();
        for (Rotor rot : _allRotors) {
            map.put(rot.name().toUpperCase(), rot);
        }

        for (int i = 0; i < rotors.length; i++) {
            if (map.containsKey(rotors[i].toUpperCase())) {
                if (orderedRotors.contains(map.get(rotors[i].toUpperCase()))) {
                    throw new EnigmaException("Duplicate rotor name");
                } else {
                    orderedRotors.add(map.get(rotors[i].toUpperCase()));
                }
            }
        }

        Rotor zero = orderedRotors.get(0);
        if (!zero.reflecting() || zero.rotates()) {
            throw new EnigmaException("Does not have functional reflector");
        }

    }

    /**
     * resets rotors.
     */
    public void fixRotors() {
        orderedRotors = new ArrayList<>();
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != (numRotors() - 1)) {
            throw new EnigmaException("Wheel settings too short");
        }
        for (int i = 0; i < orderedRotors.size() - 1; i++) {
            orderedRotors.get(i + 1).set(setting.charAt(i));
        }


    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    /**
     * @return returns converted int value
     * @param c is the converted character
     */
    int convert(int c) {

        if ((c >= 0 || c == -1) && (c <= _alphabet.size() - 1)) {

            c = _plugboard.permute(c);
            Rotor right = orderedRotors.get(orderedRotors.size() - 1);
            boolean turn = true;
            boolean next = right.atNotch();
            right.advance();
            ArrayList<Rotor> advancer = new ArrayList<>();


            for (int i = orderedRotors.size() - 2; i >= 0; i--) {
                if (!next) {
                    turn = false;
                    next = orderedRotors.get(i).atNotch();
                } else {
                    next = orderedRotors.get(i).atNotch();
                    advancer.add(orderedRotors.get(i));
                    if (!turn && orderedRotors.get(i).rotates()) {
                        advancer.add(right);
                    }
                    turn = true;
                }
                right = orderedRotors.get(i);
            }
            for (Rotor r: advancer) {
                r.advance();
            }

            for (int i = orderedRotors.size() - 1; i >= 0; i--) {

                c = orderedRotors.get(i).convertForward(c);
            }

            for (int i = 1; i < orderedRotors.size(); i++) {
                c = orderedRotors.get(i).convertBackward(c);
            }
            c = _plugboard.invert(c);
        } else {
            throw new EnigmaException("char not in alphabet");
        }


        return c;
    }




    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly.
     * @return converted character
     *  */
    String convert(String msg) {
        String converting = "";
        for (int i = 0; i < msg.length(); i++) {
            char m = msg.charAt(i);
            if (m != ' ') {
                int letter = _alphabet.toInt(m);
                converting += _alphabet.toChar(convert(letter));
            }
        }
        return converting;
    }

    /** @return final alphabet */
    private final Alphabet _alphabet;
    /**@return num rotors: number of rotors */
    private int _numRotors;
    /** @return number of pawls*/
    private int _numPawls;
    /** @return _rotors */
    private HashMap<String, Rotor> _rotors;
    /** @return _rotors */
    private Collection<Rotor> _allRotors;
    /** @return _rotors */
    private ArrayList<Rotor> orderedRotors;
    /** @return _plugboard*/
    private Permutation _plugboard;

}

