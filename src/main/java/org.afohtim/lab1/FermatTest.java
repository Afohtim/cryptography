package org.afohtim.lab1;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class FermatTest {
    private FermatTest() {

    }

    static public boolean isPrime(final BigInteger number, final SecureRandom random) {
        if (BigInteger.ONE.equals(number) || BigInteger.ZERO.equals(number)) {
            return false;
        }

        final int iterations = 100;
        for (int i = 0; i < iterations; ++i) {
            BigInteger a = getRandomBigIntegerByNumber(number, random);
            a = a.modPow(number.subtract(BigInteger.ONE), number);

            if (!BigInteger.ONE.equals(a)) {
                return false;
            }
        }
        return true;
    }

    static private BigInteger getRandomBigIntegerByNumber(final BigInteger number, final SecureRandom random) {
        while (true) {
            final  BigInteger a = new BigInteger(number.bitLength(), random);

            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(number) < 0) {
                return a;
            }
        }
    }
}
