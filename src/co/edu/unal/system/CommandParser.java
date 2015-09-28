package co.edu.unal.system;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class CommandParser {
    
    private final String cmdLine;
    private int index;
    private final List<Param> params;
    
    public CommandParser(String cmdLine) {
        
        this.cmdLine = cmdLine;
        params = new ArrayList<>();
        index = 0;
    }
    
    public Param[] parse() {
        
        readCMD();
        for (; index < cmdLine.length();) {
            readParam();
        }
        return params.toArray(new Param[]{});
    }
    
    private void readCMD() {
        
        skipSpaces();
        String cmd = readAlphaNumeric();
        params.add(new Param("cmdName", cmd));
    }
    
    private void readParam() {
        
        skipSpaces();
        String p = readAlphaNumeric();
        //skipSpaces();
        String v = null;
        if (isEqual()) {
            readEqual();
            //skipSpaces();
            if (startQuoutedString()) {
                v = readQuotedString();
            } else {
                v = readString();
            }
        }
        else if (index < cmdLine.length() && !Character.isSpaceChar(cmdLine.charAt(index))) {
            throw new IllegalArgumentException("index "+index+": Unexpected "+cmdLine.charAt(index));
        }
        params.add(new Param(p, v));
    }
    
    private boolean isEqual() {
        
        if (index < cmdLine.length()) {
            return cmdLine.charAt(index) == '=';
        }
        return false;
    }
    
    private boolean startQuoutedString() {
        
        if (index < cmdLine.length()) {
            return cmdLine.charAt(index) == '\"';
        }
        return false;
    }
    
    private void skipSpaces() {
        
        for (; index < cmdLine.length(); index++) {
            char c = cmdLine.charAt(index);
            if (!Character.isSpaceChar(c)) {
                return;
            }
        }
    }
    
    private void readEqual() {
        
        if (cmdLine.charAt(index) != '=') {
            throw new IllegalArgumentException("index "+index+": Expected =");
        }
        index++;
    }
    
    private String readString() {
        
        String s = "";
        for (; index < cmdLine.length(); index++) {
            char c = cmdLine.charAt(index);
            if (Character.isSpaceChar(c)) {
                break;
            }
            s += c;
        }
        return s;
    }
    
    private String readQuotedString() {
        
        String v = "";
        int start = index+1;
        int end = start;
        for (; end < cmdLine.length(); end++) {
            if (cmdLine.charAt(end) == '\"' && cmdLine.charAt(end-1) != '\\') {
                break;
            }
        }
        index = end+1;
        if (end == cmdLine.length()) {
            throw new IllegalArgumentException("index "+index+": Expected \"");
        }
        v = cmdLine.substring(start, end);
        String res = "";
        int i = 0;
        for (; i < v.length(); i++) {
            char c = v.charAt(i);
            if (i+1 < v.length() && c == '\\' && v.charAt(i+1) == 'n') {
                res += '\n';
                i++;
            }
            else if (i+1 < v.length() && c == '\\' && v.charAt(i+1) == '\"') {
                res += '\"';
                i++;
            }
            else if (i+1 < v.length() && c == '\\' && v.charAt(i+1) == '\'') {
                res += '\'';
                i++;
            }
            else {
                res += v.charAt(i);
            }
        }
        return res;
    }
    
    private String readAlphaNumeric() {
        
        String s = "";
        int start = index;
        for (; index < cmdLine.length(); index++) {
            char c = cmdLine.charAt(index);
            if (Character.isLetterOrDigit(c)) {
                s += c;
            } else {
                break;
            }
        }
        if (s.length() == 0 && start < cmdLine.length()) {
            throw new IllegalArgumentException("index "+start+": Unexpected "+cmdLine.charAt(start));
        }
        return s;
    }
}
