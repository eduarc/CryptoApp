package co.edu.unal.crypto.alphabet.ascii;

import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.util.CharStream;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class SimpleAlphabet implements Alphabet<Character> {

    private static final String alpha = " abcdefghijklmnopqrstuvwxyz0123456789,.:;?!()*+-=";
    private static final int[] index;
    private static final Character[] values;
    public static final int SIZE = alpha.length();
    public static final SimpleAlphabet defaultInstance;
    
    static {
        values = CharStream.fromString(alpha);
        index = new int[256];
        for (int i = 0; i < alpha.length(); i++) {
            index[values[i]] = i;
        }
        defaultInstance = new SimpleAlphabet();
    }
    
    @Override
    public int getIndex(Character val) {
        return index[val];
    }

    @Override
    public Character getValue(int idx) {
        return values[idx];
    }

    @Override
    public Character[] getValues() {
        return values;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
