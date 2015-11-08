package cryptoapp.program;

import co.edu.unal.crypto.cryptosystem.DES;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;
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
        key = ParamReader.getUnsignedLong(p);
        if (key == null) {
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
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                stdout.info("Encrypting...");
                Long[] desOutput = cipher.encrypt(key, toLongArray(input));
                String out = "";
                for (Long l : desOutput) {
                    out += String.format("%x ", l);
                }
                output = CharStream.fromString(out);
            }
            else {
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
                output = toCharArray(desOutput);
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
        return CMD_DES;
    }
    
    public Long[] toLongArray(Character[] data) {
        
        List<Long> result = new ArrayList<>();
        long c, l;
        int i;
        for (i = 0; i+3 < data.length; i += 4) {
            c = data[i+0];
            l = c;
            c = data[i+1];
            l = (l<<16)|c;
            c = data[i+2];
            l = (l<<16)|c;
            c = data[i+3];
            l = (l<<16)|c;
            result.add(l);
        }
        if (i < data.length) {
            int add = 0;
            for (l = 0L; i < data.length; i++, add++) {
                c = data[i];
                l = (l<<16)|c;
            }
            for (; 4-add > 0; add++) {
                l <<= 16;
            }
            result.add(l);
        }
        return result.toArray(new Long[]{});
    }
    
    public Character[] toCharArray(Long[] data) {
        
        List<Character> result = new ArrayList();
        
        for (Long c : data) {
            char v1 = (char)(c.intValue()&0xFFFF);
            c >>= 16;
            char v2 = (char)(c.intValue()&0xFFFF);
            c >>= 16;
            char v3 = (char)(c.intValue()&0xFFFF);
            c >>= 16;
            char v4 = (char)(c.intValue()&0xFFFF);
            result.add(v4);
            result.add(v3);
            result.add(v2);
            result.add(v1);
        }
        return result.toArray(new Character[]{});
    }
}
