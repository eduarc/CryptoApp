package co.edu.unal.crypto.cryptosystem;

import co.edu.unal.crypto.alphabet.Alphabet;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <P>
 * @param <C>
 * @param <K> 
 */
public abstract class Cryptosystem<P, C, K> implements Cipher<P, C, K> {

    protected Alphabet<P> inAlphabet;
    protected Alphabet<C> outAlphabet;

    public Cryptosystem() {

        inAlphabet = null;
        outAlphabet = null;
    }

    public Cryptosystem(Alphabet<P> inAlpha) {

        inAlphabet = inAlpha;
        outAlphabet = null;
    }

    public Cryptosystem(Alphabet<P> inAlpha, Alphabet<C> outAlpha) {

        inAlphabet = inAlpha;
        outAlphabet = outAlpha;
    }

    public Alphabet<P> getInAlphabet() {
        return inAlphabet;
    }

    public Alphabet<C> getOutAlphabet() {
        return outAlphabet;
    }

    public abstract K generateKey(Object seed);
}
