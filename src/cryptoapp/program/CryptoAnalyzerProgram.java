package cryptoapp.program;

import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;
import cryptoapp.crack.AffineAnalyzer;
import cryptoapp.crack.CaesarAnalyzer;
import cryptoapp.crack.RSAAnalyzer;
import cryptoapp.view.StringInputDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class CryptoAnalyzerProgram extends Program {

    public static final String CMD_CRACK = "crack";
    public static final String P_RSA = "rsa";
    public static final String P_CAESAR = "caesar";
    public static final String P_AFFINE = "affine";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";
    public static final String P_N = "n";
    public static final String P_E = "e";
    
    Frame frame;
    StandardConsole stdout;
    boolean exit;
    
    Character[] input;
    Character[] output;
    File outputFile;
    File inputFile;
    
    public CryptoAnalyzerProgram(Environment env) {
        super(env);
        
        stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }

    public int checkParams(Param[] params) {
        
        String[] algos = {P_RSA, P_CAESAR, P_AFFINE};
        if (!ParamUtils.containsOne(params, algos)) {
            stdout.error("Any or multiple target algorithm(s) provided");
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
                getString(param, "Input encrypted data");
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
                return -1;
            }
        }
        
        if (ParamUtils.contains(params, P_RSA)) {
            if (!ParamUtils.contains(params, P_N)) {
                stdout.error("Parameter "+P_N+" not provided");
                return -1;
            }
            if (!ParamUtils.contains(params, P_E)) {
                stdout.error("Parameter "+P_E+" not provided");
                return -1;
            }
        }
        return 0;
    }
    
    public int postProcess(Param[] params) {
        
        if (input != null && ParamUtils.contains(params, P_INPUT)) {
            
        }
        else if (ParamUtils.contains(params, P_IMAGE_INPUT)) {
            
        }
        else if (ParamUtils.contains(params, P_FILE_INPUT)) {
        }
        if (ParamUtils.contains(params, P_FILE_OUTPUT)) {
            try {
                CharStream.fwrite(outputFile, output);
            } catch (IOException ex) {
                stdout.error("Error while writing output to file: "+outputFile.getPath());
            }
        }
        else if (ParamUtils.contains(params, P_IMAGE_OUTPUT)) {
            
        }
        else if (output != null) {
            stdout.append(output);
        }
        return 0;
    }
    
    @Override
    public int exec(Param[] params) {
        
        int ret = checkParams(params);
        if (ret != 0) {
            return ret;
        }
        if (ParamUtils.contains(params, P_RSA)) {
            BigInteger n = null, e = null;
            for (Param param : params) {
                if (param.getName().equals(P_N)) {
                    n = getBigInt(param);
                }
                else if (param.getName().equals(P_E)) {
                    e = getBigInt(param);
                }
                if (exit) {
                    return 0;
                }
            }
            
            List<BigInteger> rsaInput = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(CharStream.toString(input), " ");
            while (tokenizer.hasMoreTokens()) {
                try {
                    rsaInput.add(new BigInteger(tokenizer.nextToken(), 16));
                } catch(NumberFormatException ex) {
                    stdout.error("Bad Input. "+ex.getMessage());
                    return -1;
                }
            }
            
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From File: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            stdout.info("Parameters:");
            stdout.append(P_N+" = "+n.toString());
            stdout.append(P_E+" = "+e.toString());
            
            RSAAnalyzer cracker = new RSAAnalyzer(n, e);
            try {
                stdout.info("Cracking...");
                output = cracker.analyze(rsaInput.toArray(new BigInteger[]{}));
            } catch (Exception ex) {
                stdout.error("Error while cracking. "+ex.getMessage());
            }
        }
        else if (ParamUtils.contains(params, P_CAESAR)) {
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            CaesarAnalyzer cracker = new CaesarAnalyzer();
            try {
                stdout.info("Cracking...");
                output = cracker.analyze(input);
            } catch (Exception ex) {
                stdout.error("Error while cracking. "+ex.getMessage());
            }
        }
        else if (ParamUtils.contains(params, P_AFFINE)) {
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            AffineAnalyzer cracker = new AffineAnalyzer();
            try {
                stdout.info("Cracking...");
                output = cracker.analyze(input);
            } catch (Exception ex) {
                stdout.error("Error while cracking. "+ex.getMessage());
            }
        }
        
        ret = postProcess(params);
        return ret;
    }

    @Override
    public String getName() {
        return CMD_CRACK;
    }
    
    private BigInteger getBigInt(Param p) {
        
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
    
    protected void getString(Param p, String title) {
        
        String v = p.getValue();
        if (v == null) {
            StringInputDialog sid = new StringInputDialog(frame, true);
            v = sid.showDialog(title);
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
