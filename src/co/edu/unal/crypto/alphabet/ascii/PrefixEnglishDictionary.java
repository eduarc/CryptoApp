package co.edu.unal.crypto.alphabet.ascii;

import co.edu.unal.crypto.alphabet.Dictionary;
import co.edu.unal.crypto.util.CharStream;
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
public class PrefixEnglishDictionary implements Dictionary<Character> {
    
    public static final int WORDS;
    public static final List<Character[]> words;
    public static final PrefixEnglishDictionary defaultInstance;
    
    static {
        words = new ArrayList();
        BufferedReader reader = null;
        try {
            File dic = new File("dict/eng");
            reader = new BufferedReader(new FileReader(dic));
            String w;
            while ((w = reader.readLine()) != null) {
                Character[] cw = CharStream.fromString(w);
                words.add(cw);
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
    public List<Character[]> getWords() {
        return words;
    }

}
