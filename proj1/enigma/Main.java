package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Simarjeet Kaur
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

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

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
        Machine machine = readConfig();
        String setting = _input.nextLine();
        if (setting.charAt(0) != '*') {
            throw new EnigmaException("Wrong setting");
        }
        setUp(machine, setting);
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            if (!next.equals("") && next.charAt(0) == '*') {
                setUp(machine, next);
            } else {
                printMessageLine(machine.convert(next));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine();
            if (alphabet.contains("(")
                    || alphabet.contains("*") || alphabet.contains(")")) {
                throw new EnigmaException("Incorrect Alphabet format");
            }
            _alphabet = new Alphabet(alphabet);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("No number of rotors/pawls");
            }
            int numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("No number of pawls");
            }
            int numPawls = _config.nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            Rotor[] rotors = allRotors.toArray(new Rotor[allRotors.size()]);
            return new Machine(_alphabet, numRotors, numPawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name;
            String typeAndNotches;
            char type;
            String notches;
            String permutations;
            Permutation perm;

            name = _config.next();
            typeAndNotches = _config.next();
            type = typeAndNotches.charAt(0);
            permutations = "";

            while (_config.hasNext("\\([^\\*]+\\)")) {
                permutations += _config.next();
            }

            if (typeAndNotches.length() == 1) {
                notches = "";
            } else {
                notches = typeAndNotches.substring(1);
            }

            perm = new Permutation(permutations, _alphabet);

            if (type == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'R') {
                return new Reflector(name, perm);
            } else {
                throw new EnigmaException("Wrong type");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }
    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int numRotors = M.numRotors();
        String [] set = settings.split(" +");
        String [] rotors = new String[numRotors];
        System.arraycopy(set, 1, rotors, 0, numRotors);
        if (numRotors + 1 > set.length - 1) {
            throw new EnigmaException("Incorrect Length");
        }
        String setting = set[numRotors + 1];
        M.insertRotors(rotors);
        M.setRotors(setting);
        if (set.length - 2 - numRotors != 0) {
            String[] steckered = new String[set.length - 2 - numRotors];
            System.arraycopy(set, 2 + numRotors, steckered,
                    0, set.length - 2 - numRotors);
            String result = "";
            for (int i = 0; i < steckered.length; i++) {
                result += steckered[i];
            }
            M.setPlugboard(new Permutation(result, _alphabet));
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            if (i % 5 == 0 && i > 0) {
                _output.print(" ");
            }
            _output.print(msg.charAt(i));
        }
        _output.println();
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
