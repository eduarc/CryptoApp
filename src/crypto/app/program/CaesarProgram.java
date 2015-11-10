package crypto.app.program;

import co.edu.unal.crypto.alphabet.ascii.ASCII;
import co.edu.unal.crypto.cryptosystem.Caesar;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;

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

    }
    
    @Override
    public int main(Param[] params) {
    
        Param p = ParamUtils.getParam(params, P_OFFSET);
        offset = ParamReader.getInt(p, "Key offset");
        if (offset == null) {
            stdout.error("Key parameter "+P_OFFSET+" not provided");
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
            if (operation.equals(V_OP_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(offset, input);
            } 
            else if (operation.equals(V_OP_DECRYPT)) {
                stdout.info("Decrypting...");
                output = cipher.decrypt(offset, input);
            }
        } catch (Exception ex) {
            stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
            return -1;
        }
        return 0;
    }

}
