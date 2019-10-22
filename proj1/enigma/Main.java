package enigma;

import antlr.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;

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
            if (alphabet.contains("(") || alphabet.contains("*") || alphabet.contains(")")) {
                throw new EnigmaException("Incorrect Alphabet format");
            }
            _alphabet = new Alphabet(alphabet);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("No number of rotors/pawls"); //does it need this
            }
            int numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("No number of pawls");
            }
            int numPawls = _config.nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            Rotor[] _allRotors = allRotors.toArray(new Rotor[allRotors.size()]);
            return new Machine(_alphabet, numRotors, numPawls, _allRotors);
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
            Permutation _perm;

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

            _perm = new Permutation(permutations, _alphabet);

            if (type == 'M') {
                return new MovingRotor(name, _perm, notches);
            }
            else if (type == 'N') {
                return new FixedRotor(name, _perm);
            }
            else if (type == 'R') {
                return new Reflector(name, _perm);
            }
            else {
                throw new EnigmaException("Wrong type");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }
    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        //* B Beta III IV I AXLE (YF) (ZH)
        int numRotors = M.numRotors();
        String [] _settings = settings.split(" +");
        System.out.print(Arrays.toString(_settings));
        //needs a * at the beginning
        String [] rotors = new String[numRotors];
        System.arraycopy(_settings, 1, rotors, 0, numRotors);
        System.out.print(Arrays.toString(rotors));
        //B to I is the name of the rotors now in rotors array
        String setting = _settings[numRotors + 1];
        System.out.print(setting);
        //AXLE are the names of the settings, would come after the * and after the rotors' names, so
        M.insertRotors(rotors);
        M.setRotors(setting);
        //YF and ZH are the steckered things reflectors, these become the plugboard
        if (_settings.length - 2 - numRotors == 0) {
        } else {
            String[] steckered = new String[_settings.length - 2 - numRotors];
            System.arraycopy(_settings, 2 + numRotors, rotors, 0, _settings.length - 2 - numRotors);
            String _steckered = Arrays.toString(steckered);
            M.setPlugboard(new Permutation(_steckered, _alphabet));
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

    public String findNotches(String NameType) {
        String result;
        result = "";
        for (int i = 0; i < NameType.length() - 1; i ++) {
            result += NameType.charAt(i);
        }
        return result;
    }

    /** Alphabet used in this machine. */
    Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
