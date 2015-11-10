package co.edu.unal.crypto.analyzer;

import co.edu.unal.crypto.type.Pair;
import java.util.List;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 * @param <P>
 * @param <C>
 * @param <K>
 */
public interface CryptoAnalyzer<P, C, K> {
    
    public Pair<C[], K>  analyze(P[] secret, ConfidenceMethod<C> confidence);
    public List<Pair<C[], K>> analyze(P[] secret, int n, ConfidenceMethod<C> confidence);
}
