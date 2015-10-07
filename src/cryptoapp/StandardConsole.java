package cryptoapp;

import co.edu.unal.system.TextConsole;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class StandardConsole implements TextConsole {

    JTextPane output;
    HTMLEditorKit kit;
    HTMLDocument doc;
    
    public StandardConsole(JTextPane output) {
        
        this.output = output;
        
        kit = new HTMLEditorKit();
        doc = new HTMLDocument();
        output.setEditorKit(kit);
        output.setDocument(doc);
        output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
    }
    
    public void info(String msg) {
        appendln("<font color='00FFFF'>"+msg+"</font>");
    }
    
    public void warning(String msg) {
        appendln("<font color='FFA500'>"+msg+"</font>");
    }
    
    public void error(String msg) {
        appendln("<font color='FF0000'>"+msg+"</font>");
    }
    
    @Override
    public void append(Character[] data) {
        
        String s = "";
        try {
            for (Character c : data) {
                s += c;
            }
            kit.insertHTML(doc, doc.getLength(), s, 0, 0, null);
        } 
        catch (IOException | BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void append(String s) {
        
        try {
            kit.insertHTML(doc, doc.getLength(), s, 0, 0, null);
        } catch (IOException | BadLocationException ex) {
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
