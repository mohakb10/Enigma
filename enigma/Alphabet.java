package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *
 *
 *  @author Mohak Buch
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */

    Alphabet(String chars) {
        this._chars = chars;
        if (_chars.length() == 0) {
            throw new EnigmaException("No Alphabets");
        }
        for (int i = 0; i < _chars.length(); i++) {
            for (int j = 0; j < _chars.length(); j++) {
                boolean b = (i != j);
                if (b && _chars.charAt(i) == _chars.charAt(j)) {
                    throw new EnigmaException("No duplicate chars allowed");

                }
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < _chars.length(); i++) {
            if (_chars.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this._chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar().
     * @return index of char ch which must be in alphabet
     *  */
    int toInt(char ch) {
        return this._chars.indexOf(ch);
    }

    /**
     * @return
     */
    private String _chars;

}
