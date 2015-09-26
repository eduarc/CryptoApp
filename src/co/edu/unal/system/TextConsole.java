package co.edu.unal.system;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public interface TextConsole {
    
    public void append(Character[] data);
    public void appendln(Character[] data);
    public void append(String s);
    public void appendln(String s);
    public void clear();
    public void setText(String s);
}
