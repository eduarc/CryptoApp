package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.RSAProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class RSAFactory implements ProgramFactory {

    Environment env;

    public RSAFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new RSAProgram(env);
    }
    
}
