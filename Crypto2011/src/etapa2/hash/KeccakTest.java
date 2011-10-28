package etapa2.hash;

import java.util.Arrays;

public class KeccakTest {

    public static void displayBuf(String tag, byte[] z) {
        System.out.print(tag + " = ");
        String hex = "0123456789ABCDEF";
        for (int i = 0; i < z.length; i++) {
            System.out.print(hex.charAt((z[i] >> 4) & 0xf));
            System.out.print(hex.charAt((z[i]     ) & 0xf));
        }
        System.out.println();
    }

    public static void testDuplexing(int hashbitlen, byte[] sigma, int sigmalen) {
        System.out.println("Keccak Duplexing Test @ hashbitlen = " + hashbitlen);
        Keccak k = new Keccak();
        k.init(hashbitlen);
        byte[] z = k.duplexing(sigma, sigmalen, null, 10);
        displayBuf("z", z);
    }

    public static void testHash(int hashbitlen, byte[] msg, int len) {
        System.out.println("Keccak Hash Test @ hashbitlen = " + hashbitlen);
        Keccak k = new Keccak();
        k.init(hashbitlen);
        System.out.println("bitrate     = " + k.getBitRate());
        System.out.println("capacity    = " + k.getCapacity());
        System.out.println("diversifier = " + k.diversifier);
        k.update(msg, len*8);
        byte[] h = k.getHash(null);
        System.out.println("Len = " + len);
        displayBuf("Msg", msg);
        displayBuf("MD", h);
        //displayBuf("DQ", k.dataQueue);
    }

    public static void main(String[] args) {
        //testHash(256, new byte[] {(byte)0x53, (byte)0x58, (byte)0x7B, (byte)0xC8}, 29);
        //testHash(256, new byte[] {0x00}, 0);
        byte[] msg = new byte[136];
        Arrays.fill(msg, (byte)0);
        msg[135] = (byte)0x81;
        displayBuf("M", msg);
        testHash(256, msg, msg.length);
        testDuplexing(256, msg, (msg.length-1));
    }
}
