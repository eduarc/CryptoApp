package co.edu.unal.crypto.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eduarc
 */
public class LowerCaseEnglishDict {
    
    public static final int WORDS;
    public static final String[] words;
    
    static {
        List<String> tmpWords = new ArrayList();
        BufferedReader reader = null;
        try {
            File dic = new File("dict/eng");
            reader = new BufferedReader(new FileReader(dic));
            String w;
            while ((w = reader.readLine()) != null) {
                tmpWords.add(w.toLowerCase());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LowerCaseEnglishDict.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } catch (IOException ex) {
            Logger.getLogger(LowerCaseEnglishDict.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(LowerCaseEnglishDict.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        words = tmpWords.toArray(new String[]{});
        WORDS = words.length;
    }
    
    private static void shuffle() {
        
        Random rnd = new Random();
        for (int i = words.length - 1; i > 0; i--)
        {
          int index = rnd.nextInt(i + 1);
          String a = words[index];
          words[index] = words[i];
          words[i] = a;
        }
    }
    public static boolean check(String msg) {
        
        shuffle();
        
        int l = msg.length();
        
        for (int i = 0; i < l; i++) {
            boolean found = false;
            for (String w : words) {
                int ll = w.length();
                if (i+ll-1 >= l) {
                    continue;
                }
                if (ll == 1) {
                    Character c = w.charAt(0);
                    if (c != 'a' && c != 'i') {
                        continue;
                    }
                }
                boolean ok = true;
                for (int j = 0; j < ll; j++) {
                    if (msg.charAt(i+j) != w.charAt(j)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    i += ll-1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
