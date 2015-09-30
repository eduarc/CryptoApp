package co.edu.unal.system.program;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;

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
        
        StandardConsole stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        if (params.length > 1) {
            stdout.error("Unknown paramaters");
            return -1;
        }
        stdout.clear();
        return 0;
    }

    @Override
    public String getName() {
        return CMD_CLEAR;
    }
}
