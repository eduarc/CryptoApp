package crypto.app.program;

import co.edu.unal.crypto.alphabet.ascii.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Substitution;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamUtils;
import crypto.app.view.StringInputDialog;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class SubstitutionProgram extends CryptosystemProgram {

    public static final String CMD_SUBSTITUTION = "subs";
    public static final String P_KEY = "key";
    
    Substitution.Key<Character, Character> key;
    
    public SubstitutionProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        Param p = ParamUtils.getParam(params, P_KEY);
        if (!getKey(p)) {
            stdout.error("Parameter "+P_KEY+" not provided");
            return -1;
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_KEY+" = "+key);
        
        Substitution<Character, Character> cipher = new Substitution<>(LowerCaseEnglish.defaultInstance, LowerCaseEnglish.defaultInstance);
        try {
            if (operation.equals(V_OP_ENCRYPT)) {
                stdout.info("Encrypting...");
                output =  cipher.encrypt(key, input);
            }
            else if (operation.equals(V_OP_DECRYPT)) {
                stdout.info("Decrypting...");
                output = cipher.decrypt(key, input);
            }
        } catch (Exception ex) {
            stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
            return -1;
        }
        return 0;
    }
    
    private boolean getKey(Param param) {
        
        if (param == null) {
            return false;
        }
        String strKey = param.getValue();
        if (strKey == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strKey = sid.showDialog("Key Permutation");
        }
        if (strKey == null) {
            return false;
        }
        key = new Substitution.Key<>();
        for (char c = 'a'; c <= 'z'; c++) {
            try {
                key.set(c, strKey.charAt(c-'a'));
            } catch (Exception ex) {
                stdout.error("Invalid Substitution key: Bad format");
                return false;
            }
        }
        return true;
    }
}
