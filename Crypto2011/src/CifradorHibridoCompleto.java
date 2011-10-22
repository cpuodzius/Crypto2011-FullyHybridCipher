/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cifradorhibridocompleto;

import java.io.*;
import java.util.Arrays;

/**
 *
 * @author Renan
 */
public class CifradorHibridoCompleto {

//    public static void main(String[] args) {
    public static void testes() {
        // code application logic here
        Curupira curu = new Curupira();
        
        byte a = 0x01;
        
        int j =0;
        for (int i : curu.xtimes){
            //System.out.println(j++ + " " + Integer.toHexString(i));
        }
//        System.out.println(curu.xtimes);
//        byte b = (byte)0xFF;
//        System.out.println(Integer.toBinaryString(b&0xff));
        
        //byte[] pi = {0, 1, 2, 3 ,4 ,5 ,6, 7, 8, 9, 10 ,11};
        byte[] pi = {0, 4, 8, 1 ,5 ,9 ,2, 6, 10, 3, 7 ,11};
        pi = curu.pi(pi);
//        pi = curu.pi(pi);
//        System.out.println("teste pi");
//        printMatrix(pi, false);
        
        int cu = curu.ctimes( (byte)1 );
//        System.out.println( '0' + Integer.toBinaryString(cu) );
        cu = curu.ctimes( (byte)2 );
//        System.out.println( Integer.toBinaryString(cu) );
        cu = curu.ctimes( (byte)3 );
//        System.out.println( Integer.toBinaryString(cu) );
        
//        System.out.println();
//        System.out.println( Integer.toBinaryString((byte)0x80 & 0xFF) );
        
//        byte[] teta = {1,0,0,0,1,0,0,0,1,0,0,0};
//        printMatrix(teta);
//        teta = curu.theta(teta);
//        printMatrix(teta);
        
        System.out.println();
        System.out.println("teste csi");
        byte[] csi = {0, 4, 8, 1 ,5 ,9 ,2, 6, 10, 3, 7 ,11};
        csi = curu.csi(csi);
        printMatrix(csi);
        
        System.out.println();
        System.out.println("teste mi");
        byte[] mi = {0, 4, 8, 1 ,5 ,9 ,2, 6, 10, 3, 7 ,11};
        mi = curu.mi(mi);
        printMatrix(mi);
//        
//        System.out.println();
//        System.out.println("teste mi-csi");
//        byte[] mi_csi = {0, 4, 8, 1 ,5 ,9 ,2, 6, 10, 3, 7 ,11};
//        mi = curu.mi(curu.csi(curu.mi(curu.csi(mi_csi))));
//        printMatrix(mi);
        
        System.out.println();
        System.out.println("teste key");
        System.out.println();
        
        byte[] teste = {0,0,0,0,0,0,0,0,0,0,0,0};
        //teste = new byte[] {0x23,(byte)0x87,(byte)0xF9,0x64,0x4F,(byte)0xBB,0x17,(byte)0x86,0x32,(byte)0xA6,(byte)0xB2,0xA};
        printMatrix(teste);
//        teste = curu.constant_addition(teste, 0);
//        teste = curu.whitening(teste);
        System.out.println("-------------- key " + 0 + "--------------");
        printMatrix(curu.keySelection(teste));
        for (int i = 1; i < 11; i++){
            byte[] teste2;
            System.out.println("-------------- key " + i + "--------------");
//            printMatrix(teste, false);
            System.out.println("-------------- constant_addition");
            teste = curu.constant_addition(teste, i);
            printMatrix(teste, false);
            
            System.out.println("-------------- função csi");
            teste = curu.csi(teste);
            printMatrix(teste, false);
            
            System.out.println("-------------- função mi");
            teste = curu.mi(teste);
            printMatrix(teste);
            System.out.println("-------------- key selection");
            printMatrix(curu.keySelection(teste));
            
            teste2 = teste;
//            System.out.println("-------------- função mi2");
            teste2 = curu.mi2(teste2);
//            printMatrix(teste2);
            
//            System.out.println("-------------- função csi");
            teste2 = curu.csi2(teste2);
//            printMatrix(teste2);
            
//            System.out.println("-------------- constant_addition");
            teste2 = curu.constant_addition(teste2, i);
//            printMatrix(teste2);
        }
        
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
        byte[] chave = {0x23,(byte)0x87,(byte)0xF9,0x64,0x4f,(byte)0xbb,0x17,(byte)0x86,0x32,(byte)0xa6,(byte)0xb2,0x0a,0x23,(byte)0x87,(byte)0xF9,0x64,0x4f,(byte)0xbb,0x17,(byte)0x86,0x32,(byte)0xa6,(byte)0xb2,0x0a};
        byte[] teste3 = {(byte) 0xa5,0x28,0x09,(byte)0xdd,0x53,(byte)0xd0,(byte)0xab,0x57,0x64,(byte)0x9d,(byte)0x9a,(byte)0xec};
        curu.makeKey(chave, 96);
        curu.encrypt(teste3, teste);
        printMatrix(teste);
        curu.decrypt(teste, teste3);
//        printMatrix(curu.psi(teste2,2), false);
//        System.out.print( String.format("%2h ", curu.ÉsseDeU((byte)teste[0]) & 0xFF) );
//        System.out.print( String.format("%2h ", curu.ÉsseDeU((byte)teste[3]) & 0xFF));
//        System.out.print( String.format("%2h ", curu.ÉsseDeU((byte)teste[6]) & 0xFF));
//        System.out.print( String.format("%2h ", curu.ÉsseDeU((byte)teste[9]) & 0xFF));
        
        System.out.println("\n");
        byte[] renan = new byte[2];
        byte[] renan2 = renan;
        renan[0] = 0x70;
        System.out.println(renan[0]);
        System.out.println(renan2[0]);
//        alter(renan);
        System.out.println(renan[0]);
        System.out.println(renan2[0]);
        
        System.out.println("----- Marvin -----");
        Marvin marv = new Marvin();
        curu = new Curupira();
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
        marv.setCipher(curu);
        marv.setKey(teste, 96);
        marv.init();
        byte[] aData = new byte[] {
            0x0, 0x0, 0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0, 0x0
//            ,0x0, 0x0, 0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0, 0x1 
//            ,0x2, 0x3, 0x4,  0x5,  0x6,  0x7,  0x8,  0x9,  0xa,  0xb,  0xc, 0xd
//            ,0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17
        };
        marv.update(aData, aData.length);
        aData[11] = 1;
        marv.update(aData, aData.length);
        aData = new byte[] {0x2, 0x3, 0x4,  0x5,  0x6,  0x7,  0x8,  0x9,  0xa,  0xb,  0xc, 0xd};
        marv.update(aData, aData.length);
        aData = new byte[] {0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
        marv.update(aData, aData.length);
        marv.getTag(aData, 12*8) ;
        
        marv = new Marvin();
        curu = new Curupira();
        marv.setCipher(curu);
        teste = new byte[] {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c};
        marv.setKey(teste, 96);
        marv.init();
        aData = new byte[] {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c};
        marv.update(aData, aData.length);
        aData = new byte[] {0x0d,0x0e,0x0f,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18};
        marv.update(aData, aData.length);
        marv.getTag(aData, 8*8);
        
        LetterSoup soup = new LetterSoup();
        marv = new Marvin();
        curu = new Curupira();
        soup.setMAC(marv);
        soup.setCipher(curu);
        
        
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
//        teste = new byte[] {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c};
        soup.setKey(teste, teste.length * 8);
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
        teste[11] = (byte)0x2A;
        soup.setIV(teste, teste.length);
        
        aData = new byte[] {0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,
                            0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,  0x0,0x0,0x0       };
        byte[] cData = new byte[aData.length];
        soup.encrypt(aData, aData.length, cData);
        
        
        byte[] tag = new byte[] {0,0,0,0, 0,0,0,0, 0,0,0,0};
        tag = soup.getTag(tag, tag.length*8);
        
        System.out.print("Ciphertext:");
        for (byte b : cData)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        for (byte b : tag)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        teste = new byte[cData.length];
        soup.decrypt(cData, aData.length, teste);
        
        tag = new byte[] {0,0,0,0, 0,0,0,0, 0,0,0,0};
        tag = soup.getTag(tag, tag.length*8);
        
        
        System.out.print("Ciphertext:");
        for (byte b : cData)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("Plaintext:");
        for (byte b : teste)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        for (byte b : tag)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        soup = new LetterSoup();
        marv = new Marvin();
        curu = new Curupira();
        soup.setMAC(marv);
        soup.setCipher(curu);
        
        byte[] header ={0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                        0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10,
                        0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};
        
        aData = new byte[] {0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,
                            0x0,0x0,0x0,0x0,  0x0,0x0,0x0,0x0,  0x0,0x0,0x0       };
        
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
        soup.setKey(teste, teste.length * 8);
        
        teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0x0};
        soup.setIV(teste, teste.length);
        
        soup.update(header, header.length);
        
        System.out.println("Encrypt");
        cData = new byte[aData.length];
        soup.encrypt(aData, aData.length, cData);
        
        tag = new byte[] {0,0,0,0, 0,0,0,0, 0,0,0,0};
        tag = soup.getTag(tag, tag.length*8);
        
        
        soup.update(header, header.length);
        
        teste = new byte[cData.length];
        soup.decrypt(cData, aData.length, teste);
        
        byte[] tag_2 = new byte[] {0,0,0,0, 0,0,0,0, 0,0,0,0};
        tag = soup.getTag(tag_2, tag_2.length*8);
        
        
        System.out.print("Ciphertext:");
        for (byte b : cData)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("tag1:");
        for (byte b : tag)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("Plaintext:");
        for (byte b : teste)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("tag2:");
        for (byte b : tag_2)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        //-------------------------- teste Soup 2 --------------------------------
        System.out.print("- teste Soup 2 -");
        soup = new LetterSoup();
        marv = new Marvin();
        curu = new Curupira();
        soup.setMAC(marv);
        soup.setCipher(curu);
        
        header = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                            0x09, 0x0a, 0x0b, 0x0c, 0x0d/*, 0x0e, 0x0f, 0x10,
                            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17*/};
        
