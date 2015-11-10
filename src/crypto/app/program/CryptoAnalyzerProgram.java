package crypto.app.program;

import co.edu.unal.crypto.analyzer.AllMatch;
import co.edu.unal.crypto.util.CharStream;
import co.edu.unal.crypto.analyzer.ConfidenceMethod;
import co.edu.unal.crypto.analyzer.OneMatch;
import co.edu.unal.crypto.alphabet.ascii.PrefixEnglishDictionary;
import crypto.app.system.Environment;
import crypto.app.system.Param;
import crypto.app.system.ParamReader;
import crypto.app.system.ParamUtils;
import crypto.app.system.Program;
import crypto.app.system.StandardTextConsole;
import co.edu.unal.crypto.analyzer.ascii.AffineAnalyzer;
import co.edu.unal.crypto.analyzer.ascii.CaesarAnalyzer;
import co.edu.unal.crypto.analyzer.ascii.RSAAnalyzer;
import co.edu.unal.crypto.analyzer.ascii.VigenereAnalyzer;
import co.edu.unal.crypto.cryptosystem.RSA;
import co.edu.unal.crypto.type.Pair;
import co.edu.unal.crypto.analyzer.KMP;
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
    public static final String P_CIPHER = "cipher";
    public static final String P_NUM_GUESSES = "nGuesses";
    public static final String P_CONFIDENCE_METHOD = "confidence";
    public static final String P_INPUT = "in";
    public static final String P_FILE_INPUT = "fin";
    public static final String P_IMAGE_INPUT = "iin";
    public static final String P_OUTPUT = "out";
    public static final String P_FILE_OUTPUT = "fout";
    public static final String P_IMAGE_OUTPUT = "iout";
    public static final String P_MAX_LEN = "maxKeyLen";
    public static final String P_N = "n";
    public static final String P_E = "e";
    
    public static final String V_CIPHER_RSA = "rsa";
    public static final String V_CIPHER_CAESAR = "caesar";
    public static final String V_CIPHER_AFFINE = "affine";
    public static final String V_CIPHER_VIGENERE = "vigenere";
    
    public static final String V_CONFIDENCE_ONE = "one";
    public static final String V_CONFIDENCE_ALL = "all";
    
    String[] vCipher = {V_CIPHER_RSA, V_CIPHER_CAESAR, V_CIPHER_AFFINE, V_CIPHER_VIGENERE};
    String[] vConfidence = {V_CONFIDENCE_ONE, V_CONFIDENCE_ALL};
    
    Frame frame;
    StandardTextConsole stdout;
    boolean exit;
    
    String cipher;
    Character[] input;
    Character[] output;
    File outputFile;
    File inputFile;
    Integer num_guesses;
    String confidence;
    ConfidenceMethod confidenceMethod;
    
    public CryptoAnalyzerProgram(Environment env) {
        super(env);
        
        stdout = (StandardTextConsole) getEnv().getResource(Environment.STDOUT);
        frame  = (Frame) getEnv().getResource(Environment.FRAME);
    }

    public int checkParams(Param[] params) {
        
        Param pCipher = ParamUtils.getParam(params, P_CIPHER);
        cipher = ParamReader.getOptionValue(pCipher, "Select Cipher Algorithm", vCipher);
        if (cipher == null || cipher.length() == 0) {
            stdout.error("Parameter "+P_CIPHER+" not provided");
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
        
        Param p = ParamUtils.getParam(params, P_NUM_GUESSES);
        num_guesses = ParamReader.getInt(p, "Number of tries");
        if (num_guesses == null || num_guesses <= 0) {
            num_guesses = 5;
            stdout.warning("Parameter "+P_NUM_GUESSES+" must be greater than zero. Default to "+num_guesses);
        }
        
        p = ParamUtils.getParam(params, P_CONFIDENCE_METHOD);
        confidence = ParamReader.getOptionValue(p, "Confidence Method", vConfidence);
        if (confidence == null || confidence.length() == 0) {
            confidenceMethod = new OneMatch(PrefixEnglishDictionary.defaultInstance, new KMP<>());
            stdout.warning("Parameter "+P_CONFIDENCE_METHOD+" not provided. Default to one");
        }
        else if (confidence.equals(V_CONFIDENCE_ONE)) {
            confidenceMethod = new OneMatch(PrefixEnglishDictionary.defaultInstance, new KMP<>());
        }
        else if (confidence.equals(V_CONFIDENCE_ALL)) {
            confidenceMethod = new AllMatch(PrefixEnglishDictionary.defaultInstance, new KMP<>());
        }
        else {
            stdout.error("Invalid value for parameter "+P_CONFIDENCE_METHOD+": "+confidence);
            return -1;
        }
              
        p = ParamUtils.getParam(params, P_INPUT);
        String str = ParamReader.getString(p, "Input encrypted data");
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
        return 0;
    }
    
    public int postProcess(Param[] params) {
        
        if (outputFile != null) {
            try {
                CharStream.toFile(outputFile, output);
            } catch (IOException ex) {
                stdout.error("Error while writing output to file: "+outputFile.getPath());
            }
        }
        return 0;
    }
    
    @Override
    public int exec(Param[] params) {
        
        int ret = checkParams(params);
        if (ret != 0) {
            return ret;
        }
        
        if (cipher.equals(V_CIPHER_RSA)) {
            Param p = ParamUtils.getParam(params, P_N);
            BigInteger n = ParamReader.getBigInt(p, "");
            if (n == null) {
                stdout.error("Parameter "+P_N+" not provided");
                return -1;
            }
            p = ParamUtils.getParam(params, P_E);
            BigInteger e = ParamReader.getBigInt(p, "");
            if (e == null) {
                stdout.error("Parameter "+P_E+" not provided");
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
                Pair<Character[], RSA.Key> res = cracker.analyze(rsaInput.toArray(new BigInteger[]{}), confidenceMethod);
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
        if (cipher.equals(V_CIPHER_CAESAR)) {
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            CaesarAnalyzer cracker = new CaesarAnalyzer();
            try {
                stdout.info("Cracking...");
                List<Pair<Character[], Integer>> guess = cracker.analyze(input, num_guesses, confidenceMethod);
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
        if (cipher.equals(V_CIPHER_AFFINE)) {
            stdout.info("Encrypted data:");
            if (inputFile != null) {
                stdout.append("From file: "+inputFile.getAbsolutePath());
            } else {
                stdout.append(input);
            }
            AffineAnalyzer cracker = new AffineAnalyzer();
            try {
                stdout.info("Cracking...");
                List<Pair<Character[], Pair<Integer, Integer>>> guess = cracker.analyze(input, num_guesses, confidenceMethod);
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
                ex.printStackTrace();
                stdout.error("Error while cracking. "+ex.getMessage());
            }
        }
        if (cipher.equals(V_CIPHER_VIGENERE)) {
            
            Param pr = ParamUtils.getParam(params, P_MAX_LEN);
            Integer maxLen = ParamReader.getInt(pr, "Maximun key length to test:");
            if (maxLen == null) {
                stdout.error("Parameter "+P_MAX_LEN+" not provided");
                return -1;
            }
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
                List<Pair<Character[], Character[]>> guess = cracker.analyze(input, num_guesses, confidenceMethod);
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
        return postProcess(params);
    }
}
