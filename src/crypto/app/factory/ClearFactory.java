package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.ClearProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class ClearFactory implements ProgramFactory {

    Environment env;
            
    public ClearFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new ClearProgram(env);
    }   
}
