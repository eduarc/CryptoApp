package crypto.app.program;

import co.edu.unal.crypto.alphabet.ascii.ASCII;
import co.edu.unal.crypto.alphabet.ascii.LowerCaseEnglish;
import co.edu.unal.crypto.alphabet.ascii.SimpleAlphabet;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.cryptosystem.Caesar;
import co.edu.unal.crypto.cryptosystem.DES;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.cryptosystem.Substitution;
import co.edu.unal.crypto.cryptosystem.Vigenere;
import co.edu.unal.crypto.type.Pair;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;
import java.awt.Frame;

/**
 *
 * @author eduarc
 */
public class KeyGenProgram extends Program {

    public static final String CMD_KEYGEN = "keygen";
    public static final String P_CIPHER = "cipher";
    public static final String P_NKEYS = "nKeys";
    public static final String P_LEN = "len";
    
    public static final String V_CIPHER_CAESAR = "caesar";
    public static final String V_CIPHER_AFFINE = "affine";
    public static final String V_CIPHER_SUBSTITUTION = "subs";
    public static final String V_CIPHER_VIGENERE = "vigenere";
    public static final String V_CIPHER_RSA = "rsa";
    public static final String V_CIPHER_DES = "des";
    
    private String cipher;
    private Integer nKeys;
    
    private static final String[] pCipherOptions;
    
    Frame frame;
    StandardTextConsole stdout;
    
    static {
        
        pCipherOptions = new String[] {V_CIPHER_CAESAR, V_CIPHER_AFFINE, V_CIPHER_SUBSTITUTION,
                                       V_CIPHER_VIGENERE, V_CIPHER_RSA, V_CIPHER_DES};
    }
    
    public KeyGenProgram(Environment env) {
        super(env);
        
        stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }

    @Override
    public int exec(Param[] params) {
        
        Param pCipher = ParamUtils.getParam(params, P_CIPHER);
        cipher = ParamReader.getOptionValue(pCipher, "Select Cipher Algorithm", pCipherOptions);
        if (cipher == null || cipher.length() == 0) {
            stdout.error("Parameter "+P_CIPHER+" not provided");
            return -1;
        }
        Param pNKeys = ParamUtils.getParam(params, P_NKEYS);
        nKeys = ParamReader.getInt(pNKeys, "Numbers of keys generated");
        if (nKeys == null) {
            nKeys = 1;
            stdout.warning("Parameter "+P_NKEYS+" not provided. Default to "+nKeys);
        }
        
        stdout.info("Parameters:");
        stdout.append(P_CIPHER+" = "+cipher);
        stdout.append(P_NKEYS+" = "+nKeys);
        
        if (cipher.equals(V_CIPHER_RSA)) {
            RSA keygen = new RSA(ASCII.defaultInstance);
            for (int i = 1; i <= nKeys; i++) {
                RSA.Key key = keygen.generateKey(null);
                stdout.info("Key #"+i);
                stdout.append("p = "+key.p);
                stdout.append("q = "+key.q);
                stdout.append("e = "+key.e);
                stdout.append("n = p*q = "+key.n);
            }
        }
        else if (cipher.equals(V_CIPHER_VIGENERE)) {
            Param pLen = ParamUtils.getParam(params, P_LEN);
            Integer len = ParamReader.getInt(pLen, "Length of the key to be generated");
            if (len == null) {
                stdout.error("Parameter "+P_LEN+" not provided.");
                return -1;
            }
            stdout.append(P_LEN+" = "+len);
            Vigenere<Character> keygen = new Vigenere(LowerCaseEnglish.defaultInstance);
            for (int i = 1; i <= nKeys; i++) {
                stdout.info("Key #"+i);
                Character[] key = keygen.generateKey(len);
                stdout.append(key);
            }
        }
        else if (cipher.equals(V_CIPHER_CAESAR)) {
            Caesar<Character> keygen = new Caesar(ASCII.defaultInstance);
            for (int i = 1; i <= nKeys; i++) {
                stdout.info("Key #"+i);
                Integer offset = keygen.generateKey(null);
                stdout.append("offset = "+offset);
            }
        }
        else if (cipher.equals(V_CIPHER_AFFINE)) {
            Affine<Character> keygen = new Affine(SimpleAlphabet.defaultInstance);
            for (int i = 1; i <= nKeys; i++) {
                stdout.info("Key #"+i);
                Pair<Integer, Integer> key = keygen.generateKey(null);
                stdout.append("a = "+key.first);
                stdout.append("b = "+key.second);
            }
        }
        else if (cipher.equals(V_CIPHER_SUBSTITUTION)) {
            Substitution<Character, Character> keygen = new Substitution(LowerCaseEnglish.defaultInstance, LowerCaseEnglish.defaultInstance);
            for (int i = 1; i <= nKeys; i++) {
                stdout.info("Key #"+i);
                Substitution.Key key = keygen.generateKey(null);
                stdout.append(key.toString());
            }
        }
        else if (cipher.equals(V_CIPHER_DES)) {
            DES keygen = new DES();
            for (int i = 1; i <= nKeys; i++) {
                stdout.info("Key #"+i);
                String k = String.format("%016x", keygen.generateKey(null));
                stdout.append(k);
            }
        }
        else { 
            stdout.error("Unknown cipher = "+cipher);
            return -1;
        }
        return 0;
    }
    
}
