package org.afohtim.lab3;

import static org.junit.Assert.assertArrayEquals;
import junit.framework.TestCase;
import java.io.UnsupportedEncodingException;

public class Ripemd160Test extends TestCase {

    public void testGetHash() {
        String[][] testCases = {
                {"",
                        "9C1185A5C5E9FC54612808977EE8F548B2258D31"},
                {"a",
                        "0BDC9D2D256B3EE9DAAE347BE6F4DC835A467FFE"},
                {"abc",
                        "8EB208F7E05D987A9B044A8E98C6B087F15A0BFC"},
                {"abcdefghijklmnopqrstuvwxyz",
                        "F71C27109C692C1B56BBDCEB5B9D2865B3708DBC"},
                {"abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
                        "12A053384A9C0C88E405A06C27DCF49ADA62EB2B"},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                        "B0E20B6E3116640286ED3A87A5713079B21F5189"},
                {"12345678901234567890123456789012345678901234567890123456789012345678901234567890",
                        "9B752E45573D4B39F4DBD3323CAB82BF63326BFB"},
                {"FAm{MogSYSUV]{h# p>>h0`HQ\"o~X%x&&-U4\"8t[r5mankt;i{ivjLNO^ax^&>]ur_Xn?)I=CxztG*\\w^af@->6D9L",
                        "DCCF05E342F614A497C59D134AD6B7965868BEFD"},
                {"y~nOogR/ExXD>\"H^($,l]>%VsnxdD5d}vqxKr_[y._(p0=w-nn=i['K!p]b.m|;w4?nTU1w@c/9%8,I{$W|rpFxO\\Ba",
                        "6604F4E0C9F0AAC75ED8DDE8B1094A590CABF4FD"},
                {"4bgy=e?k8(9SJczbaRwI'.IBXJAT5]WO0QbfTU!BXA,LdYSI1DrI6;n Vn1D_.?W=<gOHw ifMOlbHeq1S!LeIh0I_&w",
                        "E25151CE3871FE6BA4DD28A9485B48F41E78C548"},
                {";/a7M/e\\`*@'_xA7]U}o#.\"[HL0m<TTO5-\\ XNGl8?g~`@O]bTm!t:|MG>G}#64gnq=FVzB$MEZW=.D:uoZ\\3:E~Z6Y'j&9duKTW24nvA2'.\"c7.kRn;Al3>}>khmFU>",
                        "1290BA8D7D327925426113B2D04512D75E4E207A"}
        };

        for (String[] testCase : testCases) {
            //assert true;
            assertArrayEquals(hexToBytes(testCase[1]), Ripemd160.getHash(asciiToBytes(testCase[0])));
        }
    }

    public static byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return bytes;
    }

    public static byte[] asciiToBytes(String ascii) {
        try {
            return ascii.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}