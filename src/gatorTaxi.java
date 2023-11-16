import java.io.*;
import java.util.ArrayList;


public class gatorTaxi {
    public static final String NULL_NODE_PRINT = "(0,0,0)\n";
    public static final String OUTPUT_FILE = "output_file.txt";
    MinHeap minHeap;
    RedBlackTree redBlackTree;
    static FileWriter fileWriter;
    static {
        try {
            fileWriter = new FileWriter(OUTPUT_FILE); // a fileWriter instance to write the output to file.
        } catch (IOException e) {
            System.out.println("An Error occurred while writing to File");
        }
    }

    public gatorTaxi(){
        minHeap = new MinHeap(); // creating an instance of minHeap
        redBlackTree = new RedBlackTree(); //creating an instance of RedBlackTree
    }

    /* Method to print the given ride */
    public void print(int rideNumber) throws IOException {
        RBTNode redBlackNode = redBlackTree.searchRide(rideNumber); //call the searchRide method of RedBlack tree
        writeRideDetailsToFile(redBlackNode.rideNumber,redBlackNode.rideCost,redBlackNode.tripDuration); // write the node details to file
    }

    /* Method to print all the rides between given rideNumbers */
    public void print(int rideNumber1, int rideNumber2) throws IOException {
       ArrayList<String> result = redBlackTree.searchRide(rideNumber1,rideNumber2); //call the searchRide method of RedBlack tree with given rideNumbers
        if(result.size() >0){
            for(int i= 0; i< result.size()-1; i++){ //for each of the rideDetails in the result list print them
                fileWriter.write(result.get(i) +",");
            }
            fileWriter.write(result.get(result.size()-1) +"\n");
        } else fileWriter.write(NULL_NODE_PRINT); // if no ride is found between the given rideNumbers print null node.
    }

    /* Method to insert the given ride */
    public void insert(int rideNumber, int rideCost, int tripDuration) throws IOException {
        RBTNode redBlackTreeNode = redBlackTree.searchRide(rideNumber); //search if there is a node with same rideNumber
        if(redBlackTreeNode.rideNumber == rideNumber){
            fileWriter.write("Duplicate RideNumber\n"); // if we found the ride already Write to console
            fileWriter.close();
            removeExtraLinesIfAny();
            System.exit(0); // exit the program as duplicate rideNumber can't be inserted.
        }else{
            MinHeapNode minHeapNode = minHeap.createNode(rideNumber,rideCost,tripDuration); //create a minHeap node instance
            RBTNode rbNode = redBlackTree.createRedBlackTreeNode(rideNumber,rideCost,tripDuration); //create a RedBlackTree node instance

            minHeapNode.rbNodePointer = rbNode; // Set the minHeap redBlackPointer to the RedBlack Node instance
            rbNode.minHeapPointer = minHeapNode; // Set the RedBlack minHeapPointer to the minHeap Node instance

            minHeap.insertNodeInHeap(minHeapNode); // insert the created node into Heap
            redBlackTree.insertNodeInRedBlackTree(rbNode); // insert the redBlack created node into the tree
        }
    }

    /* Method to get the Next ride with less rideCost and tripDuration */
    public void getNextRide() throws IOException {
        try{
            MinHeapNode nextRide = minHeap.removeMin(); //get the minimum cost ride from heap
            redBlackTree.deleteRide(nextRide.rideNumber); //delete the corresponding ride from the RedBlack tree
            writeRideDetailsToFile(nextRide.rideNumber,nextRide.rideCost , nextRide.tripDuration); // write the ride details to file
        } catch (IllegalStateException e){
            fileWriter.write("No active ride requests\n"); // if the heap is empty write to console
        }
    }

    /* Method to Cancel the ride */
    private void cancelRide(int rideNumber) {
        MinHeapNode node = redBlackTree.searchRide(rideNumber).minHeapPointer; // Get the corresponding node in minHeap for given ride
        redBlackTree.deleteRide(rideNumber); //delete the ride from RedBlack Tree
        if(node != null){
            minHeap.removeNode(node.index); // delete the corresponding node from minHeap
        }

    }

