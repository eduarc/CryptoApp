package co.edu.unal.crypto.cryptosystem;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 */
public interface VisualCryptosystem<P> {

    public P[] encrypt(P secret);

    public P decrypt(P[] shares);

    public P originalDecrypt(P[] shares);
}
