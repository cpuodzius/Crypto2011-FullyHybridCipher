package etapa2.hash;

import java.util.Arrays;

public class Keccak implements HashFunction, Duplex {

    public static void displayBuf(String tag, byte[] z) {
        System.out.print(tag + " = ");
        String hex = "0123456789ABCDEF";
        for (int i = 0; i < z.length; i++) {
            System.out.print(hex.charAt((z[i] >> 4) & 0xf));
            System.out.print(hex.charAt((z[i]     ) & 0xf));
        }
        System.out.println();
    }

    public static void displayBuf(String tag, byte[] z, int from, int len) {
        System.out.print(tag + " = ");
        String hex = "0123456789ABCDEF";
        for (int i = 0; i < len; i++) {
            System.out.print(hex.charAt((z[from + i] >> 4) & 0xf));
            System.out.print(hex.charAt((z[from + i]     ) & 0xf));
        }
        System.out.println();
    }

    //TODO não sei se as constantes estão certas... tirei este algoritmo da implementação de referencia
    // mas os vetores de teste (que eu vi) usam essa sequencia
    /*
int LFSR86540(UINT8 *LFSR)
{
    int result = ((*LFSR) & 0x01) != 0;
    if (((*LFSR) & 0x80) != 0)
        // Primitive polynomial over GF(2): x^8+x^6+x^5+x^4+1
        (*LFSR) = ((*LFSR) << 1) ^ 0x71;
    else
        (*LFSR) <<= 1;
    return result;
}

void KeccakInitializeRoundConstants()
{
    UINT8 LFSRstate = 0x01;
    unsigned int i, j, bitPosition;

    for(i=0; i<nrRounds; i++) {
        KeccakRoundConstants[i] = 0;
        for(j=0; j<7; j++) {
            bitPosition = (1<<j)-1; //2^j-1
            if (LFSR86540(&LFSRstate))
                KeccakRoundConstants[i] ^= (UINT64)1<<bitPosition;
        }
    }
}
     */
    static final long[] KeccakRoundConstants = {
        0x0000000000000001L, 0x0000000000008082L, 0x800000000000808AL, 0x8000000080008000L,
        0x000000000000808BL, 0x0000000080000001L, 0x8000000080008081L, 0x8000000000008009L,
        0x000000000000008AL, 0x0000000000000088L, 0x0000000080008009L, 0x000000008000000AL,
        0x000000008000808BL, 0x800000000000008BL, 0x8000000000008089L, 0x8000000000008003L,
        0x8000000000008002L, 0x8000000000000080L, 0x000000000000800AL, 0x800000008000000AL,
        0x8000000080008081L, 0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L,
    };
    
    //TODO
    // mas os vetores de teste (que eu vi) usam essa sequencia, mas no documento está escrita a de baixo....
    static final int[] KeccakRhoOffsets = {0, 1, 62, 28, 27, 36, 44, 6, 55, 20, 3, 10, 43, 25, 39, 41, 45, 15, 21, 8, 18, 2, 61, 56, 14};
//    static final int[] KeccakRhoOffsets = {0, 1, 190, 28, 91, 36, 300, 6, 55, 276, 3, 10, 171, 153, 231, 105, 45, 15, 21, 136, 210, 66, 253, 120, 78};

    static final int nrRounds = 24;
    static final int KeccakPermutationSize = 1600;
    static final int KeccakPermutationSizeInBytes = (KeccakPermutationSize/8);
    static final int KeccakMaximumRate = 1152;
    static final int KeccakMaximumRateInBytes = (KeccakMaximumRate/8);

    byte[] state = new byte[KeccakPermutationSizeInBytes];
    long[] stateAsWords = new long[KeccakPermutationSize/64];
    byte[] dataQueue = new byte[KeccakMaximumRateInBytes];
    long[] B = new long[25];
    long[] C = new long[5];
    long[] D = new long[5];
    int rate;
    int capacity;
    byte diversifier;
    int hashbitlen;
    int bitsInQueue;
    boolean squeezing;
    int bitsAvailableForSqueezing;

    private long ROL64(long a, int offset) {
        return (a << offset) | (a >>> -offset);
    }

    private void fromBytesToWords() {
        for (int i = 0, j = 0; i < (KeccakPermutationSize/64); i++, j += 8) {
            stateAsWords[i] = ((long)state[j    ] & 0xFFL)
                            | ((long)state[j + 1] & 0xFFL) <<  8
                            | ((long)state[j + 2] & 0xFFL) << 16
                            | ((long)state[j + 3] & 0xFFL) << 24
                            | ((long)state[j + 4] & 0xFFL) << 32
                            | ((long)state[j + 5] & 0xFFL) << 40
                            | ((long)state[j + 6] & 0xFFL) << 48
                            | ((long)state[j + 7] & 0xFFL) << 56;
        }
    }

