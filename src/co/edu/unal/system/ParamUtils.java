/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unal.system;

/**
 *
 * @author eduarc
 */
public class ParamUtils {
    
    public static boolean contains(Param[] params, String p) {
        return containsMinMax(params, new String[]{p}, 1, 1);
    }
    
    public static boolean containsOne(Param[] params, String[] pNames) {
        return containsMinMax(params, pNames, 1, 1);
    }
    
    public static boolean containsAll(Param[] params, String[] pNames) {
        return containsMinMax(params, pNames, pNames.length, pNames.length);
    }
    
    public static boolean containsMin(Param[] params, String[] pNames, int min) {
        return containsMinMax(params, pNames, min, Integer.MAX_VALUE);
    }
    
    public static boolean containsMax(Param[] params, String[] pNames, int max) {
        return containsMinMax(params, pNames, 0, max);
    }
    
    public static boolean containsMinMax(Param[] params, String[] pNames, int min, int max) {
        
        int count = 0;
        for (Param param : params) {
            String name = param.getName();
            for (String test : pNames) {
                if (test.equals(name)) {
                    count++;
                }
            }
        }
        return count >= min && count <= max;
    }
}
