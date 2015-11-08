package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.tools.Arithmetic;
import co.edu.unal.crypto.types.Pair;
import java.lang.reflect.Array;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class Affine<P> extends Cryptosystem<P, P, Pair<Integer, Integer>> {

    @SuppressWarnings("rawtypes")
    private final Class Pclass;
    private final int modulus;

    public Affine(Alphabet<P> alpha) {
        super(alpha, alpha);

        Pclass = alpha.getValue(0).getClass();
        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(Pair<Integer, Integer> key, P[] input) {

        checkKey(key);
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        int p = key.first;
        p = Arithmetic.mod(p, modulus);
        int k = key.second;
        k = Arithmetic.mod(k, modulus);

        for (int i = 0; i < input.length; ++i) {
            int idx = inAlphabet.getIndex(input[i]);
            int pos = Arithmetic.mod(idx * p + k, modulus);
            output[i] = inAlphabet.getValue(pos);
        }
        return output;
    }

    @Override
    public P[] decrypt(Pair<Integer, Integer> key, P[] input) {

        checkKey(key);
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        int q = Arithmetic.modInverse(key.first, modulus);
        q = Arithmetic.mod(q, modulus);
        int k = key.second;
        k = Arithmetic.mod(k, modulus);

        for (int i = 0; i < input.length; ++i) {
            int idx = inAlphabet.getIndex(input[i]);
            int pos = Arithmetic.mod(q * (idx - k), modulus);
            output[i] = inAlphabet.getValue(pos);
        }
        return output;
    }

    public void checkKey(Pair<Integer, Integer> key) {
        
        if (!Arithmetic.coprimes(key.first, modulus)) {
            throw new IllegalArgumentException("Invalid Affine key: a and "+modulus+" aren't coprimes");
        }
    }

    @Override
    public Pair<Integer, Integer> generateKey(Object seed) {
        return null;
    }
}
