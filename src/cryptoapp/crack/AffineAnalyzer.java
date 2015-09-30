package cryptoapp.crack;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.analyzer.CryptoAnalyzer;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.tools.Arithmetic;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.crypto.tools.LowerCaseEnglishDict;
import co.edu.unal.crypto.types.Pair;

/**
 *
 * @author eduarc
 */
public class AffineAnalyzer implements CryptoAnalyzer<Character, Character> {

    @Override
    public Character[] analyze(Character[] secret) {
        
        Affine<Character> cipher = new Affine(LowerCaseEnglish.defaultInstance);
        Character[] guess = null;
        for (int a = 0; a < 26; a++) {
            if (Arithmetic.areCoprimes(a, 26)) {
                for (int b = 0; b < 26; b++) {
                    guess = cipher.decrypt(new Pair<>(a, b), secret);
                    if (LowerCaseEnglishDict.check(CharStream.toString(guess))) {
                        return guess;
                    }
                }
            }
        }
        return guess;
    }
    
}
