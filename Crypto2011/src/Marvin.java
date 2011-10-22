/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cifradorhibridocompleto;

import java.util.ArrayList;

/**
 *
 * @author Renan
 */
public class Marvin implements MAC{
    
    byte c  = 0x2A;
    private BlockCipher cipher;
    byte[] R;
    byte[] O;
    byte[] acum;
    int tamanho_mensagem;
    
    byte[] K;
    int keyBits;
    
    boolean print = false;
    
    ArrayList<byte[]> dês = new ArrayList<byte[]>();
    

    @Override
    public void setCipher(BlockCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public void setKey(byte[] cipherKey, int keyBits) {
        K = cipherKey.clone();
        this.keyBits = keyBits;
        cipher.makeKey(cipherKey, keyBits);
    }

    @Override
    public void init() {
        tamanho_mensagem = 0;
        
        R = new byte[cipher.blockBits()/8];
        cipher.encrypt(lpad(c),R);
        R = XOR(R, lpad(c));
        if (print) System.out.println("Marvin R");
        O = R.clone();
        acum = R.clone();
        if (print) CifradorHibridoCompleto.printMatrix(R);
        
//        System.out.println("Marvin O");
//        for (int i = 0; i<4; i++){
//            System.out.println(i+1);
//            R = vezes_x_a_w(R);
//            CifradorHibridoCompleto.printMatrix(R);
//        }
    }

    @Override
    public void init(byte[] R) {
        tamanho_mensagem = 0;
        
        this.R = R.clone();
        O = R.clone();
        acum = R.clone();
    }

    @Override
    public void update(byte[] aData, int aLength) {
        tamanho_mensagem += aLength*8;
        byte[] A = new byte[12];
        if (aLength < cipher.blockBits()/8)
            aData = rpad(aData /*, aLength*/);
        O = vezes_x_a_w(O);
        
//        System.out.println("antes sct" );
//            CifradorHibridoCompleto.printMatrix(aData);
        
        aData = XOR(O, aData);
//        System.out.println("antes sct" );
//            CifradorHibridoCompleto.printMatrix(aData);
        
        cipher.sct(aData, A);
        
        acum = XOR(acum, A);
        
//        if (dês.isEmpty())
//            dês.add(acum.clone());
//
//        dês.add(aData.clone());
        
        int index = (tamanho_mensagem/cipher.blockBits() + ((tamanho_mensagem%cipher.blockBits()!=0)?1:0));
        if (print) {
            System.out.println("Marvin - A" + index);
            CifradorHibridoCompleto.printMatrix(A);
            System.out.println("Marvin - O" + index);
            CifradorHibridoCompleto.printMatrix(O);
            System.out.println("Marvin - acum" + index);
            CifradorHibridoCompleto.printMatrix(acum);
//            System.out.println("Será que é o D? " + index);
//            CifradorHibridoCompleto.printMatrix(aData);
        } 
//        byte [] xor = dês.get(0);
//        for (int i = 1; i < dês.size(); i++)
//            xor = XOR(xor,dês.get(i));
//        CifradorHibridoCompleto.printMatrix(xor);
    }

    @Override
    public byte[] getTag(byte[] tag, int tagBits) {
        byte[] temp1 = rpad( ((cipher.blockBits() - tagBits) << 1) + 1 );
//        byte[] temp1 = rpad( ((cipher.blockBits() - tagBits))  );
//        temp1 = XOR(temp1, temp1);
//        temp1[0] = (byte)((byte)Integer.parseInt("01000001",2));
//        temp1[1] = (byte)Integer.parseInt("10000000",2);
        byte[] temp2 = lpad( tamanho_mensagem );
        if (print) {
            System.out.println(cipher.blockBits());
            System.out.println(tagBits);
            System.out.println( ((cipher.blockBits() - tagBits) << 1) +1 );
            System.out.println();
            System.out.println("[tag] Marvin - temp1 " + Integer.toHexString((cipher.blockBits() - tagBits) << 1 + 1));
            CifradorHibridoCompleto.printMatrix(temp1);
            System.out.println("[tag] Marvin - temp2");
            CifradorHibridoCompleto.printMatrix(temp2);
        }
        acum = XOR(acum, temp1);
        acum = XOR(acum, temp2);
        if (print) {
            System.out.println("[tag] Marvin - acum");
            CifradorHibridoCompleto.printMatrix(acum);
        }
        byte[] cBlock = new byte[12];
        cipher.makeKey(K, keyBits);
        cipher.encrypt(acum, cBlock);
        if (print) CifradorHibridoCompleto.printMatrix(cBlock);
        
        //corta tag do tamanho de tagBits
        if (tag == null || tag.length < tagBits/8) tag = new byte[tagBits/8];
        for (int i = 0; i < tagBits / 8; i++){
            tag [i] = cBlock[i];
        }
        
        return tag;
        
    }
    
    byte[] lpad(byte c){
        int bits = cipher.blockBits() / 8;
        byte[] b = new byte[bits];
        for (int i = 0; i<bits; i++){
            b[i]=0;
        }
        b[bits - 1] = c;
        return b;
    }
    
    byte[] lpad(int c){
        int bits = cipher.blockBits() / 8;
        byte[] b = new byte[bits];
        for (int i = 0; i<bits; i++){
            b[i]=0;
        }
        int i = bits - 1;
        while (c > 0){
            b[i--] = (byte) (c%0x100);
            c = (c/0x100);
        }
        return b;
    }
    
    byte[] rpad(byte[] c){
        int bytes = cipher.blockBits() / 8;
        byte[] b = new byte[bytes];
        for (int i = 0; i < bytes; i++){
            if (i < c.length)
                b[i]=c[i];
            else
                b[i]=0;
        }
        return b;
    }
    
    byte[] rpad(int valor){
        byte[] b;
        String b_str;
        if (valor == 1){
            b = new byte[] {(byte)0x80};
            return rpad(b);
        }else if (valor < 0x100){
            b_str = Integer.toBinaryString(valor);
            if (print) System.out.println(b_str);
            while (b_str.length() < 8){
                b_str = b_str + '0';
            }
            if (print) System.out.println(b_str);
            b = new byte[] {(byte)Integer.parseInt(b_str,2)};
            if (print) System.out.println(Integer.toBinaryString(b[0]));
//            b = new byte[] {(byte)((valor-1)/2),(byte)0x80};
            return rpad(b);
        }else{
            if (print) System.out.println("aqui");
            System.exit(-1);
            b_str = Integer.toBinaryString(valor);
            b = new byte[b_str.length()/8 + ((b_str.length() % 8 == 0)?0:1) ];
            String temp;
            int i = 0;
            if (print) System.out.println(b_str);
            while (b_str.length() > 0){
                if (b_str.length() >= 8)
                    temp = b_str.substring(0, 8);
                else
                    temp = b_str;
                b_str = b_str.substring(8);
                
                while (temp.length() < 8){
                    temp = temp + '0';
                }
                b[i++] = (byte)Integer.parseInt(temp,2);
            }
            return rpad(b);
        }
    }
    
    static byte[] XOR(byte[] a1, byte[] a2){
        byte[] b;
        if (a1.length == a2.length){
            b = new byte[a1.length];
        }else{
            System.out.println("XOR: tamanhos diferentes");
            b = new byte[1];
            try {
                throw new Exception("XOR: tamanhos diferentes");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
        
        for (int i = 0; i < a1.length; i++)
            b[i] = (byte)(a1[i] ^ a2[i]);
        
        return b;
    }
    
    static byte[] vezes_x_a_w(byte[] V){
        int w = 8, k3 = 16, k2 = 13, k1 = 11;
        byte[] R;
        byte b;
//        System.out.println('-');
        R = XOR(V,V);
//        CifradorHibridoCompleto.printMatrix(V);
        V = rotl_w(V);
        
//        System.out.println(k3%12);
//        System.out.println(k2%12);
//        System.out.println(k1%12);
        
//        CifradorHibridoCompleto.printMatrix(V);
        b = V[V.length-1];
//        V[V.length-1] = (byte)0x80;
//        R[0] = V[0];
        
        /* K3 */
        int words = Math.round(k3/8);
        int binary_shifts = k3%8;
        int value = V[V.length-1] & 0xFF;
        int upper, lower;
        
        if (binary_shifts != 0){
            value = value << binary_shifts;
        }
        words++;
        
        upper = (value / 0x100) & 0xFF;
        lower = (value % 0x100) & 0xFF;
        
//        System.out.println(Integer.toBinaryString(value));
//        System.out.println(Integer.toHexString(upper) + " " + Integer.toHexString(lower));
        
        R[11-words] = (byte)(R[11-words] ^ upper);
        R[11-words+1] = (byte)(R[11-words+1] ^ lower);
        
        /* K2 */
        words = k2/8;
        binary_shifts = k2%8;
        value = V[V.length-1] & 0xFF;
        
        if (binary_shifts != 0){
            value = value << binary_shifts;
        }
        words++;
        
        upper = (value / 0x100) & 0xFF;
        lower = (value % 0x100) & 0xFF;
        
//        System.out.println(Integer.toBinaryString(value));
//        System.out.println(Integer.toHexString(upper) + " " + Integer.toHexString(lower));
//        System.out.println(words);
//        System.out.println(binary_shifts);
        
        R[11-words] = (byte)(R[11-words] ^ upper);
        R[11-words+1] = (byte)(R[11-words+1] ^ lower);
        
        /* K1 */
        words = k1/8;
        binary_shifts = k1%8;
        value = V[V.length-1] & 0xFF;
        
        if (binary_shifts != 0){
            value = value << binary_shifts;
        }
        words++;
        
        upper = (value / 0x100) & 0xFF;
        lower = (value % 0x100) & 0xFF;
//        System.out.println(Integer.toBinaryString(value));
//        System.out.println(Integer.toHexString(upper) + " " + Integer.toHexString(lower));
        
        R[11-words] = (byte)(R[11-words] ^ upper);
        R[11-words+1] = (byte)(R[11-words+1] ^ lower);
        
//        CifradorHibridoCompleto.printMatrix(R);
//        (byte)( b  /*<< k3*/);
        
//        R[k2%12] = (byte)( b /*<< k2*/);
        
//        R[k1] = (byte)( b /*<< k1*/);
//        V = XOR (V, R);
//        V = XOR (V, R);
        V = XOR (V, R);
        
        return V;
    }

    static byte[] rotl_w(byte[] V) {
        byte[] rot = new byte[V.length];
        for (int i = 0; i< V.length-1; i++){
            rot[i] = V[i+1];
        }
        rot[V.length-1] = V[0];
        
        return rot;
    }

//    private byte[] shiftleft(byte[] R, int k3) {
//        byte[] shifted = R.clone();
//        int words = k3/8;
//        int binary_shifts = k3%8;
//        for (int i = 0; i < words; i++)
//            shifted = rotl_w(shifted);
//    }
    
}
