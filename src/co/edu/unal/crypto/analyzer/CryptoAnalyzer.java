package co.edu.unal.crypto.analyzer;

import co.edu.unal.crypto.tools.ConfidenceMethod;
import co.edu.unal.crypto.types.Pair;
import java.util.List;

/**
 *
 * @author eduarc
 * @param <P>
 * @param <C>
 * @email eduarcastrillo@gmail.com
 */
public interface CryptoAnalyzer<P, C, K> {
    
    public Pair<C[], K>  analyze(P[] secret, ConfidenceMethod confidence);
    public List<Pair<C[], K>> analyze(P[] secret, int n, ConfidenceMethod confidence);
}
