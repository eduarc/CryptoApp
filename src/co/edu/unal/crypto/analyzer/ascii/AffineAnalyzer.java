package co.edu.unal.crypto.analyzer.ascii;

import co.edu.unal.crypto.analyzer.CryptoAnalyzer;
import co.edu.unal.crypto.alphabet.ascii.SimpleAlphabet;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.util.Arithmetic;
import co.edu.unal.crypto.analyzer.ConfidenceMethod;
import co.edu.unal.crypto.type.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class AffineAnalyzer implements CryptoAnalyzer<Character, Character, Pair<Integer, Integer>> {

    @Override
    public Pair<Character[], Pair<Integer, Integer>> analyze(Character[] secret, ConfidenceMethod conf) {
        
        Affine<Character> cipher = new Affine(SimpleAlphabet.defaultInstance);
        Character[] guess = null;
        Pair<Integer, Integer> guessKey = null;
        int maxi = 0;
        for (int a = 0; a < SimpleAlphabet.SIZE; a++) {
            if (Arithmetic.coprimes(a, SimpleAlphabet.SIZE)) {
                for (int b = 0; b < SimpleAlphabet.SIZE; b++) {
                    Pair<Integer, Integer> key = new Pair<>(a, b);
                    Character[] tried = cipher.decrypt(key, secret);
                    int freq = conf.confidence(tried);
                    if (freq >= maxi) {
                        maxi = freq;
                        guess = tried;
                        guessKey = key;
                    }
                }
            }
        }
        return new Pair(guess, guessKey);
    }

    @Override
    public List<Pair<Character[], Pair<Integer, Integer>>> analyze(Character[] secret, int n, ConfidenceMethod conf) {
        
        PriorityQueue<Pair<Integer, Pair<Character[], Pair<Integer, Integer>>>> Q = new PriorityQueue();
        Affine<Character> cipher = new Affine(SimpleAlphabet.defaultInstance);
        
        for (int a = 0; a < SimpleAlphabet.SIZE; a++) {
            if (Arithmetic.coprimes(a, SimpleAlphabet.SIZE)) {
                for (int b = 0; b < SimpleAlphabet.SIZE; b++) {
                    Pair<Integer, Integer> key = new Pair<>(a, b);
                    Character[] tried = cipher.decrypt(key, secret);
                    int freq = conf.confidence(tried);
                    Q.add(new Pair(-freq, new Pair(tried, key)));
                }
            }
        }
        int sz = Math.min(n, Q.size());
        List<Pair<Character[], Pair<Integer, Integer>>> best = new ArrayList(sz);
        for (int i = 0; i < sz; i++) {
            best.add(Q.poll().second);
        }
        return best;
    }
}
