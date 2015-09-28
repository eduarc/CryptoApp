package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.SubstitutionProgram;

/**
 *
 * @author eduarc
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
