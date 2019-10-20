package enigma;

import antlr.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]); //args[0] is the name of the config file

        if (args.length > 1) {
            _input = getInput(args[1]); //args[1] is optional, it's the message to be put in
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]); //names the output file for the processed messages, otherwise it'll just go
            //to the standard output
        } else {
            _output = System.out;
        }
    }

    //now we hae config, output, and input that we use for the rest of this
    //config is the big file in the default.config file

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {

        // FIXME
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine();
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.nextLine() != null) {
                allRotors.add(readRotor(_config.nextLine()));
            }
            _alphabet = new Alphabet();
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor(String description) {
        try {
            //EXAMPLE: I MQ      (AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)
            //I is the name, so do that
            String desc = description;
            String[] splitDescription = desc.split(" ");
            String[] nameAndType = new String[2];
            String[] permutations = new String[splitDescription.length - 2];
            System.arraycopy(splitDescription, 0, nameAndType, 0, 2);
            //now nameAndType should look like {"I", "MQ"}
            String name = nameAndType[0];
            String typeAndNotches = nameAndType[1];
            char type = typeAndNotches.charAt(0);
            String notches = findNotches(typeAndNotches);
            System.arraycopy(splitDescription, 2, permutations, 0, splitDescription.length - 2);

            String _permutations = Arrays.toString(permutations);
            Permutation _perm;
            _perm = new Permutation(_permutations, _alphabet);

            //examine the type to determine which rotor you need to return
            if (type == 'M') {
                return new MovingRotor(name, _perm, notches);
            }
            if (typeAndNotches.length() == 'N') { //it has no notches, so must be fixed
                return new FixedRotor(name, _perm);
            }
            if (typeAndNotches.length() == 'R') {
                return new Reflector(name, _perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return null;
    } //why is this bugging out

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        // FIXME
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {

    }

    public String findNotches(String NameType) {
        String result;
        result = "";
        for (int i = 0; i < NameType.length() - 1; i ++) {
            result += NameType.charAt(i);
        }
        return result;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
