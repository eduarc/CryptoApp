package cryptoapp.program;

import co.edu.unal.crypto.visual.TwoTwo;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.ImageViewer;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class TwoTwoProgram extends VCSProgram {

    public static final String CMD_TWO = "two";
    public static final String P_SHARE1 = "share1";
    public static final String P_SHARE2 = "share2";
    
    public TwoTwoProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        TwoTwo cipher = new TwoTwo();
        if (ParamUtils.contains(params, P_ENCRYPT)) {    
            stdout.info("Encrypting...");
            Image[] shares = cipher.encrypt(input);
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.save("Share 1", shares[0]);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.save("Share 2", shares[1]);
        }
        else {
            Image[] shares = new BufferedImage[2];
            for (Param p : params) {
                if (p.getName().equals(P_SHARE1)) {
                    shares[0] = getInput("Select Share 1", p);
                }
                else if (p.getName().equals(P_SHARE2)) {
                    shares[1] = getInput("Select Share 2", p);
                }
                if (exit) {
                    return -1;
                }
            }
            stdout.info("Decrypting...");
            Image secret = cipher.decrypt((BufferedImage[]) shares);
            Image originalSecret = cipher.originalDecrypt((BufferedImage[]) shares);
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.show("Secret", secret);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.show("Original Secret", originalSecret);
        }
        return 0;
    }

    @Override
    public boolean checkParams(Param[] params) {

        if (ParamUtils.contains(params, P_DECRYPT)) {
            if (!ParamUtils.contains(params, P_SHARE1)) {
                stdout.error("Parameter "+P_SHARE1+" not provided");
                return false;
            }
            if (!ParamUtils.contains(params, P_SHARE2)) {
                stdout.error("Parameter "+P_SHARE2+" not provided");
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_TWO;
    }   
}