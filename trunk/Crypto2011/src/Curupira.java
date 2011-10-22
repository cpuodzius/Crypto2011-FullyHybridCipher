package cifradorhibridocompleto;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Renan
 */
public class Curupira implements BlockCipher{
    
    private int rodadas;
    private byte[] K;
    
    boolean print = false;
//    private byte[] originalKey;
//    private ArrayList<byte[]> keys;

    public Curupira() {
        xtimes();
    }

    @Override
    public int blockBits() {
        return 96;
    }

    @Override
    public int keyBits() {
        return K.length*8;
    }

    @Override
    public void makeKey(byte[] cipherKey, int keyBits) {
        switch (keyBits){
            case 96:
                rodadas = 10;
                break;
            case 144:
                rodadas = 14;
                break;
            case 192:
                rodadas = 18;
                break;
            default:
                System.err.println("Curupira: Chave de tamanho errado");
                System.exit(-1);
        }
        K = cipherKey.clone();
//        originalKey = cipherKey.clone();
//        keys = new ArrayList<byte[]>();
    }

    @Override
    public void encrypt(byte[] mBlock, byte[] cBlock) {
        byte[] cBlock_ref = cBlock;
        if (cBlock_ref == null) cBlock_ref = new byte[mBlock.length];
        byte[] k;
        //whitening
        if (print) System.out.println("---------- Round " + 0 + " ----------");
        k = keySelection(K);
        if (print) CifradorHibridoCompleto.printMatrix(k);
        cBlock = sigma(mBlock, k);
        if (print) CifradorHibridoCompleto.printMatrix(cBlock);
//        keys.add(k);
        for (int r = 1; r < rodadas; r++){
            
            K = mi(csi(constant_addition(K, r)));
            k = keySelection(K);
//            keys.add(k);
            cBlock = sigma(theta(pi(gama(cBlock))), k);
            
            if (print) System.out.println("---------- Round " + r + " ----------");
            if (print) CifradorHibridoCompleto.printMatrix(cBlock);
        }
        
        K = mi(csi(constant_addition(K, rodadas)));
        k = keySelection(K);
        cBlock = sigma((pi(gama(cBlock))), k);
        
//        System.arraycopy(cBlock, 0, cBlock_ref, 0, cBlock.length);
        for (int i=0; i < cBlock.length; i++){
            cBlock_ref[i] = cBlock[i];
        }
        
        if (print) System.out.println("---------- Cipher text ----------");
        if (print) CifradorHibridoCompleto.printMatrix(cBlock);
    }
    
    @Override
    public void decrypt(byte[] cBlock, byte[] mBlock) {
        byte[] mBlock_ref = mBlock;
        if (mBlock_ref == null) mBlock_ref = new byte[cBlock.length];
        byte[] k;
        //whitening
        if (print) System.out.println("---------- Round " + 0 + " ----------");
        k = keySelection(K);
        if (print) CifradorHibridoCompleto.printMatrix(k);
        mBlock = sigma(cBlock, k);
        if (print) CifradorHibridoCompleto.printMatrix(mBlock);
        
        K = constant_addition(csi2(mi2(K)), rodadas);
        
        for (int r = 1; r < rodadas; r++){
//            k = theta(keys.get(rodadas-r));
            k = theta(keySelection(K));
            K = constant_addition(csi2(mi2(K)), rodadas - r);
            
            mBlock = sigma(theta(pi(gama(mBlock))), k);
            
            if (print) {
                System.out.println("---------- Round " + r + " ---------- key" + (rodadas - r));
                CifradorHibridoCompleto.printMatrix(k);
            }
        }
//        K = mi(csi(constant_addition(K, rodadas)));
        k = keySelection(K);
        mBlock = sigma((pi(gama(mBlock))), k);
        
        System.arraycopy(mBlock, 0, mBlock_ref, 0, mBlock.length);
        
        if (print) {
            System.out.println("---------- Cipher text ----------");
            CifradorHibridoCompleto.printMatrix(mBlock);
        }
    }

