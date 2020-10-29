package spatial.nodes;

import spatial.exceptions.UnimplementedMethodException;
import spatial.kdpoint.KDPoint;
import spatial.knnutils.BoundedPriorityQueue;
import spatial.knnutils.NNData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>{@link KDTreeNode} is an abstraction over nodes of a KD-Tree. It is used extensively by
 * {@link spatial.trees.KDTree} to implement its functionality.</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  ---- Austin Han -----
 *
 * @see spatial.trees.KDTree
 */
public class KDTreeNode {


    /* *************************************************************************** */
    /* ************* WE PROVIDE THESE FIELDS TO GET YOU STARTED.  **************** */
    /* ************************************************************************** */
    private KDPoint p;
    private int height;
    private KDTreeNode left, right;

    /* *************************************************************************************** */
    /* *************  PLACE ANY OTHER PRIVATE FIELDS AND YOUR PRIVATE METHODS HERE: ************ */
    /* ************************************************************************************* */

    private void insertHelp(KDPoint pIn, int currDim, int dims, KDTreeNode root, int height) {
    	if(root == null) {
    		root = new KDTreeNode(pIn);
    		root.height = height;
    		this.height = height;
    	}else {
    		if(root.p.coords[currDim] <= pIn.coords[currDim]) {
    			if(root.right == null) {
    				root.right = new KDTreeNode(pIn);
    			}else {
    				if(dims == 1) {
    					insertHelp(pIn, currDim, dims, root.right, height+1);
    				}else {
    					insertHelp(pIn, (currDim+1)%dims, dims, root.right, height+1);
    				}
    				this.height++;
    			}
    		}else {
    			if(root.left == null) {
    				root.left = new KDTreeNode(pIn);
    			}else {
    				if(dims == 1) {
    					insertHelp(pIn, currDim, dims, root.left, height+1);
    				}else {
    					insertHelp(pIn, (currDim+1)%dims, dims, root.left, height+1);
    				}
    				this.height++;
    			}
    		}
    	}
    }
    
    private int height(KDTreeNode node) {
    	if(node == null) {
    		return 0;
    	}else {
    		int l = height(node.left);
    		int r = height(node.right);
    		
    		if(l > r) {
    			return l+1;
    		}else {
    			return r+1;
    		}
    	}
    	
    }

    /* *********************************************************************** */
    /* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
    /* *********************************************************************** */


    /**
     * 1-arg constructor. Stores the provided {@link KDPoint} inside the freshly created node.
     * @param p The {@link KDPoint} to store inside this. Just a reminder: {@link KDPoint}s are
     *          <b>mutable!!!</b>.
     */
    public KDTreeNode(KDPoint p){
    	this.p = p;
    	height = 0;
    	this.left = null;
    	this.right = null;
    }

    /**
     * <p>Inserts the provided {@link KDPoint} in the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left.</p>
     * @param currDim The current dimension to consider
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #delete(KDPoint, int, int)
     */
    public void insert(KDPoint pIn, int currDim, int dims){
    	KDTreeNode temp = new KDTreeNode(pIn);
    	if(this.p == null) {
    		System.arraycopy(pIn.coords, 0, this.p.coords, 0, pIn.coords.length);
    		this.height = 0;
    	}else {
    		insertHelp(pIn, currDim, dims, this, this.height);
    	}
    }
    
  

