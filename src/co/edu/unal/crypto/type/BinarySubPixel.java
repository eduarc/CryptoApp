package co.edu.unal.crypto.type;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class BinarySubPixel {

    private final int size;
    private final int[][] pattern;

    public BinarySubPixel(int size, int[][] pattern) {

        this.size = size;
        this.pattern = new int[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(pattern[i], 0, this.pattern[i], 0, size);
        }
    }

    public int getSize() {
        return size;
    }
    
    public int[][] getPattern() {
        return pattern;
    }
}
