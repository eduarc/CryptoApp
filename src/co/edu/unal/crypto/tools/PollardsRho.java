package co.edu.unal.crypto.tools;
/******************************************************************************
 *  Compilation:  javac PollardRho.java
 *  Execution:    java PollardRho N
 *  
 *  Factor N using the Pollard-Rho method.
 *
 *  % java PollardRho 44343535354351600000003434353
 *  149
 *  329569479697
 *  903019357561501
 *
 ******************************************************************************/

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
    
public class PollardsRho {
    
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE  = new BigInteger("1");
    private final static BigInteger TWO  = new BigInteger("2");
    private final static SecureRandom random = new SecureRandom();
    private final List<BigInteger> factors;
    
    public PollardsRho() {
        
        factors = new ArrayList();
    }

    public List<BigInteger> factor(BigInteger N) {
        
        factors.clear();
        rec_factor(N);
        return factors;
    }
    
    public BigInteger nonTrivialFactor(BigInteger N) {
        
        if (N.compareTo(ONE) == 0) {
            return null;
        }
        if (N.isProbablePrime(50)) {
            return N;
        }
        BigInteger divisor = rho(N);
        BigInteger f = nonTrivialFactor(divisor);
        if (f != null) {
            return f;
        }
        return nonTrivialFactor(N.divide(divisor));
    }
    
    private void rec_factor(BigInteger N) {
        
        if (N.compareTo(ONE) == 0) {
            return;
        }
        if (N.isProbablePrime(50)) {
            factors.add(N); 
            return; 
        }
        BigInteger divisor = rho(N);
        rec_factor(divisor);
        rec_factor(N.divide(divisor));
    }
    
    private BigInteger rho(BigInteger N) {
        
        BigInteger divisor;
        BigInteger c  = new BigInteger(N.bitLength(), random);
        BigInteger x  = new BigInteger(N.bitLength(), random);
        BigInteger xx = x;
            // check divisibility by 2
        if (!N.testBit(0)) return TWO;
        //if (N.mod(TWO).compareTo(ZERO) == 0) return TWO;
        do {
            x  =  x.multiply(x).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            divisor = x.subtract(xx).gcd(N);
        } while((divisor.compareTo(ONE)) == 0);

        return divisor;
    }
}
