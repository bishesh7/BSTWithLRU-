import java.util.List;
import java.util.Map;
/**
 * A frequency list (or a map from keys to integer frequencies).  Keys
 * can be added or removed and their frequencies can be increased by one.
 */

public interface CountMap<E>
{
    /**
     * Adds one to the given key's frequency.  If the key was not present
     * (if its frequency was 0) then the key is added with frequency 1.
     *
     * @param key a key
     */
    public void add(E key);
    
    /**
     * Returns the frequency of the given key.  The frequency is 0 if the
     * key is not present.
     *
     * @param key a key
     */
    public int get(E key);

    /**
     * Returns a list of (item, frequency) entries
     */
    public List<Map.Entry<E, Integer>> entryList();

    /**
     * Returns the number of items in this list.
     */
    public int size();
}
