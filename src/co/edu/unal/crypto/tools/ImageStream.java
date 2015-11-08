package co.edu.unal.crypto.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class ImageStream {

    public static BufferedImage readImage(String imgPath) {
        
        File imgFile = new File(imgPath);
        if (!imgFile.exists()) {
            throw new IllegalArgumentException("Image File doesn't exists: "+imgFile.getPath());
        }
        BufferedImage image = null;
        try {
            BufferedImage cImage = ImageIO.read(imgFile);
            image = new BufferedImage(cImage.getWidth(), cImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
            Graphics g = image.getGraphics();
            g.drawImage(cImage, 0, 0, null);
            g.dispose();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading the image");
        }
        return image;
    }
    
    public static BufferedImage binarize(BufferedImage grayImage) {
        
        int otsu = otsuTreshold(grayImage);
        int w = grayImage.getWidth();
        int h = grayImage.getHeight();
        BufferedImage bin = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster binRaster = bin.getRaster();
        WritableRaster raster = grayImage.getRaster();
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (raster.getSample(j, i, 0) > otsu) {
                    binRaster.setSample(j, i, 0, 1);
                } else {
                    binRaster.setSample(j, i, 0, 0);
                }
            }
        }
        return bin;
    }
    
    private static int otsuTreshold(BufferedImage grayImg) {
 
        int w = grayImg.getWidth();
        int h = grayImg.getHeight();
        
        int[] histogram = new int[256];
        WritableRaster raster = grayImg.getRaster();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                histogram[raster.getSample(j, i, 0)]++;
            }
        }
        double sum = 0;
        for(int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }
        double sumB = 0;
        int wB = 0;
        int wF;
        double varMax = 0;
        int threshold = 0;
 
        for(int i = 0 ; i < 256 ; i++) {
            wB += histogram[i];
            if(wB == 0) {
                continue;
            }
            wF = w * h - wB;
            if(wF == 0) {
                break;
            }
            sumB += i * histogram[i];
            double mB = sumB / wB;
            double mF = (sum - sumB) / wF;
            double varBetween = wB * wF * (mB - mF) * (mB - mF);
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        return threshold;
    }
}