package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.VigenereProgram;

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
