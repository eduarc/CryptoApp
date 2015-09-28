package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.HillProgram;

/**
 *
 * @author eduarc
 */
public class HillFactory implements ProgramFactory {

    Environment env;

    public HillFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new HillProgram(env);
    }
}
