package cryptoapp.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author eduarc
 */
public class PImage extends JPanel {
    
    Image img;
    
    public PImage(Image img) {
      this.img = img;  
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }
}
