package co.edu.unal.system;

import co.edu.unal.crypto.tools.ImageStream;
import cryptoapp.view.ImageViewer;
import cryptoapp.view.StringInputDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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
    
    public static String getString(Param p, String title) {
        
        String v = p.getValue();
        if (v == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            v = sid.showDialog(title);
        }
        return v;
    }
    
    public static Integer getInt(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Parameter "+p.getName());
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
    
    public static Long getUnsignedLong(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Parameter "+p.getName()+" (64 bits hexadecimal value)");
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
    
    public static BigInteger getBigInt(Param p) {
        
        String strValue = p.getValue();
        if (strValue == null) {
            strValue = JOptionPane.showInputDialog(frame, "Parameter "+p.getName());
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
    
    public static File getInputFile(Param p) {
        
        File inputFile = null;
        
        String path = p.getValue();
        if (path == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select the input file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                inputFile = fileChooser.getSelectedFile();
            }
        } else {
            inputFile = new File(path);
        }
        return inputFile;
    }
    
    public static File getOutputFile(Param p) {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select the output file");
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
        return null;
    }
    
    public static Image getImage(Param param, String title, boolean binary) {
        
        String strImage = param.getValue();
        if (strImage == null) {
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
