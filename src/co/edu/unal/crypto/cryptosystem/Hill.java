package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.tools.Arithmetic;
import co.edu.unal.crypto.tools.ModularArithmetic;
import java.util.Arrays;

import flanagan.math.Matrix;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class Hill<P> extends Cryptosystem<P, P, Matrix> {

    private final int modulus;

    public Hill(Alphabet<P> alpha) {
        super(alpha, alpha);

        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(Matrix key, P[] input) {

        checkKey(key);
        return multiply(key, input);
    }

    @Override
    public P[] decrypt(Matrix key, P[] input) {

        checkKey(key);
        int inv = ModularArithmetic.multiplicativeInverse((int) key.determinant(), modulus);
        return multiply(key.cofactor().times((double) inv).transpose(), input);
    }

    public void checkKey(Matrix key) {

        modMatrix(key);
        int det = (int) key.determinant();
        if (det == 0) {
            throw new IllegalArgumentException("Invalid Hill key: null determinant");
        }
        if (!Arithmetic.areCoprimes(det, modulus)) {
            throw new IllegalArgumentException("Invalid Hill key: determinant "+det+" and "+modulus+" aren't coprimes");
        }
    }

    @Override
    public Matrix generateKey(Object seed) {
        // TODO Auto-generated method stub
        return null;
    }

    private P[] multiply(Matrix key, P[] vec) {

        int msgLength = vec.length;
        int blockSize = key.getNcol();
        int r = msgLength % blockSize;
        int dummy = r == 0 ? 0 : blockSize - r;

        P[] output = (P[]) Arrays.copyOf(vec, msgLength + dummy);
        for (int i = output.length - 1; dummy > 0; i--, dummy--) {
            output[i] = inAlphabet.getValue(0);
        }

        modMatrix(key);
        double input[][] = new double[1][blockSize];

        for (int i = 0; i < output.length; i += blockSize) {
            for (int j = 0; j < blockSize; j++) {
                input[0][j] = inAlphabet.getIndex(output[i + j]);
            }
            Matrix aux = (new Matrix(input)).times(key);
            modMatrix(aux);
            for (int j = 0; j < blockSize; j++) {
                output[i + j] = inAlphabet.getValue((int) aux.getElement(0, j));
            }
        }
        return output;
    }

    private void modMatrix(Matrix key) {

        for (int i = 0; i < key.getNrow(); i++) {
            for (int j = 0; j < key.getNcol(); j++) {
                int v = (int) key.getElement(i, j);
                key.setElement(i, j, ModularArithmetic.modulo(v, modulus));
            }
        }
    }
}
