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
public interface TextConsole {
    
    public void append(Character[] data);
    public void appendln(Character[] data);
    public void append(String s);
    public void appendln(String s);
    public void clear();
    public void setText(String s);
}
