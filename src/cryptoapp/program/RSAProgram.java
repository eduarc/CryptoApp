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
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc
 */
public class RSAProgram extends CryptoSystemProgram {

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
                    getN(p);
                }
                else if (name.equals(P_E)) {
                    getE(p);
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
            } catch (IllegalArgumentException ex) {
                stdout.appendln("<font color='red'>"+ex.getMessage()+"</font>");
                return -1;
            }
            String result = "";
            for (BigInteger bi : rsaOutput) {
                result += bi.toString(16)+" ";
            }
            stdout.appendln(result);
        }
        else {
            for (Param p : params) {
                String name = p.getName();
                if (name.equals(P_P)) {
                    getP(p);
                }
                else if (name.equals(P_Q)) {
                    getQ(p);
                }
                else if (name.equals(P_E)) {
                    getE(p);
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
                    stdout.appendln("<font color='red'>Bad Input. "+ex.getMessage()+"</font>");
                    return -1;
                }
            }
            RSA.Key key = new RSA.Key(p, q, e);
            RSA<Character> cipher = new RSA(ASCII.defaultInstance);
            try {
                output = cipher.decrypt(key, rsaInput.toArray(new BigInteger[]{}));
            } catch (Exception ex) {
                stdout.appendln("<font color='red'>Error while encrypting/decrypting. "+ex.getMessage()+"</font>");
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {
        
        if (ParamUtils.contains(params, P_ENCRYPT)) {
            if (!ParamUtils.contains(params, P_N)) {
                stdout.appendln("<font color='red'>key parameter 'n' not defined</font>");
                return false;
            }
        }
        else {
            if (!ParamUtils.contains(params, P_P)) {
                stdout.appendln("<font color='red'>key parameter 'p' not defined</font>");
                return false;
            }
            if (!ParamUtils.contains(params, P_Q)) {
                stdout.appendln("<font color='red'>key parameter 'q' not defined</font>");
                return false;
            }
        }
        if (!ParamUtils.contains(params, P_E)) {
            stdout.appendln("<font color='red'>key parameter 'e' not defined</font>");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_RSA;
    }

    private void getN(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Key parameter N");
        }
        if (strN == null) {
            exit = true;
            return;
        }
        try {
            n = new BigInteger(strN);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter \'n\': "+strN+"</font>");
            exit = true;
        }
    }

    private void getE(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Key parameter E");
        }
        if (strN == null) {
            exit = true;
            return;
        }
        try {
            e = new BigInteger(strN);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter \'e\': "+strN+"</font>");
            exit = true;
        }
    }

    private void getP(Param param) {
        
        String strN = param.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Key parameter P");
        }
        if (strN == null) {
            exit = true;
            return;
        }
        try {
            p = new BigInteger(strN);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter \'p\': "+strN+"</font>");
            exit = true;
        }
    }

    private void getQ(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Key parameter Q");
        }
        if (strN == null) {
            exit = true;
            return;
        }
        try {
            q = new BigInteger(strN);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter \'q\': "+strN+"</font>");
            exit = true;
        }
    }
    
}
