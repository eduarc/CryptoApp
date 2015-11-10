package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.AffineProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class AffineFactory implements ProgramFactory {

    Environment env;
    
    public AffineFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new AffineProgram(env);
    }
    
}
