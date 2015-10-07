package cryptoapp.program;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Hill;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.StringInputDialog;
import flanagan.math.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class HillProgram extends CryptosystemProgram {

    public static final String CMD_HILL = "hill";
    public static final String P_KEY = "key";
    
    int n;
    Matrix key;
    
    public HillProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        for (Param param : params) {
            String name = param.getName();
            
            if (name.equals(P_KEY)) {
                getKey(param);
            }
            if (exit) {
                return -1;
            }
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.appendln("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.appendln(input);
        }
        stdout.info("Parameters:");
        stdout.appendln(P_KEY+" = "+key);
        
        Hill<Character> cipher = new Hill(LowerCaseEnglish.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(key, input);
            }
            else {
                stdout.info("Decrypting...");
                output = cipher.decrypt(key, input);
            }
        } catch (Exception ex) {
            stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
            return -1;
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {
        
        if (!ParamUtils.contains(params, P_KEY)) {
            stdout.error("Parameter "+P_KEY+" not provided");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_HILL;
    }
    
    private void getKey(Param p) {
        
        String strKey = p.getValue();
        if (strKey == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strKey = sid.showDialog("Key Matrix");
        }
        if (strKey == null) {
            exit = true;
            return;
        }
        List<Double> values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(strKey);
        while (tokenizer.hasMoreTokens()) {
            try {
                double v = Double.parseDouble(tokenizer.nextToken());
                values.add(v);
            } catch (NumberFormatException ex) {
                stdout.error("Bad matrix value. "+ex.getMessage());
                exit = true;
                return;
            }
        }
        int sq = (int) Math.sqrt(values.size());
        if (sq*sq != values.size()) {
            stdout.error("Bad key. The key must be an square invertible matrix modulo 26");
            exit = true;
            return;
        }
        key = new Matrix(sq, sq);
        for (int i = 0; i < sq; i++) {
            for (int j = 0; j < sq; j++) {
                key.setElement(i, j, values.get(j*sq+i));
            }
        }
    }
}
