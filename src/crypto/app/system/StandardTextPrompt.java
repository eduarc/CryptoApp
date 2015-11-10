package crypto.app.system;

import javax.swing.JTextField;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class StandardTextPrompt implements Prompt {
    
    JTextField field;
    
    public StandardTextPrompt(JTextField field) {
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
