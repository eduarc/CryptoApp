package crypto.app.system;

import crypto.app.system.TextConsole;
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
public class StandardTextConsole implements TextConsole {

    JTextPane output;
    HTMLEditorKit kit;
    HTMLDocument doc;
    
    public StandardTextConsole(JTextPane output) {
        
        this.output = output;
        kit = new HTMLEditorKit();
        doc = new HTMLDocument();
        output.setEditorKit(kit);
        output.setDocument(doc);
        output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
    }
    
    @Override
    public void info(String msg) {
        
        msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        formatted("<font color='00FFFF'>"+msg+"</font>");
    }
    
    @Override
    public void warning(String msg) {
        
        msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        formatted("<font color='FFA500'>"+msg+"</font>");
    }
    
    @Override
    public void error(String msg) {
        
        msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        formatted("<font color='FF0000'>"+msg+"</font>");
    }
    
    @Override
    public void append(Character[] data) {
        
        try {
            String s = "";
            for (Character c : data) {
                s += c;
            }
            s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            kit.insertHTML(doc, doc.getLength(), "<pre>"+s+"</pre>", 0, 0, null);
        }
        catch (IOException | BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void append(String s) {
        
        try {
            s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            kit.insertHTML(doc, doc.getLength(), "<pre>"+s+"</pre>", 0, 0, null);
        } catch (IOException | BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void clear() {
        output.setText("");
    }

    @Override
    public void setText(String s) {
        output.setText(s);
    }
    
    public void formatted(String s) {
        
        try {
            kit.insertHTML(doc, doc.getLength(), "<pre>"+s+"</pre>", 0, 0, null);
        } catch (IOException | BadLocationException ex) {
            JOptionPane.showMessageDialog(output, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
