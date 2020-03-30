package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author MohakBuch
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
        Machine M = readConfig();
        String temp;
        if (_input.hasNextLine()) {
            String setting = _input.next();
            if (setting.charAt(0) == '*') {
                temp = setting + _input.nextLine();
                setUp(M, temp);
            } else {
                throw new EnigmaException("No setting found");
            }
        }

        while (_input.hasNextLine()) {

            temp = _input.nextLine();
            while (temp.equals("") && _input.hasNextLine()) {
                _output.println();
                temp = _input.nextLine();
            }
            if (_input.hasNext() && temp.charAt(0) == '*') {
                M.fixRotors();
                setUp(M, temp);

            } else {
                if (temp.contains("*")) {
                    printMessageLine("");
                } else {
                    String con = M.convert(temp);
                    printMessageLine(con);
                }
            }

        }



    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {

        int numRotors;

        int numPawls;
        String rotors;
        String pawls;

        try {
            _alphabet = new Alphabet(_config.nextLine());
            rotors = _config.next();
            numRotors = Integer.parseInt(rotors);
            pawls = _config.next();
            numPawls = Integer.parseInt(pawls);
            ArrayList<Rotor> allrotors = new ArrayList<>();
            _config.nextLine();

            while (_config.hasNextLine()) {
                Rotor r = readRotor();
                allrotors.add(r);
                if (_config.hasNextLine()) {
                    _config.nextLine();
                }
            }
            return new Machine(_alphabet, numRotors, numPawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        String name;
        String cycle;
        String notch;
        String temp;
        char m;

        try {

            name = _config.next().toUpperCase();
            temp = _config.next();

            m = temp.charAt(0);
            cycle = "";
            Permutation p;
            notch = temp.substring(1, temp.length());
            while (_config.hasNext("\\(.+\\)")) {
                cycle += _config.next();
            }
            p = new Permutation(cycle, _alphabet);
            if (m == 'M') {

                return new MovingRotor(name, p, notch);

            } else if (m == 'N') {

                return new FixedRotor(name, p);

            } else if (m == 'R') {

                return new Reflector(name, p);

            } else {
                throw new EnigmaException("Incorrect Rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {


        char star = settings.charAt(0);
        Scanner scan = new Scanner(settings);
        String[] rotorslst = new String[M.numRotors()];


        if (star != '*') {
            throw new EnigmaException("Not being set up");
        }

        if (scan.hasNext("[*]")) {

            scan.next();

            for (int i = 0; i < M.numRotors(); i++) {
                rotorslst[i] = scan.next();
            }
            M.insertRotors(rotorslst);
            String settng;
            if (scan.hasNext()) {
                settng = scan.next();
            } else {
                settng = "";
            }

            int num = M.numRotors() - 1;
            if (settng.length() != num) {
                throw new EnigmaException("Settings improperly formatted");
            }
            String plugboard = "";

            M.setRotors(settng);
            while (scan.hasNext()) {
                plugboard += scan.next() + " ";
            }


            M.setPlugboard(new Permutation(plugboard.
                    substring(0, plugboard.length()),
                    _alphabet));



        }

    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() <= 5) {
            _output.println(msg);
        } else {
            for (int i = 0; i < msg.length(); i += 5) {
                int group = msg.length() - i;
                if (group <= 5) {
                    _output.println(msg.substring(i, i + group));
                } else {
                    _output.print(msg.substring(i, i + 5) + " ");
                }
            }
        }
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
