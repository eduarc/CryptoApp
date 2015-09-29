/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapp;

import cryptoapp.view.Crypto;
import javax.swing.JFrame;

/**
 *
 * @author eduarc
 */
public class CryptoApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Crypto f = new Crypto();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);
    }
    
}
