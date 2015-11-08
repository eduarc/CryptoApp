package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.alphabet.Alphabet;
import co.edu.unal.crypto.alphabet.SimpleAlphabet;
import co.edu.unal.crypto.cryptosystem.HillReloaded;
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
public class HillReloadedProgram extends CryptosystemProgram {

    public static final String CMD_HILL_RELOADED = "hillreloaded";
    public static final String P_KEY = "key";
    public static final String P_IV = "iv";
    
    int n;
    ModularMatrix mat;
    ModularMatrix iv;
    Alphabet alpha;
    
    public HillReloadedProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        alpha = SimpleAlphabet.defaultInstance;
        if (inputImage != null) {
            alpha = ASCII.defaultInstance;
        }
        
        Param p = ParamUtils.getParam(params, P_KEY);
        if (!getKey(p)) {
            return -1;
        }
        p = ParamUtils.getParam(params, P_IV);
        if (!getIV(p)) {
            return -1;
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } 
        else if (inputImage != null) {
            stdout.append("From Image");
        }
        else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_KEY+" = ");
        String strKey = "";
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getCols(); j++) {
                strKey += mat.get(i, j)+" \t";
            }
            strKey += "\n";
        }
        stdout.append(strKey);
        
        stdout.append(P_IV+" = ");
        String strIV = "";
        for (int i = 0; i < iv.getCols(); i++) {
            strIV += iv.get(0, i)+" ";
        }
        stdout.append(strIV);
        
        HillReloaded<Character> cipher = new HillReloaded(alpha);
        HillReloaded.Key key = new HillReloaded.Key(mat, iv);
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
        if (!ParamUtils.contains(params, P_IV)) {
            stdout.error("Parameter "+P_IV+" not provided");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_HILL_RELOADED;
    }
    
    private boolean getIV(Param p) {
        
        String strIV = p.getValue();
        if (strIV == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strIV = sid.showDialog("Initial Vector");
        }
        if (strIV == null) {
            return false;
        }
        List<Integer> values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(strIV);
        while (tokenizer.hasMoreTokens()) {
            try {
                int v = Integer.parseInt(tokenizer.nextToken());
                values.add(v);
            } catch (NumberFormatException ex) {
                stdout.error("Bad integer value. "+ex.getMessage());
                return false;
            }
        }
        if (mat.getCols() != values.size()) {
            stdout.error("Error. The initial vector must have the same number of elements as the colums/rows in the key.");
            return false;
        }
        iv = new ModularMatrix(1, values.size(), alpha.getSize());
        for (int i = 0; i < values.size(); i++) {
            iv.set(0, i, values.get(i));
        }
        return true;
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
                stdout.error("Bad integer value. "+ex.getMessage());
                return false;
            }
        }
        int sq = (int) Math.sqrt(values.size());
        if (sq*sq != values.size()) {
            stdout.error("Error. The key must be an square invertible matrix modulo "+alpha.getSize());
            return false;
        }
        mat = new ModularMatrix(sq, sq, alpha.getSize());
        for (int i = 0; i < sq; i++) {
            for (int j = 0; j < sq; j++) {
                mat.set(i, j, values.get(i*sq+j));
            }
        }
        return true;
    }
}
