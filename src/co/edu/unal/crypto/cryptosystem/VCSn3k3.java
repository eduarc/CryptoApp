package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.type.BinarySubPixel;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduarc
 * @email eduarcastrillo@gmail.com
 */
public class VCSn3k3 implements VisualCryptosystem<BufferedImage> {

    private static final int SZ = 2;
    private final int[][] pattern;
    private final List<BinarySubPixel[]> C0;
    private final List<BinarySubPixel[]> C1;
    
    public VCSn3k3() {
        
        C0 = new ArrayList();
        C1 = new ArrayList();
        int[] index = new int[SZ*SZ];
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }
        pattern = new int[][] {{1, 1, 0, 0},
                               {1, 0, 1, 0},
                               {1, 0, 0, 1}};
        permut(index, 0);
    }
    
    @Override
    public BufferedImage[] encrypt(BufferedImage secret) {
        
        WritableRaster secretRaster = secret.getRaster();
        int w = secret.getWidth();
        int h = secret.getHeight();
        BufferedImage share1 = new BufferedImage(w*SZ, h*SZ, BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage share2 = new BufferedImage(w*SZ, h*SZ, BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage share3 = new BufferedImage(w*SZ, h*SZ, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster[] shares = {share1.getRaster(), share2.getRaster(), share3.getRaster()};
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int idx = (int) (Math.random()*C0.size());
                if (secretRaster.getSample(j, i, 0) == 0) {
                    setValue(j*SZ, i*SZ, shares, C1.get(idx));
                } else {
                    setValue(j*SZ, i*SZ, shares, C0.get(idx));
                }
            }
        }
        return new BufferedImage[]{share1, share2, share3};
    }

    @Override
    public BufferedImage decrypt(BufferedImage[] shares) {
        
        int w = shares[0].getWidth();
        int h = shares[0].getHeight();
        BufferedImage secret = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster secretRaster = secret.getRaster();
        WritableRaster[] rasters = {shares[0].getRaster(), shares[1].getRaster(), shares[2].getRaster()};
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                secretRaster.setSample(j, i, 0, getValue(j, i, rasters));
            }
        }
        return secret;
    }
    
    @Override
    public BufferedImage originalDecrypt(BufferedImage[] shares) {
        
        int w = shares[0].getWidth()/SZ;
        int h = shares[0].getHeight()/SZ;
        BufferedImage secret = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster secretRaster = secret.getRaster();
        WritableRaster[] rasters = {shares[0].getRaster(), shares[1].getRaster(), shares[2].getRaster()};
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                secretRaster.setSample(j, i, 0, getOriginalValue(j, i, rasters));
            }
        }
        return secret;
    }
    
    private void permut(int arr[], int k) {
        
        if (k == arr.length) {
            int[][] p = new int[SZ][SZ];
            BinarySubPixel[] bBins = new BinarySubPixel[3];
            BinarySubPixel[] wBins = new BinarySubPixel[3];
            for (int i = 0; i < 3; i++) {
                p[0][0] = pattern[i][arr[0]];
                p[0][1] = pattern[i][arr[1]];
                p[1][0] = pattern[i][arr[2]];
                p[1][1] = pattern[i][arr[3]];
                bBins[i] = new BinarySubPixel(SZ, p);
                p[0][0] = 1-pattern[i][arr[0]];
                p[0][1] = 1-pattern[i][arr[1]];
                p[1][0] = 1-pattern[i][arr[2]];
                p[1][1] = 1-pattern[i][arr[3]];
                wBins[i] = new BinarySubPixel(SZ, p);
            }
            C0.add(wBins);
            C1.add(bBins);
            return;
        }
        for (int i = k; i < arr.length; i++) {
            swap(i, k, arr);
            permut(arr, k+1);
            swap(i, k, arr);
        }
    }

    private static void swap(int i, int j, int arr[]) {
        
        int v = arr[i];
        arr[i] = arr[j];
        arr[j] = v;
    }

    private void setValue(int ii, int jj, WritableRaster[] raster, BinarySubPixel[] subPixel) {
        
        int sz = subPixel[0].getSize();
        for (int r = 0; r < raster.length; r++) {
            int[][] p = subPixel[r].getPattern();
            for (int i = 0; i < sz; i++) {
                for (int j = 0; j < sz; j++) {
                    raster[r].setSample(ii+i, jj+j, 0, p[i][j]);
                }
            }
        }
    }
    
    private int getValue(int i, int j, WritableRaster[] rasters) {
        
        int v = 0;
        for (WritableRaster raster : rasters) {
            v |= raster.getSample(i, j, 0);
        }
        return v == 1 ? 0 : 1;
    }
    
    private int getOriginalValue(int i, int j, WritableRaster[] rasters) {
        
        i *= SZ;
        j *= SZ;
        int c = 0;
        for (int k = 0; k < SZ; k++) {
            for (int l = 0; l < SZ; l++) {
                int v = 0;
                for (WritableRaster raster : rasters) {
                    v |= raster.getSample(i+k, j+l, 0);
                }
                if (v == 1) {
                    c++;
                }
            }
        }
        return c == SZ*SZ ? 0 : 1;
    }
}
