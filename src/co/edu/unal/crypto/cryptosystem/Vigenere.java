package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.util.Arithmetic;
import java.lang.reflect.Array;
import java.util.Random;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 */
public class Vigenere<P> extends Cryptosystem<P, P, P[]> {

    @SuppressWarnings("rawtypes")
    private final Class Pclass;
    private final int modulus;

    public Vigenere(Alphabet<P> alpha) {
        super(alpha, alpha);

        Pclass = alpha.getValue(0).getClass();
        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(P[] key, P[] input) {

        checkKey(key);
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        for (int i = 0; i < input.length; i++) {
            int idx = inAlphabet.getIndex(input[i]);
            int kdx = inAlphabet.getIndex(key[i % key.length]);
            output[i] = inAlphabet.getValue(Arithmetic.mod(idx + kdx, modulus));
        }
        return output;
    }

    @Override
    public P[] decrypt(P[] key, P[] input) {

        checkKey(key);
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        for (int i = 0; i < input.length; i++) {
            int idx = inAlphabet.getIndex(input[i]);
            int kdx = inAlphabet.getIndex(key[i % key.length]);
            output[i] = inAlphabet.getValue(Arithmetic.mod(idx - kdx, modulus));
        }
        return output;
    }

    @Override
    public P[] generateKey(Object seed) {
        
        Random r = new Random();
        int l = (Integer) seed;
        P[] key = (P[]) Array.newInstance(Pclass, l);
        for (int i = 0; i < l; i++) {
            key[i] = inAlphabet.getValue(r.nextInt(modulus-1));
        }
        return key;
    }

    public void checkKey(P[] key) {

        if (key.length <= 0) {
            throw new IllegalArgumentException("Invalid Vigenere key: Zero-length key");
        }
    }
}
