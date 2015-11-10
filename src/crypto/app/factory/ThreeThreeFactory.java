package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.VCSn3k3Program;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class ThreeThreeFactory implements ProgramFactory {
    
    Environment env;
    
    public ThreeThreeFactory(Environment env) {
        this.env = env;
    }

    @Override
    public Program newInstance() {
        return new VCSn3k3Program(env);
    }
}