    private void fromWordsToBytes() {
        for (int i = 0, j = 0; i < (KeccakPermutationSize/64); i++, j += 8) {
            state[j    ] = (byte)(stateAsWords[i]      );
            state[j + 1] = (byte)(stateAsWords[i] >>  8);
            state[j + 2] = (byte)(stateAsWords[i] >> 16);
            state[j + 3] = (byte)(stateAsWords[i] >> 24);
            state[j + 4] = (byte)(stateAsWords[i] >> 32);
            state[j + 5] = (byte)(stateAsWords[i] >> 40);
            state[j + 6] = (byte)(stateAsWords[i] >> 48);
            state[j + 7] = (byte)(stateAsWords[i] >> 56);
        }
    }

    private static int index(int x, int y) {
        return (((x)%5)+5*((y)%5));
    }

    private void theta() {//TODO parece meio diferente da implementação de ref
        for (int x=0; x<5; x++) {
            C[x] = 0; //TODO pag 17: C[x] = a[x, 0]
            for (int y=0; y<5; y++) 
                C[x] ^= stateAsWords[index(x, y)];
            D[x] = ROL64(C[x], 1);
        }
        for (int x=0; x<5; x++)
            for (int y=0; y<5; y++)
                stateAsWords[index(x, y)] ^= D[(x+1)%5] ^ C[(x+4)%5];
    }
    
    private void rho() {//TODO não entendi, mas está igual implementação de ref
        for (int x=0; x<5; x++)
            for (int y=0; y<5; y++)
                stateAsWords[index(x, y)] = ROL64(stateAsWords[index(x, y)], KeccakRhoOffsets[index(x, y)]);
    }
    
    private void pi() {
        for (int x=0; x<5; x++) for (int y=0; y<5; y++)
            B[index(x, y)] = stateAsWords[index(x, y)];
        for (int x=0; x<5; x++) for (int y=0; y<5; y++)
            stateAsWords[index(0*x+1*y, 2*x+3*y)] = B[index(x, y)];
    }
    
    private void chi() {
        for (int y=0; y<5; y++) { 
            for (int x=0; x<5; x++)
                C[x] = stateAsWords[index(x, y)] ^ ((~stateAsWords[index(x+1, y)]) & stateAsWords[index(x+2, y)]);
            for (int x=0; x<5; x++)
                stateAsWords[index(x, y)] = C[x];
        }
    }
    
    private void iota(int indexRound) {
        stateAsWords[index(0, 0)] ^= KeccakRoundConstants[indexRound];
    }

    void KeccakPermutation() {
        displayBuf("state before permutation", state);
        fromBytesToWords();
        for (int i=0; i<nrRounds; i++) {
            theta();
            rho();
            pi();
            chi();
            iota(i);
        }
        fromWordsToBytes();
        displayBuf("state after  permutation", state);
    }

    private void AbsorbQueue() {
        // bitsInQueue is assumed to be a multiple of 8
        Arrays.fill(dataQueue, bitsInQueue/8, rate/8, (byte)0);
        for (int i = 0; i < rate/8; i++) {
            state[i] ^= dataQueue[i];
        }
        displayBuf("dataQueue               ", dataQueue);
        KeccakPermutation();
        bitsInQueue = 0;
    }

    private void KeccakPad() {
        if ((bitsInQueue % 8) != 0) {
            // The bits are numbered from 0=LSB to 7=MSB
            byte padByte = (byte)(1 << (bitsInQueue % 8));
            dataQueue[bitsInQueue/8] |= padByte;
            bitsInQueue += 8-(bitsInQueue % 8);
        } else {
            dataQueue[bitsInQueue/8] = 0x01;
            bitsInQueue += 8;
        }
        if (bitsInQueue == rate) {
            AbsorbQueue();
        }
        dataQueue[bitsInQueue/8] = diversifier;
        bitsInQueue += 8;
        if (bitsInQueue == rate) {
            AbsorbQueue();
        }
        dataQueue[bitsInQueue/8] = (byte)(rate/8);
        bitsInQueue += 8;
        if (bitsInQueue == rate) {
            AbsorbQueue();
        }
        dataQueue[bitsInQueue/8] = 0x01;
        bitsInQueue += 8;
        if (bitsInQueue > 0) {
            AbsorbQueue();
        }
        System.arraycopy(state, 0, dataQueue, 0, rate/8);
        bitsAvailableForSqueezing = rate;
    }

    // PUBLIC INTERFACE:

    public Keccak() {
    }

    /**
     * Get the duplex bit-rate r.
     */
    public int getBitRate() {
        return rate;
    }

    /**
     * Get the duplex capacity c.
     */
    public int getCapacity() {
        return capacity;
    }

    public void init() {
        init(0);
    }

    public void init(int hashbitlen) {
        switch(hashbitlen) {
        case 0: // Arbitrary length output
            capacity = 576;
            break;
        case 224:
            capacity = 448;
            break;
        case 256:
            capacity = 512;
            break;
        case 384:
            capacity = 768;
            break;
        case 512:
            capacity = 1024;
            break;
        default:
            throw new RuntimeException("BAD_HASHLEN");
        }
        rate = KeccakPermutationSize - capacity;
        diversifier = (byte)(hashbitlen/8);
        this.hashbitlen = hashbitlen;
        Arrays.fill(state, (byte)0);
        Arrays.fill(dataQueue, (byte)0);
        bitsInQueue = 0;
        squeezing = false;
        bitsAvailableForSqueezing = 0;
    }

