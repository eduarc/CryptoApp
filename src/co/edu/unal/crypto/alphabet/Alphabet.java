package co.edu.unal.crypto.alphabet;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <T> 
 */
public abstract class Alphabet<T> {

    public abstract int getIndex(T val);

    public abstract T getValue(int idx);

    public abstract T[] getValues();

    public abstract int getSize();
}
