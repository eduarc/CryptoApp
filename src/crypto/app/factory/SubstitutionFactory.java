package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.SubstitutionProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class SubstitutionFactory implements ProgramFactory {

    Environment env;
    
    public SubstitutionFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new SubstitutionProgram(env);
    }
    
}
