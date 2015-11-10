package co.edu.unal.crypto.analyzer.ascii;

import co.edu.unal.crypto.analyzer.CryptoAnalyzer;
import co.edu.unal.crypto.alphabet.ascii.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Vigenere;
import co.edu.unal.crypto.analyzer.ConfidenceMethod;
import co.edu.unal.crypto.type.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class VigenereAnalyzer implements CryptoAnalyzer<Character, Character, Character[]> {

    private final int maxLength;
    private static final double[] pr;
    
    static {
        pr = new double[256];
        pr['e'] = 0.127;
        pr['t'] = 0.091;
        pr['a'] = 0.082;
        pr['o'] = 0.075;
        pr['i'] = 0.070;
        pr['n'] = 0.067; 
        pr['s'] = 0.063; 
        pr['h'] = 0.061;
        pr['r'] = 0.060; 
        pr['d'] = 0.043; 
        pr['l'] = 0.040; 
        pr['c'] = 0.028; 
        pr['u'] = 0.028; 
        pr['m'] = 0.024; 
        pr['w'] = 0.023; 
        pr['f'] = 0.022; 
        pr['g'] = 0.020; 
        pr['y'] = 0.020; 
        pr['p'] = 0.019; 
        pr['b'] = 0.015; 
        pr['v'] = 0.010; 
        pr['k'] = 0.008; 
        pr['j'] = 0.002; 
        pr['x'] = 0.001;
        pr['q'] = 0.001; 
        pr['z'] = 0.001;
    }
    
    public VigenereAnalyzer(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public Pair<Character[], Character[]> analyze(Character[] secret, ConfidenceMethod conf) {
        
        Vigenere<Character> cipher = new Vigenere(LowerCaseEnglish.defaultInstance);
        
        int n = secret.length;
        Character[] guess = null;
        Character[] guessKey = null;
        int maxi = 0;
        for (int m = 1; m <= maxLength; m++) {
            Character[] key = getKey(m, secret);
            Character[] tried = cipher.decrypt(key, secret);
            int freq = conf.confidence(tried);
            if (freq >= maxi) {
                maxi = freq;
                guess = tried;
                guessKey = key;
            }
        }
        return new Pair(guess, guessKey);
    }

    @Override
    public List<Pair<Character[], Character[]>> analyze(Character[] secret, int num_guesses, ConfidenceMethod conf) {
        
        PriorityQueue<Pair<Integer, Pair<Character[], Character[]>>> Q = new PriorityQueue();
        Vigenere<Character> cipher = new Vigenere(LowerCaseEnglish.defaultInstance);
        
        int n = secret.length;
        for (int m = 1; m <= maxLength; m++) {
            Character[] key = getKey(m, secret);
            Character[] tried = cipher.decrypt(key, secret);
            int freq = conf.confidence(tried);
            Q.add(new Pair(-freq, new Pair(tried, key)));
        }
        int sz = Math.min(num_guesses, Q.size());
        List<Pair<Character[], Character[]>> best = new ArrayList(sz);
        for (int i = 0; i < sz; i++) {
            best.add(Q.poll().second);
        }
        return best;
    }
    
    private double M(int g, int[] freq, double n) {
        
        double p = 0;
        for (int i = 0; i < 26; i++) {
            p += pr['a'+i]*(freq[(i+g)%26]/n);
        }
        return p;
    }
    
    private Character[] getKey(int m, Character[] secret) {
        
        Character[] key = new Character[m];
        int n = secret.length;
        for (int l = 0; l < m; l++) {
            int[] freq = new int[26];
            for (int i = l; i < n; i += m) {
                freq[secret[i]-'a']++;
            }
            double bestG = 1;
            for (int g = 0; g < 26; g++) {
                double q = M(g, freq, n/m);
                if (Math.abs(0.065-q) < bestG) {
                    bestG = Math.abs(0.065-q);
                    key[l] = (char)('a'+g);
                }
            }
        }
        return key;
    }
// TEST 
// key: janet
// src: thealmondtreewasintentativeblossomthedayswerelongeroftenendingwithmagnificenteveningsofcorrugatedpinskiesthehuntingseasonwasoverwithhoundsandgunsputawayforsixmonthsthevineyardswerebusyagainasthewellorganizedfarmerstreatedtheirvinesandthemorelackadaisicalneighborshurriedtodothepruningtheyshouldhavedoneinnovember
// dst: chreevoahmaeratbiaxxwtnxbeeophbsbqmqeqerbwrvxuoakxaosxxweahbwgjmmqmnkgrfvgxwtrzxwiaklxfpskautemndcmgbkvilchrlnwtvrzbenwhwwnwheeeabchusnwdfegmghrlyugepjysskbikqhwtuwmqeimgnynvwbwrvxkufctpavrtbtuipnlyskpaamsndsekveewmaenxxmtuibavvrxbaahmqezsknlngdjdnmlrcnpgnitluxrflnarviwcoqsmqecvnwiakmqelwaxuyhajvrhhwevrgxvrqunr
}
