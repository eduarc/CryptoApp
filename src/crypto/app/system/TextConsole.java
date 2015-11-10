package crypto.app.system;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public interface TextConsole {
    
    public void info(String msg);
    public void warning(String msg);
    public void error(String msg);
    public void append(Character[] data);
    public void append(String s);
    public void clear();
    public void setText(String s);
}
