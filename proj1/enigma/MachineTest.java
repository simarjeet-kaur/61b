package enigma;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineTest {

    Alphabet _alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    Rotor reflectorB = new Reflector("B",
            new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) "
                    + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
            _alphabet));
    Rotor rotorBeta = new FixedRotor("Beta",
            new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                    _alphabet));
    Rotor rotorIII = new MovingRotor("III",
            new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
                    _alphabet), "V");
    Rotor rotorIV = new MovingRotor("IV",
            new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                    _alphabet), "J");
    Rotor rotorI = new MovingRotor("I",
            new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)",
                    _alphabet), "Q");
    Rotor rotorII = new MovingRotor("II",
            new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)",
                    _alphabet), "E");
    Machine testMachine = new Machine(_alphabet, 5, 3,
            new Rotor[]{reflectorB, rotorBeta, rotorIII, rotorII, rotorI});

    @Test
    public void testNumRotors() {
        assertEquals(5, testMachine.numRotors());
    }

    @Test
    public void testNumPawls() {
        assertEquals(3, testMachine.numPawls());
    }

    @Test
    public void testinsertRotors() {
        String [] names = new String [5];
        names[0] = "B";
        names[1] = "Beta";
        names[2] = "III";
        names[3] = "IV";
        names[4] = "I";
        testMachine.insertRotors(names);
        assertEquals(names[0], testMachine.getSorted()[0].name());
        assertEquals(names[4], testMachine.getSorted()[4].name());
    }

    @Test
    public void testSetRotors() {
        String settingString = "AXLE";
        String [] names = new String [5];
        names[0] = "B";
        names[1] = "Beta";
        names[2] = "III";
        names[3] = "II";
        names[4] = "I";
        testMachine.insertRotors(names);
        testMachine.setRotors(settingString);
        assertEquals(_alphabet.toInt('A'),
                testMachine.getSorted()[1].setting());
        assertEquals(_alphabet.toInt('X'),
                testMachine.getSorted()[2].setting());
        assertEquals(_alphabet.toInt('L'),
                testMachine.getSorted()[3].setting());
        assertEquals(_alphabet.toInt('E'),
                testMachine.getSorted()[4].setting());
    }

    @Test
    public void testSetPlugboard() {
        Permutation test = new Permutation("(YF) (ZH)", _alphabet);
        testMachine.setPlugboard(test);
        assertEquals(test, testMachine.getPlugboard());
    }

    @Test
    public void testMessageConvert() {
        Permutation test = new Permutation("(AQ) (EP)", _alphabet);
        testMachine.setPlugboard(test);
        String settingString = "AAAA";
        String [] names = new String [5];
        names[0] = "B";
        names[1] = "Beta";
        names[2] = "I";
        names[3] = "II";
        names[4] = "III";
        testMachine.insertRotors(names);
        testMachine.setRotors(settingString);
        assertEquals("IHBDQQMTQZ", testMachine.convert("HELLO WORLD"));
    }

    @Test
    public void testMessageConvert2() {
        Permutation test = new Permutation("(TD) (KC) (JZ)", _alphabet);
        testMachine.setPlugboard(test);
        String settingString = "AAAA";
        String [] names = new String [5];
        names[0] = "B";
        names[1] = "Beta";
        names[2] = "I";
        names[3] = "II";
        names[4] = "III";
        testMachine.insertRotors(names);
        testMachine.setRotors(settingString);
        assertEquals("HGJNBOKDWALBFKU",
                testMachine.convert("I WAS SCARED OF CODING IN JAVA"
                        + "I WAS SCARED OF USING GIT"
                        + "AND STARTING ALL THESE PROJECTS"
                        + "COMPILER KEEPS GETTING MAD AT ME"
                        + "NOW MY PROJECT ONLY RUNS IN MY DREAMS"
                        + "OH OH ALL THESE MERGE CONFLICTS"));
    }
}