        aData = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                            0x09, 0x0a, 0x0b, 0x0c, 0x0d};
        
        teste = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};
        soup.setKey(teste, teste.length * 8);
        
        teste = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};
        soup.setIV(teste, teste.length);
        
        soup.update(header, header.length);
        
        System.out.println("Encrypt");
        cData = new byte[aData.length];
        soup.encrypt(aData, aData.length, cData);
        
        tag = new byte[] {  //0,0,0,0,
                            //0,0,0,0,
                            0,0,0,0};
        tag = soup.getTag(tag, tag.length*8);
        
        
        soup.update(header, header.length);
        
        teste = new byte[cData.length];
        soup.decrypt(cData, aData.length, teste);
        
        tag_2 = new byte[] {//0,0,0,0,
                            //0,0,0,0,
                            0,0,0,0};
        tag = soup.getTag(tag_2, tag_2.length*8);
        
        
        System.out.print("Ciphertext:");
        for (byte b : cData)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("tag1:");
        for (byte b : tag)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("Plaintext:");
        for (byte b : teste)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        
        System.out.print("tag2:");
        for (byte b : tag_2)
            System.out.print(String.format("%2h ", b&0xFF));
        System.out.println();
        //-------------------------- FIM teste Soup 2 --------------------------------
        
//        printMatrix(aData);
        