    @Override
    public void sct(byte[] cBlock, byte[] mBlock) {
        /* Qual será que é entrada e saída?
         * considerarei entrada cBloc e saída mBlock */
        byte[] mBlock_ref = mBlock;
        if (mBlock_ref == null) mBlock_ref = new byte[cBlock.length];
//        System.out.println("---------- sct - inicio ----------");
//        CifradorHibridoCompleto.printMatrix(cBlock);
        
        mBlock = cBlock.clone();

        for (int r = 1; r <= 4; r++){
//            System.out.println("---------- sct " + r + " ----------");
            mBlock = theta(pi(gama(mBlock)));
//            CifradorHibridoCompleto.printMatrix(mBlock);
        }
        
        for (int i = 0; i < cBlock.length; i++)
            mBlock_ref[i] = mBlock[i];
        
//        System.out.println("---------- sct saida ----------");
//        CifradorHibridoCompleto.printMatrix(mBlock);
    }
    
    byte[] c = {0, 1}; //
    byte[] m = {0x1, 0x4D}; // x8 + x4 + x3 + x + 1
    byte[] xtimes = new byte[256];
    
    void xtimes ( ){
        int d;
        for (int b = 0; b < 0x100; b++){
            d = b * 2;
            if (d >= 0x100)
                d = d ^ 0x14D;
            xtimes[b] = (byte)d;
        
        }
//        int teste = 0;
//        for (int i = a.length - 1; i>= 0; i--)
//            teste += Math.pow(256,i) * a[i]; 
//        teste = teste << 1;
//        return teste;
    }
    
    byte ctimes (byte u){
        int U = u & 0xFF;
        int resposta = u & 0xFF;
        resposta = ((xtimes[U] ^ U) & 0xFF);
        resposta = ((xtimes[resposta] ^ U) & 0xFF);
        resposta = (xtimes[resposta] & 0xFF);
        resposta = (xtimes[resposta] & 0xFF);
        return (byte) resposta;
    }
    
    
    byte[] P = { 0x3, 0xF, 0xE, 0x0, 0x5, 0x4, 0xB, 0xC, 0xD, 0xA, 0x9, 0x6, 0x7, 0x8, 0x2, 0x1};
    byte[] Q = { 0x9, 0xE, 0x5, 0x6, 0xA, 0x2, 0x3, 0xC, 0xF, 0x0, 0x4, 0xD, 0x7, 0xB, 0x1, 0x8};
    
    byte ÉsseDeU (byte u){
        byte uh, uh_linha, ul, ul_linha;
        
        uh = P[(u >>> 4) & 0xF];
        ul = Q[u & 0xF];
        
        uh_linha = Q[(uh & 0xC) ^ ( (ul >>> 2) & 3 )];
        ul_linha = P[((uh << 2) & 0xC) ^ (ul & 3)];
        
        uh = P[ (uh_linha & 0xC) ^ ( (ul_linha >>> 2) & 3 )];
        ul = Q[ ((uh_linha << 2) & 0xC) ^ (ul_linha & 3)];
                
        return (byte)((uh << 4) ^ ul);
    }
    /* funções que tratam dos dados supoes mapeamento por linhas */
    byte[] gama (byte [] a){
        if (a.length != 12){
            System.err.println("Curupira\t Problema na função gama: tamanho errado de a");
            return null;
        }
        byte[] b = new byte[12];
        for (int i = 0; i < 12; i++)
            b[i] = ÉsseDeU(a[i]);
//        System.out.println("gama");
//        CifradorHibridoCompleto.printMatrix(b);
        return b;
    }
    
    byte[] pi (byte [] a){
        if (a.length != 12){
            System.err.println("Curupira\t Problema na função pi: tamanho errado de a");
            return null;
        }
        byte[] b = new byte[12];
        for (int count = 0; count < 12; count++){
//            int i = count / 4;
//            int j = count % 4;
            int i = count % 3;
            int j = count / 3;
            int new_j = i ^ j;
            int new_count = i + new_j*3;
//            System.out.println(i + " " + j +"\t" + count + " " + new_count);
            /*
             * 0 1 2  3 
             * 4 5 6  7
             * 8 9 10 11
             */
            b[count] = a[new_count];
        }
        return b;
    }
    
