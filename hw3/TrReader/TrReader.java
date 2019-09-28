import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.k
 *  @author your name here
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    private final String _from;
    private final String _to;
    private final Reader _str;
    public TrReader(Reader str, String from, String to) {
        this._from = from;
        this._to = to;
        this._str = str;
        }
        // TODO: YOUR CODE HERE

    public void close() throws IOException {
        _str.close();
        //need to call close() on your own instance
        //before it was just a general close, not applied to anything
        //in this case you need to apply it to your _str
    }

    private char conversion(char in) {
        int i = _from.indexOf(in);
        if (i == -1) {
            return in;
        } else {
        return _to.charAt(i);
        }
    }

    //@Override
    public int read(char[] characters, int number, int len) throws IOException {
        int actual = _str.read(characters, number, len);
        for (int i = number; i < number + actual; i++) {
            characters[i] = conversion(characters[i]);
        }
        return actual;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
}
