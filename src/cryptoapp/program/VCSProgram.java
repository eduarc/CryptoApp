package cryptoapp.program;

import co.edu.unal.crypto.tools.ImageStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;
import cryptoapp.view.ImageViewer;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public abstract class VCSProgram extends Program {

    public static final String P_ENCRYPT = "encrypt";
    public static final String P_DECRYPT = "decrypt";
    public static final String P_SECRET = "secret";
    
    Frame frame;
    StandardConsole stdout;
    boolean exit;
    BufferedImage input;
    
    public VCSProgram(Environment env) {
        super(env);
        
        stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    public int preProcess(Param[] params) {
        
        String[] operation = {P_ENCRYPT, P_DECRYPT};
        
        if (!ParamUtils.containsOne(params, operation)) {
            stdout.error("Any or multiple encrypt/decrypt operation(s) provided");
            return -1;
        }

        if (ParamUtils.contains(params, P_ENCRYPT)) {
            if (!ParamUtils.contains(params, P_SECRET)) {
                stdout.error("Parameter "+P_SECRET+" not provided");
                return -1;
            }
            for (Param param : params) {
                String name = param.getName();
                if (name.equals(P_SECRET)) {
                    input = (BufferedImage) getInput("Select the Secret Image", param);
                }
                if (exit) {
                    return -1;
                }
            }
        }
        if (!checkParams(params)) {
            return -1;
        }
        return 0;
    }
    
    public int postProcess(Param[] params) {
        return 0;
    }
    
    @Override
    public int exec(Param[] params) {
        
        int r = preProcess(params);
        if (r != 0) {
            return r;
        }
        r = main(params);
        if (r != 0) {
            return r;
        }
        return postProcess(params);
    }
    
    public abstract int main(Param[] params);
    
    public abstract boolean checkParams(Param[] params);

    public Image getInput(String title, Param param) {
        
        String strImage = param.getValue();
        Image img = null;
        if (strImage == null) {
            ImageViewer imageChooser = new ImageViewer(frame, true);
            img = (BufferedImage) imageChooser.select(title);
            if (img == null) {
                exit = true;
            }
        } else {
            try {
                img = ImageStream.readImage(strImage);
            } catch (Exception ex) {
                stdout.error("Error while loading the image. "+ex.getMessage());
                exit = true;
            }
        }
        return img;
    }
    
}
