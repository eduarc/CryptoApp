package cryptoapp;

import co.edu.unal.system.Prompt;
import javax.swing.JTextField;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class StandardPrompt implements Prompt {
    
    JTextField field;
    
    public StandardPrompt(JTextField field) {
        this.field = field;
    }

    @Override
    public void disable() {
        field.setEditable(false);
    }

    @Override
    public void enable() {
        field.setEditable(true);
    }

    @Override
    public void clear() {
        field.setText("");
    }
}
