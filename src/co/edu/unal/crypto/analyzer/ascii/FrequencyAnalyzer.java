package co.edu.unal.crypto.analyzer.ascii;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class FrequencyAnalyzer {
    
    public static Map<String, Integer> trigrams(Character[] str) {
        
        int len = str.length;
        Map<String, Integer> freq = new TreeMap();
        
        for (int i = 0; i+2 < len; i++) {
            if ((!Objects.equals(str[i], str[i+1]) && !Objects.equals(str[i], str[i+2]) && !Objects.equals(str[i+1], str[i+2])) ||
                    (!Objects.equals(str[i], str[i+1]) && Objects.equals(str[i], str[i+2]))) {
                String tri = "";
                tri += str[i+0];
                tri += str[i+1];
                tri += str[i+2];
                Integer f = freq.get(tri);
                if (f == null) {
                    f = 0;
                }
                freq.put(tri, ++f);
            }
        }
        return freq;
    }
    
    public static Map<String, Integer> digrams(Character[] str) {
        
        int len = str.length;
        Map<String, Integer> freq = new TreeMap();
        
        for (int i = 0; i+1 < len; i++) {
            if (!Objects.equals(str[i], str[i+1])) {
                String tri = "";
                tri += str[i+0];
                tri += str[i+1];
                Integer f = freq.get(tri);
                if (f == null) {
                    f = 0;
                }
                freq.put(tri, ++f);
            }
        }
        return freq;
    }
    
    public static Map<Character, Integer> characters(Character[] str) {
        
        int len = str.length;
        Map<Character, Integer> freq = new TreeMap();
        
        for (int i = 0; i < len; i++) {
            Integer f = freq.get(str[i]);
            if (f == null) {
                f = 0;
            }
            freq.put(str[i], ++f);
        }
        return freq;
    }
}
