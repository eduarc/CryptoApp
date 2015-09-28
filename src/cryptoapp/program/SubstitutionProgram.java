package cryptoapp.program;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Substitution;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.StringInputDialog;

/**
 *
 * @author eduarc
 */
public class SubstitutionProgram extends CryptoSystemProgram {

    public static final String CMD_SUBSTITUTION = "subs";
    public static final String P_KEY = "key";
    
    Substitution.Key<Character, Character> key;
    
    public SubstitutionProgram(Environment env) {
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
        Substitution<Character, Character> cipher = new Substitution<>(LowerCaseEnglish.defaultInstance, LowerCaseEnglish.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                output =  cipher.encrypt(key, input);
            } else {
                output = cipher.decrypt(key, input);
            }
        } catch (Exception ex) {
            stdout.appendln("<font color='red'>Error while encrypting/decrypting. "+ex.getMessage()+"</font>");
            return -1;
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {
        
        if (!ParamUtils.contains(params, P_KEY)) {
            stdout.appendln("<font color='red'>Key Permutation not defined</font>");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_SUBSTITUTION;
    }
    
    private void getKey(Param param) {
        
        String strKey = param.getValue();
        if (strKey == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strKey = sid.showDialog("Key Permutation");
        }
        if (strKey == null) {
            exit = true;
            return;
        }
        key = new Substitution.Key<>();
        for (char c = 'a'; c <= 'z'; c++) {
            try {
                key.set(c, strKey.charAt(c-'a'));
            } catch (Exception ex) {
                stdout.appendln("<font color='red'>Invalid Substitution key: Bad format</font>");
                exit = true;
                return;
            }
        }
    }
}
