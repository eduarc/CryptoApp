package crypto.app.factory;

import crypto.app.system.Environment;
import crypto.app.system.Program;
import crypto.app.system.ProgramFactory;
import crypto.app.program.FrequencyProgram;

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
