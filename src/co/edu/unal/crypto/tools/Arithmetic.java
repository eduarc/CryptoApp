package co.edu.unal.crypto.tools;

public class Arithmetic {

    public static int gcd(int a, int b) {
        
        int r;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static boolean coprimes(int a, int b) {
        return Math.abs(gcd(a, b)) == 1;
    }

    public static int modInverse(int a, int n) {
        
        a = mod(a, n);
        int b = n, d = 1, r = 1, p0 = 0, p1 = 1;
        for (int i = 0; r > 0; ++i) {
            if (i > 1) {
                r = mod(p0 - (p1 * d), n);
                p0 = p1;
                p1 = r;
            }
            d = a / b;
            r = a % b;
            a = b;
            b = r;
        }
        return p1;
    }

    public static int mod(int a, int n) {

        if (a < 0) {
            return (a + n * (-a / n) + n) % n;
        }
        return a % n;
    }

    public static int modPow(int base, int exp, int modulus) {
        
        int result = 1;
        base %= modulus;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % modulus;
            }
            exp >>= 1;
            base = (base * base) % modulus;
        }
        return result;
    }
}
