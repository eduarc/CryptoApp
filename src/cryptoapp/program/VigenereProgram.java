package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.Vigenere;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.StringInputDialog;

/**
 *
 * @author eduarc
 */
public class VigenereProgram extends CryptoSystemProgram {

    public static final String CMD_VIGENERE = "vigenere";
    public static final String P_KEY = "key";
    
    Character[] key;
    
    public VigenereProgram(Environment env) {
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
        Vigenere<Character> cipher = new Vigenere<>(ASCII.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                output = (Character[]) cipher.encrypt(key, input);
            } else {
                output = (Character[]) cipher.decrypt(key, input);
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
            stdout.appendln("<font color='red'>Key phrase not defined</font>");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_VIGENERE;
    }

    private void getKey(Param param) {
        
        String strKey = param.getValue();
        if (strKey == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            strKey = sid.showDialog("Key Phrase");
        }
        if (strKey == null) {
            exit = true;
            return;
        }
        key = CharStream.fromString(strKey);
    }
    
}
