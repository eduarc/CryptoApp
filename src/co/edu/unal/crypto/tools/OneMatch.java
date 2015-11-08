package co.edu.unal.crypto.tools;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class OneMatch implements ConfidenceMethod {

    private final Dictionary dic;
            
    public OneMatch(Dictionary dic) {
        this.dic = dic;
    }
    
    @Override
    public int confidence(String text) {
        
        int c = 0;
        for (String w : dic.getWords()) {
            if (text.contains(w)) {
                c++;
            }
        }
        return c;
    }
    
}
