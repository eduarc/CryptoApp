package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.ThreeThreeProgram;

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
        return new ThreeThreeProgram(env);
    }
}
