package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.FactorSuccession;

/**
 *
 * @author eduarc
 */
public class FactorSuccessionFactory implements ProgramFactory {

    private final Environment env;
    
    public FactorSuccessionFactory(Environment env) {    
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new FactorSuccession(env);
    }
    
}
