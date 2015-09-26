package co.edu.unal.crypto.types;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <X>
 * @param <Y> 
 */
public class Pair<X, Y> {

    public X first;
    public Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

}
