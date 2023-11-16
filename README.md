# GatorTaxi Ride-Tracking System

## Overview

GatorTaxi is a ride-sharing service managing a high volume of ride requests. A new software system has been developed to efficiently handle these requests, focusing on tracking pending rides.

## Ride Identification

Every ride is uniquely identified by three attributes:

1. **rideNumber:** A distinct integer identifier.
2. **rideCost:** Estimated cost in integer dollars.
3. **tripDuration:** Total time in integer minutes from pickup to destination.

## Implemented Operations

### 1. Print(rideNumber)

Prints the triplet (rideNumber, rideCost, tripDuration) for the specified rideNumber.

### 2. Print(rideNumber1, rideNumber2)

Prints triplets (rx, rideCost, tripDuration) for ride numbers between rideNumber1 and rideNumber2.

### 3. Insert(rideNumber, rideCost, tripDuration)

Adds a new ride if the rideNumber is unique.

### 4. GetNextRide()

Outputs the ride with the lowest rideCost. In ties, selects the one with the lowest tripDuration and removes it.

### 5. CancelRide(rideNumber)

Deletes the triplet (rideNumber, rideCost, tripDuration) if it exists.

### 6. UpdateTrip(rideNumber, new_tripDuration)

Allows a rider to update the destination by changing the tripDuration for the specified rideNumber.

## Implementation Details

The GatorTaxi ride-tracking software employs a min-heap and a Red-Black Tree (RBT) for efficient storage and retrieval of ride information.

### Min-Heap

Stores triplets ordered by rideCost. In case of tie, prioritizes the one with the shortest tripDuration. Root node always represents the lowest cost ride and, in ties, the shortest trip duration.

### Red-Black Tree (RBT)

Stores triplets ordered by rideNumber for efficient retrieval and deletion based on unique rideNumber.

### Pointers and Synchronization

Maintains synchronization between the min-heap and RBT with pointers between corresponding nodes. This ensures consistency between the two data structures.

- The implemented structures handle ride requests efficiently.
- Pointers guarantee synchronization between the structures.
- The system accommodates up to 2000 active rides for scalability.
- Returns "No active ride requests" if there are none.
