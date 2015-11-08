package cryptoapp.program;

import co.edu.unal.crypto.alphabet.SimpleAlphabet;
import co.edu.unal.crypto.cryptosystem.Hill;
import co.edu.unal.crypto.types.ModularMatrix;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.StringInputDialog;
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
    ModularMatrix key;
    
    public HillProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        Param p = ParamUtils.getParam(params, P_KEY);
        if (!getKey(p)) {
            return -1;
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_KEY+" = ");
        String strKey = "";
        for (int i = 0; i < key.getRows(); i++) {
            for (int j = 0; j < key.getCols(); j++) {
                strKey += key.get(i, j)+" ";
            }
            strKey += "\n";
        }
        stdout.append(strKey);
        
        Hill<Character> cipher = new Hill(SimpleAlphabet.defaultInstance);
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
    
    private boolean getKey(Param p) {
        
        String strKey = p.getValue();
        if (strKey == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strKey = sid.showDialog("Key Matrix");
        }
        if (strKey == null) {
            return false;
        }
        List<Integer> values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(strKey);
        while (tokenizer.hasMoreTokens()) {
            try {
                int v = Integer.parseInt(tokenizer.nextToken());
                values.add(v);
            } catch (NumberFormatException ex) {
                stdout.error("Bad matrix value. "+ex.getMessage());
                return false;
            }
        }
        int sq = (int) Math.sqrt(values.size());
        if (sq*sq != values.size()) {
            stdout.error("Bad key. The key must be an square invertible matrix modulo "+SimpleAlphabet.SIZE);
            return false;
        }
        key = new ModularMatrix(sq, sq, SimpleAlphabet.SIZE);
        for (int i = 0; i < sq; i++) {
            for (int j = 0; j < sq; j++) {
                key.set(i, j, values.get(i*sq+j));
            }
        }
        return true;
    }
}
