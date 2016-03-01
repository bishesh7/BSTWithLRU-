import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * An adapter to turn any Map into a CountMap.
 *
 * @author Jim Glenn
 * @version 0.1 2015-10-14
 */

public class MapToCountMapAdapter<K> implements CountMap<K>
{
    /**
     * The map that is adapted into a frequency list.
     */
    private Map<K, Integer> freq;

    /**
     * Creates a new count map backed by the given generic map.
     * 
     * @param m a map
     */
    public MapToCountMapAdapter(Map<K, Integer> m)
	{
	    freq = m;
	}

    /**
     * Adds one to the given key's frequency.  If the key was not present
     * (if its frequency was 0) then the key is added with frequency 1.
     *
     * @param key a key
     */
    public void add(K key)
    {
	if (!freq.containsKey(key))
	    {
		freq.put(key, 1);
	    }
	else
	    {
		freq.put(key, freq.get(key) + 1);
	    }
    }

    /**
     * Returns the frequency of the given key.  The frequency is 0 if the
     * key is not present.
     *
     * @param key a key
     */
    public int get(K key)
    {
	Integer val = freq.get(key);
	if (val == null)
	    {
		return 0;
	    }
	else
	    {
		return val;
	    }
    }

    /**
     * Returns a list of (item, frequency) entries.
     */
    public List<Map.Entry<K, Integer>> entryList()
	{
	    return new ArrayList<Map.Entry<K, Integer>>(freq.entrySet());
	}

    /**
     * Returns the number of items in this list.
     */
    public int size()
    {
	return freq.size();
    }

    /**
     * Returns a printable represenation of this map.
     *
     * @return a printable represenation of this map
     */
    public String toString()
    {
	return freq.toString();
    }

}
