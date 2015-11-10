package crypto.app.system;

import co.edu.unal.crypto.util.ImageStream;
import crypto.app.view.ImageViewer;
import crypto.app.view.StringInputDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc
 */
public class ParamReader {
    
    public static TextConsole stdout;
    public static Frame frame;
    
    public static void setFrame(Frame f) {
        frame = f;
    }
    
    public static void setStdout(TextConsole out) {
        stdout = out;
    }
    
    public static String getOptionValue(Param p, String title, String[] options) {
        
        if (p == null) {
            return null;
        }
        String op = p.getValue();
        if (op == null || op.length() == 0) {
            JComboBox combo = new JComboBox();
            combo.addItem("");
            for (String s : options) {
                combo.addItem(s);
            }
            int r = JOptionPane.showConfirmDialog(frame, combo, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r == JOptionPane.OK_OPTION) {
                return (String) combo.getSelectedItem();
            }
        }
        return op;
    }
    
    public static String getLineString(Param p, String title) {
        
        String v = p.getValue();
        if (v == null || v.length() == 0) {
            v = JOptionPane.showInputDialog(frame, title, "Parameter "+p.getName(), JOptionPane.PLAIN_MESSAGE);
        }
        return v;
    }
    
    public static String getString(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String v = p.getValue();
        if (v == null || v.length() == 0) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            v = sid.showDialog(title);
        }
        return v;
    }
    
    public static Integer getInt(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String strN = p.getValue();
        if (strN == null || strN.length() == 0) {
            strN = JOptionPane.showInputDialog(frame, title, "Parameter "+p.getName(), JOptionPane.PLAIN_MESSAGE);
        }
        if (strN == null) {
            return null;
        }
        try {
            return Integer.parseInt(strN);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid value for parameter "+p.getName()+". "+ex.getMessage());
        }
        return null;
    }
    
    public static Long getUnsignedLong(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String strN = p.getValue();
        if (strN == null || strN.length() == 0) {
            strN = JOptionPane.showInputDialog(frame, title, "Parameter "+p.getName(), JOptionPane.PLAIN_MESSAGE);
        }
        if (strN == null) {
            return null;
        }
        try {
            return Long.parseUnsignedLong(strN, 16);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid "+p.getValue()+" value: "+strN);
        }
        return null;
    }
    
    public static BigInteger getBigInt(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String strValue = p.getValue();
        if (strValue == null || strValue.length() == 0) {
            strValue = JOptionPane.showInputDialog(frame, title, "Parameter "+p.getName(), JOptionPane.PLAIN_MESSAGE);
        }
        if (strValue == null) {
            return null;
        }
        BigInteger value = null;
        try {
            value = new BigInteger(strValue);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid parameter '"+p.getName()+"'. "+ex.getMessage());
        }
        return value;
    }
    
    public static File getInputFile(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String path = p.getValue();
        if (path == null || path.length() == 0) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(title);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                return fileChooser.getSelectedFile();
            }
        } else {
            return new File(path);
        }
        return null;
    }
    
    public static File getOutputFile(Param p, String title) {
        
        if (p == null) {
            return null;
        }
        String path = p.getValue();
        if (path == null || path.length() == 0) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(title);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                if (!outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                    } catch (IOException ex) {
                        stdout.error("Cannot create output file: "+outputFile.getPath());
                        return null;
                    }
                }
                return outputFile;
            }
        } else {
            return new File(path);
        }
        return null;
    }
    
    public static Image getImage(Param param, String title, boolean binary) {
        
        if (param == null) {
            return null;
        }
        String strImage = param.getValue();
        if (strImage == null || strImage.length() == 0) {
            ImageViewer imageChooser = new ImageViewer(frame, true);
            return (BufferedImage) imageChooser.select(title, binary);
        } else {
            try {
                return ImageStream.readImage(strImage);
            } catch (Exception ex) {
                stdout.error("Error while loading the image. "+ex.getMessage());
            }
            return null;
        }
    }
}
