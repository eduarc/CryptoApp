package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.Caesar;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class CaesarProgram extends CryptosystemProgram {

    public static final String CMD_CAESAR = "caesar";
    public static final String P_OFFSET = "offset";
    
    Integer offset;

    public CaesarProgram(Environment env) {
        super(env);
        
        exit = false;
    }
    
    @Override
    public boolean checkParams(Param[] params) {
        
        if (!ParamUtils.contains(params, P_OFFSET)) {
            stdout.error("Key parameter "+P_OFFSET+" not provided");
            return false;
        }
        return true;
    }
    
    @Override
    public int main(Param[] params) {
    
        Param p = ParamUtils.getParam(params, P_OFFSET);
        offset = ParamReader.getInt(p);
        if (offset == null) {
            return -1;
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_OFFSET+" = "+offset);
        
        Caesar<Character> cipher = new Caesar(ASCII.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(offset, input);
            } else {
                stdout.info("Decrypting...");
                output = cipher.decrypt(offset, input);
            }
        } catch (Exception ex) {
            stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
            return -1;
        }
        return 0;
    }

    @Override
    public String getName() {
        return CMD_CAESAR;
    }
}
