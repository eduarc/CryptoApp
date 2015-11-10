package crypto.app.factory;

import crypto.app.program.KeyGenProgram;
import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;

/**
 *
 * @author eduarc
 */
public class KeyGenFactory implements ProgramFactory {

    Environment env;
    
    public KeyGenFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new KeyGenProgram(env);
    }
    
}
