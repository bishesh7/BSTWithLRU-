import java.util.Map;
import java.util.List;

/**
 * A map from keys to integer counts.
 */

public interface CountMapWithLRU<E extends Comparable<? super E>>
{
    /**
     * Adds the given amount to the count for the given key.  If the key is
     * not present in this map then the return value is false, otherwise
     * the return value is true.
     *
     * @param key a key
     * @param amount a positive integer
     * @return true if and only if the given key was already in this map
     */
    public boolean update(E key, int amount);

    /**
     * Adds the given amount to the count for the given key.  If the key is
     * not present in this map then a new entry is added.
     *
     * @param key a key
     * @param amount a positive integer
     */
    public void add(E key, int amount);

    /**
     * Removes the given key from this map and returns the old entry.
     * If the key was not present then the return value is null.
     *
     * @param key a key
     * @return the (key, value) pair that was removed, or null if the key was
     * not present
     */
    public Map.Entry<E, Integer> remove(E key);

    /**
     * Removes the least recently used key and it associated count from this
     * map and returns them.
     *
     * @return the removed (key, count) pair
     */
    public Map.Entry<E, Integer> removeLRU();

    /**
     * Returns the count associated with the given key, or zero if the key
     * is not present in this map.
     *
     * @param key a key
     * @return the value associated with that key
     */
    public int get(E key);

    /**
     * Returns the number of keys in this map.
     *
     * @return the number of keys in this map
     */
    public int size();

    /**
     * Adds all the (key, count) pairs in this map to the given list.
     *
     * @param l a list
     */
    public void addToList(List<Map.Entry<E, Integer>> l);
}
