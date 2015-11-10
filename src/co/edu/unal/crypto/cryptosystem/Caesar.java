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
public class Caesar<P> extends Cryptosystem<P, P, Integer> {

    @SuppressWarnings("rawtypes")
    private final Class Pclass;
    private final int modulus;

    public Caesar(Alphabet<P> alpha) {
        super(alpha, alpha);

        Pclass = alpha.getValue(0).getClass();
        modulus = alpha.getSize();
    }

    @Override
    public P[] encrypt(Integer key, P[] input) {

        checkKey(key);
        key = Arithmetic.mod(key, modulus);

        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        for (int i = 0; i < input.length; ++i) {
            int idx = Arithmetic.mod(inAlphabet.getIndex(input[i]) + key, modulus);
            output[i] = inAlphabet.getValue(idx);
        }
        return output;
    }

    @Override
    public P[] decrypt(Integer key, P[] input) {

        checkKey(key);
        key = Arithmetic.mod(key, modulus);

        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        for (int i = 0; i < input.length; ++i) {
            int idx = Arithmetic.mod(outAlphabet.getIndex(input[i]) - key, modulus);
            output[i] = inAlphabet.getValue(idx);
        }
        return output;
    }

    public void checkKey(Integer key) {
        
    }

    @Override
    public Integer generateKey(Object seed) {
        
        Random r = new Random();
        return r.nextInt()%modulus;
    }
}
