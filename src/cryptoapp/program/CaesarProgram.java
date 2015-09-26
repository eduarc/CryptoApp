package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.cryptosystem.Caesar;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class CaesarProgram extends CryptoProgram {

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
            stdout.appendln("No offset (key) provided");
            return false;
        }
        return true;
    }
    
    @Override
    public int main(Param[] params) {
    
        for (Param param : params) {
            String name = param.getName();
            if (name.equals(P_OFFSET)) {
                getOffset(param);
            }
            if (exit) {
                return -1;
            }
        }
        cipher = new Caesar(ASCII.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                output = (Character[]) cipher.encrypt(offset, input);
            } else {
                output = (Character[]) cipher.decrypt(offset, input);
            }
        } catch (Exception ex) {
            stdout.appendln("Ops! something went wrong while encrypting/decrypting. Maybe bad inputs");
            return -1;
        }
        return 0;
    }

    @Override
    public String getName() {
        return CMD_CAESAR;
    }

    private void getOffset(Param p) {
        
        String strOffset = p.getValue();
        if (strOffset == null) {
            strOffset = JOptionPane.showInputDialog(frame, "Offset");
        }
        if (strOffset == null) {
            exit = true;
            return;
        }
        try {
            offset = Integer.parseInt(strOffset);
        } catch(NumberFormatException ex) {
            stdout.appendln("Invalid offset value: "+strOffset);
            exit = true;
        }
    }
}
