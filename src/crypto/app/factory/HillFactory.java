package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.HillProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class HillFactory implements ProgramFactory {

    Environment env;

    public HillFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new HillProgram(env);
    }
}
