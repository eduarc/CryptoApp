package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.tools.Arithmetic;
import co.edu.unal.crypto.types.ModularMatrix;
import java.util.Arrays;


/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class HillReloaded<P> extends Cryptosystem<P, P, HillReloaded.Key> {

    private final int modulus;

    public HillReloaded(Alphabet<P> alpha) {
        super(alpha, alpha);

        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(Key key, P[] input) {

        checkKey(key);
        return multiply(key.mat.copy(), key.iv, input);
    }

    @Override
    public P[] decrypt(Key key, P[] input) {

        ModularMatrix mat = key.mat;
        checkKey(key);
        return multiplyInv(mat.copy(), key.iv, input);
    }

    public void checkKey(Key key) {

        ModularMatrix mat = key.mat;
        int det = mat.determinant();
        if (det == 0) {
            throw new IllegalArgumentException("Invalid Hill key: The key matrix must be invertible");
        }
        if (!Arithmetic.coprimes(det, modulus)) {
            throw new IllegalArgumentException("Invalid Hill key: Matrix determinant "+det+" and "+modulus+" aren't coprimes");
        }
        for (int i = 0; i < key.iv.getCols(); i++) {
            int v = key.iv.get(0, i);
            if (!Arithmetic.coprimes(v, modulus)) {
                throw new IllegalArgumentException("Invalid Hill IV: "+det+" and "+modulus+" aren't coprimes");
            }
        }
    }
    
    @Override
    public Key generateKey(Object seed) {
        return null;
    }

    private P[] multiply(ModularMatrix mat, ModularMatrix iv, P[] vec) {

        int msgLength = vec.length;
        int blockSize = mat.getCols();
        int r = msgLength % blockSize;
        int dummy = r == 0 ? 0 : blockSize - r;

        P[] output = (P[]) Arrays.copyOf(vec, msgLength + dummy);
        for (int i = output.length - 1; dummy > 0; i--, dummy--) {
            output[i] = inAlphabet.getValue(0);
        }

        ModularMatrix input = new ModularMatrix(1, blockSize, modulus);
        for (int i = 0; i < output.length; i += blockSize) {
            for (int j = 0; j < blockSize; j++) {
                input.set(0, j, inAlphabet.getIndex(output[i + j]));
            }
            input = input.mul(mat);
            for (int j = 0; j < blockSize; j++) {
                int p = input.get(0, j);
                output[i + j] = inAlphabet.getValue(p);
            }
            modify(mat, iv);
        }
        return output;
    }

    private P[] multiplyInv(ModularMatrix mat, ModularMatrix iv, P[] vec) {

        int msgLength = vec.length;
        int blockSize = mat.getCols();
        int r = msgLength % blockSize;
        int dummy = r == 0 ? 0 : blockSize - r;

        P[] output = (P[]) Arrays.copyOf(vec, msgLength + dummy);
        for (int i = output.length - 1; dummy > 0; i--, dummy--) {
            output[i] = inAlphabet.getValue(0);
        }

        ModularMatrix input = new ModularMatrix(1, blockSize, modulus);
        for (int i = 0; i < output.length; i += blockSize) {
            for (int j = 0; j < blockSize; j++) {
                input.set(0, j, inAlphabet.getIndex(output[i + j]));
            }
            input = input.mul(mat.inverse());
            for (int j = 0; j < blockSize; j++) {
                int p = (int)input.get(0, j);
                output[i + j] = inAlphabet.getValue(p);
            }
            modify(mat, iv);
        }
        return output;
    }
    
    private void modify(ModularMatrix mat, ModularMatrix iv) {
        
        int n = mat.getRows();
        for (int i = 0; i < n; i++) {
            ModularMatrix r = iv.mul(mat);
            for (int j = 0; j < n; j++) {
                mat.set(i, j, r.get(0, j));
            }
        }
    }

    public static class Key {
        
        public ModularMatrix mat;
        public ModularMatrix iv;
        
        public Key(ModularMatrix mat, ModularMatrix iv) {
            
            this.mat = mat;
            this.iv = iv;
        }
    }
    
    /*public static void main(String args[]) {
        
        ModularMatrix mat = new ModularMatrix(3, 3, SimpleAlphabet.SIZE);
        mat.set(0, 0, 17);
        mat.set(0, 1, 17);
        mat.set(0, 2, 5);
        mat.set(1, 0, 21);
        mat.set(1, 1, 18);
        mat.set(1, 2, 21);
        mat.set(2, 0, 2);
        mat.set(2, 1, 2);
        mat.set(2, 2, 19);
        
        ModularMatrix iv = new ModularMatrix(1, 3, SimpleAlphabet.SIZE);
        iv.set(0, 0, 1);
        iv.set(0, 1, 1);
        iv.set(0, 2, 3);
        
        HillReloaded<Character> cipher = new HillReloaded(SimpleAlphabet.defaultInstance);
        
        String strInput = "hola mundo, como estan el dia de hoy. Esta es la prueba del cifrador de hill version mejorada";
        Character[] input = CharStream.fromString(strInput);
        
        Character[] output = cipher.encrypt(new HillReloaded.Key(mat, iv), input);
        CharStream.out(output);
        input = cipher.decrypt(new HillReloaded.Key(mat, iv), output);
        CharStream.out(input);
    }*/
}
