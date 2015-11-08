package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
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
            Param pr = ParamUtils.getParam(params, P_N);
            n = ParamReader.getBigInt(pr);
            if (n == null) {
                return -1;
            }
            pr = ParamUtils.getParam(params, P_E);
            e = ParamReader.getBigInt(pr);
            if (e == null) {
                return -1;
            }
            
            stdout.info("Input:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            stdout.info("Parameters:");
            stdout.append(P_N+" = "+n);
            stdout.append(P_E+" = "+e);
            
            RSA.Key key = new RSA.Key(n, e);
            RSA<Character> cipher = new RSA(ASCII.defaultInstance);
            BigInteger[] rsaOutput = null;
            try {
                stdout.info("Encrypting...");
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
            Param pr = ParamUtils.getParam(params, P_P);
            p = ParamReader.getBigInt(pr);
            if (n == null) {
                return -1;
            }
            pr = ParamUtils.getParam(params, P_Q);
            q = ParamReader.getBigInt(pr);
            if (q == null) {
                return -1;
            }
            pr = ParamUtils.getParam(params, P_E);
            e = ParamReader.getBigInt(pr);
            if (e == null) {
                return -1;
            }
            
            stdout.info("Input:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            stdout.info("Parameters:");
            stdout.append(P_P+" = "+p);
            stdout.append(P_Q+" = "+q);
            stdout.append(P_E+" = "+e);
            
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
                stdout.info("Decrypting...");
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
                stdout.error("Key parameter "+P_N+" not provided");
                return false;
            }
        }
        else {
            if (!ParamUtils.contains(params, P_P)) {
                stdout.error("Key parameter "+P_P+" not provided");
                return false;
            }
            if (!ParamUtils.contains(params, P_Q)) {
                stdout.error("Key parameter "+P_Q+" not provided");
                return false;
            }
        }
        if (!ParamUtils.contains(params, P_E)) {
            stdout.error("Key parameter "+P_E+" not provided");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_RSA;
    }    
}
