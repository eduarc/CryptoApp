package co.edu.unal.crypto.tools;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class Hashy {
    
    public static final int MAX_LEN = 100001;
    public static final long[] pow31;
    private final long[] hash31;
    
    static {
        pow31 = new long[MAX_LEN];
        pow31[0] = 1;
        for (int i = 1; i < MAX_LEN; i++) {
            pow31[i] = pow31[i-1]*31L;
        }
    }
    
    public Hashy(String s) {
        
        int len = s.length();
        hash31 = new long[len];
        hash31[0] = s.charAt(0);
        for (int i = 1; i < len; i++) {
            hash31[i] = hash31[i-1]*31L+s.charAt(i);
        }
    }
    
    public long hash31(int i, int j) {
        
        long h = hash31[j];
        if(i > 0) {
            h -= hash31[i-1]*pow31[j-i+1];
        }
        return h;
    }
}
