package co.edu.unal.crypto.analyzer;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.Caesar;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.crypto.tools.ConfidenceMethod;
import co.edu.unal.crypto.types.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class CaesarAnalyzer implements CryptoAnalyzer<Character, Character, Integer> {

    @Override
    public Pair<Character[], Integer> analyze(Character[] secret, ConfidenceMethod conf) {
        
        Caesar<Character> cipher = new Caesar(ASCII.defaultInstance);
        Character[] guess = null;
        Integer guessKey = null;
        int maxi = 0;
        for (int i = 0; i < ASCII.SIZE; i++) {
            Character[] tried = cipher.decrypt(i, secret);
            int freq = conf.confidence(CharStream.toString(tried));
            if (freq >= maxi) {
                maxi = freq;
                guess = tried;
                guessKey = i;
            }
        }
        return new Pair(guess, guessKey);
    }

    @Override
    public List<Pair<Character[], Integer>> analyze(Character[] secret, int n, ConfidenceMethod conf) {
        
        PriorityQueue<Pair<Integer, Pair<Character[], Integer>>> Q = new PriorityQueue();
        Caesar<Character> cipher = new Caesar(ASCII.defaultInstance);
        
        for (int i = 0; i < ASCII.SIZE; i++) {
            Character[] tried = cipher.decrypt(i, secret);
            int freq = conf.confidence(CharStream.toString(tried));
            Q.add(new Pair(-freq, new Pair(tried, i)));
        }
        int sz = Math.min(n, Q.size());
        List<Pair<Character[], Integer>> best = new ArrayList(sz);
        for (int i = 0; i < sz; i++) {
            best.add(Q.poll().second);
        }
        return best;
    }
}
