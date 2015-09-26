package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.types.Function;
import java.lang.reflect.Array;
import java.util.TreeMap;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 * @param <C> 
 */
public class Substitution<P, C> extends Cryptosystem<P, C, Substitution.Key<P, C>> {

    @SuppressWarnings("rawtypes")
    private final Class Cclass;
    @SuppressWarnings("rawtypes")
    private final Class Pclass;

    public Substitution(Alphabet<P> inAlpha, Alphabet<C> outAlpha) {
        super(inAlpha, outAlpha);

        Pclass = inAlpha.getValue(0).getClass();
        Cclass = outAlpha.getValue(0).getClass();
    }

    @Override
    public C[] encrypt(Key<P, C> key, P[] input) {

        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid Subtitution Key");
        }
        @SuppressWarnings("unchecked")
        C[] output = (C[]) Array.newInstance(Cclass, input.length);
        for (int i = 0; i < input.length; ++i) {
            output[i] = key.eval(input[i]);
        }
        return output;
    }

    @Override
    public P[] decrypt(Key<P, C> key, C[] input) {

        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid Subtitution Key");
        }
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);
        for (int i = 0; i < input.length; ++i) {
            output[i] = key.inverse(input[i]);
        }
        return output;
    }

    @Override
    public Key<P, C> generateKey(Object seed) {
        return null;
    }

    @Override
    public boolean isValidKey(Key<P, C> key) {

        for (int i = 0; i < inAlphabet.getSize(); ++i) {
            if (!key.mapped(inAlphabet.getValue(i))) {
                return false;
            }
        }
        return true;
    }

    public static class Key<P, C> implements Function<P, C> {

        private final TreeMap<P, C> func;
        private final TreeMap<C, P> inve;

        public Key() {
            func = new TreeMap<>();
            inve = new TreeMap<>();
        }

        @Override
        public void set(P x, C y) {

            func.put(x, y);
            inve.put(y, x);
        }

        @Override
        public boolean mapped(P x) {
            return func.containsKey(x);
        }

        @Override
        public C eval(P x) {
            return func.get(x);
        }

        @Override
        public P inverse(C y) {
            return inve.get(y);
        }
    }
}
