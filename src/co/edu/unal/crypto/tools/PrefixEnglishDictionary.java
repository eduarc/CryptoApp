package co.edu.unal.crypto.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class PrefixEnglishDictionary extends Dictionary {
    
    public static final int WORDS;
    public static final List<String> words;
    public static final PrefixEnglishDictionary defaultInstance;
    
    static {
        words = new ArrayList();
        BufferedReader reader = null;
        try {
            File dic = new File("dict/eng");
            reader = new BufferedReader(new FileReader(dic));
            String w;
            while ((w = reader.readLine()) != null) {
                words.add(w);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrefixEnglishDictionary.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } catch (IOException ex) {
            Logger.getLogger(PrefixEnglishDictionary.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(PrefixEnglishDictionary.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        WORDS = words.size();
        defaultInstance = new PrefixEnglishDictionary();
    }

    @Override
    public List<String> getWords() {
        return words;
    }
}
