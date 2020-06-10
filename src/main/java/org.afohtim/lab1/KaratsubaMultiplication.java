package org.afohtim.lab1;

import java.math.BigInteger;
import java.net.FileNameMap;

public class KaratsubaMultiplication {
    private KaratsubaMultiplication() {}

    static private boolean isSingleValue(final BigInteger number) {
        return number.toString().length() == 1;
    }

    static private BigInteger addZerosToEnd(final BigInteger number, int count) {
        return new BigInteger(number.toString() + "0".repeat(Math.max(0, count)));
    }

    static private BigInteger getFirstHalf(final BigInteger number, int size) {
        if (number.toString().length() - size == 0) {
            return BigInteger.ZERO;
        }
        return new BigInteger(number.toString().substring(0, number.toString().length() - size));
    }

    static private BigInteger getSecondHalf(final BigInteger number, int size) {
        return new BigInteger(number.toString().substring(number.toString().length() - size));
    }

    static public BigInteger Multiply(final BigInteger first, final BigInteger second) {
        if (isSingleValue(first) && isSingleValue(second)) {
            return first.multiply(second);
        }

        final int firstDigitsCount = first.toString().length();
        final int secondDigitsCount = second.toString().length();

        final int minimumDigitsCount = Math.min(firstDigitsCount, secondDigitsCount);

        final int x = (minimumDigitsCount + 1) / 2;

        final BigInteger a = getFirstHalf(first, x);
        final BigInteger b = getSecondHalf(first, x);

        final BigInteger c = getFirstHalf(second, x);
        final BigInteger d = getSecondHalf(second, x);

        final BigInteger ac = Multiply(a, c);
        final BigInteger bd = Multiply(b, d);
        final BigInteger ad = Multiply(a, d);
        final BigInteger bc = Multiply(b, c);

        final BigInteger ad_plus_bc = ad.add(bc);
        final BigInteger acx2 = addZerosToEnd(ac, x * 2);
        final BigInteger ad_plus_bc_x = addZerosToEnd(ad_plus_bc, x);

        return acx2.add(ad_plus_bc_x).add(bd);
    }


}
