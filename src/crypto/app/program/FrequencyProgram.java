package crypto.app.program;

import co.edu.unal.crypto.analyzer.ascii.FrequencyAnalyzer;
import co.edu.unal.crypto.util.CharStream;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author eduarc
 */
public class FrequencyProgram extends Program {

    public static final String CMD_FREQUENCY = "freq";
    public static final String P_TRIGRAM = "trigram";
    public static final String P_DIGRAM = "digram";
    public static final String P_CHARS = "chars";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";

    Frame frame;
    StandardTextConsole stdout;
    
    File outputFile;
    File inputFile;
    
    public FrequencyProgram(Environment env) {
        super(env);
        
        stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }
    
    @Override
    public int exec(Param[] params) {
        
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
        
        Character[] input = null;
        String output = "";
        
        Param p = ParamUtils.getParam(params, P_INPUT);
        String str = ParamReader.getString(p, "Input Text to Analyse");
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
        
        stdout.info("Input:");
        if (inputFile != null) {
            stdout.append("From file: "+inputFile.getAbsolutePath());
        } else {
            stdout.append(input);
        }
        stdout.info("Counting...");
        
        if (ParamUtils.contains(params, P_TRIGRAM)) {
            output = "Trigram Frequency:\n";
            Map<String, Integer> trigrams = FrequencyAnalyzer.trigrams(input);
            int cols = 0;
            for (Map.Entry<String, Integer> k : trigrams.entrySet()) {
                output += k.getKey()+" = "+k.getValue();
                if (cols > 6) {
                    cols = 0;
                    output += "\n";
                } else {
                    output += "\t\t";
                    cols++;
                }
            }
            if (!output.endsWith("\n")) {
                output += "\n";
            }
        }
        if (ParamUtils.contains(params, P_DIGRAM)) {
            output += "Digram Frequency:\n";
            int cols = 0;
            Map<String, Integer> digrams = FrequencyAnalyzer.digrams(input);
            for (Map.Entry<String, Integer> k : digrams.entrySet()) {
                output += k.getKey()+" = "+k.getValue();
                if (cols > 6) {
                    cols = 0;
                    output += "\n";
                } else {
                    output += "\t\t";
                    cols++;
                }
            }
            if (!output.endsWith("\n")) {
                output += "\n";
            }
        }
        if (ParamUtils.contains(params, P_CHARS)) {
            output += "Character Frequency:\n";
            Map<Character, Integer> chars = FrequencyAnalyzer.characters(input);
            int cols = 0;
            for (Map.Entry<Character, Integer> k : chars.entrySet()) {
                output += k.getKey()+" = "+k.getValue();
                if (cols > 6) {
                    cols = 0;
                    output += "\n";
                } else {
                    output += "\t\t";
                    cols++;
                }
            }
            if (!output.endsWith("\n")) {
                output += "\n";
            }
        }
        
        if (outputFile != null) {
            try {
                CharStream.toFile(outputFile, CharStream.fromString(output));
            } catch (IOException ex) {
                stdout.error("Error while writing output to file: "+outputFile.getPath());
                return -1;
            }
        }
        else {
            stdout.append(CharStream.fromString(output));
        }
        return 0;
    }
    
}
