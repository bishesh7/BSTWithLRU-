import java.util.Map;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.AbstractMap;

public class MapToCountMapWithLRUAdapter<K extends Comparable<? super K>> implements CountMapWithLRU<K>
{
    /**
     * The map that backs this count map.
     */
    private Map<K, Integer> counts;

    /**
     * The map that maps keys to their access times.
     */
    private Map<K, Long> times;

    /**
     * The map that maps access times to keys accessed at that time.
     */
    private SortedMap<Long, K> lru;

    /**
     * A counter of accesses.
     */
    private long time;

    /**
     * Creates a count map backed by the given map.
     *
     * @param m an empty map
     */
    public MapToCountMapWithLRUAdapter(Map<K, Integer> m)
	{
	    counts = m;
	    times = new TreeMap<K, Long>();
	    lru = new TreeMap<Long, K>();
	}

    /**
     * Adds the given amount to the count for the given key.  If the key is
     * not present in this map then the return value is false, otherwise
     * the return value is true.
     *
     * @param key a key
     * @param amount a positive integer
     * @return true if and only if the given key was already in this map
     */
    public boolean update(K key, int amount)
    {
	if (counts.containsKey(key))
	    {
		// update count
		counts.put(key, counts.get(key) + amount);

		// update access times and LRU queue
		long oldTime = times.get(key);
		lru.remove(oldTime);
		time++;
		times.put(key, time);
		lru.put(time, key);

		return true;
	    }
	else
	    {
		return false;
	    }
    }

    /**
     * Adds the given amount to the count for the given key.  If the key is
     * not present in this map then a new entry is added.
     *
     * @param key a key
     * @param amount a positive integer
     */
    public void add(K key, int amount)
    {
	if (counts.containsKey(key))
	    {
		update(key, amount);
	    }
	else
	    {
		counts.put(key, amount);
		time++;
		times.put(key, time);
		lru.put(time, key);
	    }
    }

    /**
     * Removes the given key from this map and returns the old entry.
     * If the key was not present then the return value is null.
     *
     * @param key a key
     * @return the (key, value) pair that was removed, or null if the key was
     * not present
     */
    public Map.Entry<K, Integer> remove(K key)
	{
	    if (counts.containsKey(key))
		{
		    int count = counts.get(key);
		    counts.remove(key);
		    long access = times.get(key);
		    times.remove(key);
		    lru.remove(access);

		    return new AbstractMap.SimpleEntry<K, Integer>(key, count);
		}
	    else
		{
		    return null;
		}
	}
	    
    /**
     * Removes the least recently used key and it associated count from this
     * map and returns them.
     *
     * @return the removed (key, count) pair
     */
    public Map.Entry<K, Integer> removeLRU()
	{
	    long lowTime = lru.firstKey();
	    K key = lru.get(lowTime);
	    int count = counts.get(key);
	    
	    counts.remove(key);
	    times.remove(key);
	    lru.remove(lowTime);

	    return new AbstractMap.SimpleEntry<K, Integer>(key, count);
	}

    /**
     * Returns the count associated with the given key, or zero if the key
     * is not present in this map.
     *
     * @param key a key
     * @return the value associated with that key
     */
    public int get(K key)
    {
	if (counts.containsKey(key))
	    {
		return counts.get(key);
	    }
	else
	    {
		return 0;
	    }
    }

    /**
     * Returns the number of keys in this map.
     *
     * @return the number of keys in this map
     */
    public int size()
    {
	return counts.size();
    }

    /**
     * Adds all the (key, count) pairs in this map to the given list.
     *
     * @param l a list
     */
    public void addToList(List<Map.Entry<K, Integer>> l)
    {
	for (Map.Entry<K, Integer> e : counts.entrySet())
	    {
		l.add(e);
	    }
    }
}