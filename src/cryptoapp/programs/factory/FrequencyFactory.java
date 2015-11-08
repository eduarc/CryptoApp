package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.FrequencyProgram;

/**
 *
 * @author eduarc
 */
public class FrequencyFactory implements ProgramFactory {

    Environment env;
    
    public FrequencyFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new FrequencyProgram(env);
    }
    
}
