package org.afohtim.lab1;

import java.math.BigInteger;

public final class ModularExponentiation {
    private ModularExponentiation() {}

    static public BigInteger powerModule(final BigInteger number, final BigInteger exponent, final BigInteger module) {
        BigInteger result = BigInteger.ONE;

        BigInteger x = number.mod(module);

        if (x.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }

        BigInteger y = new BigInteger(exponent.toString());

        while (!y.equals(BigInteger.ZERO))
        {
            if (y.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(x).mod(module);
            }

            y = y.shiftRight(1);
            x = x.multiply(x).mod(module);
        }
        return result;
    }
}