    /* Method to update the trip based on the conditions */
    private void updateTrip(int rideNumber, int newTripDuration) throws IOException {
        RBTNode rbNode = redBlackTree.searchRide(rideNumber);// get the ride with given rideNumber
        int existingTripDuration = rbNode.tripDuration;
        if(newTripDuration > (2 *existingTripDuration)){ //if newTripDuration is greater than 2times existing duration cancel it.
            cancelRide(rideNumber);
        } else if(existingTripDuration < newTripDuration && // check if new trip duration is in between existing duration and 2* existing duration
                newTripDuration < (2 *existingTripDuration)){
            cancelRide(rideNumber); //cancel the existing ride
            insert(rideNumber,rbNode.rideCost+10,newTripDuration); //insert the trip with new trip duration and cost increased by 10
        } else if(newTripDuration < existingTripDuration){ //if new trip duration is less than existing duration
            redBlackTree.searchRide(rideNumber).tripDuration = newTripDuration; // update the trip Duration for ride in RedBlack tree with new trip duration
            minHeap.updateNode(rbNode.minHeapPointer, newTripDuration); // also update in the minHeap with new trip duration
        }
    }

    private void writeRideDetailsToFile(int rideNumber, int rideCost, int tripDuration) throws IOException {
        fileWriter.write("(" +rideNumber + "," + rideCost + "," +tripDuration + ")\n"); // write to file ride Details
    }

    /* Remove if any extra line are present in the file in the end */
    private static void removeExtraLinesIfAny() throws IOException {
        RandomAccessFile file  = new RandomAccessFile(OUTPUT_FILE, "rw");
        long fileLength = file.length();
        if (fileLength > 0) {
            file.setLength(fileLength - 1);
        }
        file.close();
    }

    private static int convertToInteger(String rideDetails) {
        return Integer.parseInt(rideDetails);
    }

    public static void main(String[] args) throws IOException {

        gatorTaxi gatorTaxi = new gatorTaxi();

        String inputFile = args[0];
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        String operation = bufferedReader.readLine(); //read each line from input file

        /* Read input file until the end,
           For each of the operation read from the input file and
           then check which operation using contains method and
           then split the substring to get the input parameters and
            perform the action by calling the required method */
        while(operation != null){
            String variables = operation.substring(operation.indexOf("(") + 1, operation.indexOf(")"));

            if(operation.contains("Print")){
                String[] rideNumbers = variables.split(",", 0);
                if(rideNumbers.length == 1){
                    gatorTaxi.print(convertToInteger(rideNumbers[0]));
                } else{
                    gatorTaxi.print(convertToInteger(rideNumbers[0]), convertToInteger(rideNumbers[1]));
                }
            } else if(operation.contains("Insert")){
                String[] rideDetails = variables.split(",", 0);
                gatorTaxi.insert(convertToInteger(rideDetails[0]), convertToInteger(rideDetails[1]), convertToInteger(rideDetails[2]));
            } else if(operation.contains("GetNextRide")) {
                gatorTaxi.getNextRide();
            } else if(operation.contains("CancelRide")){
                gatorTaxi.cancelRide(convertToInteger(variables));
            } else if(operation.contains("UpdateTrip")){
                String[] rideDetails = variables.split(",", 0);
                gatorTaxi.updateTrip(convertToInteger(rideDetails[0]), convertToInteger(rideDetails[1]));
            }
            operation = bufferedReader.readLine();
        }
        fileWriter.close();
        removeExtraLinesIfAny();


//        String file_name = args[0];
        String file_name = "src/input.txt";
        BufferedReader br = new BufferedReader(new FileReader(file_name));
        String command = br.readLine();
        while(command != null){
            if(command.contains("Insert")){
                String prunedCommand = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                String[] nums = prunedCommand.split(",", 0);
                gatorTaxi.insert(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]), Integer.parseInt(nums[2]));
            }
            else if(command.contains("GetNextRide"))
            {
                gatorTaxi.getNextRide();
            }
            else if(command.contains("Print")){
                String prunedCommand = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                String[] nums = prunedCommand.split(",", 0);
                if(nums.length == 1){
                    gatorTaxi.print(Integer.parseInt(nums[0]));
                }
                else{
                    gatorTaxi.print(Integer.parseInt(nums[0]),Integer.parseInt(nums[1]));
                }
            }
            else if(command.contains("CancelRide")){
                String rideNumber = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                gatorTaxi.cancelRide( Integer.parseInt(rideNumber) );
            }
            else if(command.contains("UpdateTrip")){
                String prunedCommand = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                String[] nums = prunedCommand.split(",", 0);
                gatorTaxi.updateTrip(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
            }

            command = br.readLine();
        }

    }


}
