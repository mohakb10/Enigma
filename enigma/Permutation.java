package enigma;
import java.util.HashMap;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Mohak Buch
 */

class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */

    Permutation(String cycles, Alphabet alphabet) {

        _alphabet = alphabet;
        result = new HashMap<>();
        reverse = new HashMap<>();
        _cycles = cycles;
        char first = ' ';
        for (int i = 0; i < cycles.length(); i++) {
            char c = _cycles.charAt(i);
            if (c == '(' && Character.isLetter(_cycles.charAt(i + 1))) {
                first = _cycles.charAt(i + 1);
            }
            if (Character.isLetter(_cycles.charAt(i))) {
                if (Character.isLetter(_cycles.charAt(i + 1))) {
                    result.put(_cycles.charAt(i), _cycles.charAt(i + 1));
                    reverse.put(_cycles.charAt(i + 1), _cycles.charAt(i));
                } else {
                    result.put(_cycles.charAt(i), first);
                    reverse.put(first, _cycles.charAt(i));
                }

            }

        }
        splitCycles = _cycles.trim().replace("(", "")
                .replace(")", "").split(" ");
    }
    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            if (cycle.length() == 1) {
                char c = _alphabet.toChar(cycle.charAt(i));
                result.put(c, alphabet().toChar(cycle.charAt(0)));
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. *
     * @return value of P modulo the size of permutation.
     */

    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute.
     * @return size of alphabet permuted.
     * */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size.
     * @return result of applying permutation to p module alphabet size.
     *  */
    int permute(int p) {
        if (result.containsKey(_alphabet.toChar(wrap(p)))) {
            return _alphabet.toInt(result.get(_alphabet.toChar(wrap(p))));
        }
        return p;

    }

    /** Return the result of applying the inverse of this permutation.
     *  to  C modulo the alphabet size
     *  @return inverse of permutation.
     *  */
    int invert(int c) {
        if (reverse.containsKey(_alphabet.toChar(wrap(c)))) {
            return _alphabet.toInt(reverse.get(_alphabet.toChar(wrap(c))));
        }
        return c;

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET.
     * @return applied permutation to index of p.
     *  */
    char permute(char p) {
        if (result.containsKey(p)) {
            return result.get(p);
        }
        return p;

    }


    /** Return the result of applying the inverse of this permutation to C.
     * @return inverse of permutation.
     * */
    char invert(char c) {
        if (reverse.containsKey(c)) {
            return reverse.get(c);
        }
        return c;

    }

    /** Return the alphabet used to initialize this Permutation.
     * @return alphabet.
     * */
    Alphabet alphabet() {
        return _alphabet;

    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself).
     * @return true when no value maps to itself.
     *  */
    boolean derangement() {

        int count = 0;
        for (int i = 0; i < splitCycles.length; i++) {
            count += splitCycles[i].length();
            if (splitCycles[i].length() == 1) {
                return false;
            }
        }

        if (count == _alphabet.size()) {
            return true;
        }
        return false;

    }

    /** @return cycles*/
    private String _cycles;
    /** @return alphabet*/
    private Alphabet _alphabet;
    /** @return hashmap of rotors*/
    private HashMap<Character, Character> result;
    /** @return reverse hashmap of rotors*/
    private HashMap<Character, Character> reverse;
    /** @return splitcycles*/
    private String[]  splitCycles;


}
