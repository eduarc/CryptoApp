/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapp.program;

import co.edu.unal.crypto.cryptosystem.Cipher;
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
import javax.swing.JFileChooser;

/**
 *
 * @author eduarc
 */
public abstract class CryptoProgram extends Program {
    
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
    Cipher cipher;
    Frame frame;
    StandardConsole stdout;
    boolean exit;

    File outputFile;
    File inputFile;
    
    public CryptoProgram(Environment env) {
        super(env);
        
        stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    public int preProcess(Param[] params) {
        
        String[] operation = {P_ENCRYPT, P_DECRYPT};
        
        if (!ParamUtils.containsOne(params, operation)) {
            stdout.appendln("Any or multiple encrypt/decrypt operation(s) provided");
            return -1;
        }
        String[] sourceInput = {P_INPUT, P_FILE_INPUT, P_IMAGE_INPUT};
        if (!ParamUtils.containsOne(params, sourceInput)) {
            stdout.appendln("Any or multiple input(s) provided.");
            return -1;
        }
        String[] destOutput = {P_FILE_OUTPUT, P_IMAGE_OUTPUT, P_OUTPUT};
        if (!ParamUtils.containsMax(params, destOutput, 1)) {
            stdout.appendln("multiple output(s) provided.");
            return -1;
        }
        for (Param param : params) {
            String name = param.getName();
            if (name.equals(P_INPUT)) {
                getInput(param);
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
        
        stdout.appendln("\n--- INPUT ---\n");
        if (ParamUtils.contains(params, P_INPUT)) {
            stdout.appendln(input);
        }
        else if (ParamUtils.contains(params, P_IMAGE_INPUT)) {
            
        }
        else if (ParamUtils.contains(params, P_FILE_INPUT)) {
            stdout.appendln("File: "+inputFile.getAbsolutePath());
        }
        stdout.appendln("\n--- OUTPUT ---\n");
        if (ParamUtils.contains(params, P_FILE_OUTPUT)) {
            stdout.appendln("File: "+outputFile.getAbsolutePath());
            try {
                CharStream.fwrite(outputFile, output);
            } catch (IOException ex) {
                stdout.appendln("Error while writing output to file: "+outputFile.getPath());
            }
        }
        else if (ParamUtils.contains(params, P_IMAGE_OUTPUT)) {
            
        }
        else {
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
    
    protected void getInput(Param p) {
        
        String v = p.getValue();
        if (v == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            sid.showDialog("Input");
            v = sid.input;
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
            CharStream.out(input);
        } catch (IOException ex) {
            stdout.appendln("Error while reading the input file: "+inputFile.getPath());
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
                    stdout.appendln("Cannot create output file: "+outputFile.getPath());
                    exit = true;
                }
            }
        } else {
            exit = true;
        }
    }
}
