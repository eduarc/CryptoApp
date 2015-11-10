package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.VigenereProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class VigenereFactory implements ProgramFactory {

    Environment env;
    
    public VigenereFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new VigenereProgram(env);
    }
    
}
