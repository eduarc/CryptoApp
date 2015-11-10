package crypto.app.program;

import co.edu.unal.crypto.alphabet.ascii.SimpleAlphabet;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.type.Pair;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class AffineProgram extends CryptosystemProgram {

    public static final String CMD_AFFINE = "affine";
    public static final String P_A = "a";
    public static final String P_B = "b";
    
    Pair<Integer, Integer> key;
    
    public AffineProgram(Environment env) {
        super(env);
        
        key = new Pair<>();
    }
    
    @Override
    public int main(Param[] params) {
        
        Param p = ParamUtils.getParam(params, P_A);
        key.first = ParamReader.getInt(p, "");
        if (key.first == null) {
            stdout.error("Parameter "+P_A+" not provided");
            return -1;
        }
        p = ParamUtils.getParam(params, P_B);
        key.second = ParamReader.getInt(p, "");
        if (key.second == null) {
            stdout.error("Parameter "+P_B+" not provided");
            return -1;
        }
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Parameters:");
        stdout.append(P_A+" = "+key.first);
        stdout.append(P_B+" = "+key.second);
        
        Affine<Character> cipher = new Affine(SimpleAlphabet.defaultInstance);
        try {
            if (operation.equals(V_OP_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(key, input);
            } 
            else if (operation.equals(V_OP_DECRYPT)) {
                stdout.info("Decrypting...");
                output = cipher.decrypt(key, input);
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
