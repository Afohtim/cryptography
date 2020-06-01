package org.afohtim.lab1;


import java.math.BigInteger;
import java.util.Objects;


public final class MontgomeryReducer {
    private final BigInteger modulus;

    private final int reducerBits;
    private final BigInteger reciprocal;
    private final BigInteger mask;
    private final BigInteger factor;
    private final BigInteger convertedOne;

    public BigInteger toMontgomeryForm(BigInteger number) {
        return number.shiftLeft(reducerBits).mod(modulus);
    }

    public BigInteger fromMontgomeryForm(BigInteger number) {
        return number.multiply(reciprocal).mod(modulus);
    }

    public BigInteger multiply(BigInteger first, BigInteger second) {
        assert first.signum() >= 0 && first.compareTo(modulus) < 0;
        assert second.signum() >= 0 && second.compareTo(modulus) < 0;

        BigInteger product = first.multiply(second);
        BigInteger temp = product.and(mask).multiply(factor).and(mask);
        BigInteger reduced = product.add(temp.multiply(modulus)).shiftRight(reducerBits);
        BigInteger result = reduced.compareTo(modulus) < 0 ? reduced : reduced.subtract(modulus);

        assert result.signum() >= 0 && result.compareTo(modulus) < 0;
        return result;
    }

    public BigInteger multiplyWithConvert(BigInteger first, BigInteger second) {
        final BigInteger convertedFirst = toMontgomeryForm(first);
        final BigInteger convertedSecond = toMontgomeryForm(second);
        final BigInteger result = multiply(convertedFirst, convertedSecond);
        return fromMontgomeryForm(result);
    }

    public BigInteger pow(BigInteger number, BigInteger exponent) {
        assert number.signum() >= 0 && number.compareTo(modulus) < 0;
        if (exponent.signum() == -1) {
            throw new IllegalArgumentException("Negative exponent");
        }

        BigInteger result = convertedOne;
        for (int i = 0, len = exponent.bitLength(); i < len; i++) {
            if (exponent.testBit(i)) {
                result = multiply(result, number);
            }
            number = multiply(number, number);
        }
        return result;
    }

    public BigInteger powWithConvert(BigInteger number, BigInteger exponent) {
        final BigInteger convertedNumber = toMontgomeryForm(number);
        final BigInteger result = pow(convertedNumber, exponent);
        return fromMontgomeryForm(result);
    }

    // Must be an odd number at least 3
    public MontgomeryReducer(BigInteger modulus) {
        Objects.requireNonNull(modulus);
        if (!modulus.testBit(0) || modulus.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException("Modulus must be an odd number at least 3");
        }
        this.modulus = modulus;

        reducerBits = (modulus.bitLength() / 8 + 1) * 8;
        BigInteger reducer = BigInteger.ONE.shiftLeft(reducerBits);
        mask = reducer.subtract(BigInteger.ONE);

        assert reducer.compareTo(modulus) > 0 && reducer.gcd(modulus).equals(BigInteger.ONE);

        reciprocal = reducer.modInverse(modulus);
        factor = reducer.multiply(reciprocal).subtract(BigInteger.ONE).divide(modulus);
        convertedOne = reducer.mod(modulus);
    }


}