import java.util.Map;
import java.util.AbstractMap;
import java.util.List;
import java.util.ArrayList;
 import java.util.TreeMap;


public class ListOfCountMapsWithLRU <E extends Comparable <? super E>> implements CountMap<E>
{
	private BSTWithLRU<E> firstTree;
	private BSTWithLRU<E> secondTree;
	private BSTWithLRU<E> thirdTree;
	private BSTWithLRU<E> fourthTree;
	private BSTWithLRU<E> fifthTree;

	public ListOfCountMapsWithLRU()
	{
       firstTree = new BSTWithLRU<E>();
       secondTree = new BSTWithLRU<E>();
       thirdTree = new BSTWithLRU<E>();
       fourthTree = new BSTWithLRU<E>();
       fifthTree = new BSTWithLRU<E>();

	}


	public void add(E key)
	{
		if (firstTree.update(key,1)==true)
		{
		
		}
	    else if (secondTree.update(key,1)==true)
		{

			firstTree.add(key, secondTree.get(key));
			
			secondTree.remove(key);
			

			maintainSize();
		}
		else if (thirdTree.update(key,1)==true)
		{
			firstTree.add(key, thirdTree.get(key));
			
			thirdTree.remove(key);
			
			maintainSize();
		}
		else if (fourthTree.update(key,1)==true)
		{
			firstTree.add(key, fourthTree.get(key));
           
			fourthTree.remove(key);
			maintainSize();
		}
		else if (fifthTree.update(key,1)==true)
		{
			firstTree.add(key, fifthTree.get(key));
			fifthTree.remove(key);
        
			maintainSize();
		}

		else
		{
			firstTree.add(key,1);
			maintainSize();
			
		}
	   
	   

	}

	public int get(E key)
	{
      if (firstTree.get(key)==0)
      {
      	if (secondTree.get(key)==0)
      	{
      		if (thirdTree.get(key)==0)
      		{
      			if (fourthTree.get(key)==0)
      			{
      				if (fifthTree.get(key)==0)
      				{
      					return 0;
      				}
      				else
      				{
      					int val = fifthTree.get(key);
      					firstTree.add(key,val);
                        fifthTree.remove(key);
                        maintainSize();
      					return val;

      				}
      			}
      			else
      			{
      				int val = fourthTree.get(key);
      				firstTree.add(key,val);
                    fourthTree.remove(key);
                    maintainSize();
      			    return val;
      			}
      		}	
      		else
      		{
      			int val = thirdTree.get(key);
      			firstTree.add(key,val);
                thirdTree.remove(key);
                maintainSize();
      			return val;
      		}
      	}	
      	else
      		{
      			int val = secondTree.get(key);
      			firstTree.add(key,val);
                secondTree.remove(key);
                maintainSize();
      			return val;
      		}	
      }
      else
      		{
      			return firstTree.get(key);
      		}	
      
	}

	public List<Map.Entry<E, Integer>> entryList()
	{
      List<Map.Entry<E, Integer>> l = new ArrayList<Map.Entry<E, Integer>>();
      firstTree.addToList(l);
      secondTree.addToList(l);
      thirdTree.addToList(l);
      fourthTree.addToList(l);
      fifthTree.addToList(l);
      
      return  l;

	}

	public int size()
	{
      return (firstTree.size()+secondTree.size()+thirdTree.size()+fourthTree.size()+fifthTree.size());
	}


	public void maintainSize()
	{
		if (firstTree.size()>2)
		{
		   Map.Entry <E, Integer> temp = firstTree.removeLRU();
		   secondTree.add(temp.getKey(),temp.getValue());
		}
		if (secondTree.size()>4)
		{
			Map.Entry <E, Integer>  temp = secondTree.removeLRU();
			thirdTree.add(temp.getKey(),temp.getValue());
		}
		if (thirdTree.size()>16)
		{
			Map.Entry <E, Integer>  temp = thirdTree.removeLRU();
			fourthTree.add(temp.getKey(), temp.getValue());
		}

		if (fourthTree.size()>256)
		{
			Map.Entry <E, Integer>  temp = fourthTree.removeLRU();
			fifthTree.add(temp.getKey(), temp.getValue());
		}	

	}
}