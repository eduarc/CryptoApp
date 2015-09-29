package co.edu.unal.crypto.analyzer;

/**
 *
 * @author eduarc
 * @param <P>
 * @param <C>
 * @email eduarcastrillo@gmail.com
 */
public interface CryptoAnalyzer<P, C> {
    
    public C[] analyze(P[] secret);
}
