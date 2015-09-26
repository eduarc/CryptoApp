/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unal.system;

import java.util.TreeMap;

/**
 *
 * @author eduarc
 */
public class Environment {
    
    public static String FRAME = "frame";
    public static String PROMPT = "prompt";
    public static String STDOUT = "stdout";
    
    public static final int PARSING_ERROR = -2;
    public static final int CMD_NOT_FOUND = -3;
    public static final int RUNTIME_ERROR = -3;
    
    private String name;
    private final TreeMap<String, Object> resources;
    private final TreeMap<String, ProgramFactory> installed;
    
    public Environment() {
        
        resources = new TreeMap();
        installed = new TreeMap();
    }
    
    public void addResource(String name, Object obj) {
        resources.put(name, obj);
    }
    
    public void removeResouce(String name) {
        resources.remove(name);
    }
    
    public void addProgram(String pName, ProgramFactory factory) {
        installed.put(pName, factory);
    } 
    
    public void removeProgram(String pName) {
        installed.remove(name);
    } 
    
    public Object getResource(String rsrc) {
        return resources.get(rsrc);
    }
    
    public int exec(String cmdLine) {
        
        TextConsole output = (TextConsole) getResource(Environment.STDOUT);
        
        CommandParser cmdParser = new CommandParser(cmdLine);
        Param[] params;
        try {
            params = cmdParser.parse();
        } catch (IllegalArgumentException ex) {
            output.append(ex.getMessage());
            return PARSING_ERROR;
        }
        ProgramFactory factory = installed.get(params[0].getValue());
        if (factory == null) {
            output.appendln("Command not found.");
            return Environment.CMD_NOT_FOUND;
        }
        Prompt prompt = (Prompt) getResource(Environment.PROMPT);
        
        Program program = factory.newInstance();
        prompt.disable();
        program.exec(params);
        prompt.enable();
        return program.getExitCode();
    }
}
