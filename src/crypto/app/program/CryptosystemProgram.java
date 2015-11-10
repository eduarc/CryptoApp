package crypto.app.program;

import co.edu.unal.crypto.util.CharStream;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;
import crypto.app.view.ImageViewer;
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
    
    public static final String P_OP = "op";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";
    
    public static final String V_OP_ENCRYPT = "encrypt";
    public static final String V_OP_DECRYPT = "decrypt";
    
    String[] vOperation = {V_OP_ENCRYPT, V_OP_DECRYPT};
    
    String operation;
    Character[] input;
    Character[] output;
    
    Frame frame;
    StandardTextConsole stdout;
    boolean exit;

    File outputFile;
    File inputFile;
    BufferedImage inputImage;
    
    public CryptosystemProgram(Environment env) {
        super(env);
        
        stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    public int preProcess(Param[] params) {
        
        Param pOperation = ParamUtils.getParam(params, P_OP);
        operation = ParamReader.getOptionValue(pOperation, "Select the operation to execute", vOperation);
        if (operation == null || operation.length() == 0) {
            stdout.error("Parameter "+P_OP+" not provided");
            return -1;
        }
        if (!operation.equals(V_OP_ENCRYPT) && !operation.equals(V_OP_DECRYPT)) {
            stdout.error("Unknown "+P_OP+"="+operation);
            return -1;
        }
        
        String[] sourceInput = {P_INPUT, P_FILE_INPUT, P_IMAGE_INPUT};
        if (!ParamUtils.containsOne(params, sourceInput)) {
            stdout.error("Any or multiple input(s) sources provided");
            return -1;
        }
        String[] destOutput = {P_FILE_OUTPUT, P_IMAGE_OUTPUT, P_OUTPUT};
        if (!ParamUtils.containsMax(params, destOutput, 1)) {
            stdout.error("Multiple output(s) destionation provided");
            return -1;
        }
        
        Param p = ParamUtils.getParam(params, P_INPUT);
        String str = ParamReader.getString(p, "Input data to Encrypt/Decrypt");
        if (str != null) {
            input = CharStream.fromString(str);
        }
        
        p = ParamUtils.getParam(params, P_FILE_INPUT);
        inputFile = ParamReader.getInputFile(p, "Select the input file");
        if (inputFile != null) {
            try {
                input = CharStream.fromFile(inputFile);
            } catch (IOException ex) {
                stdout.error("Error while reading the input file: "+inputFile.getPath());
                return -1;
            }
        }
        
        p = ParamUtils.getParam(params, P_IMAGE_INPUT);
        inputImage = (BufferedImage) ParamReader.getImage(p, "Input Image to Encrypt/Decrypt", false);
        if (inputImage != null) {
            input = CharStream.fromImage(inputImage);
        }
        
        if (input == null) {
            stdout.error("No input data was provided");
            return -1;
        }
        
        p = ParamUtils.getParam(params, P_FILE_OUTPUT);
        outputFile = ParamReader.getOutputFile(p, "Select the output file");
        if (outputFile == null && p != null) {
            stdout.error("Output file not provided");
            return -1;
        }
        return 0;
    }
    
    public int postProcess(Param[] params) {
        
        if (outputFile != null) {
            stdout.append("To file: "+outputFile.getAbsolutePath());
            try {
                CharStream.toFile(outputFile, output);
            } catch (IOException ex) {
                stdout.error("Error while writing to the output file: "+outputFile.getPath());
                return -1;
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
}
