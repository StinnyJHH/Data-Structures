package avlg;

import avlg.exceptions.UnimplementedMethodException;
import avlg.exceptions.EmptyTreeException;
import avlg.exceptions.InvalidBalanceException;

/** <p>{@link AVLGTree}  is a class representing an <a href="https://en.wikipedia.org/wiki/AVL_tree">AVL Tree</a> with
 * a relaxed balance condition. Its constructor receives a strictly  positive parameter which controls the <b>maximum</b>
 * imbalance allowed on any subtree of the tree which it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3.</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations. On the other hand, increasing
 *  the balance parameter also means that we will be making <b>insertions</b> faster.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see EmptyTreeException
 * @see InvalidBalanceException
 * @see StudentTests
 */
public class AVLGTree<T extends Comparable<T>> {

    /* ********************************************************* *
     * Write any private data elements or private methods here...*
     * ********************************************************* */
	
	private static class Node<T>{
		private T data;
		private Node<T> left;
		private Node<T> right;
	}

	private Node<T> root;
	private int maxImbalance;

    /* ******************************************************** *
     * ************************ PUBLIC METHODS **************** *
     * ******************************************************** */

    /**
     * The class constructor provides the tree with the maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if(maxImbalance < 1)
        	throw new InvalidBalanceException(null);
    	this.maxImbalance = maxImbalance;
        root = null;
    }

    /**
     * Insert key in the tree. You will <b>not</b> be tested on
     * duplicates! This means that in a deletion test, any key that has been
     * inserted and subsequently deleted should <b>not</b> be found in the tree!
     * s
     * @param key The key to insert in the tree.
     */
   
    public void insert(T key) {
    	if(isEmpty()) {
    		this.root = new Node<T>();
    		this.root.data = key;
    		this.root.left = null;
    		this.root.right = null;
    	}else{
    		this.root = insertHelper(this.root, key);
    	}
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or {@code null} if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    private Node<T> min(Node<T> node) {
    	Node<T> curr = node;
    	while(curr.left != null)
    		curr = curr.left;
    	return curr;
    }
    
    private Node<T> max(Node<T> node){
    	Node<T> curr = node;
    	while(curr.right != null)
    		curr = curr.right;
    	return curr;
    }
    
    public T delete(T key) throws EmptyTreeException {
       	if(root == null)
       		throw new EmptyTreeException(null);
       	else {
       		if(search(key) == null) {
       			return null;
       		}
       		this.root = balance(delete(root, key));
       		return key;
       	}
    }
    
    private Node<T> delete(Node<T> node, T key) {
    	
    	if(node == null || searchHelper(key, root) == null)
    		return null;
    	
    	if(node.data.compareTo(key) > 0) {
    		node.left = delete(node.left, key);
    	}else if(node.data.compareTo(key) < 0) {
    		node.right = delete(node.right, key);
    	}else {
		  	if(node.left == null || node.right == null) {
		  		Node<T> temp = null;
		  		if(temp == node.left) {
		    		temp = node.right;
		    	}else {
		    		temp = node.left;
		    	}
		    	if(temp == null) {
		    		temp = node;
		    		node = null;
		    	}else {
		    		node = temp;
		    	}
		    }else {
		    	Node<T> temp = min(node.right);
	    		node.data = temp.data;
	    		node.right = delete(node.right, temp.data);
		    }
    	}
	    if(node == null) {
	    	return node;
	    }
	    		
	    int balance = balancedHelper(node);
	    
	    	if(balance > this.maxImbalance && balancedHelper(node.left) >= 0) {
	    		return rRotate(node);
	    	}
	    	
	    	if(balance > this.maxImbalance && balancedHelper(node.left) < 0) {
	    		node.left = lRotate(node.left);
	    		return rRotate(node);
	    	}
	    	
	    	if(balance < -(this.maxImbalance) && balancedHelper(node.left) >= 0) {
	    		return lRotate(node);
	    	}
	    	
	    	if(balance < -(this.maxImbalance) && balancedHelper(node.left) < 0) {
	    		node.right = rRotate(node.right);
	    		return lRotate(node);
	    	}
	    	return balance(node);
	    }
    

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or {@code null} otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or {@code null} otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
    	if(root == null) 
    		throw new EmptyTreeException(null);
    	return searchHelper(key, root);
    }
    
