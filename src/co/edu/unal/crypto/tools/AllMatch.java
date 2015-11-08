package co.edu.unal.crypto.tools;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class AllMatch implements ConfidenceMethod {

    private final Dictionary dic;
    
    public AllMatch(Dictionary dic) {
        this.dic = dic;
    }
    
    @Override
    public int confidence(String text) {
        
        char[] arr = text.toCharArray();
        int c = 0;
        for (String w : dic.getWords()) {
            c += KMP.count(w, arr);
        }
        return c;
    }

}
