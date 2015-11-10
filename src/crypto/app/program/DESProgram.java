package crypto.app.program;

import co.edu.unal.crypto.cryptosystem.DES;
import co.edu.unal.crypto.util.CharStream;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class DESProgram extends CryptosystemProgram {

    public static final String CMD_DES = "des";
    public static final String P_KEY = "key";
    
    Long key;
    
    public DESProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        Param p = ParamUtils.getParam(params, P_KEY);
        key = ParamReader.getUnsignedLong(p, "Key (64-bits hexadecimal value");
        if (key == null) {
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
        stdout.append(P_KEY+" = "+String.format("%x", key));
        
        DES cipher = new DES();
        try {
            if (operation.equals(V_OP_ENCRYPT)) {
                stdout.info("Encrypting...");
                Long[] desOutput = cipher.encrypt(key, CharStream.toLongArray(input));
                String out = "";
                for (Long l : desOutput) {
                    out += String.format("%x ", l);
                }
                output = CharStream.fromString(out);
            }
            else if (operation.equals(V_OP_DECRYPT)) {
                List<Long> desInput = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(CharStream.toString(input), " ");
                while (tokenizer.hasMoreTokens()) {
                    try {
                        desInput.add(Long.parseUnsignedLong(tokenizer.nextToken(), 16));
                    } catch(NumberFormatException ex) {
                        stdout.error("Bad Input. "+ex.getMessage());
                        return -1;
                    }
                }
                stdout.info("Decrypting...");
                Long[] desOutput = cipher.decrypt(key, desInput.toArray(new Long[]{}));
                output = CharStream.toCharArray(desOutput);
            }
            else {
                stdout.error("Unknown "+P_OP+"="+operation);
                return -1;
            }
        } catch (Exception ex) {
            stdout.error("Error while encrypting/decrypting. "+ex.getMessage());
            return -1;
        }
        return 0;
    }
    
}
