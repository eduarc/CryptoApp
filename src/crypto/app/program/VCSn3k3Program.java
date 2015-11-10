package crypto.app.program;

import co.edu.unal.crypto.cryptosystem.VCSn3k3;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.view.ImageViewer;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class VCSn3k3Program extends VCSProgram {

    public static final String CMD_THREE = "vcsn2k2";
    public static final String P_SHARE1 = "share1";
    public static final String P_SHARE2 = "share2";
    public static final String P_SHARE3 = "share3";
    
    public VCSn3k3Program(Environment env) {
        super(env);
    }

    @Override
    public int main(Param[] params) {
        
        VCSn3k3 cipher = new VCSn3k3();
        if (operation.equals(V_OP_ENCRYPT)) {
            stdout.info("Encrypting...");
            Image[] shares = cipher.encrypt(input);
            stdout.info("OK!");
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.save("Share 1", shares[0]);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.save("Share 2", shares[1]);
            ImageViewer s3 = new ImageViewer(frame, false);
            s3.save("Share 3", shares[2]);
        }
        else if (operation.equals(V_OP_DECRYPT)) {
            Image[] shares = new BufferedImage[3];
            Param p = ParamUtils.getParam(params, P_SHARE1);
            shares[0] = ParamReader.getImage(p, "Select Share 1", true);
            if (shares[0] == null) {
                stdout.error("Parameter "+P_SHARE1+" not provided");
                return -1;
            }
            p = ParamUtils.getParam(params, P_SHARE2);
            shares[1] = ParamReader.getImage(p, "Select Share 2", true);
            if (shares[1] == null) {
                stdout.error("Parameter "+P_SHARE2+" not provided");
                return -1;
            }
            p = ParamUtils.getParam(params, P_SHARE3);
            shares[2] = ParamReader.getImage(p, "Select Share 3", true);
            if (shares[2] == null) {
                stdout.error("Parameter "+P_SHARE3+" not provided");
                return -1;
            }
            stdout.info("Decrypting...");
            Image secret = cipher.decrypt((BufferedImage[]) shares);
            Image originalSecret = cipher.originalDecrypt((BufferedImage[]) shares);
            stdout.info("OK!");
            
            ImageViewer s1 = new ImageViewer(frame, false);
            s1.save("Secret", secret);
            ImageViewer s2 = new ImageViewer(frame, false);
            s2.save("Original Secret", originalSecret);
        }
        return 0;
    }

}