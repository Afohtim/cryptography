package org.afohtim.lab1;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ModularExponentiationTest extends TestCase {
    private final SecureRandom random = new SecureRandom();

    public void testPowerModule() {
        final int bitLength = 512;

        int iteration = 100;
        for (int i = 0; i < iteration; ++i) {
            final BigInteger number = new BigInteger(bitLength, random);
            final BigInteger exponent = new BigInteger(bitLength, random);
            final BigInteger module = new BigInteger(bitLength, random);

            final BigInteger expected = number.modPow(exponent, module);
            final BigInteger actual = ModularExponentiation.powerModule(number, exponent, module);

            assertEquals(expected, actual);
        }
    }
}