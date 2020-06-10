package org.afohtim.lab3;

import static java.lang.Integer.rotateLeft;
import java.util.Arrays;
import java.util.Objects;

class CompressIterator {
    int al, ar, bl, br, cl, cr, dl, dr, el, er;
    CompressIterator(int[] state) {
        al = state[0];
        ar = state[0];
        bl = state[1];
        br = state[1];
        cl = state[2];
        cr = state[2];
        dl = state[3];
        dr = state[3];
        el = state[4];
        er = state[4];
    }

    private static int f(int i, int x, int y, int z) {
        assert 0 <= i && i < 80;
        if (i < 16) {
            return x ^ y ^ z;
        }
        if (i < 32) {
            return (x & y) | (~x & z);
        }
        if (i < 48) {
            return (x | ~y) ^ z;
        }
        if (i < 64) {
            return (x & z) | (y & ~z);
        }
        return x ^ (y | ~z);
    }

    void iteration(int i, int j, int[] schedule) {
        int temp;
        temp = rotateLeft(al + f(j, bl, cl, dl) +
                schedule[Constants.RL[j]] + Constants.KL[j / 16], Constants.SL[j]) + el;
        al = el;
        el = dl;
        dl = rotateLeft(cl, 10);
        cl = bl;
        bl = temp;
        temp = rotateLeft(ar + f(79 - j, br, cr, dr) +
                schedule[Constants.RR[j]] + Constants.KR[j / 16], Constants.SR[j]) + er;
        ar = er;
        er = dr;
        dr = rotateLeft(cr, 10);
        cr = br;
        br = temp;
    }

    public int[] getState(int[] state) {
        int temp = state[1] + cl + dr;
        state[1] = state[2] + dl + er;
        state[2] = state[3] + el + ar;
        state[3] = state[4] + al + br;
        state[4] = state[0] + bl + cr;
        state[0] = temp;
        return state;
    }
}


public class Ripemd160 {
    private Ripemd160() {}

    static public byte[] getHash(byte[] message) {
        Objects.requireNonNull(message);
        int[] state = {
                0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0
        };

        int offset = message.length / Constants.BLOCK_SIZE * Constants.BLOCK_SIZE;
        compress(state, message, offset);

        byte[] block = new byte[Constants.BLOCK_SIZE];
        System.arraycopy(message, offset, block, 0, message.length - offset);
        offset = message.length % block.length;
        block[offset] = (byte) 0x80;
        offset++;
        if (offset + 8 > block.length) {
            compress(state, block, block.length);
            Arrays.fill(block, (byte) 0);
        }
        long size = (long) message.length << 3;
        for (int i = 0; i < 8; i++) {
            block[block.length - 8 + i] = (byte) (size >>> (i * 8));
        }
        compress(state, block, block.length);

        byte[] result = new byte[state.length * 4];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (state[i / 4] >>> (i % 4 * 8));
        }
        return result;
    }

    private static void compress(int[] state, byte[] blocks, int size) {
        if (size % Constants.BLOCK_SIZE != 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < size; i += Constants.BLOCK_SIZE) {
            int[] schedule = new int[16];
            for (int j = 0; j < Constants.BLOCK_SIZE; j++) {
                schedule[j / 4] |= (blocks[i + j] & 0xFF) << (j % 4 * 8);
            }

            CompressIterator compressIterator = new CompressIterator(state);
            for (int j = 0; j < 80; j++) {
                compressIterator.iteration(i, j, schedule);
            }
            state = compressIterator.getState(state);
        }
    }
}