    byte[] theta (byte[] a){
        byte[] b = new byte[a.length];
        byte[] coluna = new byte[3];
        
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 3; i++)
                coluna[i] = a[i + j*3];
            
//            System.out.println(coluna[0] + " " + coluna[1] + " " + coluna[2]);
            coluna = D_vezes_a(coluna);
//            System.out.println(coluna[0] + " " + coluna[1] + " " + coluna[2]);
            
            for (int i = 0; i < 3; i++){
                 b[i + j*3] = coluna[i];
            }
        }
        
        return b;
    }
    
    byte[] sigma(byte[] a, byte[] k){
        byte[] b = new byte[a.length];
        for (int i = 0; i < a.length; i++)
            b[i] = (byte) (a[i] ^ k[i]);
        return b;
    }
    
    byte[] D_vezes_a (byte [] a){
        byte [] b = new byte[3];
        byte v = xtimes[(short)(a[0] ^ a[1] ^ a[2]) & 0xFF];
        byte w = xtimes[(short)(v) & 0xFF];
        b[0] = (byte)(a[0] ^ v);
        b[1] = (byte)(a[1] ^ w);
        b[2] = (byte)(a[2] ^ v ^ w);
        return b;
    }
    
    byte[] E_vezes_a (byte [] a, boolean inversa){
        byte [] b = new byte[3];
        byte v = (byte)(a[0] ^ a[1] ^ a[2]);
        
        if (!inversa)
            v = (byte)(ctimes(v) ^ v);
        else
            v = ctimes(v);
                    
        for (int i = 0; i < 3; i++)
            b[i] = (byte)(a[i] ^ v);
        return b;
    }
    
    /* funções que tratam da chave supoe mapeamento por coluna */
    byte[] schedule_constants(int s, int t){
        byte[] q = new byte[t*6];
        if (s == 0){
            for (int count = 0; count < 6*t; count++){
//                int i = count % 3;
//                int j = count / 3;
//                if (i == 0){
//                    q[count] = ÉsseDeU( (byte) (0) );
//                }else{
                    q[count] = 0;
//                }
            }
        }else{
            for (int count = 0; count < 6*t; count++){
                int i = count % 3;
                int j = count / 3;
                if (i == 0){
                    q[count] = ÉsseDeU( (byte) (2*t*(s-1)+j) );
//                    System.out.println(String.format("%h",q[count] & 0xFF));
                }else{
                    q[count] = 0;
                }
            }
        }
        return q;
    }
    
    byte[] constant_addition(byte[] a, int s){
        byte[] b = schedule_constants(s, a.length/6);
//        CifradorHibridoCompleto.printMatrix(b);
        for (int i = 0; i < b.length; i++)
            b[i] =(byte)(b[i] ^ a[i]);
        
        return b;
    }
    
    byte[] csi(byte[] a){
        byte[] b = new byte[a.length];
        for (int count = 0; count < a.length; count++){
                int i = count % 3;
                int j = count / 3;
                if (i == 0){
//                    System.out.println("i==0 " + j );
                    b[count] = a[i + j*3];
                } else if (i == 1){
//                    System.out.println(count + " i==1 " + j + " " + (j+1)%(2*a.length/6) );
                    b[count] = a[i + 3*((j+1)%(2*a.length/6))];
                } else if (i == 2){
//                    System.out.println(count + " i==2 " + j + " " + (j-1)%(2*a.length/6) );
                    int new_j = ((j-1)%(2*a.length/6));
                    if (new_j < 0 ) new_j += 2*a.length/6;
                    b[count] = a[i + 3*new_j];
                } else {
                    System.out.println("mas hein??");
                }
                
            }
        return b;
    }
    
    byte[] csi2(byte[] a){
        byte[] b = new byte[a.length];
        for (int count = 0; count < a.length; count++){
            int i = count % 3;
            int j = count / 3;
            if (i == 0){
                //                    System.out.println("i==0 " + j );
                b[count] = a[i + j*3];
            } else if (i == 1){
                //                    System.out.println(count + " i==1 " + j + " " + (j+1)%(2*a.length/6) );
                int new_j = ((j-1)%(2*a.length/6));
                if (new_j < 0 ) new_j += 2*a.length/6;
                b[count] = a[i + 3*new_j];
            } else if (i == 2){
                //                    System.out.println(count + " i==2 " + j + " " + (j-1)%(2*a.length/6) );
                
                
                b[count] = a[i + 3*((j+1)%(2*a.length/6))];
            } else {
                System.out.println("mas hein??");
            }
            
        }
        return b;
    }
    
    byte[] mi(byte[] a){
        byte[] b = new byte[a.length];
        byte[] coluna = new byte[3];
        int t = a.length/6;
        
        for (int j = 0; j < 2*t; j++){
            for (int i = 0; i < 3; i++)
                coluna[i] = a[i + j*3];
            
//            System.out.println(String.format("antes  %3h %3h %3h" , coluna[0] & 0xFF ,coluna[1] & 0xFF, coluna[2]& 0xFF));
            coluna = E_vezes_a(coluna, true);
//            System.out.println(String.format("depois %3h %3h %3h" , coluna[0] & 0xFF ,coluna[1] & 0xFF, coluna[2]& 0xFF));
            
            for (int i = 0; i < 3; i++)
                 b[i + j*3] = coluna[i];
        }
        
        return b;
    }
    
    byte[] mi2(byte[] a){
        byte[] b = new byte[a.length];
        byte[] coluna = new byte[3];
        int t = a.length/6;
        
        for (int j = 0; j < 2*t; j++){
            for (int i = 0; i < 3; i++)
                coluna[i] = a[i + j*3];
            
//            System.out.println(String.format("antes  %3h %3h %3h" , coluna[0] & 0xFF ,coluna[1] & 0xFF, coluna[2]& 0xFF));
            coluna = E_vezes_a(coluna, false);
//            System.out.println(String.format("depois %3h %3h %3h" , coluna[0] & 0xFF ,coluna[1] & 0xFF, coluna[2]& 0xFF));
            
            for (int i = 0; i < 3; i++)
                 b[i + j*3] = coluna[i];
        }
        
        return b;
    }
    
