package co.edu.unal.crypto.visual;

/**
 *
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <S>
 */
public interface VCS<S> {

    public S[] encrypt(S secret);

    public S decrypt(S[] shares);

    public S originalDecrypt(S[] shares);
}
