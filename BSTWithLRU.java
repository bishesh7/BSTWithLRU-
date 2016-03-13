import java.util.Scanner;
import java.util.Map;
import java.util.AbstractMap;
import java.util.List;
/**
 * A binary search tree.
 */

public class BSTWithLRU <E extends Comparable<? super E>> implements CountMapWithLRU<E>
{
    private Node<E> root;
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public BSTWithLRU()
    {
    	
    	head = new Node<E>();
    	tail = new Node<E>();
    	size = 0;
    	head.next = tail;
    	tail.prev = head;
    }
 
    /**
     * Adds the given value to this tree if it is not already present.
     *
     * @param r a value
     */
    public void add(E r, int amount)
    {
	// start at root
	Node<E> curr = root;
	// keep track of parent of current and last direction travelled
	Node<E> parCurr = null;
	int dir = 0;
	int depth = 0;

	// traverse tree to insertion location or containing interval
	while (curr != null && (dir = r.compareTo(curr.data)) != 0)
	    {
		parCurr = curr;
		if (dir < 0)
		    {
			curr = curr.left;
		    }
		else
		    {
			curr = curr.right;
		    }
		depth++;
	    }

	if (curr == null)
	    {
		// didn't find value; create new node
		Node<E> newNode = new Node<E>(r, parCurr);
		size++;

		// link from parent according to last direction
		if (parCurr == null)
		    {
			root = newNode;
            
		    }
		else
		    {
			if (dir < 0)
			    {
				parCurr.left = newNode;

			    }
			else
			    {
				parCurr.right = newNode;

			    }
		    }

		// recompute heights on path back to root
		recomputeHeights(parCurr);
		
		// yes, we added
		//return true;
		newNode.next=head.next;
		head.next.prev = newNode;
		head.next=newNode;
        newNode.prev= head;

		newNode.count += amount;		
	    }
	else
	    {
		// no, we didn't add
		//return false;
	    }
    }

    public Map.Entry<E, Integer> remove(E r)
    {
	// start at root
	Node<E> curr = root;

	// keep track of parent of current and last direction travelled
	Node<E> parCurr = null;
	int dir = 0;
	int depth = 0;

	// traverse tree to insertion location or value
	while (curr != null && (dir = r.compareTo(curr.data)) != 0)
	    {
		parCurr = curr;
		if (dir < 0)
		    {
			curr = curr.left;
		    }
		else
		    {
			curr = curr.right;
		    }
		depth++;
	    }
	
	if (curr == null)
	    {
	    	return null;
	    }
	else
	    {
		delete(curr);
		Map.Entry<E, Integer> temp = new AbstractMap.SimpleEntry (curr.data, curr.count);
		return temp;
	    }
	    
    }

    /**
     * Deletes the given node from this tree.
     *
     * @param curr a node in this tree
     */
    private void delete(Node<E> curr)
    {
	size--; 

	if (curr.left == null && curr.right == null)
	    {
		Node<E> parent = curr.parent;
		if (curr.isLeftChild())
		    {
			parent.left = null;
			recomputeHeights(parent);

		    }
		else if (curr.isRightChild())
		    {
			parent.right = null;
			recomputeHeights(parent);
		    }
		else
		    {
			// deleting the root
			root = null;
		    }
	    }
	else if (curr.left == null)
	    {
		// node to delete has only right child
		Node<E> parent = curr.parent;

		if (curr.isLeftChild())
		    {
			parent.left = curr.right;
			curr.right.parent = parent;
			recomputeHeights(parent);
		    }
		else if (curr.isRightChild())
		    {
			parent.right = curr.right;
			curr.right.parent = parent;
			recomputeHeights(parent);
		    }
		else
		    {
			root = curr.right;
			root.parent = null;
		    }
	    }
	else if (curr.right == null)
	    {
		// node to delete only has left child
		Node<E> parent = curr.parent;

		if (curr.isLeftChild())
		    {
			parent.left = curr.left;
			curr.left.parent = parent;
			recomputeHeights(parent);
		    }
		else if (curr.isRightChild())
		    {
			parent.right = curr.left;
			curr.left.parent = parent;
			recomputeHeights(parent);
		    }
		else
		    {
			root = curr.left;
			root.parent = null;
		    }
	    }
	else
	      {
		  // node to delete has two children

		  // find inorder successor (leftmost in right subtree)
		  Node<E> replacement = curr.right;
		  while (replacement.left != null)
		      {
			  replacement = replacement.left;
		      }

		  Node<E> replacementChild = replacement.right;
		  Node<E> replacementParent = replacement.parent;

		  // stitch up
		  if (curr.isLeftChild())
		      {
			  curr.parent.left = replacement;
		      }
		  else if (curr.isRightChild())
		      {
			  curr.parent.right = replacement;
		      }
		  else
		      {
			  root = replacement;
		      }
		  
		  if (replacement.parent != curr)
		      {
			  replacement.parent.left = replacementChild;
			  if (replacementChild != null)
			      {
				  replacementChild.parent = replacement.parent;
			      }

			  replacement.right = curr.right;
			  curr.right.parent = replacement;
		      }

		  replacement.left = curr.left;
		  curr.left.parent = replacement;

		  replacement.parent = curr.parent;

		  // recompute heights (node we deleted is on the path
		  // of nodes whose heights is recomputes because
		  // replacementParent is a descendant of curr)
		  if (replacementParent != curr)
		      {
			  recomputeHeights(replacementParent);
		      }
		  else
		      {
			  recomputeHeights(replacement);
		      }
	      }
	      curr.prev.next=curr.next;
	      
	      curr.next.prev = curr.prev;
	      
    }

