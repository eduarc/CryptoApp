package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.HillReloadedProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class HillReloadedFactory implements ProgramFactory {

    Environment env;

    public HillReloadedFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new HillReloadedProgram(env);
    }
}
