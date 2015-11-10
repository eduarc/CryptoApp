package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.type.Function;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        checkKey(key);
        @SuppressWarnings("unchecked")
        C[] output = (C[]) Array.newInstance(Cclass, input.length);
        for (int i = 0; i < input.length; ++i) {
            output[i] = key.eval(input[i]);
        }
        return output;
    }

    @Override
    public P[] decrypt(Key<P, C> key, C[] input) {

        checkKey(key);
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);
        for (int i = 0; i < input.length; ++i) {
            output[i] = key.inverse(input[i]);
        }
        return output;
    }

    @Override
    public Key<P, C> generateKey(Object seed) {
        
        int inSZ = inAlphabet.getSize();
        int ouSZ = outAlphabet.getSize();
        if (inSZ > ouSZ) {
            throw new IllegalArgumentException("No injective function can be constructed: |Input Alpha| < |Output Alpha|");
        }
        List<Integer> map = new ArrayList(inSZ);
        for (int i = 0; i < ouSZ; i++) {
            map.add(i);
        }
        Collections.shuffle(map);
        Key k = new Key();
        for (int x = 0; x < inSZ; x++) {
            int y = map.get(x);
            k.set(inAlphabet.getValue(x), outAlphabet.getValue(y));
        }
        return k;
    }

    public void checkKey(Key<P, C> key) {

        for (int i = 0; i < inAlphabet.getSize(); ++i) {
            P p = inAlphabet.getValue(i);
            C c = key.eval(p);
            if (c == null || key.inverse(c) != p) {
                throw new IllegalArgumentException("Invalid Substitution key: The key must be an inyective function on the input alphabet");
            }
        }
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
        
        @Override
        public String toString() {
            
            String s = "";
            for (P p : func.keySet()) {
                s += eval(p);
            }
            return s;
        }
    }
}
