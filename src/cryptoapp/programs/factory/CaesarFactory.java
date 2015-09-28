package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.CaesarProgram;

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
