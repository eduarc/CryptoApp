package cryptoapp.program;

import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;
import cryptoapp.view.ImageViewer;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    BufferedImage inputImage;
    
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
        
        Param p = ParamUtils.getParam(params, P_INPUT);
        if (p != null) {
            String str = ParamReader.getString(p, "Input data to Encrypt/Decrypt");
            if (str == null) {
                return -1;
            }
            input = CharStream.fromString(str);
        }
        
        p = ParamUtils.getParam(params, P_FILE_INPUT);
        if (p != null) {
            inputFile = ParamReader.getInputFile(p);
            try {
                input = CharStream.fromFile(inputFile);
            } catch (IOException ex) {
                stdout.error("Error while reading the input file: "+inputFile.getPath());
                return -1;
            }
        }
         
        p = ParamUtils.getParam(params, P_IMAGE_INPUT);
        if (p != null) {
            inputImage = (BufferedImage) ParamReader.getImage(p, "Input Image to Encrypt/Decrypt", false);
            if (inputImage == null) {
                return -1;
            }
            input = CharStream.fromImage(inputImage);
        }
        
        p = ParamUtils.getParam(params, P_FILE_OUTPUT);
        if (p != null) {
            outputFile = ParamReader.getOutputFile(p);
            if (outputFile == null) {
                return -1;
            }
        }
        if (!checkParams(params)) {
            return -1;
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
            stdout.append("To file: "+outputFile.getAbsolutePath());
            try {
                CharStream.toFile(outputFile, output);
            } catch (IOException ex) {
                stdout.error("Error while writing to the output file: "+outputFile.getPath());
            }
        }
        else if (ParamUtils.contains(params, P_IMAGE_OUTPUT)) {
            ImageViewer viewer = new ImageViewer(frame, false);
            viewer.show("Result", CharStream.toImage(output, inputImage.getWidth(), inputImage.getHeight()));
        }
        else if (output != null) {
            stdout.append(output);
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
}
