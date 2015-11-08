package co.edu.unal.crypto.types;

/**
 * 
 * @author eduarc (Eduar Castrillo Velilla)
 * @email eduarcastrillo@gmail.com
 * @param <X>
 * @param <Y> 
 */
public class Pair<X, Y> implements Comparable {

    public X first;
    public Y second;

    public Pair() {
    }
    
    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Object o) {
        
        if (!(o instanceof Pair)) {
            return -1;
        }
        Comparable a = (Comparable)first;
        Comparable b = (Comparable)((Pair)o).first;
        if (a.compareTo(b) < 0) return -1;
        if (a.compareTo(b) > 0) return 1;
        return 0;
    }
}
