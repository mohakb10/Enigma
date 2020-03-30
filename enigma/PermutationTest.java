package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @MohakBuch
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);



    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);

            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                  c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                  ci, perm.invert(ei));
        }

    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", new Alphabet());
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
    @Test
    public void testInvertChar() {
        Permutation p1 = new Permutation("(AC) (BD)", new Alphabet("ABCD"));
        assertEquals(p1.invert('A'), 'C');
        assertEquals(p1.invert('C'), 'A');
        assertEquals(p1.invert('B'), 'D');
        assertEquals(p1.invert('D'), 'B');
        Permutation p2 = new Permutation("(ABD) (C) (E) (F)",
                new Alphabet("ABCDEF"));
        assertEquals(p2.invert('B'), 'A');
        assertEquals(p2.invert('A'), 'D');
        assertEquals(p2.invert('D'), 'B');
        assertEquals(p2.invert('C'), 'C');
        assertEquals(p2.invert('F'), 'F');
        assertEquals(p2.invert('E'), 'E');
    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(AC) (BD)",
                new Alphabet("ABCD"));
        assertEquals(p.invert(0), 2);
        assertEquals(p.invert(2), 0);
        assertEquals(p.invert(1), 3);
        assertEquals(p.invert(3), 1);
        Permutation p2 = new Permutation("(FBG) (A) (D) (CHIJE)",
                new Alphabet("ABCDEFGHIJ"));
        assertEquals(p2.invert(5), 6);
        assertEquals(p2.invert(1), 5);
        assertEquals(p2.invert(6), 1);
        assertEquals(p2.invert(2), 4);
        assertEquals(p2.invert(7), 2);
        assertEquals(p2.invert(8), 7);
        assertEquals(p2.invert(4), 9);
    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(A) (B) (C) (D)",
                new Alphabet("ABCD"));
        assertEquals(p.size(), 4);
        Permutation p2 = new Permutation("(ABC) (DEF)",
                new Alphabet("ABCDEF"));
        assertEquals(p2.size(), 6);
        Permutation p3 = new Permutation("(A)",
                new Alphabet("A"));
        assertEquals(p3.size(), 1);
    }

    @Test
    public void testPermute() {
        Permutation p = new Permutation("(AD) (BC)",
                new Alphabet("ABCD"));
        assertEquals(p.permute(0), 3);
        assertEquals(p.permute(6), 1);
        assertEquals(p.permute(7), 0);
        p = new Permutation("(AD) (FBG) (CE)",
                new Alphabet("ABCDEFG"));
        assertEquals(p.permute(0), 3);
        assertEquals(p.permute(3), 0);
        assertEquals(p.permute(5), 1);
        assertEquals(p.permute(1), 6);
        assertEquals(p.permute(6), 5);
        assertEquals(p.permute(2), 4);
        assertEquals(p.permute(4), 2);
        p = new Permutation("(ABCD)", new Alphabet("ABCD"));
        assertEquals(p.permute('A'), 'B');
        assertEquals(p.permute('B'), 'C');
        assertEquals(p.permute('C'), 'D');
        assertEquals(p.permute('D'), 'A');
        p = new Permutation("(FBG) (CHIJE)",
                new Alphabet("ABCDEFGHIJ"));
        assertEquals(p.permute('F'), 'B');
        assertEquals(p.permute('B'), 'G');
        assertEquals(p.permute('C'), 'H');
        assertEquals(p.permute('H'), 'I');
        assertEquals(p.permute('I'), 'J');
        assertEquals(p.permute('E'), 'C');
    }

    @Test
    public void testDerangement() {


        Permutation p2 = new Permutation("(ABCD)", new Alphabet("ABCD"));
        assertEquals(p2.derangement(), true);

        Permutation p3 = new Permutation("(ABC) (DEF) (GHI)",
                new Alphabet("ABCDEFGHI"));
        assertEquals(p3.derangement(), true);

        Permutation p = new Permutation(
                "(AB) (CD) (EF) (G) (HIJK) (LMN) (OPQRST) (U) (V)",
                 new Alphabet("ABCDEFGHIJKLMNOPQRSTUV"));
        assertEquals(p.derangement(), false);
    }


}
