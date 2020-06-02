package org.afohtim.lab1;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ExtendedEuclideanAlgorithmTest extends TestCase {
    private final SecureRandom random = new SecureRandom();


    public void testGcd() {
        final int bitLength = 512;

        int iteration = 100;
        for (int i = 0; i < iteration; ++i) {
            final BigInteger firstNumber = new BigInteger(bitLength, random);
            final BigInteger secondNumber = new BigInteger(bitLength, random);

            final BigInteger expected = firstNumber.gcd(secondNumber);
            final BigInteger[] result = ExtendedEuclideanAlgorithm.gcd(firstNumber, secondNumber);
            final BigInteger actual = result[0];

            assertEquals(expected, actual);
        }
    }

    public void testXYFromGcd() {
        final int bitLength = 512;

        int iteration = 100;
        for (int i = 0; i < iteration; ++i) {
            final BigInteger firstNumber = new BigInteger(bitLength, random);
            final BigInteger secondNumber = new BigInteger(bitLength, random);

            final BigInteger expected = firstNumber.gcd(secondNumber);
            final BigInteger[] result = ExtendedEuclideanAlgorithm.gcd(firstNumber, secondNumber);
            final BigInteger x = result[1];
            final BigInteger y = result[2];

            final BigInteger actual = firstNumber.multiply(x).add(secondNumber.multiply(y));

            assertEquals(expected, actual);
        }
    }
}