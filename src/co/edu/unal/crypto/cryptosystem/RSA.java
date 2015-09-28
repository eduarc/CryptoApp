package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class RSA<P> extends Cryptosystem<P, BigInteger, RSA.Key> {

    @SuppressWarnings("rawtypes")
    private final Class Pclass;
    private final int characterLength;
    private final int modulus;

    public RSA(Alphabet<P> alpha) {
        super(alpha);

        Pclass = alpha.getValue(0).getClass();
        characterLength = (alpha.getSize() + "").length();
        modulus = alpha.getSize();
    }

    @Override
    public BigInteger[] encrypt(Key key, P[] input) {

        checkKey(key);
        int msgLength = input.length;
        int blockSize = getBlockSize(key.n);
        int r = msgLength % blockSize;
        int dummy = r == 0 ? 0 : blockSize - r;

        P[] data = Arrays.copyOf(input, msgLength + dummy);
        for (int i = data.length - 1; dummy > 0; i--, dummy--) {
            data[i] = inAlphabet.getValue(0);
        }
        BigInteger[] output = new BigInteger[data.length / blockSize];

        for (int i = 0, j = 0; i < data.length; i += blockSize) {
            String block = "";
            for (int k = 0; k < blockSize; k++) {
                block += pad(inAlphabet.getIndex(data[i + k]) + "", characterLength);
            }
            output[j++] = new BigInteger(block).modPow(key.e, key.n);
        }
        return output;
    }

    @SuppressWarnings("unchecked")
    @Override
    public P[] decrypt(Key key, BigInteger[] input) {

        checkKey(key);
        int blockSize = getBlockSize(key.n);
        List<P> output = new ArrayList<>();

        for (BigInteger bi : input) {
            String block = bi.modPow(key.d, key.n).toString();
            block = pad(block, blockSize * characterLength);
            for (int i = 0; i < block.length(); i += characterLength) {
                int idx = 0;
                for (int j = 0; j < characterLength; j++) {
                    idx *= 10;
                    idx += block.charAt(i + j) - '0';
                }
                output.add(inAlphabet.getValue(idx));
            }
        }
        return output.toArray((P[]) Array.newInstance(Pclass, 0));
    }

    @Override
    public Key generateKey(Object seed) {
        return null;
    }

    private void checkKey(Key key) {
        
        if (key.privateKey) {
            if (!key.p.isProbablePrime(98) || !key.q.isProbablePrime(98)) {
                throw new IllegalArgumentException("Invalid RSA key: p or q aren't primes numbers");
            }
            if (!key.phi.gcd(key.e).equals(BigInteger.ONE)) {
                throw new IllegalArgumentException("Invalid RSA key: (p-1)*(q-1) and e aren't coprimes");
            }
        }
        if (!key.n.gcd(key.e).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("Invalid RSA key: n and e aren't coprimes");
        }
    }
    
    private String pad(String str, int len) {

        int r = len - str.length();
        if (str.length() > len) {
            r = str.length() % len;
        }
        for (; r-- > 0;) {
            str = "0" + str;
        }
        return str;
    }

    private int getBlockSize(BigInteger n) {

        BigInteger pow = BigInteger.TEN.pow(characterLength);
        BigInteger val = BigInteger.valueOf(modulus - 1);
        BigInteger bound = BigInteger.valueOf(0);

        while (bound.compareTo(n) < 0) {
            bound = bound.multiply(pow).add(val);
        }
        return bound.toString().length() / characterLength - 1;
    }

    /* RSA Key */
    public static class Key {

        protected boolean privateKey;
        protected BigInteger p;
        protected BigInteger q;
        protected BigInteger e;
        protected BigInteger d;
        protected BigInteger n;
        protected BigInteger phi;

        public Key(BigInteger p, BigInteger q, BigInteger e) {

            privateKey = true;
            this.p = p;
            this.q = q;
            this.e = e;
            this.n = p.multiply(q);
            this.phi = p.subtract(BigInteger.ONE);
            this.phi = phi.multiply(q.subtract(BigInteger.ONE));
            this.d = e.modInverse(phi);
        }
        
        public Key(BigInteger n, BigInteger e) {
            
            privateKey = false;
            this.n = n;
            this.e = e;
        }
    }
}
