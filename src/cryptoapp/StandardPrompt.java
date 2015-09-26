/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapp;

import co.edu.unal.system.Prompt;
import javax.swing.JTextField;

/**
 *
 * @author eduarc
 */
public class StandardPrompt implements Prompt {
    
    JTextField field;
    
    public StandardPrompt(JTextField field) {
        this.field = field;
    }

    @Override
    public void disable() {
        field.setEditable(false);
    }

    @Override
    public void enable() {
        field.setEditable(true);
    }

    @Override
    public void clear() {
        field.setText("");
    }
}
