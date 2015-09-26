package cryptoapp;

import co.edu.unal.system.TextConsole;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class StandardConsole implements TextConsole {

    JTextPane output;
    
    public StandardConsole(JTextPane output) {
        this.output = output;
    }
    
    @Override
    public void append(Character[] data) {
        
        try {
            Document doc = output.getDocument();
            for (Character c : data) {
                doc.insertString(doc.getEndPosition().getOffset(), c+"", null);
            }
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void append(String s) {
        
        try {
            Document doc = output.getDocument();
            doc.insertString(doc.getEndPosition().getOffset(), s, null);
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void appendln(String s) {
        append(s+"\n");
    }

    @Override
    public void clear() {
        output.setText("");
    }

    @Override
    public void setText(String s) {
        output.setText(s);
    }

    @Override
    public void appendln(Character[] data) {
        
        append(data);
        append("\n");
    }
}
