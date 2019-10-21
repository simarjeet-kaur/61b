package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.Collection;

import static org.junit.Assert.*;
import static enigma.TestUtils.*;

public class MachineTest {

    //* B Beta III IV I AXLE (YF) (ZH)

    Collection<Rotor> Rotors;
    Alphabet _alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    Machine testMachine = new Machine(_alphabet, 5, 3, Rotors);
    Rotor reflectorB = new Reflector("B", new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", _alphabet));
    Rotor rotorBeta = new FixedRotor("Beta", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", _alphabet));
    Rotor rotorIII = new MovingRotor("III", new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", _alphabet), "V");
    Rotor rotorIV = new MovingRotor("IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", _alphabet), "J");
    Rotor rotorI = new MovingRotor("I", new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", _alphabet), "Q");

    //private Object EnigmaException;

    @Test
    public void testNumRotors() {
        assertEquals(5, testMachine.numRotors());
    }

    @Test
    public void testNumPawls() {
        assertEquals(3, testMachine.numPawls());
    }

    //* B Beta III IV I AXLE (YF) (ZH)

    @Test
    public void testinsertRotors() {
        String [] names = new String [5];
        names[0] = "B";
        names[1] = "Beta";
        names[2] = "III";
        names[3] = "IV";
        names[4] = "I";
        testMachine.insertRotors(names);
        assertEquals(names[0], testMachine._allRotors[0].name()); //see how to find the first value of a collection
        assertEquals(names[5], testMachine._allRotors[4].name());
    }

    @Test
    public void testSetRotors() {
        String settingString = "AXLE";
        testMachine.setRotors(settingString);
        assertEquals(testMachine._allRotors[0].setting(), 'A');
        assertEquals(testMachine._allRotors[1].setting(), 'X');
        assertEquals(testMachine._allRotors[2].setting(), 'L');
        assertEquals(testMachine._allRotors[3].setting(), 'E');
    }

    @Test
    public void testSetPlugboard() {
        testMachine.setPlugboard(new Permutation("(YF) (ZH)", _alphabet));
        assertEquals(new Permutation("(YF) (ZH)", _alphabet), testMachine._plugboard);
    }

    @Test
    public void testConvert() {
        assertEquals('Z', testMachine.convert('Y'));
    }

}
