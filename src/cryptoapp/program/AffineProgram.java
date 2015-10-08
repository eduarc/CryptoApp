package cryptoapp.program;

import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.types.Pair;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;

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
        
        for (Param param : params) {
            String name = param.getName();
            if (name.equals(P_A)) {
                key.first = getInt(param);
            }
            else if (name.equals(P_B)) {
                key.second = getInt(param);
            }
            if (exit) {
                return -1;
            }
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
        
        Affine<Character> cipher = new Affine(LowerCaseEnglish.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                stdout.info("Encrypting...");
                output = cipher.encrypt(key, input);
            } else {
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
        
        if (!ParamUtils.contains(params, P_A)) {
            stdout.error("Key Parameter "+P_A+" not provided");
            return false;
        }
        if (!ParamUtils.contains(params, P_B)) {
            stdout.error("Key Parameter "+P_B+" not provided");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_AFFINE;
    }    
}
