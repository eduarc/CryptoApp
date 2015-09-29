package co.edu.unal.crypto.analizer;

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
