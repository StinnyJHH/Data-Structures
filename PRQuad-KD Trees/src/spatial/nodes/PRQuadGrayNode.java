package spatial.nodes;

import spatial.exceptions.UnimplementedMethodException;
import spatial.kdpoint.KDPoint;
import spatial.knnutils.BoundedPriorityQueue;
import spatial.knnutils.NNData;
import spatial.trees.CentroidAccuracyException;
import spatial.trees.PRQuadTree;

import java.util.Collection;

/** <p>A {@link PRQuadGrayNode} is a gray (&quot;mixed&quot;) {@link PRQuadNode}. It
 * maintains the following invariants: </p>
 * <ul>
 *      <li>Its children pointer buffer is non-null and has a length of 4.</li>
 *      <li>If there is at least one black node child, the total number of {@link KDPoint}s stored
 *      by <b>all</b> of the children is greater than the bucketing parameter (because if it is equal to it
 *      or smaller, we can prune the node.</li>
 * </ul>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 *  @author --- Austin Han ---
 */
public class PRQuadGrayNode extends PRQuadNode{


    /* ******************************************************************** */
    /* *************  PLACE ANY  PRIVATE FIELDS AND METHODS HERE: ************ */
    /* ********************************************************************** */
	
	private int height;
	private PRQuadNode[] quadrant;
	
