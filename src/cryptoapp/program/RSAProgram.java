package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class RSAProgram extends CryptosystemProgram {

    public static final String CMD_RSA = "rsa";
    public static final String P_P = "p";
    public static final String P_Q = "q";
    public static final String P_E = "e";
    public static final String P_N = "n";
    
    BigInteger n, p, q, e;
    
    public RSAProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        if (ParamUtils.contains(params, P_ENCRYPT)) {
            for (Param p : params) {
                String name = p.getName();
                if (name.equals(P_N)) {
                    n = getBigInt(p);
                }
                else if (name.equals(P_E)) {
                    e = getBigInt(p);
                }
                if (exit) {
                    return -1;
                }
            }
            RSA.Key key = new RSA.Key(n, e);
            RSA<Character> cipher = new RSA(ASCII.defaultInstance);
            BigInteger[] rsaOutput = null;
            try {
                rsaOutput = cipher.encrypt(key, input);
            } catch (Exception ex) {
                stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
                return -1;
            }
            String result = "";
            for (BigInteger bi : rsaOutput) {
                result += bi.toString(16)+" ";
            }
            output = CharStream.fromString(result);
        }
        else {
            for (Param param : params) {
                String name = param.getName();
                if (name.equals(P_P)) {
                    p = getBigInt(param);
                }
                else if (name.equals(P_Q)) {
                    q = getBigInt(param);
                }
                else if (name.equals(P_E)) {
                    e = getBigInt(param);
                }
                if (exit) {
                    return -1;
                }
            }
            List<BigInteger> rsaInput = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(CharStream.toString(input), " ");
            while (tokenizer.hasMoreTokens()) {
                try {
                    rsaInput.add(new BigInteger(tokenizer.nextToken(), 16));
                } catch(NumberFormatException ex) {
                    stdout.error("Bad Input. "+ex.getMessage());
                    return -1;
                }
            }
            RSA.Key key = new RSA.Key(p, q, e);
            RSA<Character> cipher = new RSA(ASCII.defaultInstance);
            try {
                output = cipher.decrypt(key, rsaInput.toArray(new BigInteger[]{}));
            } catch (Exception ex) {
                stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {
        
        if (ParamUtils.contains(params, P_ENCRYPT)) {
            if (!ParamUtils.contains(params, P_N)) {
                stdout.error("Key parameter 'n' not provided");
                return false;
            }
        }
        else {
            if (!ParamUtils.contains(params, P_P)) {
                stdout.error("Key parameter 'p' not provided");
                return false;
            }
            if (!ParamUtils.contains(params, P_Q)) {
                stdout.error("Key parameter 'q' not provided");
                return false;
            }
        }
        if (!ParamUtils.contains(params, P_E)) {
            stdout.error("Key parameter 'e' not provided");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_RSA;
    }    
}
