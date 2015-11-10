package co.edu.unal.crypto.analyzer;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @eduarcastrillo@gmail.com
 * @param <P>
 */
public abstract class Matcher<P> {
    
    public abstract int count(P[] pattern, P[] text);
    
    public abstract int contains(P[] pattern, P[] text);
}
