/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unal.system.factory;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Program;
import co.edu.unal.system.ProgramFactory;
import co.edu.unal.system.program.ClearProgram;

/**
 *
 * @author eduarc
 */
public class ClearFactory extends ProgramFactory {

    Environment env;
            
    public ClearFactory(Environment env) {
        this.env = env;
    }
    
    @Override
    public Program newInstance() {
        return new ClearProgram(env);
    }   
}
