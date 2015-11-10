package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.CaesarProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class CaesarFactory implements ProgramFactory {
    
    private final Environment env;
    
    public CaesarFactory(Environment env) {
        this.env = env;
    }

    @Override
    public Program newInstance() {
        return new CaesarProgram(env);
    }
}
