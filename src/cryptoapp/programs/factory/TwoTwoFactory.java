package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.TwoTwoProgram;

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
        return new TwoTwoProgram(env);
    }
    
}