	private int getQuad(KDPoint p) {
		
		if (p != null) {
			if (p.coords[0] < centroid.coords[0] && p.coords[1] >= centroid.coords[1]) {
			    return 0;
			} else if (p.coords[0] >= centroid.coords[0] && p.coords[1] >= centroid.coords[1]) {
			    return 1;
			} else if (p.coords[0] < centroid.coords[0]) {
			    return 2;
			} else {
			    return 3;
			}
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	private KDPoint getCent(int quad, int k) {
		if(quad == 0) {
			return new KDPoint((int) (centroid.coords[0] - Math.pow(2, k) / 2),
                    (int) (centroid.coords[1] + Math.pow(2, k) / 2));
		}else if(quad == 1) {
			return new KDPoint((int) (centroid.coords[0] + Math.pow(2, k) / 2),
                    (int) (centroid.coords[1] + Math.pow(2, k) / 2));
		}else if(quad == 2) {
			return new KDPoint((int) (centroid.coords[0] - Math.pow(2, k) / 2),
                    (int) (centroid.coords[1] - Math.pow(2, k) / 2));
		}else if(quad == 3){
			return new KDPoint((int) (centroid.coords[0] + Math.pow(2, k) / 2),
	                    (int) (centroid.coords[1] - Math.pow(2, k) / 2));
		}
		throw new IllegalArgumentException();
	}

	private void updateHeight() {
		int max = -1;
		if(quadrant[0] != null) {
			max = Math.max(quadrant[0].height(), max);
		}
		if(quadrant[1] != null) {
			max = Math.max(quadrant[1].height(), max);
		}
		if(quadrant[2] != null) {
			max = Math.max(quadrant[2].height(), max);
		}
		if(quadrant[3] != null) {
			max = Math.max(quadrant[3].height(), max);
		}
		height = max+1;
	}
	
    /* *********************************************************************** */
    /* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
    /* *********************************************************************** */

    /**
     * Creates a {@link PRQuadGrayNode}  with the provided {@link KDPoint} as a centroid;
     * @param centroid A {@link KDPoint} that will act as the centroid of the space spanned by the current
     *                 node.
     * @param k The See {@link PRQuadTree#PRQuadTree(int, int)} for more information on how this parameter works.
     * @param bucketingParam The bucketing parameter fed to this by {@link PRQuadTree}.
     * @see PRQuadTree#PRQuadTree(int, int)
     */
    public PRQuadGrayNode(KDPoint centroid, int k, int bucketingParam){
        super(centroid, k, bucketingParam); // Call to the super class' protected constructor to properly initialize the object!
        this.height = 0;
        this.quadrant = new PRQuadNode[4];
    }


    /**
     * <p>Insertion into a {@link PRQuadGrayNode} consists of navigating to the appropriate child
     * and recursively inserting elements into it. If the child is a white node, memory should be allocated for a
     * {@link PRQuadBlackNode} which will contain the provided {@link KDPoint} If it's a {@link PRQuadBlackNode},
     * refer to {@link PRQuadBlackNode#insert(KDPoint, int)} for details on how the insertion is performed. If it's a {@link PRQuadGrayNode},
     * the current method would be called recursively. Polymorphism will allow for the appropriate insert to be called
     * based on the child object's runtime object.</p>
     * @param p A {@link KDPoint} to insert into the subtree rooted at the current {@link PRQuadGrayNode}.
     * @param k The side length of the quadrant spanned by the <b>current</b> {@link PRQuadGrayNode}. It will need to be updated
     *          per recursive call to help guide the input {@link KDPoint}  to the appropriate subtree.
     * @return The subtree rooted at the current node, potentially adjusted after insertion.
     * @see PRQuadBlackNode#insert(KDPoint, int)
     */
    @Override
    public PRQuadNode insert(KDPoint p, int k) {
    	if(k < 0){
    		throw new CentroidAccuracyException(null);
    	}
    	int quad = getQuad(p);
    	
    	if(quadrant[quad] != null) {
    		quadrant[quad] = quadrant[quad].insert(p, k-1);
    	}else {
    		quadrant[quad] = new PRQuadBlackNode(getCent(quad, k-1), k-1, bucketingParam, p);
    	}
    	
    	updateHeight();
    	return this;
    }

    /**
     * <p>Deleting a {@link KDPoint} from a {@link PRQuadGrayNode} consists of recursing to the appropriate
     * {@link PRQuadBlackNode} child to find the provided {@link KDPoint}. If no such child exists, the search has
     * <b>necessarily failed</b>; <b>no changes should then be made to the subtree rooted at the current node!</b></p>
     *
     * <p>Polymorphism will allow for the recursive call to be made into the appropriate delete method.
     * Importantly, after the recursive deletion call, it needs to be determined if the current {@link PRQuadGrayNode}
     * needs to be collapsed into a {@link PRQuadBlackNode}. This can only happen if it has no gray children, and one of the
     * following two conditions are satisfied:</p>
     *
     * <ol>
     *     <li>The deletion left it with a single black child. Then, there is no reason to further subdivide the quadrant,
     *     and we can replace this with a {@link PRQuadBlackNode} that contains the {@link KDPoint}s that the single
     *     black child contains.</li>
     *     <li>After the deletion, the <b>total</b> number of {@link KDPoint}s contained by <b>all</b> the black children
     *     is <b>equal to or smaller than</b> the bucketing parameter. We can then similarly replace this with a
     *     {@link PRQuadBlackNode} over the {@link KDPoint}s contained by the black children.</li>
     *  </ol>
     *
     * @param p A {@link KDPoint} to delete from the tree rooted at the current node.
     * @return The subtree rooted at the current node, potentially adjusted after deletion.
     */
    @Override
    public PRQuadNode delete(KDPoint p) {
    	PRQuadNode curr = this;
    	int count = 0;
    	int quad = getQuad(p);
    	
    	if(quadrant[quad] != null) {
    		quadrant[quad] = quadrant[quad].delete(p);
    		for(PRQuadNode node : quadrant) {
    			if(node != null) {
    				count++;
    			}
    		}
    		if(count == 1) {
	    		curr = new PRQuadBlackNode(centroid, k, bucketingParam);
	    		
	    		if(count() <= bucketingParam) {
	    			for (PRQuadNode node : quadrant) {
	    				if(node != null) {
		                    for (KDPoint point : ((PRQuadBlackNode) node).getPoints()) {
		                        curr.insert(point, k);
		                    }
	    				}
	                }
	            } else {
	            	updateHeight();
	            }
    		}
        }
        return curr;
    }

    @Override
    public boolean search(KDPoint p){
    	int quad = getQuad(p);
    	
    	if(quadrant[quad] != null) {
    		return quadrant[quad].search(p);
    	}
    	return false;
    }

    @Override
    public int height(){
        return height;
    }

    @Override
    public int count(){
    	int num = 0;
    	
    	for(PRQuadNode n : quadrant) {
    		if(n != null) {
    			num+= n.count();
    		}
    	}
    	
    	return num;
    }

    /**
     * Returns the children of the current node in the form of a Z-ordered 1-D array.
     * @return An array of references to the children of {@code this}. The order is Z (Morton), like so:
     * <ol>
     *     <li>0 is NW</li>
     *     <li>1 is NE</li>
     *     <li>2 is SW</li>
     *     <li>3 is SE</li>
     * </ol>
     */
    public PRQuadNode[] getChildren(){
    	return this.quadrant;
    }

    @Override
    public void range(KDPoint anchor, Collection<KDPoint> results,
                      double range) {
    	if(quadrant[0] != null) {
    		quadrant[0].range(anchor, results, range);
    	}
		if(quadrant[1] != null) {
			quadrant[1].range(anchor, results, range);
		}
		if(quadrant[2] != null) {
			quadrant[2].range(anchor, results, range);
		}
		if(quadrant[3] != null) {
			quadrant[3].range(anchor, results, range);
		}

    }

    @Override
    public NNData<KDPoint> nearestNeighbor(KDPoint anchor, NNData<KDPoint> n)  {
    	if(quadrant[0] != null) {
    		quadrant[0].nearestNeighbor(anchor, n);
    	}
		if(quadrant[1] != null) {
			quadrant[1].nearestNeighbor(anchor, n);
		}
		if(quadrant[2] != null) {
			quadrant[2].nearestNeighbor(anchor, n);
		}
		if(quadrant[3] != null) {
			quadrant[3].nearestNeighbor(anchor, n);
		}
		return n;
    }

    @Override
    public void kNearestNeighbors(int k, KDPoint anchor, BoundedPriorityQueue<KDPoint> queue) {
    	if(quadrant[0] != null) {
    		quadrant[0].kNearestNeighbors(k, anchor, queue);
    	}
		if(quadrant[1] != null) {
			quadrant[1].kNearestNeighbors(k, anchor, queue);
		}
		if(quadrant[2] != null) {
			quadrant[2].kNearestNeighbors(k, anchor, queue);
		}
		if(quadrant[3] != null) {
			quadrant[3].kNearestNeighbors(k, anchor, queue);
		}
    }
}

