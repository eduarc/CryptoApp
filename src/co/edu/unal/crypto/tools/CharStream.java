package co.edu.unal.crypto.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class CharStream {

    public static Character[] fromString(String str) {

        Character[] output = new Character[str.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = str.charAt(i);
        }
        return output;
    }

    public static Character[] fromCharArray(char[] arr) {

        Character[] output = new Character[arr.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = arr[i];
        }
        return output;
    }

    public static Character[] fromByteArray(byte[] arr) {

        Character[] output = new Character[arr.length];
        for (int i = 0; i < output.length; i++) {
            char c = (char) arr[i];
            output[i] = (Character) c;
        }
        return output;
    }

    public static Character[] fromIntArray(int[] arr) {

        Character[] output = new Character[arr.length];
        for (int i = 0; i < output.length; i++) {
            char c = (char) arr[i];
            output[i] = (Character) c;
        }
        return output;
    }

    public static Character[] fromFile(File f) throws FileNotFoundException, IOException {
        
        FileReader freader = new FileReader(f);
        List<Character> readed = new ArrayList<>();
        char[] buf = new char[1024];
        int r = 0;
        while ((r = freader.read(buf, 0, buf.length)) != -1) {
            for (int i = 0; i < r; i++) {
                readed.add(buf[i]);
            }
        }
        freader.close();
        return readed.toArray(new Character[]{});
    }
    
    public static Character[] fromImage(Image img) {

        BufferedImage image = (BufferedImage) img;
        int w = image.getWidth();
        int h = image.getHeight();
        Character[] output = new Character[w * h];
        WritableRaster raster = image.getRaster();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                output[i * w + j] = (char)raster.getSample(j, i, 0);
            }
        }
        return output;
    }

    public static Image toImage(Character[] data, int w, int h) {

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                raster.setSample(j, i, 0, data[i * w + j]);
            }
        }
        return image;
    }
    
    public static void toFile(File f, Character[] data) throws FileNotFoundException, IOException {
        
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fwriter = new FileWriter(f);
        for (Character c : data) {
            fwriter.write(c);
        }
        fwriter.close();
    }
    
    public static String toString(Character[] data) {
        
        String s = "";
        for (Character c : data) {
            s += c;
        }
        return s;
    }
    
    public static void err(Character[] arr) {

        for (Character c : arr) {
            System.err.print(c);
        }
        System.err.println();
    }

    public static void out(Character[] arr) {

        for (Character c : arr) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static boolean equals(Character[] a, Character[] b) {

        for (int i = 0; i < a.length; i++) {
            if (!Objects.equals(a[i], b[i])) {
                return false;
            }
        }
        return true;
    }
}
