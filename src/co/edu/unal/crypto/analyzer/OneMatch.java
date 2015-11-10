package co.edu.unal.crypto.analyzer;

import co.edu.unal.crypto.alphabet.Dictionary;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P> 
 */
public class OneMatch<P> implements ConfidenceMethod<P> {

    private final Dictionary<P> dic;
    private final Matcher<P> matcher;
    
    public OneMatch(Dictionary<P> dic, Matcher<P> matcher) {
        
        this.dic = dic;
        this.matcher = matcher;
    }
    
    @Override
    public int confidence(P[] guess) {
        
        int c = 0;
        for (P[] w : dic.getWords()) {
            if (matcher.contains(w, guess) != -1) {
                c++;
            }
        }
        return c;
    }
}
