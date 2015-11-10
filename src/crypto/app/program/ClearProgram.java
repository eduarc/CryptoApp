package crypto.app.program;

import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class ClearProgram extends Program {

    public static final String CMD_CLEAR = "clear";
    
    public ClearProgram(Environment env) {
        super(env);
    }

    @Override
    public int exec(Param[] params) {
        
        StandardTextConsole stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        if (params.length > 1) {
            stdout.error("Unknown paramaters");
            return -1;
        }
        stdout.clear();
        return 0;
    }

}
