import java.util.ArrayList;

class MinHeapNode {
    int rideNumber,rideCost,tripDuration; //variables to store the ride details
    int index; //the index in which the node is stored
    RBTNode rbNodePointer; //pointer to corresponding node in RedBlack Tree

    // Constructor to create the MinHeap node
    public MinHeapNode(int rideNumber,int rideCost, int tripDuration, int index){
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;
        this.index = index;
        this.rbNodePointer = null;
    }
}

public class MinHeap {
    private final ArrayList<MinHeapNode> minHeap;

    public MinHeap() {
        minHeap = new ArrayList<>(); //To store the MinHeap Elements
    }

    public MinHeapNode createNode(int rideNumber, int rideCost, int rideDuration) {
        //creating an instance of MinHeap node with ride details provided
        return new MinHeapNode(rideNumber,rideCost,rideDuration, minHeap.size());
    }

    public void insertNodeInHeap(MinHeapNode minHeapNode) {
        minHeap.add(minHeapNode); //inserting the given node into the minheap
        heapifyUp(minHeap.size() - 1); // after inserting the node heapifying to satisfy the minHeap condition
    }

    public MinHeapNode removeMin() {
        return removeNode(0); //calling removeNode method with root index i.e; 0.
    }

    public MinHeapNode removeNode(int nodeIndex) {
        if (minHeap.isEmpty()) { //If heap is empty, if yes we cannot remove so throw error.
            throw new IllegalStateException("Heap is empty");
        }
        MinHeapNode removedNode;
        swapNodes(nodeIndex, minHeap.size()-1); //swap the current node with the last node in the heap
        removedNode = minHeap.remove(minHeap.size()-1); //remove the node in last index as our required node is swapped to that position
        if(!minHeap.isEmpty()) heapifyDown(nodeIndex); //if heap is not empty then perform heapify down from nodeIndex as we placed high priority node.
        return removedNode;
    }

    public void updateNode(MinHeapNode node, int newTripDuration) {
        node.tripDuration = newTripDuration; //update the node tripDuration to new tripDuration
        heapifyUp(node.index); //perform heapifyUp from the node index as there is a chance for violation for minHeap property
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (minHeap.get(index).rideCost < minHeap.get(parentIndex).rideCost) { //check if rideCost of current index is less than parent
                swapNodes(index, parentIndex); //swap the parent and currentIndex as minHeap condition is violated
                index = parentIndex; //set index to parent and continue the process
            } else if(minHeap.get(index).rideCost == minHeap.get(parentIndex).rideCost && // if the rideCost are equal for parent and current node
                    minHeap.get(index).tripDuration < minHeap.get(parentIndex).tripDuration){ //check if tripDuration of current index is less than parent
                swapNodes(index, parentIndex); //swap the parent and currentIndex as minHeap condition is violated
                index = parentIndex; //set index to parent and continue the process
            }else {
                break; //If heap condition is not violated break.
            }
        }
    }

    private void heapifyDown(int index) {
        while (index < minHeap.size()) {
            int leftChildIndex = 2 * index + 1; //index of leftChild for given index
            int rightChildIndex = 2 * index + 2; //index of rightChild for given index
            if (leftChildIndex >= minHeap.size()) {
                break;
            }
            int childIndex = getMinimumChildIndex(leftChildIndex,rightChildIndex); // compare between right and left children for min priority node
            /* Check if the minheap property is satisfied for the current node and
             the minimum priority child we got from above step, is condition is violated
             then swap the nodes and continue the process until no violation of heap property */
            if (minHeap.get(index).rideCost > minHeap.get(childIndex).rideCost) {
                swapNodes(index, childIndex);
                index = childIndex;
            }else if(minHeap.get(index).rideCost == minHeap.get(childIndex).rideCost &&
                    minHeap.get(index).tripDuration > minHeap.get(childIndex).tripDuration){
                swapNodes(index, childIndex);
                index = childIndex;
            } else {
                break;
            }
        }
    }

    private int  getMinimumChildIndex(int leftChildIndex, int rightChildIndex) {
        int minChildIndex = leftChildIndex; //first set the minChildIndex to leftChild

        //check if rightChildIndex cost is less than leftChild, if yes set the minChildIndex to rightChild
        if (rightChildIndex < minHeap.size() && minHeap.get(rightChildIndex).rideCost < minHeap.get(leftChildIndex).rideCost) {
            minChildIndex = rightChildIndex;
        } else if(rightChildIndex < minHeap.size()
                && minHeap.get(rightChildIndex).rideCost == minHeap.get(leftChildIndex).rideCost  // if left and right child rideCost are equal
                && minHeap.get(rightChildIndex).tripDuration < minHeap.get(leftChildIndex).tripDuration){ // check if tripDuration is less for rightChild, if yes set minChildIndex to right
            minChildIndex = rightChildIndex;
        }
        return minChildIndex;
    }

    private void swapNodes(int index1, int index2) {
        MinHeapNode temp1 = minHeap.get(index1); //create a temporary node for node at index1
        temp1.index = index2; // set the index of this temporary node to index2
        MinHeapNode temp2 = minHeap.get(index2); //create a temporary node for node at index2
        temp2.index = index1; // set the index of this temporary node to index1
        minHeap.set(index1, temp2); // At index1 set the node temp2 node i.e; node at index2 with updated index
        minHeap.set(index2, temp1); // At index2 set the node temp2 node i.e; node at index2 with updated index
    }

}