    /*recursive search helper method*/
    private T searchHelper(T key, Node<T> node) {
    	if(node == null) {
    		return null;
    	}
    	if(key.compareTo(node.data) > 0) {
        	return searchHelper(key, node.right);
        }else if(key.compareTo(node.data) < 0){
        	return searchHelper(key, node.left);
        }else{
        	return key;
        }
    }
    
    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
       	return this.maxImbalance;
    }


    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
       	return heightHelper(root)-1;
    }
  

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        if(root == null)
        	return true;
        else return false;
    }

    /**
     * Return the key at the tree's root node.
     * @return The key at the tree's root node.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
        if(root == null)
        	throw new EmptyTreeException("getRoot");
        return root.data;
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the Binary Search Tree property,
     * {@code false} otherwise.
     */
    public boolean isBST() {
        return isAVLGBalanced();
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the balance requirements of an AVLG tree, {@code false}
     * otherwise.
     */
    public boolean isAVLGBalanced() {
        return (balancedHelper(root) <= this.maxImbalance);
    }

    /**
     * <p>Empties the AVL-G Tree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
        root = null;
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return getCount(root);
    }
    
    private int getCount(Node<T> node) {
    	if(node != null) {
    		return 1+getCount(node.left)+getCount(node.right);
    	} else {
    		return 0;
    	}
    }
    
    /*insert recursive helper method*/
    private Node<T> insertHelper(Node<T> node, T key) {

    	if(node == null) {
    		Node<T> temp = new Node<T>();
    		temp.data = key;
    		temp.left = null;
    		temp.right = null;
    		return temp;
    	}
    	if(node.data.compareTo(key) > 0) {
    		node.left = insertHelper(node.left, key);
    	}else {
    		node.right = insertHelper(node.right, key);
    	}
    	return balance(node);
    }
    
    private Node<T> findParentNode(Node<T> node, T key){
    	if(node == null)
    		return null;
    	if(node.left.data.compareTo(key) == 0 || node.right.data.compareTo(key) == 0)
    		return node;
    	findParentNode(node.left, key);
    	return findParentNode(node.right, key);
    }
    
    /*height helper method*/
    private int heightHelper(Node<T> node) {
    	if(node == null)
    		return 0;
    	return 1+Math.max(heightHelper(node.left),heightHelper(node.right));
    }

    private int balancedHelper(Node<T> node) {
    	if(node == null)
    		return 0;
    	return (heightHelper(node.left) - heightHelper(node.right));
    }
    
    private Node<T> lRRotate(Node<T> node){
    	node.left = lRotate(node.left);
    	node = rRotate(node);
    	return node;
    }
    
    private Node<T> rLRotate(Node<T> node){
    	node.right = rRotate(node.right);
    	node = lRotate(node);
    	return node;
    }
    
    private Node<T> lRotate(Node<T> node) {
    	Node<T> newRoot = node.right;
    	node.right = newRoot.left;
    	newRoot.left = node;
    	return newRoot;
    }
    
    private Node<T> rRotate(Node<T> node) {
    	Node<T> newRoot = node.left;
    	node.left = newRoot.right;
    	newRoot.right = node;
    	return newRoot;
    }
    
    private Node<T> balance(Node<T> node){
    	int balance = balancedHelper(node);
    	if(balance > this.maxImbalance) {
    		if(balancedHelper(node.left) >= 0) {
    			return rRotate(node);
    		}else {
    			node.left = lRotate(node.left);
    			return rRotate(node);
    		}
    	}
    	if(balance < -(this.maxImbalance)) {
    		if(balancedHelper(node.right) <= 0) {
    			return lRotate(node);
    		}else {
    			node.right = rRotate(node.right);
    			return lRotate(node);
    		}
    	}
    	return node;
    }
    
}
