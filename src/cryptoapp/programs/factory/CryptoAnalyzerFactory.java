package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.CryptoAnalyzerProgram;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class CryptoAnalyzerFactory implements ProgramFactory {

    Environment env;

    public CryptoAnalyzerFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new CryptoAnalyzerProgram(env);
    }
    
}
