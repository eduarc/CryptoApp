package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.AffineProgram;

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