//    byte[] whitening(byte[] a){
//        byte[] b = new byte[a.length];
//        for (int count = 0; count < a.length; count++){
//            int i = count % 3;
//            int j = count / 3;
//            if (i == 0){
//                b[count] = ÉsseDeU(a[i + j*3]);
//            } else if (i == 1 || i == 2){
//                b[count] = a[i + 3*j];
//            } else {
//                System.out.println("mas hein?? white");
//            }
//        }
//        return b;
//    }
    
    byte[] keySelection (byte[] K){
        byte[] k = new byte[12];
        for (int count = 0; count < 12; count++){
            int i = count % 3;
            int j = count / 3;
            if (i == 0)
                k[count] = ÉsseDeU(K[count]);
            else
                k[count] = K[count];
        }
        return k;
    }
    
//     byte[] omega(byte[] a){
//         return mi(csi(a));
//     }
//     
//     byte[] Q(int s, int t){
//         byte[] b = new byte[t*6];
//         byte[] temp = new byte[t*6];
//         for (int i = 0; i < t*2; i++)
//             b[i] = 0;
//         
//         for (int i = 0; i < s; i++){
//             temp = omega(schedule_constants(i, t));
//             for (int j = 0; j < s - i + 1; j ++){
//                 temp = omega(temp);
//             }
//             for (int j = 0; j < t*2; j++)
//                b[j] = (byte) (b[j] ^ temp[j]);
//         }
//         return b;
//     }
//     
//     byte[] psi (byte[] K, int r){
//         byte[] temp = new byte[ K.length];
//         byte[] K_r = new byte[ K.length];
//         
//         temp = omega(K);
//         for (int j = 0; j < r; j ++){
//             temp = omega(temp);
//         }
//         
//         K_r = temp.clone();
//         
//         temp = Q(r, K.length/6);
//         for (int j = 0; j < K.length; j++)
//                K_r[j] = (byte) (K_r[j] ^ temp[j]);
//         
//         return K_r;

    //     }
    public static void main(String[] args){
        Curupira curu = new Curupira();
        
        for (int i = 0; i< 0x100; i++)
            System.out.println(String.format(i + " %d", curu.ÉsseDeU((byte)i) & 0xFF ) );
    }

}