    public void update(byte[] data, int databitlen) {
        if ((bitsInQueue % 8) != 0) {
            throw new RuntimeException("Only the last call may contain a partial byte");
        }
        if (squeezing) {
            throw new RuntimeException("Too late for additional input");
        }
        int k = 0;
        while (k < databitlen) {
            if ((bitsInQueue == 0) && (databitlen >= rate) && (k <= (databitlen-rate))) {
                int wholeBlocks = (databitlen - k)/rate;
                int curData = (int)(k/8);
                for (int j=0; j<wholeBlocks; j++, curData+=rate/8) {
                    for (int i = 0; i < rate/8; i++) {
                        state[i] ^= data[i+curData];
                    }
                    displayBuf("data                    ", data, curData, rate/8);
                    KeccakPermutation();
                    displayBuf("Z                       ", state, 0, rate/8);
                }
                k += wholeBlocks*rate;
            } else {
                int partialBlock = databitlen - k;
                if (partialBlock+bitsInQueue > rate) {
                    partialBlock = rate-bitsInQueue;
                }
                int partialByte = partialBlock%8;
                partialBlock -= partialByte;
                System.arraycopy(data, k/8, dataQueue, bitsInQueue/8, partialBlock/8);
                bitsInQueue += partialBlock;
                k += partialBlock;
                if (bitsInQueue == rate) {
                    AbsorbQueue();
                }
                if (partialByte > 0) {
                    // Align the last partial byte to the least significant bits
                    byte lastByte = (byte)((data[k/8] & 0xFF) >>> (8-partialByte));
                    dataQueue[bitsInQueue/8] = lastByte;
                    bitsInQueue += partialByte;
                    k += partialByte;
                }
            }
        }
    }

    public byte[] getHash(byte[] hashval) {
        if (!squeezing) {
            KeccakPad();
            squeezing = true;
        }
        if (hashval == null) {
            hashval = new byte[hashbitlen/8];
        }
        if (hashbitlen > 0) {
            System.arraycopy(dataQueue, 0, hashval, 0, hashbitlen/8);
        }
        return hashval;
    }

    public byte[] squeeze(byte[] output, int outputLength) {
        if (!squeezing)
            throw new RuntimeException("Too early, we are still absorbing");
        if (hashbitlen != 0)
            throw new RuntimeException("Arbitrary length output is not permitted in this case");
        if ((outputLength % 8) != 0)
            throw new RuntimeException("Only multiple of 8 bits are allowed, truncation can be done at user level");
        if (output == null) {
            output = new byte[(int)outputLength];
        }
        int i = 0;
        while (i < outputLength) {
            if (bitsAvailableForSqueezing == 0) {
                System.out.println("...squeezing...");
                KeccakPermutation();
                if (rate != 1024) {
                    throw new RuntimeException("Inconsistent rate");
                }
                System.arraycopy(state, 0, dataQueue, 0, rate/8);
                bitsAvailableForSqueezing = rate;
            }
            int partialBlock = outputLength - i;
            if (partialBlock > bitsAvailableForSqueezing) {
                partialBlock = bitsAvailableForSqueezing;
            }
            System.arraycopy(dataQueue, (rate-bitsAvailableForSqueezing)/8, output, (int)(i/8), (int)(partialBlock/8));
            bitsAvailableForSqueezing -= partialBlock;
            i += partialBlock;
        }
        return output;
    }

    public byte[] hash(int hashbitlen, byte[] data, int databitlen, byte[] hashval) {
        init(hashbitlen);
        update(data, databitlen);
        hashval = getHash(hashval);
        return hashval;
    }

    public byte[] duplexing(byte[] sigma, int sigmaLength, byte[] z, int zLength) {
        // The maximum l (8*zLength) one can request is r (rate).
        if(8*sigmaLength > rate - 2){
            throw new IllegalArgumentException("sigma exceeds the pad10^*1 rho rate: " + sigmaLength + " vs " + (rate - 2));
        }
        if(8*zLength > rate){
            throw new IllegalArgumentException("z exceeds the sponge rate.");
        }
        // copy sigmaLength bytes:
        for (int i = 0; i < sigmaLength; i++) {
            state[i] ^= sigma[i];
        }
        // pad with pad10^*1:
        state[sigmaLength] ^= 0x01;
        state[rate/8-1] ^= 0x80;
        // permute, i.e. absorb:
        displayBuf("sigma                   ", sigma, 0, sigmaLength);
        KeccakPermutation();
        displayBuf("Z                       ", state, 0, rate/8);
        // squeeze the first (8*zLength) bits of the state
        if (z == null && zLength > 0) {
            z = new byte[zLength];
        }
        for (int i = 0; i < zLength; i++) {
            z[i] = state[i];
        }
        return z;
    }

}