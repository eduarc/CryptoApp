package co.edu.unal.crypto.analyzer.ascii;

import co.edu.unal.crypto.analyzer.CryptoAnalyzer;
import co.edu.unal.crypto.alphabet.ascii.ASCII;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.analyzer.ConfidenceMethod;
import co.edu.unal.crypto.util.PollardsRho;
import co.edu.unal.crypto.type.Pair;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class RSAAnalyzer implements CryptoAnalyzer<BigInteger, Character, RSA.Key> {

    BigInteger n;
    BigInteger e;
    
    public RSAAnalyzer(BigInteger n, BigInteger e) {
        
        this.n = n;
        this.e = e;
    }

    @Override
    public Pair<Character[], RSA.Key> analyze(BigInteger[] secret, ConfidenceMethod match) {
        
        PollardsRho magic = new PollardsRho();
        List<BigInteger> factors = magic.factor(n);

        if (factors.size() != 2) {
            throw new IllegalArgumentException("Invalid key value. N must be two-prime factorizable");
        }
        BigInteger p = factors.get(0);
        BigInteger q = factors.get(1);
        
        RSA<Character> rsa = new RSA(ASCII.defaultInstance);
        RSA.Key guessKey = new RSA.Key(p, q, e);
        Character[] guess = rsa.decrypt(guessKey, secret);
        return new Pair(guess, guessKey);
    }

    @Override
    public List<Pair<Character[], RSA.Key>> analyze(BigInteger[] secret, int n, ConfidenceMethod match) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
