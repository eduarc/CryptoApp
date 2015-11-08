package cryptoapp.program;

import co.edu.unal.crypto.tools.AllMatch;
import co.edu.unal.crypto.tools.CharStream;
import co.edu.unal.crypto.tools.ConfidenceMethod;
import co.edu.unal.crypto.tools.OneMatch;
import co.edu.unal.crypto.tools.PrefixEnglishDictionary;
import co.edu.unal.system.Environment;
import co.edu.unal.system.Param;
import co.edu.unal.system.ParamReader;
import co.edu.unal.system.ParamUtils;
import co.edu.unal.system.Program;
import cryptoapp.StandardConsole;
import co.edu.unal.crypto.analyzer.AffineAnalyzer;
import co.edu.unal.crypto.analyzer.CaesarAnalyzer;
import co.edu.unal.crypto.analyzer.RSAAnalyzer;
import co.edu.unal.crypto.analyzer.VigenereAnalyzer;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.types.Pair;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    public static final String P_VIGENERE = "vigenere";
    public static final String P_NUM_GUESSES = "num_guesses";
    public static final String P_CONFIDENCE_METHOD = "confidence_method";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";
    public static final String P_MAX_LEN = "max_key_len";
    public static final String P_N = "n";
    public static final String P_E = "e";
    
    Frame frame;
    StandardConsole stdout;
    boolean exit;
    
    Character[] input;
    Character[] output;
    File outputFile;
    File inputFile;
    Integer num_guesses;
    String confidenceMethodValue;
    ConfidenceMethod confidence;
    
    public CryptoAnalyzerProgram(Environment env) {
        super(env);
        
        stdout = (StandardConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }

    public int checkParams(Param[] params) {
        
        String[] algos = {P_RSA, P_CAESAR, P_AFFINE, P_VIGENERE};
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
        
        if (ParamUtils.contains(params, P_VIGENERE)) {
            if (!ParamUtils.contains(params, P_MAX_LEN)) {
                stdout.error("Parameter "+P_MAX_LEN+" not provided");
                return -1;
            }
        }
        
        num_guesses = 1;
        Param p = ParamUtils.getParam(params, P_NUM_GUESSES);
        if (p != null) {
            num_guesses = ParamReader.getInt(p);
            if (num_guesses == null) {
                return -1;
            }
            if (num_guesses <= 0) {
                stdout.error("Parameter "+P_NUM_GUESSES+" must be greater than zero: "+num_guesses);
                return -1;
            }
        }
        
        confidence = new OneMatch(PrefixEnglishDictionary.defaultInstance);
        p = ParamUtils.getParam(params, P_CONFIDENCE_METHOD);
        if (p != null) {
            String str = ParamReader.getString(p, "Confidence Method");
            if (str == null) {
                return -1;
            }
            if (str.equals("one")) {
                
            }
            else if (str.equals("all")) {
                confidence = new AllMatch(PrefixEnglishDictionary.defaultInstance);
            }
            else {
                stdout.error("Invalid value for parameter "+P_CONFIDENCE_METHOD+": "+str);
                return -1;
            }
        }
              
        p = ParamUtils.getParam(params, P_INPUT);
        if (p != null) {
            String str = ParamReader.getString(p, "Input encrypted data");
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
         
        p = ParamUtils.getParam(params, P_FILE_OUTPUT);
        if (p != null) {
            outputFile = ParamReader.getOutputFile(p);
            if (outputFile == null) {
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
                CharStream.toFile(outputFile, output);
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
            
            Param p = ParamUtils.getParam(params, P_N);
            BigInteger n = ParamReader.getBigInt(p);
            p = ParamUtils.getParam(params, P_E);
            BigInteger e = ParamReader.getBigInt(p);
            if (n == null || e == null) {
                return -1;
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
                Pair<Character[], RSA.Key> res = cracker.analyze(rsaInput.toArray(new BigInteger[]{}), confidence);
                output = res.first;
                RSA.Key key = res.second;
                stdout.info("Key:");
                stdout.append("p = "+key.p+" q = "+key.q+" e = "+key.e);
                stdout.info("Secret:");
                stdout.append(output);
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
                List<Pair<Character[], Integer>> guess = cracker.analyze(input, num_guesses, confidence);
                for (int i = 0; i < guess.size(); i++) {
                    stdout.info("Guess #"+(i+1));
                    Pair<Character[], Integer> p = guess.get(i);
                    Character[] secret = p.first;
                    Integer key = p.second;
                    stdout.info("Key:");
                    stdout.append("offset = "+key);
                    stdout.info("Secret:");
                    stdout.append(secret);
                }
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
                List<Pair<Character[], Pair<Integer, Integer>>> guess = cracker.analyze(input, num_guesses, confidence);
                for (int i = 0; i < guess.size(); i++) {
                    stdout.info("Guess #"+(i+1));
                    Pair<Character[], Pair<Integer, Integer>> p = guess.get(i);
                    Character[] secret = p.first;
                    Pair<Integer, Integer> key = p.second;
                    stdout.info("Key:");
                    stdout.append("a = "+key.first+" b = "+key.second);
                    stdout.info("Secret:");
                    stdout.append(secret);
                }
            } catch (Exception ex) {
                stdout.error("Error while cracking. "+ex.getMessage());
            }
        }
        else if (ParamUtils.contains(params, P_VIGENERE)) {
            
            Param pr = ParamUtils.getParam(params, P_MAX_LEN);
            int maxLen = ParamReader.getInt(pr);
            
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            stdout.info("Parameters:");
            stdout.append(P_MAX_LEN+" = "+maxLen);
            
            VigenereAnalyzer cracker = new VigenereAnalyzer(maxLen);
            try {
                stdout.info("Cracking...");
                List<Pair<Character[], Character[]>> guess = cracker.analyze(input, num_guesses, confidence);
                for (int i = 0; i < guess.size(); i++) {
                    stdout.info("Guess #"+(i+1));
                    Pair<Character[], Character[]> p = guess.get(i);
                    Character[] secret = p.first;
                    Character[] key = p.second;
                    stdout.info("Key:");
                    stdout.append(key);
                    stdout.info("Secret:");
                    stdout.append(secret);
                }
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
}
