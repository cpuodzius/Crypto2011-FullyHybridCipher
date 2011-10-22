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
public class LetterSoup implements AEAD{
    
    MAC mac;
    BlockCipher cipher;
    byte[] iv_nounce;
    int ivLength;
    byte[] O;
    byte[] K;
    byte[] R;
    byte[] H;
    int keyBits;
    ArrayList<byte[]> C;
    ArrayList<byte[]> M;
    byte[] Marvin_passos_3_a_5;
    int headerBits;
    
    boolean print = false;
    
    @Override
    public void setMAC(MAC mac) {
        this.mac = mac;
    }
    
    @Override
    public void setCipher(BlockCipher cipher) {
        this.cipher = cipher;
        mac.setCipher(cipher);
    }
    
    @Override
    public void setKey(byte[] cipherKey, int keyBits) {
        K = cipherKey;
        this.keyBits = keyBits;
        cipher.makeKey(cipherKey, keyBits);
        mac.setKey(cipherKey, keyBits);
    }
    
    @Override
    public void setIV(byte[] iv, int ivLength) {
        this.iv_nounce = iv.clone();
        byte[] iv_encripted = new byte[cipher.blockBits()/8];
        cipher.encrypt(lpad(iv), iv_encripted);
        R = XOR (iv_encripted, lpad(iv));
//        mac.init(R);
        if (print) {
            System.out.println("setIV -- R\n");
            CifradorHibridoCompleto.printMatrix(R);
        }
        
        this.ivLength = ivLength;
    }
    
    @Override
    public void update(byte[] aData, int aLength) {
        if (print) System.out.println("------------ Soup Update -------------");
        this.headerBits = aLength*8;
        byte[] L = new byte[cipher.blockBits()/8];
        for (int i =0; i < L.length; i++)
            L[i] = 0;
        
        cipher.makeKey(K, keyBits);
        cipher.encrypt(L.clone(), L);
        mac.init(L);
        
        int num_bytes;
        byte[] Hi;
        
        for (int i = 0; i < aLength; i+= cipher.blockBits()/8){
            if (print) System.out.println("------------ " + i + " -------------");
            num_bytes = ((aLength - i > cipher.blockBits()/8)?(cipher.blockBits()/8):(aLength - i)) ;
            
            Hi = new byte[num_bytes];
            for (int j = 0; j < num_bytes; j++)
                Hi[j] = aData[i+j];
            
            mac.update(Hi, num_bytes);
//            CifradorHibridoCompleto.printMatrix(Hi);
        }
        
        if (mac instanceof Marvin){
//            mac.getTag(null, 96);
            Marvin_passos_3_a_5 = ((Marvin)mac).acum;
            if (print) {
                System.out.println("------------ Soup - marvin's acum");
                CifradorHibridoCompleto.printMatrix(Marvin_passos_3_a_5);
            }
        }
    }
    
    @Override
    public byte[] encrypt(byte[] mData, int mLength, byte[] cData) {
        if (cData == null) cData = new byte[mLength];
        
        mac.init(R);
        byte[] Mi;
        int num_bytes;
        
        this.O = R.clone();
        C = new ArrayList<byte[]>();
        
        for (int i = 0; i < mLength; i+= cipher.blockBits()/8){
            num_bytes = ((mLength - i > cipher.blockBits()/8)?(cipher.blockBits()/8):(mLength - i)) ;
            
            Mi = new byte[num_bytes];
            for (int j = 0; j < num_bytes; j++)
                Mi[j] = mData[i+j];
            LFSDFFRFVCSFS(Mi, num_bytes);
            
            mac.update(C.get(C.size() -1), num_bytes);
        }
        
        int k = 0;
        for (byte[] cBlock : C){
            num_bytes = ((mLength - k > cipher.blockBits()/8)?(cipher.blockBits()/8):(mLength - k)) ;
            for (int i = 0; i < num_bytes; i++)
                cData[k+i] = cBlock[i];
            k += cipher.blockBits()/8;
        }

        return cData;
    }
    
    void LFSDFFRFVCSFS (byte[] mData, int mLength){
        O = vezes_x_a_w(O);
        
        if (print) {
            System.out.println("LSoup -- O " + (C.size()+1) );
            CifradorHibridoCompleto.printMatrix(O);
        }
        
        byte[] temp = new byte[12];
        
        cipher.makeKey(K, keyBits);
        cipher.encrypt(O, temp);
        
        if (print) {
            System.out.println("LSoup -- E(O)" + (C.size()+1) );
            CifradorHibridoCompleto.printMatrix(temp);
        }
        
        if (temp.length > mLength){
            byte[] temp2 = new byte[mLength];
            for (int i = 0; i < mLength; i++)
                temp2[i] = temp[i];
            temp = temp2;
        }
        C.add(XOR(mData, temp));
        
//        temp = new byte[12];
//        for (int i =0; i < 12 ; i++){
//            if ( i < C.get(C.size()-1).length )
//                temp[i] = C.get(C.size()-1)[i];
//            else
//                temp[i] = 0;
//        }
//        
//        System.out.println("setIV -- C" + (C.size()) + " XOR O");
//        CifradorHibridoCompleto.printMatrix(XOR(temp, O));
        
        if (print) {
            System.out.println("LSoup -- C" + (C.size()));
            CifradorHibridoCompleto.printMatrix(C.get(C.size()-1));
        }
    }
    
