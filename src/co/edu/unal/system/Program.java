/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unal.system;

/**
 *
 * @author eduarc
 */
public abstract class Program {
    
    private final Environment env;
    private int exitCode;
    
    public Program(Environment env) {
        
        this.env = env;
    }
    
    public abstract int exec(Param[] params);
    public abstract String getName();
    
    public Environment getEnv() {
        return env;
    }
    
    public int getExitCode() {
        return exitCode;
    }
}
