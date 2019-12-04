package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {
    }

    @Test
    public void testingDoubleInit() {
        Repo theRepo;
        theRepo = new Repo();
        assertTrue(repoExists());
        //return repoExists();
    }

    @Test
    public void testingAdd() {
        Repo theRepo = new Repo();
        theRepo.init();
        theRepo.add("test.txt");
    }

    private static boolean repoExists() {
            File checking = new File(".gitlet");
            boolean check = checking.exists();
            return check;
    }

}


