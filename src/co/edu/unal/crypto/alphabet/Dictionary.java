package co.edu.unal.crypto.alphabet;

import java.util.List;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @param <P>
 * @email eduarcastrillo@gmail.com
 */
public interface Dictionary<P> {
    
    public List<P[]> getWords();
}
