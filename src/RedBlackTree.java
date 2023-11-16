import java.util.ArrayList;

class RBTNode {

    public static final int RED_COLOR = 1;
    public static final int BLACK_COLOR = 0;
    int rideNumber, rideCost, tripDuration;
    RBTNode parent, leftChild, rightChild;
    MinHeapNode minHeapPointer;
    int nodeColor;

    //Empty constructor
    public RBTNode(){}

    // Constructor to create the RedBlack node with the given ride details
    public RBTNode(int rideNumber, int rideCost, int tripDuration, RBTNode rbTreeNullNode){
        this.parent = null;
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;
        this.leftChild = rbTreeNullNode;
        this.rightChild = rbTreeNullNode;
        this.nodeColor = RED_COLOR;
    }

}
public class RedBlackTree {

    private RBTNode root;
    private final RBTNode externalBlackNode;
    public RedBlackTree() {
        externalBlackNode = new RBTNode();
        externalBlackNode.rideNumber = 0;
        externalBlackNode.rideCost = 0;
        externalBlackNode.tripDuration = 0;
        externalBlackNode.nodeColor = RBTNode.BLACK_COLOR;
        externalBlackNode.leftChild = null;
        externalBlackNode.rightChild = null;
        externalBlackNode.parent = null;
        root = externalBlackNode;
    }

    /* Method to search the RedBlack Tree*/
    private RBTNode searchRideHelper(RBTNode rbNode, int rideNumber) {
        /* check if the rideNumber is equal to current node rideNumber, or it is an external null Node*/
        if (rbNode == externalBlackNode || rideNumber == rbNode.rideNumber) {
            return rbNode; //if yes return the node
        }

        if (rideNumber < rbNode.rideNumber) { //search in the left subtree if the given rideNumber is less than current rideNumber
            return searchRideHelper(rbNode.leftChild, rideNumber);
        }
        return searchRideHelper(rbNode.rightChild, rideNumber); //search in the right subtree if above condition didn't satisfy
    }

    /* Method to search all the rides between the give range and return all the rides present */
    private ArrayList<String> searchBetweenRides(RBTNode rbNode, int rideNumber1, int rideNumber2, ArrayList<String> rides)   {
        if (rbNode == externalBlackNode) {
            return null;
        }

        //check the left subtree only if the current node rideNumber is greater than lower range rideNumber
        if(rideNumber1 < rbNode.rideNumber) searchBetweenRides(rbNode.leftChild, rideNumber1, rideNumber2, rides);

        if(rideNumber1 <= rbNode.rideNumber && rbNode.rideNumber <= rideNumber2){
            rides.add("(" +rbNode.rideNumber + "," + rbNode.rideCost + "," + rbNode.tripDuration + ")");
        }

        //check the right subtree only if the current node rideNumber is less than upper range rideNumber
        if(rbNode.rideNumber < rideNumber2) searchBetweenRides(rbNode.rightChild, rideNumber1, rideNumber2, rides);
        return rides;
    }

    public RBTNode searchRide(int rideNumber) {
        return searchRideHelper(this.root, rideNumber); //calling helper method
    }

    public ArrayList<String> searchRide(int rideNumber1, int rideNumber2) {
        ArrayList<String> rides = new ArrayList<>();
        rides = searchBetweenRides(this.root, rideNumber1, rideNumber2,rides); //calling helper method
        return rides;
    }

    public RBTNode createRedBlackTreeNode(int rideNumber, int rideCost, int tripDuration) {
        return new RBTNode(rideNumber,rideCost,tripDuration, externalBlackNode); // creating an instance of RedBlack Node
    }

    public void insertNodeInRedBlackTree(RBTNode redBlackNode){
        RBTNode parentNode = null;
        RBTNode currentNode = this.root;

        /* Find the parent node where we need to insert the current Node */
        while (currentNode != externalBlackNode) {
            parentNode = currentNode;
            if (redBlackNode.rideNumber < currentNode.rideNumber) {
                currentNode = currentNode.leftChild;
            } else {
                currentNode = currentNode.rightChild;
            }
        }

        redBlackNode.parent = parentNode; // set the parent pointer to the node we found above
        if (parentNode == null) {
            root = redBlackNode; // make current node as root if parent we found is null
        } else if (redBlackNode.rideNumber < parentNode.rideNumber) { //if rideNumber of current node is less than parent node rideNumber
            parentNode.leftChild = redBlackNode; // set the current node as leftChild to the parentNode
        } else {
            parentNode.rightChild = redBlackNode; // if above condition doesn't satisfy set the current node as rightChild to the parentNode
        }

        if (redBlackNode.parent == null) { // if no node is found, that means the current node is root so set black color
            redBlackNode.nodeColor = RBTNode.BLACK_COLOR;
            return ;
        }

        if (redBlackNode.parent.parent == null) { // if no grandParent for the current node then no need to change the color
            return ;
        }

        balanceAfterInsert(redBlackNode);
    }

