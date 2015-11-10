package crypto.app.program;

import co.edu.unal.crypto.alphabet.ascii.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Vigenere;
import co.edu.unal.crypto.util.CharStream;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;

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
            if (operation.equals(V_OP_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(key, input);
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
    
}
