package cryptoapp.program;

import co.edu.unal.crypto.visual.ThreeThree;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;
import cryptoapp.view.ImageViewer;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class ThreeThreeProgram extends VCSProgram {

    public static final String CMD_THREE = "three";
    public static final String P_SHARE1 = "share1";
    public static final String P_SHARE2 = "share2";
    public static final String P_SHARE3 = "share3";
    
    public ThreeThreeProgram(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        ThreeThree cipher = new ThreeThree();
        if (ParamUtils.contains(params, P_ENCRYPT)) {  
            stdout.info("Encrypting...");
            Image[] shares = cipher.encrypt(input);
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.save("Share 1", shares[0]);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.save("Share 2", shares[1]);
            ImageViewer s3 = new ImageViewer(frame, false);
            s3.save("Share 3", shares[2]);
        }
        else {
            Image[] shares = new BufferedImage[3];
            Param p = ParamUtils.getParam(params, P_SHARE1);
            shares[0] = ParamReader.getImage(p, "Select Share 1", true);
            if (shares[0] == null) {
                return -1;
            }
            p = ParamUtils.getParam(params, P_SHARE2);
            shares[1] = ParamReader.getImage(p, "Select Share 2", true);
            if (shares[1] == null) {
                return -1;
            }
            p = ParamUtils.getParam(params, P_SHARE3);
            shares[2] = ParamReader.getImage(p, "Select Share 3", true);
            if (shares[2] == null) {
                return -1;
            }
            stdout.info("Decrypting...");
            Image secret = cipher.decrypt((BufferedImage[]) shares);
            Image originalSecret = cipher.originalDecrypt((BufferedImage[]) shares);
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.save("Secret", secret);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.save("Original Secret", originalSecret);
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
            if (!ParamUtils.contains(params, P_SHARE3)) {
                stdout.error("Parameter "+P_SHARE3+" not provided");
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return CMD_THREE;
    }   
}