    // Method to Balance the node after insertion
    private void balanceAfterInsert(RBTNode p) {
        RBTNode d;
        /*  Below while loop executes and balances the tree until there are no 2 consecutive redNodes,
             Each balancing strategy follows XYz rules as discussed in class */
        while (p.parent.nodeColor == RBTNode.RED_COLOR) {
            if (p.parent == p.parent.parent.rightChild) { //Check the Node's parent position is right to its parents. X =R
                d = p.parent.parent.leftChild;
                /* If parent's parents leftChild color is red(z=r) this is XYr case
                   do color flip irrespective of position of node & it's parent( X and Y)*/
                if (d.nodeColor == RBTNode.RED_COLOR) {
                    d.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.parent.nodeColor = RBTNode.RED_COLOR;
                    p = p.parent.parent; //set the node to grandParent and continue
                } else { // X= R & z= b
                    if (p == p.parent.leftChild) { //Y = L ,XYz =RLr so RL perform rotation, first leftRotate followed by rightRotate
                        p = p.parent;
                        leftRotate(p);
                    }
                    p.parent.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.parent.nodeColor = RBTNode.RED_COLOR;
                    rightRotate(p.parent.parent);  //for both RR  and RL we need to perform right rotation
                }
            } else { //Node's parent position is left to its parents, X = L
                d = p.parent.parent.rightChild;
                /* If parent's parents rightChild color is red(z=r) this is XYr case
                   then do color flip irrespective of position of node & it's parent( X and Y)*/
                if (d.nodeColor == RBTNode.RED_COLOR) {
                    d.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.parent.nodeColor = RBTNode.RED_COLOR;
                    p = p.parent.parent;
                } else { // X= L & z= b
                    if (p == p.parent.rightChild) { //Y = R so XYz =LRb, so perform LR rotation,first rightRotate followed by leftRotate
                        p = p.parent;
                        rightRotate(p);
                    }
                    p.parent.nodeColor = RBTNode.BLACK_COLOR;
                    p.parent.parent.nodeColor = RBTNode.RED_COLOR;
                    leftRotate(p.parent.parent); //for both LL and LR we need to perform right rotation
                }
            }
            if (p == root) {
                break; //if current node reaches root we can break the loop
            }
        }
        root.nodeColor = RBTNode.BLACK_COLOR;
    }


    // Method to deleteNode which calls helper method
    public void deleteRide(int data) {
        deleteRideHelper(this.root, data);
    }

    // Method to delete the ride with the given rideNumber
    private void deleteRideHelper(RBTNode cNode, int rideNumber) {
        RBTNode node = externalBlackNode;
        RBTNode deficientRoot, dNode;

        /* Traverse the tree and find the node in the tree we need to delete*/
        while (cNode != externalBlackNode) {
            if (cNode.rideNumber == rideNumber) {
                node = cNode;
            }
            if (cNode.rideNumber <= rideNumber) {
                cNode = cNode.rightChild;
            } else {
                cNode = cNode.leftChild;
            }
        }

        if (node == externalBlackNode) {
            return;
        }

        dNode = node;
        int deletingNodeColor = dNode.nodeColor;
        if (node.leftChild == externalBlackNode) { // Degree one node with no left Child deletion, degree 0 will also be handled here.
            deficientRoot = node.rightChild;
            replaceNode1WithNode2(node, node.rightChild);
        } else if (node.rightChild == externalBlackNode) { // Degree one node deletion with no right Child
            deficientRoot = node.leftChild;
            replaceNode1WithNode2(node, node.leftChild);
        } else {  // Degree two node deletion
            dNode = findMinimumRide(node.rightChild); // find the minimum ride from right subtree to replace the deletingNode
            deletingNodeColor = dNode.nodeColor;
            deficientRoot = dNode.rightChild;
            if (dNode.parent == node) {
                deficientRoot.parent = dNode;
            } else {
                replaceNode1WithNode2(dNode, dNode.rightChild); //swap the deleting Node and it's right Child.
                dNode.rightChild = node.rightChild;
                dNode.rightChild.parent = dNode;
            }

            replaceNode1WithNode2(node, dNode); // swap the minimum node found with deleting node
            dNode.leftChild = node.leftChild;
            dNode.leftChild.parent = dNode;
            dNode.nodeColor = node.nodeColor;
        }
        if (deletingNodeColor == RBTNode.BLACK_COLOR) { // if the deleted nodes color is black we need balance the tree
            balanceAfterDelete(deficientRoot);
        }
    }

