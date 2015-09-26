package co.edu.unal.crypto.cryptosystem;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 * @param <C>
 * @param <K> 
 */
public interface Cipher<P, C, K> {

    public C[] encrypt(K key, P[] input);

    public P[] decrypt(K key, C[] input);
}
