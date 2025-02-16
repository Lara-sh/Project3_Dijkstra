package Project3;

import java.util.*;

public class Graph {
    int numberOfVertices; // Total number of vertices in the graph
    HashTable hashTable; // Hash table for storing and retrieving vertices efficiently
    Vertix[] vertixArray; // Array to store vertices instead of using ArrayList

    // Enumeration for the types of weights associated with edges
    public enum WeightType {
        DISTANCE, PRICE, TIME
    }

    // Constructor to initialize the graph with the given number of vertices
    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        this.hashTable = new HashTable(this.numberOfVertices); // Initialize hash table
        this.vertixArray = new Vertix[numberOfVertices]; // Initialize vertex array
    }

    // Method to add a vertex to the graph
    public void addVertix(Vertix vertix) {
        hashTable.put(vertix); // Add vertex to hash table
        for (int i = 0; i < vertixArray.length; i++) {
            if (vertixArray[i] == null) { // Find the first empty slot in the array
                vertixArray[i] = vertix; // Add the vertex
                break;
            }
        }
    }

    // Method to retrieve a vertex by its country name using the hash table
    public Vertix getVertix(String countryName) {
        return hashTable.getVertex(countryName);
    }

    // Getter for the hash table
    public HashTable getHashTable() {
        return hashTable;
    }

    // Method to print all vertices and their associated edges
    public void printVerticesAndEdges() {
        System.out.println("Vertices and their Edges:");
        for (Vertix vertix : vertixArray) {
            if (vertix != null) {
                Country country = vertix.getCountry();
                System.out.println("Vertex: " + country.getCountryName());

                LinkedList edges = vertix.getVertices(); // Get the list of edges for the vertex
                if (edges == null || edges.isEmpty()) {
                    System.out.println("  No edges.");
                } else {
                    LinkedListNode node = edges.getFirstNode(); // Traverse through the edges
                    while (node != null) {
                        Edge edge = node.getEdge();
                        Vertix destination = edge.getDestination(); // Destination vertex of the edge
                        System.out.println("  Edge to: " + destination.getCountry().getCountryName() +
                                ", Distance: " + edge.getDist() +
                                ", Price: " + edge.getPrice() +
                                ", Time: " + edge.getTime());
                        node = node.getNext(); // Move to the next edge
                    }
                }
            }
        }
    }

    // Method to find the best path between source and destination based on a given criterion
    public LinkedList getResult(int numberOfEdges, String source, String destination, String criterion) {
        Vertix sourceVertix = this.getVertix(source); // Get source vertex
        if (sourceVertix == null) {
            System.out.println("Source vertex not found: " + source);
            return null;
        }

        Vertix destinationVertix = this.getVertix(destination); // Get destination vertex
        if (destinationVertix == null) {
            System.out.println("Destination vertex not found: " + destination);
            return null;
        }

        // Initialize cost, distance, and time arrays for each vertex
        double[] costs = new double[numberOfVertices];
        LinkedList[] paths = new LinkedList[numberOfVertices];
        double[] distances = new double[numberOfVertices];
        double[] times = new double[numberOfVertices];

        for (int i = 0; i < numberOfVertices; i++) {
            costs[i] = Double.MAX_VALUE; // Set initial cost to infinity
            paths[i] = new LinkedList(); // Initialize empty paths
            distances[i] = Double.MAX_VALUE; // Set initial distance to infinity
            times[i] = Double.MAX_VALUE; // Set initial time to infinity
        }

        // Get the index of the source vertex and initialize its values
        int sourceIndex = findVertixIndex(sourceVertix);
        costs[sourceIndex] = 0.0;
        distances[sourceIndex] = 0.0;
        times[sourceIndex] = 0.0;

        boolean[] visited = new boolean[numberOfVertices]; // Track visited vertices
        List<Integer> unvisited = new ArrayList<>(); // List of unvisited vertices
        unvisited.add(sourceIndex); // Add the source index to unvisited

        while (!unvisited.isEmpty()) {
            // Get the vertex with the minimum cost from the unvisited list
            int currIndex = getMinCostIndex(unvisited, costs);
            unvisited.remove(Integer.valueOf(currIndex));

            Vertix curr = vertixArray[currIndex];
            if (curr == null || visited[currIndex]) continue;

            visited[currIndex] = true;

            // Check if the destination vertex is reached
            if (curr.getCountry().getCountryName().equals(destination)) {
                LinkedList resultPath = paths[currIndex];
                resultPath.setDistance(distances[currIndex]);
                resultPath.setTime(times[currIndex]);
                resultPath.setCost(costs[currIndex]);
                hashTable.setAllVerticesToFalse(); // Reset the visited state of all vertices
                return resultPath;
            }

            // Traverse the edges of the current vertex
            LinkedList vertices = curr.getVertices();
            LinkedListNode node = vertices.getFirstNode();
            while (node != null) {
                Edge edge = node.getEdge();
                Vertix neighbor = edge.getDestination();
                int neighborIndex = findVertixIndex(neighbor);

                if (neighborIndex != -1 && !visited[neighborIndex]) {
                    double edgeCost = 0.0;
                    double accumulatedDistance = distances[currIndex];
                    double accumulatedTime = times[currIndex];

                    // Update cost based on the specified criterion
                    switch (criterion.toLowerCase()) {
                        case "price":
                            edgeCost = edge.getPrice();
                            break;
                        case "time":
                            edgeCost = edge.getTime();
                            accumulatedTime += edge.getTime();
                            break;
                        case "distance":
                            edgeCost = edge.getDist();
                            accumulatedDistance += edge.getDist();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid criterion: " + criterion);
                    }

                    // Update costs and paths if a better path is found
                    double newCost = costs[currIndex] + edgeCost;
                    if (newCost < costs[neighborIndex]) {
                        costs[neighborIndex] = newCost;

                        LinkedList newPath = new LinkedList();
                        newPath.addAll(paths[currIndex]);
                        newPath.addLast(edge);
                        paths[neighborIndex] = newPath;

                        distances[neighborIndex] = accumulatedDistance;
                        times[neighborIndex] = accumulatedTime;

                        if (!unvisited.contains(neighborIndex)) {
                            unvisited.add(neighborIndex); // Add neighbor to unvisited
                        }
                    }
                }
                node = node.getNext(); // Move to the next edge
            }
        }

        hashTable.setAllVerticesToFalse(); // Reset the visited state of all vertices
        return null; // Return null if no path is found
    }

    // Method to find the index of the vertex with the minimum cost
    private int getMinCostIndex(List<Integer> indices, double[] costs) {
        int minIndex = -1;
        double minCost = Double.MAX_VALUE;

        for (int index : indices) {
            if (costs[index] < minCost) {
                minCost = costs[index];
                minIndex = index;
            }
        }

        return minIndex;
    }

    // Method to find the index of a vertex in the array
    private int findVertixIndex(Vertix vertix) {
        for (int i = 0; i < vertixArray.length; i++) {
            if (vertixArray[i] != null && vertixArray[i].equals(vertix)) {
                return i;
            }
        }
        return -1;
    }
}