    /* Method to balance the tree after the deletion */
    private void balanceAfterDelete(RBTNode rbNode) {
        RBTNode treeNode;

        /*  Below while loop executes and balances the tree until
            there mare no violations, We follow the Xcn rules  */
        while (rbNode != root && rbNode.nodeColor == RBTNode.BLACK_COLOR) {
            /* If Parents right child is red , do color flip and perform right rotation, X=L*/
            if (rbNode == rbNode.parent.leftChild) {
                treeNode = rbNode.parent.rightChild;
                if (treeNode.nodeColor == RBTNode.RED_COLOR) {
                    treeNode.nodeColor = RBTNode.BLACK_COLOR;
                    rbNode.parent.nodeColor = RBTNode.RED_COLOR;
                    rightRotate(rbNode.parent);
                    treeNode = rbNode.parent.rightChild;
                }
                // check if both children for the sibling node are black then color the sibling red
                if (treeNode.leftChild.nodeColor == RBTNode.BLACK_COLOR
                        && treeNode.rightChild.nodeColor == RBTNode.BLACK_COLOR) {
                    treeNode.nodeColor = RBTNode.RED_COLOR;
                    rbNode = rbNode.parent; //check if any violations by traversing up the tree
                } else {
                    // if right child of sibling is black, do color flip and perform left rotate
                    if (treeNode.rightChild.nodeColor == RBTNode.BLACK_COLOR) {
                        treeNode.leftChild.nodeColor = RBTNode.BLACK_COLOR;
                        treeNode.nodeColor = RBTNode.RED_COLOR; // change the node color to black
                        leftRotate(treeNode);
                        treeNode = rbNode.parent.rightChild;
                    }

                    //do re-coloring and perform rotation to remove violations
                    treeNode.nodeColor = rbNode.parent.nodeColor;
                    rbNode.parent.nodeColor = RBTNode.BLACK_COLOR;
                    treeNode.rightChild.nodeColor = RBTNode.BLACK_COLOR;
                    rightRotate(rbNode.parent);
                    rbNode = root;
                }
            } else { // The position of current node with respect to parent, X= R
                treeNode = rbNode.parent.leftChild;

                /* Check the sibling color is red, if yes change the color to black
                 *  and set the nodes parent color to red and perform RL rotation */
                if (treeNode.nodeColor == RBTNode.RED_COLOR) {
                    treeNode.nodeColor = RBTNode.BLACK_COLOR;
                    rbNode.parent.nodeColor = RBTNode.RED_COLOR;
                    leftRotate(rbNode.parent);
                    treeNode = rbNode.parent.leftChild;
                }

                if (treeNode.rightChild.nodeColor == RBTNode.BLACK_COLOR) { //Rr(n) or Rb1 case
                    treeNode.nodeColor = RBTNode.RED_COLOR; // set the color of sibling to red and
                    rbNode = rbNode.parent; //  continue the process to check by setting current node's parent as current node
                } else {
                    /* Check if left child color is black if so set the rightChild color to black
                    * and then node color to red and perform LR rotation*/
                    if (treeNode.leftChild.nodeColor == RBTNode.BLACK_COLOR) {
                        treeNode.rightChild.nodeColor = RBTNode.BLACK_COLOR;
                        treeNode.nodeColor = RBTNode.RED_COLOR;
                        rightRotate(treeNode);
                        treeNode = rbNode.parent.leftChild;
                    }

                    treeNode.nodeColor = rbNode.parent.nodeColor;
                    rbNode.parent.nodeColor = RBTNode.BLACK_COLOR; // set the
                    treeNode.leftChild.nodeColor = RBTNode.BLACK_COLOR;
                    leftRotate(rbNode.parent);
                    rbNode = root;
                }
            }
        }
        rbNode.nodeColor = RBTNode.BLACK_COLOR;
    }

    /* Method to get the minimum value for the tree rooted at given node */
    public RBTNode findMinimumRide(RBTNode node) {
        while (node.leftChild != externalBlackNode) {
            node = node.leftChild;
        }
        return node;
    }

    /* Method to replace Node1 with Node2 */
    private void replaceNode1WithNode2(RBTNode node1, RBTNode node2) {
        if (node1.parent == null) {
            root = node2;
        } else if (node1 == node1.parent.leftChild) {
            node1.parent.leftChild = node2;
        } else {
            node1.parent.rightChild = node2;
        }
        node2.parent = node1.parent;
    }

    /* Method to make a rightRotation with respect to the current node */
    public void rightRotate(RBTNode node) {
        RBTNode rightChild = node.rightChild;
        node.rightChild = rightChild.leftChild;
        if (rightChild.leftChild != externalBlackNode) {
            rightChild.leftChild.parent = node;
        }

        rightChild.parent = node.parent; //set the parent node for child of given node to node's parent
        // Set the child pointer for node's parent node based on the position of node
        if (node.parent == null) {
            this.root = rightChild;
        } else if (node == node.parent.leftChild) {
            node.parent.leftChild = rightChild;
        } else {
            node.parent.rightChild = rightChild;
        }
        rightChild.leftChild = node;
        node.parent = rightChild;
    }


    /* Method to make a leftRotation with respect to the current node */
    public void leftRotate(RBTNode node) {
        RBTNode leftChild = node.leftChild;
        node.leftChild = leftChild.rightChild;
        if (leftChild.rightChild != externalBlackNode) {
            leftChild.rightChild.parent = node;
        }
        leftChild.parent = node.parent; //set the parent node for child of given node to node's parent
        // Set the child pointer for node's parent node based on the position of node
        if (node.parent == null) {
            this.root = leftChild;
        } else if (node == node.parent.rightChild) {
            node.parent.rightChild = leftChild;
        } else {
            node.parent.leftChild = leftChild;
        }

        leftChild.rightChild = node;
        node.parent = leftChild;
    }

}