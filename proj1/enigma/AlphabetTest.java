package enigma;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlphabetTest {

        Alphabet test = new Alphabet("ABCD");

        @Test
        public void testSize() {
            assertEquals(test.size(), 4);
        }
        @Test
        public void testContains() {
            assertEquals(test.contains('A'), true);
            assertEquals(test.contains('Z'), false);
        }
        @Test
        public void testToChar() {
            assertEquals(test.toChar(0), 'A');
            assertEquals(test.toChar(2), 'C');
        }
        @Test
        public void testToInt() {
            assertEquals(test.toInt('A'), 0);
            assertEquals(test.toInt('C'), 2);
        }
    }
