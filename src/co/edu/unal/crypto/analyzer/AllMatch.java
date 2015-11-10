package co.edu.unal.crypto.analyzer;

import co.edu.unal.crypto.alphabet.Dictionary;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 */
public class AllMatch<P> implements ConfidenceMethod<P> {

    private final Dictionary<P> dic;
    private final Matcher<P> matcher;
    
    public AllMatch(Dictionary<P> dic, Matcher<P> matcher) {
        
        this.dic = dic;
        this.matcher = matcher;
    }
    
    @Override
    public int confidence(P[] text) {
        
        int c = 0;
        for (P[] w : dic.getWords()) {
            c += matcher.count(w, text);
        }
        return c;
    }
}
