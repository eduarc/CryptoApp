package co.edu.unal.system;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
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
