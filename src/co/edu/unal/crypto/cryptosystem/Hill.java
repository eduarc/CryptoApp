package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.util.Arithmetic;
import co.edu.unal.crypto.type.ModularMatrix;
import java.util.Arrays;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class Hill<P> extends Cryptosystem<P, P, ModularMatrix> {

    private final int modulus;

    public Hill(Alphabet<P> alpha) {
        super(alpha, alpha);

        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(ModularMatrix key, P[] input) {

        checkKey(key);
        return multiply(key, input);
    }

    @Override
    public P[] decrypt(ModularMatrix key, P[] input) {

        checkKey(key);
        int inv = Arithmetic.modInverse(key.determinant(), modulus);
        return multiply(key.cofactor().mul(inv).transpose(), input);
    }

    public void checkKey(ModularMatrix key) {

        int det = key.determinant();
        if (det == 0) {
            throw new IllegalArgumentException("Invalid Hill key: zero determinant");
        }
        if (!Arithmetic.coprimes(det, modulus)) {
            throw new IllegalArgumentException("Invalid Hill key: determinant "+det+" and "+modulus+" aren't coprimes");
        }
    }

    @Override
    public ModularMatrix generateKey(Object seed) {
        return null;
    }

    private P[] multiply(ModularMatrix key, P[] vec) {

        int msgLength = vec.length;
        int blockSize = key.getCols();
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
            input = input.mul(key);
            for (int j = 0; j < blockSize; j++) {
                int p = input.get(0, j);
                output[i + j] = inAlphabet.getValue(p);
            }
        }
        return output;
    }
    
    /*public static void main(String args[]) {
        
        ModularMatrix mat = new ModularMatrix(2, 2, LowerCaseEnglish.SIZE);
        mat.set(0, 0, 3);
        mat.set(0, 1, 3);
        mat.set(1, 0, 2);
        mat.set(1, 1, 5);
        
        ModularMatrix iv = new ModularMatrix(1, 2, LowerCaseEnglish.SIZE);
        iv.set(0, 0, 5);
        iv.set(0, 1, 7);
        
        Hill<Character> cipher = new Hill(LowerCaseEnglish.defaultInstance);
        
        String strInput = "helphelp";
        Character[] input = CharStream.fromString(strInput);
        
        Character[] output = cipher.encrypt(mat, input);
        CharStream.out(output);
        input = cipher.decrypt(mat, output);
        CharStream.out(input);
    }*/
}
