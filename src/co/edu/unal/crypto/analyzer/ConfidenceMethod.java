package co.edu.unal.crypto.analyzer;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 */
public interface ConfidenceMethod<P> {
    
    public int confidence(P[] input);
}
