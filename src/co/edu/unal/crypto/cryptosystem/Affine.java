package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.tools.Arithmetic;
import co.edu.unal.crypto.tools.ModularArithmetic;
import co.edu.unal.crypto.types.Pair;
import java.lang.reflect.Array;

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

        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid Affine Key");
        }
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        int p = key.first;
        int k = key.second;
        p = ModularArithmetic.modulo(p, modulus);
        k = ModularArithmetic.modulo(k, modulus);

        for (int i = 0; i < input.length; ++i) {
            int idx = inAlphabet.getIndex(input[i]);
            output[i] = inAlphabet.getValue((idx * p + k) % modulus);
        }
        return output;
    }

    @Override
    public P[] decrypt(Pair<Integer, Integer> key, P[] input) {

        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid Affine Key");
        }
        @SuppressWarnings("unchecked")
        P[] output = (P[]) Array.newInstance(Pclass, input.length);

        int q = ModularArithmetic.multiplicativeInverse(key.first, modulus);
        int k = key.second;
        q = ModularArithmetic.modulo(q, modulus);
        k = ModularArithmetic.modulo(k, modulus);

        for (int i = 0; i < input.length; ++i) {
            int idx = inAlphabet.getIndex(input[i]);
            output[i] = inAlphabet.getValue((q * (idx - k + modulus)) % modulus);
        }
        return output;
    }

    @Override
    public boolean isValidKey(Pair<Integer, Integer> key) {
        return Arithmetic.areCoprimes(key.first, modulus);
    }

    @Override
    public Pair<Integer, Integer> generateKey(Object seed) {
        return null;
    }
}