    /**
     * <p>Deletes the provided {@link KDPoint} from the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left. There exist two special cases of deletion,
     * depending on whether we are deleting a {@link KDPoint} from a node who either:</p>
     *
     * <ul>
     *      <li>Has a NON-null subtree as a right child.</li>
     *      <li>Has a NULL subtree as a right child.</li>
     * </ul>
     *
     * <p>You should consult the class slides, your notes, and the textbook about what you need to do in those two
     * special cases.</p>
     * @param currDim The current dimension to consider.
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #insert(KDPoint, int, int)
     * @return A reference to this after the deletion takes place.
     */
    public KDTreeNode delete(KDPoint pIn, int currDim, int dims){
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Searches the subtree rooted at the current node for the provided {@link KDPoint}.
     * @param pIn The {@link KDPoint} to search for.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @return true iff pIn was found in the subtree rooted at this, false otherwise.
     */
    
    public boolean search(KDPoint pIn, int currDim, int dims){
    	if(this != null && this.p.equals(pIn)) {
         	return true;
        }else if(this != null && !this.p.equals(pIn)){
        	KDTreeNode temp = this;
        	while(temp != null && !temp.p.equals(pIn)) {
        		if(temp.p.coords[currDim] <= pIn.coords[currDim]) {
        			temp = temp.right;
        		}else {
        			temp = temp.left;
        		}
        		currDim = (currDim+1)%dims;
        	}
        	if(temp == null) {
        		return false;
        	}else {
        		return true;
        	}
        }else {
        	return false;
        }
    }

    /**
     * <p>Executes a range query in the given {@link KDTreeNode}. Given an &quot;anchor&quot; {@link KDPoint},
     * all {@link KDPoint}s that have a {@link KDPoint#euclideanDistance(KDPoint) euclideanDistance} of <b>at most</b> range
     * <b>INCLUSIVE</b> from the anchor point <b>except</b> for the anchor itself should be inserted into the {@link Collection}
     * that is passed.</p>
     *
     * <p>Remember: range queries behave <em>greedily</em> as we go down (approaching the anchor as &quot;fast&quot;
     * as our currDim allows and <em>prune subtrees</em> that we <b>don't</b> have to visit as we backtrack. Consult
     * all of our resources if you need a reminder of how these should work.</p>
     *
     * @param anchor The centroid of the hypersphere that the range query implicitly creates.
     * @param results A {@link Collection} that accumulates all the {@link }
     * @param currDim The current dimension examined by the {@link KDTreeNode}.
     * @param dims The total number of dimensions of our {@link KDPoint}s.
     * @param rangeghlekThe <b>INCLUSIVE</b> range from the &quot;anchor&quot; {@link KDPoint}, within which all the
     *              {@link KDPoint}s that satisfy our query will fall. The euclideanDistance metric used} is defined by
     *              {@link KDPoint#euclideanDistance(KDPoint)}.
     */
    public void range(KDPoint anchor, Collection<KDPoint> results,
                      double range, int currDim , int dims){
//    	KDTreeNode curr = this, parent = null;
//        while(curr != null) {
//        	double dist = curr.p.euclideanDistance(anchor);
//        	/*exclude the anchor point itself*/
//        	if(dist <= range && dist != 0) {
//        		results.add(curr.p);
//        	}
//        	/*check for location of anchor node*/
//        	if(curr.p.coords[currDim] <= anchor.coords[currDim]) {
//        		parent = curr;
//        		curr = curr.right;
//        	}else {
//        		parent = curr;
//        		curr = curr.left;
//        	}
//        	currDim = (currDim+1)%dims;
//        }
    	if(this != null) {
	    	double dist = this.p.euclideanDistance(anchor);
	    	if(dist <= range && dist != 0) {
	    		results.add(this.p);
	    	}
	    	if(left != null) {
	    		left.range(anchor, results, range, currDim, dims);
	    	}
	    	if(right != null) {
	    		right.range(anchor, results, range, currDim, dims);
	    	}
    	}
    }
    

    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#euclideanDistance(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>best solution</b>, which is passed as
     * an argument. This approach is known in Computer Science as &quot;branch-and-bound&quot; and it helps us solve an
     * otherwise exponential complexity problem (nearest neighbors) efficiently. Remember that when we want to determine
     * if we need to recurse to a different subtree, it is <b>necessary</b> to compare the euclideanDistance reported by
     * {@link KDPoint#euclideanDistance(KDPoint)} and coordinate differences! Those are comparable with each other because they
     * are the same data type ({@link Double}).</p>
     *
     * @return An object of type {@link NNData}, which exposes the pair (distance_of_NN_from_anchor, NN),
     * where NN is the nearest {@link KDPoint} to the anchor {@link KDPoint} that we found.
     *
     * @param anchor The &quot;ancor&quot; {@link KDPoint}of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param n An object of type {@link NNData}, which will define a nearest neighbor as a pair (distance_of_NN_from_anchor, NN),
     *      * where NN is the nearest neighbor found.
     *
     * @see NNData
     * @see #kNearestNeighbors(int, KDPoint, BoundedPriorityQueue, int, int)
     */
    public NNData<KDPoint> nearestNeighbor(KDPoint anchor, int currDim,
                                            NNData<KDPoint> n, int dims){
    	KDTreeNode curr = this;
    	double min;
    	while(curr != null) {
    		min = curr.p.euclideanDistance(anchor);
        	/*exclude the anchor point itself*/
        	if((n.getBestDist() == -1 && min > 0) || (min <= n.getBestDist() && min > 0)) {
        		KDPoint temp = new KDPoint(curr.p);
        		n.update(temp, min);
        	}
        	if(curr.p.coords[currDim] <= anchor.coords[currDim]) {
        		curr = curr.right;
        	}else {
        		curr = curr.left;
        	}
        	currDim = (currDim+1)%dims;
        }
    	ArrayList<KDPoint> list = new ArrayList<>();
    	range(anchor, list, n.getBestDist(), currDim, dims);
    	for(int x = 0; x < list.size(); x++) {
    		double dist = list.get(x).euclideanDistance(anchor);
    		if(dist <= n.getBestDist() && dist > 0) {
    			KDPoint temp = new KDPoint(list.get(x));
    			n.update(temp, dist);
    		}
    	}
    	return n;
    }
   

    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#euclideanDistance(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>worst solution</b>, which is maintained as the
     * last element of the provided {@link BoundedPriorityQueue}. This is another instance of &quot;branch-and-bound&quot;
     * Remember that when we want to determine if we need to recurse to a different subtree, it is <b>necessary</b>
     * to compare the euclideanDistance reported by* {@link KDPoint#euclideanDistance(KDPoint)} and coordinate differences!
     * Those are comparable with each other because they are the same data type ({@link Double}).</p>
     *
     * <p>The main difference of the implementation of this method and the implementation of
     * {@link #nearestNeighbor(KDPoint, int, NNData, int)} is the necessity of using the class
     * {@link BoundedPriorityQueue} effectively. Consult your various resources
     * to understand how you should be using this class.</p>
     *
     * @param k The total number of neighbors to retrieve. It is better if this quantity is an odd number, to
     *          avoid ties in Binary Classification tasks.
     * @param anchor The &quot;anchor&quot; {@link KDPoint} of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param queue A {@link BoundedPriorityQueue} that will maintain at most k nearest neighbors of
     *              the anchor point at all times, sorted by euclideanDistance to the point.
     *
     * @see BoundedPriorityQueue
     */
    public void kNearestNeighbors(int k, KDPoint anchor, BoundedPriorityQueue<KDPoint> queue, int currDim, int dims){
       	if(this != null) {
	    	double dist = p.euclideanDistance(anchor);
	    	if(!this.p.equals(anchor)) {
	    		queue.enqueue(p, dist);
	    	}
	    	if(left != null) {
	    		left.kNearestNeighbors(k, anchor, queue, currDim, dims);
	    	}
	    	if(right != null) {
	    		right.kNearestNeighbors(k, anchor, queue, currDim, dims);
	    	}
    	}
    }

    /**
     * Returns the height of the subtree rooted at the current node. Recall our definition of height for binary trees:
     * <ol>
     *     <li>A null tree has a height of -1.</li>
     *     <li>A non-null tree has a height equal to max(height(left_subtree), height(right_subtree))+1</li>
     * </ol>
     * @return the height of the subtree rooted at the current node.
     */
    public int height(){
    	if(this.p == null) {
    		return -1;
    	}else {
    		if(this.left == null && this.right == null) {
    			return 0;
    		}else {
    			int l = height(this.left);
    			int r = height(this.right);
    			if(l > r) {
    				return l;
    			}else	{
    				return r;
    			}	
    		}
    	}
    }
    
   

    /**
     * A simple getter for the {@link KDPoint} held by the current node. Remember: {@link KDPoint}s ARE
     * MUTABLE, SO WE NEED TO DO DEEP COPIES!!!
     * @return The {@link KDPoint} held inside this.
     */
    public KDPoint getPoint(){
        return this.p;
    }

    public KDTreeNode getLeft(){
        return this.left;
    }

    public KDTreeNode getRight(){
        return this.right;
    }
}
