package crypto.app.program;

import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;
import java.awt.Frame;
import java.awt.image.BufferedImage;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public abstract class VCSProgram extends Program {

    public static final String P_OP = "op";
    public static final String P_SECRET = "secret";
    
    public static final String V_OP_ENCRYPT = "encrypt";
    public static final String V_OP_DECRYPT = "decrypt";
    
    String[] vOperation = {V_OP_ENCRYPT, V_OP_DECRYPT};
    
    String operation;
    Frame frame;
    StandardTextConsole stdout;
    BufferedImage input;
    
    public VCSProgram(Environment env) {
        super(env);
        
        stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    public int preProcess(Param[] params) {
        
        Param pOperation = ParamUtils.getParam(params, P_OP);
        operation = ParamReader.getOptionValue(pOperation, "Select the operation to execute", vOperation);
        if (operation == null || operation.length() == 0) {
            stdout.error("Parameter "+P_OP+" not provided");
            return -1;
        }
        if (!operation.equals(V_OP_ENCRYPT) && !operation.equals(V_OP_DECRYPT)) {
            stdout.error("Unknown "+P_OP+"="+operation);
            return -1;
        }

        Param p = ParamUtils.getParam(params, P_SECRET);
        input = (BufferedImage) ParamReader.getImage(p, "Select the Secret Image", true);
        if (input == null) {
            stdout.error("Parameter "+P_SECRET+" not provided");
            return -1;
        }
        return 0;
    }
    
    @Override
    public int exec(Param[] params) {
        
        int r = preProcess(params);
        if (r != 0) {
            return r;
        }
        return main(params);
    }
    
    public abstract int main(Param[] params);
}
