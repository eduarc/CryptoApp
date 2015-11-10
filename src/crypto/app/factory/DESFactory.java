package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.DESProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class DESFactory implements ProgramFactory {

    Environment env;
    
    public DESFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new DESProgram(env);
    }
    
}
