package org.afohtim.lab1;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MontgomeryReducerTest extends TestCase {
    private final SecureRandom random = new SecureRandom();


    public void testMultiply() {
        final int bitLength = 512;

        int iteration = 100;
        for (int i = 0; i < iteration; ++i) {
            final BigInteger firstNumber = new BigInteger(bitLength, random);
            final BigInteger secondNumber = new BigInteger(bitLength, random);
            final BigInteger module = BigInteger.probablePrime(bitLength, random);

            MontgomeryReducer montgomeryReducer = new MontgomeryReducer(module);

            final BigInteger expected = firstNumber.multiply(secondNumber).mod(module);
            final BigInteger actual = montgomeryReducer.multiplyWithConvert(firstNumber, secondNumber);

            assertEquals(expected, actual);
        }
    }

    public void testPow() {
        final int bitLength = 512;

        int iteration = 100;
        for (int i = 0; i < iteration; ++i) {
            final BigInteger number = new BigInteger(bitLength, random);
            final BigInteger exponent = new BigInteger(bitLength, random);
            final BigInteger module = BigInteger.probablePrime(bitLength, random);;

            MontgomeryReducer montgomeryReducer = new MontgomeryReducer(module);

            final BigInteger expected = number.modPow(exponent, module);
            final BigInteger actual = montgomeryReducer.powWithConvert(number, exponent);

            assertEquals(expected, actual);
        }
    }
}