    /**
     * Recomputes the heights for nodes on the path from the given node
     * back to the root.
     *
     * @param n a node in this tree
     */
    private void recomputeHeights(Node<E> n)
    {
	while (n != null)
	    {
		n.recomputeHeight();
		n = n.parent;
	    }
    }

    /**
     * Returns a printable representation of this tree.
     *
     * @return a printable representation of this tree
     */
    public String toString()
    {
	StringBuilder s = new StringBuilder();
	buildOutput(root, s, 0);
	return s.toString();
    }

    /**
     * Appends a printable representation of the subtree rooted at the
     * given node to the given string builder.
     *
     * @param curr a node in this tree
     * @param s a string builder
     * @param depth the depth of curr
     */
    private void buildOutput(Node<E> curr, StringBuilder s, int depth)
    {
	if (curr != null)
	    {
		buildOutput(curr.left, s, depth + 1);
		s.append(depth + "/" + curr.height + " " + curr.data + "\n");
		buildOutput(curr.right, s, depth + 1);
	    }
    }

    

    public static class Node<E>
    {
	/**
	 * References to the parents and children of this node.
	 */
	private Node<E> parent;
	private Node<E> left;
	private Node<E> right;
    private Node<E> prev;
    private Node<E> next;
	/**
	 * The data stored in this node.
	 */
	private E data;

	/**
	 * The height of the subtree rooted at this node.
	 */
	private int height;
    private int count;
	/**
	 * Creates a node holding the given data with the given parent
	 * parent reference.  The parent's child references are not updated
	 * to refer to the new node.
	 *
	 * @param d the data to store in the new node
	 * @param p the parent of the new node
	 */

	private Node ()
	{

	}
	private Node(E d, Node<E> p)
	    {
		data = d;
		parent = p;
		height = 1;
		count = 0;
	    }
    private void setCount(int a )
    {
    	count = a;
    }
	/**
	 * Determines if this node is a left child.
	 *
	 * @return true if and only if this node is its parent's left child
	 */
	private boolean isLeftChild()
	{
	    return (parent != null && parent.left == this);
	}

	/**
	 * Determines if this node is a right child.
	 *
	 * @return true if and only if this node is its parent's right child
	 */
	private boolean isRightChild()
	{
	    return (parent != null && parent.right == this);
	}

	/**
	 * Returns the height of the left subtree of this node.  The height
	 * of an empty subtree is defined to be 0.
	 *
	 * @return the height of the left subtree of this node
	 */
	private int leftHeight()
	{
	    return (left != null ? left.height : 0);
	}

	/**
	 * Returns the height of the right subtree of this node.  The height
	 * of an empty subtree is defined to be 0.
	 *
	 * @return the height of the right subtree of this node
	 */
	private int rightHeight()
	{
	    return (right != null ? right.height : 0);
	}

	/**
	 * Recomputes this node's height.  Intended to be used when the
	 * heights of the children have possibly changed.
	 */
	private void recomputeHeight()
	{
	    height = 1 + Math.max(leftHeight(), rightHeight());
	}
    }

    /**
     * Command-driven test driver for binary search trees.  Commands
     * are 'a' for add and 'r' for remove; both take an integer to add
     * or remove from the tree.  The final tree is written to standard output
     * in sorted order with the depths of the corresponding nodes.
     * The commands can be specified as a single command-line argument
     * (quoted in most shells to avoid being broken into several strings), or,
     * if there are no command-line arguments, from standard input.
     *
     * @param args ignored
     */
    

    public boolean update(E r, int amount)
    {
     Node<E> curr = root;
     Node<E> parCurr = null;
     int dir = 0;
     while (curr != null && (dir = r.compareTo(curr.data)) != 0)
	    {
		parCurr = curr;
		if (dir < 0)
		    {
			curr = curr.left;
		    }
		else
		    {
			curr = curr.right;
		    }
		
	    }
     if ( curr!= null && dir==0 )
        {
     	curr.count += amount;
     	curr.next.prev = curr.prev;
     	curr.prev.next = curr.next;
     	curr.next = head.next;
     	head.next.prev = curr;
     	head.next = curr;
     	curr.prev = head;

        return true;
        }
     	
     return false;
    }

    
    public Map.Entry<E, Integer> removeLRU()
    { 
         
     Map.Entry<E, Integer> temp = new AbstractMap.SimpleEntry(tail.prev.data, tail.prev.count);
     delete(tail.prev);
     return temp;
    }

    public int get(E key)
    {
     Node<E> curr = root;
     Node<E> parCurr = null;
     int dir = 0;
     while (curr != null && (dir = key.compareTo(curr.data)) != 0)
	    {
		parCurr = curr;
		if (dir < 0)
		    {
			curr = curr.left;
		    }
		else
		    {
			curr = curr.right;
		    }
		
	    }
     if (curr!= null && dir==0)
     	return curr.count;
     else 
     	return 0;
    }

    public int size()
    {
     return size;
    }

    public void addToList(List<Map.Entry<E, Integer>> l)
    {
      Node <E> entry = head.next;
      while(entry!=tail)
      {
      	Map.Entry<E, Integer> ele = new AbstractMap.SimpleEntry(entry.data, entry.count);
      	l.add(ele);
      	entry = entry.next;
      
      }
      
    }
}

    
