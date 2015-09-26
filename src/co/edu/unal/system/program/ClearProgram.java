/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unal.system.program;

import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;

/**
 *
 * @author eduarc
 */
public class ClearProgram extends Program {

    public static final String CMD_CLEAR = "clear";
    
    public ClearProgram(Environment env) {
        super(env);
    }

    @Override
    public int exec(Param[] params) {
        
        StandardConsole stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        if (params.length > 1) {
            stdout.appendln("Error: Invalid paramaters");
            return -1;
        }
        stdout.clear();
        return 0;
    }

    @Override
    public String getName() {
        return CMD_CLEAR;
    }
}
