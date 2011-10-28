/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etapa2.random;

/**
 *
 * @author Renan
 */
public class KeccakPRG implements SpongePRG{

    @Override
    public int getBitRate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCapacity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init(int hashBits) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void feed(byte[] sigma, int sigmaLength) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] fetch(byte[] z, int zLength) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void forget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