    @Override
    public byte[] decrypt(byte[] cData, int cLength, byte[] mData) {
        if (mData == null) mData = new byte[cLength];
        
        mac.init(R);
        byte[] Ci;
        int num_bytes;
        M = new ArrayList<byte[]>();
        
        this.O = R.clone();
        for (int i = 0; i < cLength; i+= cipher.blockBits()/8){
            num_bytes = ((cLength - i > cipher.blockBits()/8)?(cipher.blockBits()/8):(cLength - i)) ;
            
            Ci = new byte[num_bytes];
            for (int j = 0; j < num_bytes; j++)
                Ci[j] = cData[i+j];
            LFSRC_decrypt(Ci, num_bytes);
            
            mac.update(Ci, num_bytes);
        }
        
        int k = 0;
        for (byte[] mBlock : M){
            num_bytes = ((cLength - k > cipher.blockBits()/8)?(cipher.blockBits()/8):(cLength - k)) ;
            for (int i = 0; i < num_bytes; i++)
                mData[k+i] = mBlock[i];
            k += cipher.blockBits()/8;
        }
        return mData;
    }
    
    void LFSRC_decrypt (byte[] cData, int cLength){
        O = vezes_x_a_w(O);
        
        if (print) {
            System.out.println("LSoup decrypt -- O " + (M.size()+1) );
            CifradorHibridoCompleto.printMatrix(O);
        }
        
        byte[] temp = new byte[12];
        
        cipher.makeKey(K, keyBits);
        cipher.encrypt(O, temp);
        
        if (print) {
            System.out.println("LSoup decrypt -- E(O)" + (M.size()+1) );
            CifradorHibridoCompleto.printMatrix(temp);
        }
        
        if (temp.length > cLength){
            byte[] temp2 = new byte[cLength];
            for (int i = 0; i < cLength; i++)
                temp2[i] = temp[i];
            temp = temp2;
        }
        M.add(XOR(cData, temp));
        
        if (print) {
            System.out.println("LSoup decrypt -- C" + (M.size()));
            CifradorHibridoCompleto.printMatrix(M.get(M.size()-1));
        }
    }
    
    @Override
    public byte[] getTag(byte[] tag, int tagBits) {
        if (Marvin_passos_3_a_5 == null)
            return mac.getTag(tag, tagBits);
        else{
            if (mac instanceof Marvin){
                byte[] temp1 = ((Marvin)mac).rpad( ((cipher.blockBits() - tagBits) << 1) + 1 );
                byte[] temp2 = lpad( headerBits );
                
                Marvin_passos_3_a_5 = XOR(Marvin_passos_3_a_5, temp1);
                Marvin_passos_3_a_5 = XOR(Marvin_passos_3_a_5, temp2);
                
//                cipher.sct(Marvin_passos_3_a_5.clone(), Marvin_passos_3_a_5);
                mac.getTag(tag, tagBits);
                
                byte[] A = new byte[cipher.blockBits()/8];
//                A = XOR(((Marvin)mac).acum, Marvin_passos_3_a_5);
                if (print) System.out.println("--------------- A -------------------");
                
                cipher.sct(Marvin_passos_3_a_5.clone(), Marvin_passos_3_a_5);
                A = ((Marvin)mac).acum;
                A = XOR(Marvin_passos_3_a_5, A.clone());
                cipher.makeKey(K, keyBits);
                cipher.encrypt(A.clone(), A);
                if (print) CifradorHibridoCompleto.printMatrix(A);
                if (tag == null) tag = new byte[tagBits/8];
                for (int i = 0; i < tagBits/8; i++)
                    tag[i] = A[i];
                
                return tag;
            }
            return null;
        }
    }
    
    static byte[] vezes_x_a_w(byte[] O) {
        return Marvin.vezes_x_a_w(O);
    }
    
    byte[] XOR(byte[] a, byte[] b) {
        return Marvin.XOR(a, b);
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
    
    byte[] lpad(byte[] c){
        int bytes = cipher.blockBits() / 8;
        byte[] b = new byte[bytes];
        for (int i = 0; i < bytes; i++){
            if (i < c.length)
                b[b.length -i -1]=c[c.length -i -1];
            else
                b[b.length -i -1]=0;
        }
        return b;
    }

}

