package cryptoapp.crack;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.analizer.CryptoAnalyzer;
import co.edu.unal.crypto.cryptosystem.Caesar;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.crypto.tools.LowerCaseEnglishDict;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class CaesarAnalyzer implements CryptoAnalyzer<Character, Character> {

    @Override
    public Character[] analyze(Character[] secret) {
        
        Caesar<Character> cipher = new Caesar(LowerCaseEnglish.defaultInstance);
        Character[] guess = null;
        for (int i = 0; i < 26; i++) {
            guess = cipher.decrypt(i, secret);
            if (LowerCaseEnglishDict.check(CharStream.toString(guess))) {
                return guess;
            }
        }
        return guess;
    }
}
