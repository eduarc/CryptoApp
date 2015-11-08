package cryptoapp.program;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Vigenere;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class VigenereProgram extends CryptosystemProgram {

    public static final String CMD_VIGENERE = "vigenere";
    public static final String P_KEY = "key";
    
    Character[] key;
    
    public VigenereProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        Param p = ParamUtils.getParam(params, P_KEY);
        String strKey = ParamReader.getString(p, "Key Phrase");
        if (strKey == null) {
            return -1;
        }
        key = CharStream.fromString(strKey);
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_KEY+" = "+CharStream.toString(key));
        
        Vigenere<Character> cipher = new Vigenere<>(LowerCaseEnglish.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = (Character[]) cipher.encrypt(key, input);
            } else {
                stdout.info("Decrypting...");
                output = (Character[]) cipher.decrypt(key, input);
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
        return CMD_VIGENERE;
    }
    
}
