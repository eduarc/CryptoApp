package cryptoapp.program;

import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;
import cryptoapp.view.StringInputDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public abstract class CryptosystemProgram extends Program {
    
    public static final String P_ENCRYPT = "encrypt";
    public static final String P_DECRYPT = "decrypt";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";
    
    Character[] input;
    Character[] output;
    
    Frame frame;
    StandardConsole stdout;
    boolean exit;

    File outputFile;
    File inputFile;
    
    public CryptosystemProgram(Environment env) {
        super(env);
        
        stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    public int preProcess(Param[] params) {
        
        String[] operation = {P_ENCRYPT, P_DECRYPT};
        
        if (!ParamUtils.containsOne(params, operation)) {
            stdout.error("Any or multiple encrypt/decrypt operation(s) provided");
            return -1;
        }
        String[] sourceInput = {P_INPUT, P_FILE_INPUT, P_IMAGE_INPUT};
        if (!ParamUtils.containsOne(params, sourceInput)) {
            stdout.error("Any or multiple input(s) provided");
            return -1;
        }
        String[] destOutput = {P_FILE_OUTPUT, P_IMAGE_OUTPUT, P_OUTPUT};
        if (!ParamUtils.containsMax(params, destOutput, 1)) {
            stdout.error("Multiple output(s) provided");
            return -1;
        }
        for (Param param : params) {
            String name = param.getName();
            if (name.equals(P_INPUT)) {
                getString(param);
            }
            else if (name.equals(P_FILE_INPUT)) {
                getInputFromFile(param);
            }
            else if (name.equals(P_IMAGE_INPUT)) {
                
            }
            else if (name.equals(P_FILE_OUTPUT)) {
                getOutputFile(param);
            }
            else if (name.equals(P_IMAGE_OUTPUT)) {
                
            }
            if (exit) {
                return 0;
            }
        }
        if (!checkParams(params)) {
            return -1;
        }
        return 0;
    }
    
    public int postProcess(Param[] params) {
        
        if (input != null && ParamUtils.contains(params, P_INPUT)) {
            //stdout.appendln(input);
        }
        else if (ParamUtils.contains(params, P_IMAGE_INPUT)) {
            
        }
        else if (ParamUtils.contains(params, P_FILE_INPUT)) {
            //stdout.appendln("Input:  "+inputFile.getAbsolutePath());
        }
        if (ParamUtils.contains(params, P_FILE_OUTPUT)) {
            //stdout.appendln("Output: "+outputFile.getAbsolutePath());
            try {
                CharStream.fwrite(outputFile, output);
            } catch (IOException ex) {
                stdout.error("Error while writing to the output file: "+outputFile.getPath());
            }
        }
        else if (ParamUtils.contains(params, P_IMAGE_OUTPUT)) {
            
        }
        else if (output != null) {
            stdout.appendln(output);
        }
        return 0;
    }
    
    @Override
    public int exec(Param[] params) {
        
        int r = preProcess(params);
        if (r != 0) {
            return r;
        }
        r = main(params);
        if (r != 0) {
            return r;
        }
        return postProcess(params);
    }
    
    public abstract int main(Param[] params);
    
    public abstract boolean checkParams(Param[] params);
    
    public BigInteger getBigInt(Param p) {
        
        String strValue = p.getValue();
        if (strValue == null) {
            strValue = JOptionPane.showInputDialog(frame, "Parameter "+p.getName());
        }
        if (strValue == null) {
            exit = true;
            return null;
        }
        BigInteger value = null;
        try {
            value = new BigInteger(strValue);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid parameter '"+p.getName()+"'. "+ex.getMessage());
            exit = true;
        }
        return value;
    }
    
    public Integer getInt(Param p) {
        
        String strN = p.getValue();
        if (strN == null) {
            strN = JOptionPane.showInputDialog(frame, "Parameter "+p.getName());
        }
        if (strN == null) {
            exit = true;
            return null;
        }
        try {
            return Integer.parseInt(strN);
        } catch(NumberFormatException ex) {
            stdout.error("Invalid value for parameter "+p.getName()+". "+ex.getMessage());
            exit = true;
        }
        return null;
    }
    
    protected void getString(Param p) {
        
        String v = p.getValue();
        if (v == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            v = sid.showDialog("Input");
        }
        if (v == null) {
            exit = true;
            return;
        }
        input = CharStream.fromString(v);
    }

    protected void getInputFromFile(Param p) {
        
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
        if (inputFile == null) {
            exit = true;
            return;
        }
        try {
            input = CharStream.fread(inputFile);
        } catch (IOException ex) {
            stdout.error("Error while reading the input file: "+inputFile.getPath());
            exit = true;
        }
    }
    
    protected void getOutputFile(Param p) {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select the output file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            outputFile = fileChooser.getSelectedFile();
            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException ex) {
                    stdout.error("Cannot create output file: "+outputFile.getPath());
                    exit = true;
                }
            }
        } else {
            exit = true;
        }
    }
}
