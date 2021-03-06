package org.afohtim.lab2;

public class IDEA {

    private static final int iterations = 8;

    private final int[] subKey;

    public IDEA (byte[] key, boolean encrypt) {
        int[] tempSubKey = expandUserKey(key);
        if (encrypt) {
            subKey = tempSubKey;
        }
        else {
            subKey = invertSubKey(tempSubKey);
        }
    }

    public IDEA (String charKey, boolean encrypt) {
        this(generateUserKeyFromCharKey(charKey), encrypt);
    }

    public void crypt (byte[] data) {
        crypt(data, 0);
    }

    public void crypt (byte[] data, int dataPos) {
        int x0 = ((data[dataPos + 0] & 0xFF) << 8) | (data[dataPos + 1] & 0xFF);
        int x1 = ((data[dataPos + 2] & 0xFF) << 8) | (data[dataPos + 3] & 0xFF);
        int x2 = ((data[dataPos + 4] & 0xFF) << 8) | (data[dataPos + 5] & 0xFF);
        int x3 = ((data[dataPos + 6] & 0xFF) << 8) | (data[dataPos + 7] & 0xFF);

        int currentKeyPosition = 0;
        for (int i = 0; i < iterations; i++) {
            int y0 = mul(x0, subKey[currentKeyPosition++]);
            int y1 = add(x1, subKey[currentKeyPosition++]);
            int y2 = add(x2, subKey[currentKeyPosition++]);
            int y3 = mul(x3, subKey[currentKeyPosition++]);

            int k1 = y0 ^ y2;
            int k2 = y1 ^ y3;

            int t0 = mul(k1, subKey[currentKeyPosition++]);
            int t1 = add(k2, t0);
            int t2 = mul(t1, subKey[currentKeyPosition++]);
            int t3 = add(t0, t2);

            x0 = y0 ^ t2;
            x1 = y2 ^ t2;
            x2 = y1 ^ t3;
            x3 = y3 ^ t3;
        }

        int r0 = mul(x0, subKey[currentKeyPosition++]);
        int r1 = add(x2, subKey[currentKeyPosition++]);
        int r2 = add(x1, subKey[currentKeyPosition++]);
        int r3 = mul(x3, subKey[currentKeyPosition++]);

        data[dataPos + 0] = (byte)(r0 >> 8);
        data[dataPos + 1] = (byte)r0;
        data[dataPos + 2] = (byte)(r1 >> 8);
        data[dataPos + 3] = (byte)r1;
        data[dataPos + 4] = (byte)(r2 >> 8);
        data[dataPos + 5] = (byte)r2;
        data[dataPos + 6] = (byte)(r3 >> 8);
        data[dataPos + 7] = (byte)r3;
    }

    private static int add (int a, int b) {
        return (a + b) & 0xFFFF;
    }

    private static int addInv (int x) {
        return (0x10000 - x) & 0xFFFF;
    }

    private static int mul (int a, int b ) {
        long r = (long)a * b;
        if (r != 0) {
            return (int)(r % 0x10001) & 0xFFFF;
        }
        else {
            return (1 - a - b) & 0xFFFF;
        }
    }

    private static int mulInv (int x) {
        if (x <= 1) {
            return x;
        }
        int y = 0x10001;
        int t0 = 1;
        int t1 = 0;
        while (true) {
            t1 += y / x * t0;
            y %= x;
            if (y == 1) {
                return 0x10001 - t1; }
            t0 += x / y * t1;
            x %= y;
            if (x == 1) {
                return t0; }
        }
    }

    private static int[] invertSubKey (int[] key) {
        int[] invKey = new int[key.length];
        int p = 0;
        int i = iterations * 6;
        invKey[i + 0] = mulInv(key[p++]);
        invKey[i + 1] = addInv(key[p++]);
        invKey[i + 2] = addInv(key[p++]);
        invKey[i + 3] = mulInv(key[p++]);
        for (int r = iterations - 1; r >= 0; r--) {
            i = r * 6;
            int m = r > 0 ? 2 : 1;
            int n = r > 0 ? 1 : 2;
            invKey[i + 4] =        key[p++];
            invKey[i + 5] =        key[p++];
            invKey[i + 0] = mulInv(key[p++]);
            invKey[i + m] = addInv(key[p++]);
            invKey[i + n] = addInv(key[p++]);
            invKey[i + 3] = mulInv(key[p++]);
        }
        return invKey;
    }

    private static int[] expandUserKey (byte[] userKey) {
        if (userKey.length != 16) {
            throw new IllegalArgumentException();
        }
        int[] key = new int[iterations * 6 + 4];
        for (int i = 0; i < userKey.length / 2; i++) {
            key[i] = ((userKey[2 * i] & 0xFF) << 8) | (userKey[2 * i + 1] & 0xFF);
        }
        for (int i = userKey.length / 2; i < key.length; i++) {
            key[i] = ((key[(i + 1) % 8 != 0 ? i - 7 : i - 15] << 9) | (key[(i + 2) % 8 < 2 ? i - 14 : i - 6] >> 7)) & 0xFFFF;
        }
        return key;
    }
    private static byte[] generateUserKeyFromCharKey (String charKey) {
        final int minChar = 0x21;
        final int maxChar = 0x7E;
        final int nofChar = maxChar - minChar + 1;    // Number of different valid characters
        int[] a = new int[8];
        for (int p = 0; p < charKey.length(); p++) {
            int c = charKey.charAt(p);
            if (c < minChar || c > maxChar) {
                throw new IllegalArgumentException("Wrong character in key string.");
            }
            int val = c - minChar;
            for (int i = a.length - 1; i >= 0; i--) {
                val += a[i] * nofChar;
                a[i] = val & 0xFFFF;
                val >>= 16;
            }
        }
        byte[] key = new byte[16];
        for (int i = 0; i < 8; i++) {
            key[i * 2] = (byte)(a[i] >> 8);
            key[i * 2 + 1] = (byte)a[i];
        }
        return key;
    }
}