//        printMatrix(marv.lpad(0x181));
        
//        System.out.println(String.format("%2h ",curu.ÉsseDeU((byte)0xaa)));
//        System.out.println(String.format("%2h ",curu.ÉsseDeU((byte)0x1)));
//        System.out.println(String.format("%2h ",curu.ÉsseDeU((byte)0x23)));
    }

    /**
     * 
     * @param tam_nounce em bytes
     * @return 
     */
    private static byte[] generate_nounce(int tam_nounce) {
        byte [] nounce =  new byte[tam_nounce];
//                            new byte[12];
        for (int i = 0; i < tam_nounce; i++)
            nounce[i] = (byte)(System.nanoTime());
//        for (int i = 0; i < 12-tam_nounce; i++)
//            nounce[i] = 0;
        return nounce;
    }

    private static void printMatrixToFile(byte[] b, File f) throws IOException{
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(b);
        bos.flush();
        bos.close();
    }

    private static OpcoesMenu printMenu() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int i = 0;
        String readString;
        String inicio = "";
        for (OpcoesMenu o : OpcoesMenu.values()){
            if (i!=0)
                inicio = "\t" + i + ") ";
            i++;
            System.out.println(inicio + o.getValue());
        }
        readString = in.readLine();
        try{
            return OpcoesMenu.values()[Integer.parseInt(readString)];
        }catch (Exception e){
            System.out.println("\nOpção inválida...\n");
            return OpcoesMenu.menu;
        }
    }
    
    
    public enum OpcoesMenu {
        menu("Selecione uma opção:"),
        selecionar_tam_chave("Selecionar um tamanho de chave dentre os valores admissíveis"),
        selecionar_tam_IV_e_MAC("Selecionar um tamanho de IV e de MAC entre o mínimo de 64 bits e o tamanho completo do bloco"),
        selecionar_chave("Escolher uma senha alfanumérica"),
        autenticar_somente("Selecionar um arquivo para ser apenas autenticado"),
        validar_somente("Selecionar um arquivo com seu respectivo MAC para ser validado"),
        cifrar_e_autenticar("Selecionar um arquivo para ser cifrado e autenticado"),
        decifrar_e_validar("Selecionar um arquivo cifrado com seus respectivos IV e MAC para ser validado e decifrado"),
        cifrar_autenticar_com_AD("Selecionar um arquivo para ser cifrado e autenticado, e um arquivo correspondente de dados associados para ser autenticado."),
        decifrar_e_validar_com_AD("Selecionar um arquivo cifrado com seus respectivos IV e MAC para ser validado e decifrado, um arquivo correspondente de dados associados para ser autenticado"),
        sair("Sair");
        
        private String texto;
        private OpcoesMenu(String texto)
	{
		this.texto = texto;
	}
	public String getValue()
	{
		return this.texto;
	}
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        testes();
        System.out.println("----- Marvin -----");
        Marvin marv = new Marvin();
        Curupira curu = new Curupira();
        byte[] teste = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0};
        marv.setCipher(curu);
        marv.setKey(teste, 96);
        marv.init();
        byte[] aData = new byte[] {
            0x0, 0x0, 0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0, 0x0
//            ,0x0, 0x0, 0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0,  0x0, 0x1 
//            ,0x2, 0x3, 0x4,  0x5,  0x6,  0x7,  0x8,  0x9,  0xa,  0xb,  0xc, 0xd
//            ,0xe, 0xf, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17
        };
        marv.update(aData, aData.length);
        printMatrix(marv.getTag(null, 12*8));
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String readString; 
        File infile;
        File infile2;
        File file;
        
        byte[] chave    = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c};//null;
        byte[] nounce   = null;
        byte[] mData    = null;
        byte[] cData    = null;
        //byte[] aData    = null;
        byte[] tag      = null;
        byte[] tag2      = null;
        
        int tam_chave   = 0 + 96;
        int tam_nounce  = 0 + 12;
        int tam_tag  = 0 + 12;
        int i;
        
        Curupira cipher;
        LetterSoup aead;
        Marvin mac;
        
        OpcoesMenu op = OpcoesMenu.menu;
        while (op != OpcoesMenu.sair){
            switch (op){
                case menu:
                    op = printMenu();
                    break;
                case selecionar_tam_chave:
                    System.out.println("Digite o tamanho da chave: 96, 144 ou 192 bits");
                    readString = in.readLine();
                    try{
                        tam_chave = Integer.parseInt(readString);
                        if (tam_chave != 96 && tam_chave != 144 && tam_chave != 192)
                            throw new Exception();
                        op = OpcoesMenu.menu;
                    }catch (Exception e){
                        System.out.println("\nOpção inválida...\n");
                    }
                    break;
                case selecionar_tam_IV_e_MAC:
                    System.out.println("Digite o tamanho da tag: 4, 8 ou 12 bytes:");
                    readString = in.readLine();
                    try{
                        tam_tag = Integer.parseInt(readString);
                        if (tam_tag != 4 && tam_tag != 8 && tam_tag != 12)
                            throw new Exception();
                        op = OpcoesMenu.menu;
                    }catch (Exception e){
                        System.out.println("\nOpção inválida...\n");
                    }
                    
                    System.out.println("Digite o tamanho de nounce: 4, 8 ou 12 bytes:");
                    readString = in.readLine();
                    try{
                        tam_nounce = Integer.parseInt(readString);
                        if (tam_nounce != 4 && tam_nounce != 8 && tam_nounce != 12)
                            throw new Exception();
                        op = OpcoesMenu.menu;
                    }catch (Exception e){
                        System.out.println("\nOpção inválida...\n");
                    }
                    break;
                case selecionar_chave:
                    if (tam_chave != 0){
                        System.out.println("Digite uma senha de " + tam_chave/8 + " caracteres:");
                        readString = in.readLine();
                        if (readString.length() > tam_chave/8)
                            System.out.println("Senha grande demais\n");
                        else{
                            chave = new byte[tam_chave/8];
                            i = 0;
                            for (char c : readString.toCharArray())
                                chave[i++] = (byte)c;
                            for(;i<tam_chave/8;i++)
                                chave[i] = 0;
                            op = OpcoesMenu.menu;
                        }
                    }else{
                        System.out.println("Selecione tamanho de senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
                case autenticar_somente:
                    if (chave != null && tam_nounce != 0){
//                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
                        mac.setCipher(cipher);
//                        aead.setMAC(mac);
//                        aead.setCipher(cipher);
//                        aead.setKey(chave, tam_chave);
//                        nounce = generate_nounce(tam_nounce);
//                        aead.setIV(nounce, tam_nounce);
                        mac.setKey(chave, tam_chave);
                        mac.init();
                        
                        System.out.println("Digite o nome do arquivo a ser autenticado");
                        readString = in.readLine();
                        infile = new File(readString);
                        
                        mData = getBytesFromFile(infile);
                        
//                        for (byte b : mData)
//                                System.out.print((char)b);
//                        System.out.println();
                        
                        cData = new byte[mData.length];
//                        aead.encrypt(mData, mData.length, cData);
//                        tag = aead.getTag(null, tam_nounce*8);
                        
                        System.out.println(mData.length);
                        
                        for (i = 0; i < mData.length; i+= cipher.blockBits()/8){
                            int num_bytes = ((mData.length - i > cipher.blockBits()/8)?(cipher.blockBits()/8):(mData.length - i)) ;
                            
                            byte[] Hi = new byte[num_bytes];
                            for (int j = 0; j < num_bytes; j++)
                                Hi[j] = mData[i+j];
                            
                            mac.update(Hi, num_bytes);
                        }
                        
                        tag = mac.getTag(null, tam_nounce*8);
                        
//                        printMatrixToFile(nounce, new File(readString + ".iv"));
                        printMatrixToFile(tag, new File(readString + ".mac"));
                        
                        op = OpcoesMenu.menu;
                        
                        System.out.println("Atenticação concluída. Arquivos gerados: ");
//                        System.out.println("\t"+readString + ".iv");
                        System.out.println("\t"+readString + ".mac");
                        System.out.println();
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
/*------------------------------- FIM AUTENTICAR SOMENTE ------------------------------*/                
                case validar_somente:
                    if (chave != null){
//                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
//                        aead.setMAC(mac);
//                        aead.setCipher(cipher);
//                        aead.setKey(chave, tam_chave);
                        mac.setCipher(cipher);
                        mac.setKey(chave, tam_chave);
                        
                        System.out.println("Digite o nome do arquivo a ser validade");
                        readString = in.readLine();
                        infile = new File(readString);
                        mData = getBytesFromFile(infile);
                        
//                        do{
//                            System.out.println("Utilizar arquivo " + readString + ".iv? [s/n]");
//                            readString = in.readLine();
//                        } while(!"s".equals(readString) && !"n".equals(readString));
//                        
//                        if ("s".equals(readString)){
//                            infile2 = new File(infile.getName()+".iv");
//                            nounce = getBytesFromFile(infile2);
//                        }else{
//                            System.out.println("Digite o nome do arquivo com IV");
//                            readString = in.readLine();
//                            nounce = getBytesFromFile(new File(readString));
//                        }
                        
                        do{
                            System.out.println("Utilizar arquivo " + infile.getName() + ".mac? [s/n]");
                            readString = in.readLine();
                        } while(!"s".equals(readString) && !"n".equals(readString));
                        
                        if ("s".equals(readString)){
                            infile2 = new File(infile.getName()+".mac");
                            tag = getBytesFromFile(infile2);
                        }else{
                            System.out.println("Digite o nome do arquivo com MAC");
                            readString = in.readLine();
                            tag = getBytesFromFile(new File(readString));
                        }
                        
//                        aead.setIV(nounce, tam_nounce);
                        mac.init();
                        
                        cData = new byte[mData.length];
//                        aead.encrypt(mData, mData.length, cData);
                        for (i = 0; i < mData.length; i+= cipher.blockBits()/8){
                            int num_bytes = ((mData.length - i > cipher.blockBits()/8)?(cipher.blockBits()/8):(mData.length - i)) ;
                            
                            byte[] Hi = new byte[num_bytes];
                            for (int j = 0; j < num_bytes; j++)
                                Hi[j] = mData[i+j];
                            
                            mac.update(Hi, num_bytes);
                        }
//                        tag2 = aead.getTag(null, tam_nounce*8);
                        tag2 = mac.getTag(null, tam_nounce*8);
                        
                        System.out.println();
                        if (Arrays.equals(tag2, tag)){
                            System.out.println("Resumos OK.");
                        }else{
                            System.out.println("Resumos não conferem.");
                        }
                        
                        op = OpcoesMenu.menu;
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
/*------------------------------- FIM VALIDAR SOMENTE ------------------------------*/
                case cifrar_e_autenticar:
                    if (chave != null && tam_nounce != 0){
                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
                        aead.setMAC(mac);
                        aead.setCipher(cipher);
                        aead.setKey(chave, tam_chave);
                        nounce = generate_nounce(tam_nounce);
                        aead.setIV(nounce, tam_nounce);
                        
                        System.out.println("Digite o nome do arquivo a ser encriptado e autenticado");
                        readString = in.readLine();
                        infile = new File(readString);
                        
                        mData = getBytesFromFile(infile);
                        
                        cData = new byte[mData.length];
                        aead.encrypt(mData, mData.length, cData);
                        
                        tag = aead.getTag(null, tam_nounce*8);
                        printMatrixToFile(nounce, new File(readString + ".iv"));
                        printMatrixToFile(tag, new File(readString + ".mac"));
                        printMatrixToFile(cData, new File(readString + ".ciph"));
                        
                        op = OpcoesMenu.menu;
                        
                        System.out.println("Atenticação concluída. Arquivos gerados: ");
                        System.out.println("\t"+readString + ".iv");
                        System.out.println("\t"+readString + ".mac");
                        System.out.println("\t"+readString + ".ciph");
                        System.out.println();
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
/*------------------------------- FIM AUTENTICAR E CIFRAR ------------------------------*/
                case decifrar_e_validar:
                    if (chave != null){
                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
                        aead.setMAC(mac);
                        aead.setCipher(cipher);
                        aead.setKey(chave, tam_chave);
                        
                        System.out.println("Digite o nome do arquivo a ser validado e descifrado");
                        readString = in.readLine();
                        infile = new File(readString);
                        cData = getBytesFromFile(infile);
                        
                        String file_name = readString.substring(0, readString.length()-5);
                        
                        do{
                            System.out.println("Utilizar arquivo " + file_name + ".iv? [s/n]");
                            readString = in.readLine();
                        } while(!"s".equals(readString) && !"n".equals(readString));
                        
                        if ("s".equals(readString)){
                            infile2 = new File(file_name+".iv");
                            nounce = getBytesFromFile(infile2);
                        }else{
                            System.out.println("Digite o nome do arquivo com IV");
                            readString = in.readLine();
                            nounce = getBytesFromFile(new File(readString));
                        }
                        
                        do{
                            System.out.println("Utilizar arquivo " + file_name + ".mac? [s/n]");
                            readString = in.readLine();
                        } while(!"s".equals(readString) && !"n".equals(readString));
                        
                        if ("s".equals(readString)){
                            infile2 = new File(file_name+".mac");
                            tag = getBytesFromFile(infile2);
                        }else{
                            System.out.println("Digite o nome do arquivo com MAC");
                            readString = in.readLine();
                            tag = getBytesFromFile(new File(readString));
                        }
                        
                        aead.setIV(nounce, tam_nounce);
                        
                        mData = new byte[cData.length];
                        aead.decrypt(cData, mData.length, mData);
                        
                        System.out.println("Escrevendo arquivo descifrado...");
                        printMatrixToFile(mData, new File(file_name+".unciph"));
                        
                        tag2 = aead.getTag(null, tam_nounce*8);
                        
                        System.out.println();
                        if (Arrays.equals(tag2, tag)){
                            System.out.println("Resumos OK.");
                        }else{
                            System.out.println("Resumos não conferem.");
                        }
                        
                        op = OpcoesMenu.menu;
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
/*------------------------------- FIM VALIDAR E DESCIFRAR ------------------------------*/
                case cifrar_autenticar_com_AD:
                    if (chave != null && tam_nounce != 0){
                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
                        aead.setMAC(mac);
                        aead.setCipher(cipher);
                        aead.setKey(chave, tam_chave);
                        nounce = generate_nounce(tam_nounce);
                        aead.setIV(nounce, tam_nounce);
                        
                        System.out.println("Digite o nome do arquivo de dados associados para ser autenticado:");
                        readString = in.readLine();
                        infile = new File(readString);
                        aData = getBytesFromFile(infile);
                        
                        System.out.println("Digite o nome do arquivo a ser encriptado e autenticado:");
                        readString = in.readLine();
                        infile = new File(readString);
                        mData = getBytesFromFile(infile);
                        
                        aead.update(aData, aData.length);
                        cData = new byte[mData.length];
                        aead.encrypt(mData, mData.length, cData);
                        
                        tag = aead.getTag(null, tam_tag*8);
                        printMatrixToFile(nounce, new File(readString + ".iv"));
                        printMatrixToFile(tag, new File(readString + ".mac"));
                        printMatrixToFile(cData, new File(readString + ".ciph"));
                        
                        op = OpcoesMenu.menu;
                        
                        System.out.println("Atenticação concluída. Arquivos gerados: ");
                        System.out.println("\t"+readString + ".iv");
                        System.out.println("\t"+readString + ".mac");
                        System.out.println("\t"+readString + ".ciph");
                        System.out.println();
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
/*------------------------------- FIM AUTENTICAR E CIFRAR com DADOS AD ------------------------------*/
                case decifrar_e_validar_com_AD:
                    if (chave != null){
                        aead = new LetterSoup();
                        mac = new Marvin();
                        cipher = new Curupira();
                        
                        aead.setMAC(mac);
                        aead.setCipher(cipher);
                        aead.setKey(chave, tam_chave);
                        
                        System.out.println("Digite o nome do arquivo de dados associados para ser validado:");
                        readString = in.readLine();
                        infile = new File(readString);
                        aData = getBytesFromFile(infile);
                        
                        System.out.println("Digite o nome do arquivo a ser validado e descifrado");
                        readString = in.readLine();
                        infile = new File(readString);
                        cData = getBytesFromFile(infile);
                        
                        String file_name = readString.substring(0, readString.length()-5);
                        
                        do{
                            System.out.println("Utilizar arquivo " + file_name + ".iv? [s/n]");
                            readString = in.readLine();
                        } while(!"s".equals(readString) && !"n".equals(readString));
                        
                        if ("s".equals(readString)){
                            infile2 = new File(file_name+".iv");
                            nounce = getBytesFromFile(infile2);
                        }else{
                            System.out.println("Digite o nome do arquivo com IV");
                            readString = in.readLine();
                            nounce = getBytesFromFile(new File(readString));
                        }
                        
                        do{
                            System.out.println("Utilizar arquivo " + file_name + ".mac? [s/n]");
                            readString = in.readLine();
                        } while(!"s".equals(readString) && !"n".equals(readString));
                        
                        if ("s".equals(readString)){
                            infile2 = new File(file_name+".mac");
                            tag = getBytesFromFile(infile2);
                        }else{
                            System.out.println("Digite o nome do arquivo com MAC");
                            readString = in.readLine();
                            tag = getBytesFromFile(new File(readString));
                        }
                        
                        aead.setIV(nounce, tam_nounce);
                        
                        aead.update(aData, aData.length);
                        mData = new byte[cData.length];
                        aead.decrypt(cData, mData.length, mData);
                        
                        System.out.println("Escrevendo arquivo descifrado...");
                        printMatrixToFile(mData, new File(file_name+".unciph"));
                        
                        tag2 = aead.getTag(null, tam_tag*8);
                        
                        System.out.println();
                        if (Arrays.equals(tag2, tag)){
                            System.out.println("Resumos OK.");
                        }else{
                            System.out.println("Resumos não conferem.");
                        }
                        
                        op = OpcoesMenu.menu;
                    }else{
                        System.out.println("Selecione a senha primeiro.");
                        op = OpcoesMenu.menu;
                    }
                    break;
                case sair:
                    break;
                default:
                    op = OpcoesMenu.menu;
                    break;
            }
        }
        System.out.println("Saindo...");
        
    }
    
    static void printMatrix(byte[] a, boolean por_linha){
        int t = a.length/6;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < t*2 ; j++)
                System.out.print( String.format("%2h ", (a[ (por_linha)? (i*t*2+j) : (i+j*3) ] & 0xFF)));
            if (a.length % 6 != 0){
                try{
                    int novo_t = a.length/6+1;
                    System.out.print( String.format("%2h ", (a[ (por_linha)? (i*t*2+novo_t) : (i+novo_t*3) ] & 0xFF)));
                    novo_t = a.length/6+2;
                    System.out.print( String.format("%2h ", (a[ (por_linha)? (i*t*2+novo_t) : (i+novo_t*3) ] & 0xFF)));
                }catch (ArrayIndexOutOfBoundsException e){System.out.print( "--");}
            }
            System.out.println();
        }
        System.out.println();
    }
    
    static void printMatrix(byte[] a){
        printMatrix(a, false);
    }
    
     public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
}

