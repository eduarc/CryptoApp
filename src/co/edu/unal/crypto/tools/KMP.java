package co.edu.unal.crypto.tools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduarc
 */
public class KMP {
    
    private static int[] computeLSP(String pattern) {
        
        int[] lsp = new int[pattern.length()];
        lsp[0] = 0;
        for (int i = 1; i < pattern.length(); i++) {
            int j = lsp[i-1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = lsp[j-1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            lsp[i] = j;
        }
        return lsp;
    }
    
    public static List<Integer> searchAll(String pattern, String text) {
        
        int[] lsp = computeLSP(pattern);
        List<Integer> matches = new ArrayList();
        
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = lsp[j-1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
                if (j == pattern.length()) {
                    matches.add(i-j+1);
                    j = 0;
                }
            }
        }
        return matches;
    }
    
    public static int count(String pattern, char[] text) {
        
        int[] lsp = computeLSP(pattern);
        int matches = 0;
        
        int j = 0;
        for (int i = 0; i < text.length; i++) {
            while (j > 0 && text[i] != pattern.charAt(j)) {
                j = lsp[j-1];
            }
            if (text[i] == pattern.charAt(j)) {
                j++;
                if (j == pattern.length()) {
                    matches++;
                    j = 0;
                }
            }
        }
        return matches;
    }
    
    public static int search(String pattern, String text) {
        
        int[] lsp = computeLSP(pattern);

        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = lsp[j-1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
                if (j == pattern.length()) {
                    return i-(j-1);
                }
            }
        }
        return -1;
    }
}
