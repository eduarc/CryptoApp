package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.CryptoAnalyzerProgram;

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
