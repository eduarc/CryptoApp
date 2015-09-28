package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.RSAProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class RSAFactory implements ProgramFactory {

    Environment env;

    public RSAFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new RSAProgram(env);
    }
    
}
