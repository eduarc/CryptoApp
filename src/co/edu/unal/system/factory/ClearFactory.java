package co.edu.unal.system.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import co.edu.unal.system.program.ClearProgram;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class ClearFactory implements ProgramFactory {

    Environment env;
            
    public ClearFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new ClearProgram(env);
    }   
}
