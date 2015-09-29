package cryptoapp.crack;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.analyzer.CryptoAnalyzer;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.tools.PollardsRho;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class RSAAnalyzer implements CryptoAnalyzer<BigInteger, Character> {

    BigInteger n;
    BigInteger e;
    
    public RSAAnalyzer(BigInteger n, BigInteger e) {
        
        this.n = n;
        this.e = e;
    }

    @Override
    public Character[] analyze(BigInteger[] secret) {
        
        PollardsRho magic = new PollardsRho();
        List<BigInteger> factors = magic.factor(n);

        if (factors.size() != 2) {
            throw new IllegalArgumentException("Invalid key value. N must be two-prime factorizable");
        }
        BigInteger p = factors.get(0);
        BigInteger q = factors.get(1);
        
        RSA<Character> rsa = new RSA(ASCII.defaultInstance);
        return rsa.decrypt(new RSA.Key(p, q, e), secret);
    }
    
}
