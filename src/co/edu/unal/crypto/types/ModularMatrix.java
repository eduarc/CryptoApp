package co.edu.unal.crypto.types;

import co.edu.unal.crypto.tools.Arithmetic;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 */
public class ModularMatrix {
    
    private final int rows;
    private final int cols;
    private final int modulus;
    private final int[][] data;
    
    public ModularMatrix(int r, int c, int m) {
        
        rows = r;
        cols = c;
        modulus = m;
        data = new int[r][c];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getModulus() {
        return modulus;
    }
    
    public void set(int r, int c, int v) {
        data[r][c] = Arithmetic.mod(v, modulus);
    }
    
    public int get(int r, int c) {
        return data[r][c];
    }
    
    public ModularMatrix copy() {
        
        ModularMatrix res = new ModularMatrix(rows, cols, modulus);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = data[i][j];
            }
        }
        return res;
    }
    
    public ModularMatrix mul(ModularMatrix mat) {
        
        ModularMatrix res = new ModularMatrix(rows, mat.cols, modulus);
        for (int i = 0; i < res.rows; i++) {
            for (int j = 0; j < res.cols; j++) {
                int v = 0;
                for (int k = 0; k < cols; k++) {
                    v += data[i][k]*mat.data[k][j];
                }
                res.set(i, j, v);
            }
        }
        return res;
    }
    
    public ModularMatrix mul(int v) {
        
        ModularMatrix res = new ModularMatrix(rows, cols, modulus);
        for (int i = 0; i < res.rows; i++) {
            for (int j = 0; j < res.cols; j++) {
                res.set(i, j, data[i][j]*v);
            }
        }
        return res;
    }
    
    public ModularMatrix transpose() {
        
        ModularMatrix res = new ModularMatrix(cols, rows, modulus);
        for (int i = 0; i < res.rows; i++) {
            for (int j = 0; j < res.cols; j++) {
                res.data[j][i] = data[i][j];
            }
        }
        return res;
    }
    
    public int determinant() {
        
        if (this.cols != this.rows) {
            throw new IllegalArgumentException("Determinant is defined only for square matrix");
        }
        return determinant(this);
    }
    
    private int determinant(ModularMatrix mat) {
        
        if (mat.rows == 1) {
            return mat.data[0][0];
        }
        int d = 0;
        for (int i = 0; i < mat.cols; i++) {
            d += sign(i)*mat.data[0][i]*determinant(mat.subMatrix(0, i));
        }
        return d;
    }
    
    public ModularMatrix cofactor() {
        
        ModularMatrix res = new ModularMatrix(cols, rows, modulus);
        for (int i = 0; i < res.rows; i++) {
            for (int j = 0; j < res.cols; j++) {
                res.set(i, j, sign(i)*sign(j)*subMatrix(i, j).determinant());
            }
        }
        return res;
    }
    
    public ModularMatrix inverse() {
        
        int inv = Arithmetic.modInverse(determinant(), modulus);
        return cofactor().mul(inv).transpose();
    }
    
    private int sign(int i) {
        return (i&1) == 1 ? -1 : 1;
    }
    
    private ModularMatrix subMatrix(int row, int col) {
        
        ModularMatrix res = new ModularMatrix(rows-1, cols-1, modulus);
        int r = 0;
        for (int i = 0; i < rows; i++) {
            if (i == row) {
                continue;
            }
            int c = 0;
            for (int j = 0; j < cols; j++) {
                if (j == col) {
                    continue;
                }
                res.data[r][c] = data[i][j];
                c++;
            }
            r++;
        }
        return res;
    }
}
