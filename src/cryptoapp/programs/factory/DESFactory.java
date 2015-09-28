package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.DESProgram;

/**
 *
 * @author eduarc
 */
public class DESFactory implements ProgramFactory {

    Environment env;
    
    public DESFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new DESProgram(env);
    }
    
}
