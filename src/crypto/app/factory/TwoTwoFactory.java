package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.VCSn2k2Program;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class TwoTwoFactory implements ProgramFactory {

    Environment env;
    
    public TwoTwoFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new VCSn2k2Program(env);
    }
    
}
