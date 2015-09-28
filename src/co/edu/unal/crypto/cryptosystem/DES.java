package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.types.Pair;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class DES extends Cryptosystem<Long, Long, Long> {

    @Override
    public Long[] encrypt(Long key, Long[] input) {

        checkKey(key);
        Long[] k = roundKeys(key);
        return process(k, input);
    }

    @Override
    public Long[] decrypt(Long key, Long[] input) {

        checkKey(key);
        Long[] k = roundKeys(key);
        for (int i = 0, j = k.length-1; i < j; i++, j--) {
            Long t = k[i];
            k[i] = k[j];
            k[j] = t;
        }
        return process(k, input);
    }

    @Override
    public Long generateKey(Object seed) {
        return 0L;
    }

    public void checkKey(Long key) {

    }

    private Long[] process(Long[] k, Long[] input) {

        Long[] output = new Long[input.length];
        Long[] l = new Long[17];
        Long[] r = new Long[17];

        for (int j = 0; j < input.length; j++) {
            input[j] = swap(input[j], 64);
            Long m = apply(IP, input[j]);
            Pair<Long, Long> lr = split(m, 32);
            l[0] = lr.first;
            r[0] = lr.second;
            for (int i = 1; i <= 16; i++) {
                l[i] = r[i-1];
                r[i] = l[i-1]^F(r[i-1], k[i-1]);
            }
            output[j] = join(r[16], l[16], 32);
            output[j] = apply(FP, output[j]);
            output[j] = swap(output[j], 64);
        }
        return output;
    }

    private Long swap(Long v, int bits) {

        Long r = 0L;
        for (int i = 0; i < bits; i++) {
            if (((v>>i)&1) == 1) {
                r |= 1L<<(bits-i-1);
            }
        }
        return r;
    }

    private Pair<Long, Long> split(Long v, int bits) {

        Long c = v&~((~0L)<<bits);
        Long d = (v>>bits)&~((~0L)<<bits);
        return new Pair(c, d);
    }

    private Long join(Long c, Long d, int bits) {
        return (c&~((~0L)<<bits))|(d<<bits);
    }

    private Long leftShift(Long v, int s, int bits) {

        if (s == 0) {
            return v;
        }
        v &= ~((~0L)<<bits);
        Long cycled = v&~((~0L)<<s);
        v >>= s;
        cycled <<= bits-s;
        return v|cycled;
    }

    private Long apply(int[] P, Long v) {
        
        Long p = 0L;
        for (int i = 0; i < P.length; i++) {
            Long b = (v>>P[i])&1;
            p |= (b<<i);
        }
        return p;
    }
    
    private Long F(Long e, Long k) {

        e = apply(E, e);
        Long x = e^k;

        int[] b = new int[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = split(x>>(i*6), 6).first.intValue();
        }
        long f = 0;
        f = (f|S(1, b[0]))<<4;
        f = (f|S(2, b[1]))<<4;
        f = (f|S(3, b[2]))<<4;
        f = (f|S(4, b[3]))<<4;
        f = (f|S(5, b[4]))<<4;
        f = (f|S(6, b[5]))<<4;
        f = (f|S(7, b[6]))<<4;
        f = (f|S(8, b[7]));
        f = swap(f, 32);
        return apply(P, f);
    }

    private int S(int t, int b) {

        int i = (b>>5)&1;
        i |= (b&1)<<1;

        int j = swap((long)b, 6).intValue();
        j >>= 1;
        j &= ~(1<<4);

        if (t == 1) return S1[i][j];
        if (t == 2) return S2[i][j];
        if (t == 3) return S3[i][j];
        if (t == 4) return S4[i][j];
        if (t == 5) return S5[i][j];
        if (t == 6) return S6[i][j];
        if (t == 7) return S7[i][j];
        return S8[i][j];
    }

    private Long[] roundKeys(Long k) {

        k = swap(k, 64);
        Long[] rKeys = new Long[16];
        Long k0 = apply(R_IP, k);
        Pair<Long, Long> cd = split(k0, 28);
        Long[] c = new Long[rKeys.length+1];
        Long[] d = new Long[rKeys.length+1];

        c[0] = cd.first;
        d[0] = cd.second;

        for (int i = 1; i <= 16; i++) {
            c[i] = leftShift(c[i-1], roundLeftShift[i-1], 28);
            d[i] = leftShift(d[i-1], roundLeftShift[i-1], 28);
            rKeys[i-1] = apply(R, join(c[i], d[i], 28));
        }
        return rKeys;
    }

    private static final int[] roundLeftShift = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private static final int[] R_IP = {56, 48, 40, 32, 24, 16, 8,
                                        0, 57, 49, 41, 33, 25, 17,
                                        9, 1, 58, 50, 42, 34, 26,
                                        18, 10, 2, 59, 51, 43, 35,
                                        62, 54, 46, 38, 30, 22, 14,
                                        6, 61, 53, 45, 37, 29, 21,
                                        13, 5, 60, 52, 44, 36, 28,
                                        20, 12, 4, 27, 19, 11, 3};

    private static final int[] IP = {57, 49, 41, 33, 25, 17, 9, 1,
                                59, 51, 43, 35, 27, 19, 11, 3,
                                61, 53, 45, 37, 29, 21, 13, 5,
                                63, 55, 47, 39, 31, 23, 15, 7,
                                56, 48, 40, 32, 24, 16, 8, 0,
                                58, 50, 42, 34, 26, 18, 10, 2,
                                60, 52, 44, 36, 28, 20, 12, 4,
                                62, 54, 46, 38, 30, 22, 14, 6};

    private static final int[] FP = {39, 7, 47, 15, 55, 23, 63, 31,
                                38, 6, 46, 14, 54, 22, 62, 30,
                                37, 5, 45, 13, 53, 21, 61, 29,
                                36, 4, 44, 12, 52, 20, 60, 28,
                                35, 3, 43, 11, 51, 19, 59, 27,
                                34, 2, 42, 10, 50, 18, 58, 26,
                                33, 1, 41, 9, 49, 17, 57, 25,
                                32, 0, 40, 8, 48, 16, 56, 24};

    private static final int[] E = {31, 0, 1, 2, 3, 4,
                                    3, 4, 5, 6, 7, 8,
                                    7, 8, 9, 10, 11, 12,
                                    11, 12, 13, 14, 15, 16,
                                    15, 16, 17, 18, 19, 20,
                                    19, 20, 21, 22, 23, 24,
                                    23, 24, 25, 26, 27, 28,
                                    27, 28, 29, 30, 31, 0};

    private static final int[] P = {15, 6, 19, 20,
                                    28, 11, 27, 16,
                                    0, 14, 22, 25,
                                    4, 17, 30, 9,
                                    1, 7, 23, 13,
                                    31, 26, 2, 8,
                                    18, 12, 29, 5,
                                    21, 10, 3, 24};

    private static final int[] R = {13, 16, 10, 23, 0, 4,
                                    2, 27, 14, 5, 20, 9,
                                    22, 18, 11, 3, 25, 7,
                                    15, 6, 26, 19, 12, 1,
                                    40, 51, 30, 36, 46, 54,
                                    29, 39, 50, 44, 32, 47,
                                    43, 48, 38, 55, 33, 52,
                                    45, 41, 49, 35, 28, 31};



    private static final int[][] S1 = {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                                        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                                            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                                                {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};

    private static final int[][] S2 = {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                                        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                                            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                                                {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};

    private static final int[][] S3 = {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                                        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                                            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                                                {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};

    private static final int[][] S4 = {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                                        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                                            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                                                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};

    private static final int[][] S5 = {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                                        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                                            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                                                {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};

    private static final int[][] S6 = {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                                        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                                            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                                                {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};

    private static final int[][] S7 = {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                                        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                                            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                                                {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};

    private static final int[][] S8 = {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                                        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                                            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                                                {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};
    
}
