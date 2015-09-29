package cryptoapp.program;

import co.edu.unal.crypto.alphabet.ASCII;
import co.edu.unal.crypto.alphabet.LowerCaseEnglish;
import co.edu.unal.crypto.cryptosystem.Affine;
import co.edu.unal.crypto.types.Pair;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import javax.swing.JOptionPane;

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
                getA(param);
            }
            else if (name.equals(P_B)) {
                getB(param);
            }
            if (exit) {
                return -1;
            }
        }
        Affine<Character> cipher = new Affine(LowerCaseEnglish.defaultInstance);
        try {
            if (ParamUtils.contains(params, P_ENCRYPT)) {
                output = cipher.encrypt(key, input);
            } else {
                output = cipher.decrypt(key, input);
            }
        } catch (Exception ex) {
            stdout.appendln("<font color='red'>Error while encrypting/decrypting. "+ex.getMessage()+"</font>");
            return -1;
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {
        
        if (!ParamUtils.contains(params, P_A)) {
            stdout.appendln("<font color='red'>Key Parameter 'a' not especified</font>");
            return false;
        }
        if (!ParamUtils.contains(params, P_B)) {
            stdout.appendln("<font color='red'>Key parameter 'b' not especified</font>");
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_AFFINE;
    }
    
    
    private void getA(Param p) {
        
        String strA = p.getValue();
        if (strA == null) {
            strA = JOptionPane.showInputDialog(frame, "Key parameter A");
        }
        if (strA == null) {
            exit = true;
            return;
        }
        try {
            key.first = Integer.parseInt(strA);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter 'a': "+strA+"</font>");
            exit = true;
        }
    }
    
    private void getB(Param p) {
        
        String strB = p.getValue();
        if (strB == null) {
            strB = JOptionPane.showInputDialog(frame, "Key parameter B");
        }
        if (strB == null) {
            exit = true;
            return;
        }
        try {
            key.second = Integer.parseInt(strB);
        } catch(NumberFormatException ex) {
            stdout.appendln("<font color='red'>Invalid key parameter 'b': "+strB+"</font>");
            exit = true;
        }
    }
}
