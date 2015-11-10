package co.edu.unal.crypto.analyzer;

/**
 *
 * @author eduarc
 * @param <P>
 */
public class KMP<P> extends Matcher<P> {
    
    private int[] computeLSP(P[] pattern) {
        
        int[] lsp = new int[pattern.length];
        lsp[0] = 0;
        for (int i = 1; i < pattern.length; i++) {
            int j = lsp[i-1];
            while (j > 0 && pattern[i] != pattern[j]) {
                j = lsp[j-1];
            }
            if (pattern[i] == pattern[j]) {
                j++;
            }
            lsp[i] = j;
        }
        return lsp;
    }
    
    @Override
    public int count(P[] pattern, P[] text) {
        
        int[] lsp = computeLSP(pattern);
        int matches = 0;
        
        int j = 0;
        for (int i = 0; i < text.length; i++) {
            while (j > 0 && text[i] != pattern[j]) {
                j = lsp[j-1];
            }
            if (text[i] == pattern[j]) {
                j++;
                if (j == pattern.length) {
                    matches++;
                    j = 0;
                }
            }
        }
        return matches;
    }
    
    @Override
    public int contains(P[] pattern, P[] text) {
        
        int[] lsp = computeLSP(pattern);

        int j = 0;
        for (int i = 0; i < text.length; i++) {
            while (j > 0 && text[i] != pattern[j]) {
                j = lsp[j-1];
            }
            if (text[i] == pattern[j]) {
                j++;
                if (j == pattern.length) {
                    return i-(j-1);
                }
            }
        }
        return -1;
    }
}
