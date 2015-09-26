/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapp.programs.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import cryptoapp.program.CaesarProgram;

/**
 *
 * @author eduarc
 */
public class CaesarFactory extends ProgramFactory {
    
    private Environment env;
    
    public CaesarFactory(Environment env) {
        this.env = env;
    }

    @Override
    public Program newInstance() {
        return new CaesarProgram(env);
    }
}
