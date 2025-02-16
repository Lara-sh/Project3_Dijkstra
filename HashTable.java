package Project3;

public class HashTable {
    // The size of the hash table 
    private int tableSize;

    // The array that represents the hash table, storing Vertix objects
    private Vertix[] table;

    // Constructor to initialize the hash table with the specified size
    public HashTable(int tableSize) {
        this.tableSize = tableSize; // Set the table size
        this.table = new Vertix[tableSize]; // Create an array to store vertices
        // Initialize each slot in the table to null
        for (int i = 0; i < tableSize; i++) {
            table[i] = null;
        }
    }

    // Method to insert a vertex into the hash table
    public void put(Vertix value) {
        // Compute the hash index using the vertex's country name
        int hash = getHash(value.getCountry().getCountryName());

        // Handle collisions using linear probing
        while (table[hash] != null &&
                !table[hash].getCountry().getCountryName().equals(value.getCountry().getCountryName())) {
            // Move to the next slot (circularly) until an empty slot is found
            hash = (hash + 1) % tableSize;
        }

        // Insert the vertex into the calculated slot
        table[hash] = value;
    }

    // Method to retrieve a vertex (Vertix) by its country name
    public Vertix getVertex(String key) {
        // Compute the hash index for the key
        int hash = getHash(key);

        // Search for the vertex using linear probing
        while (table[hash] != null &&
                !table[hash].getCountry().getCountryName().equals(key)) {
            // Move to the next slot (circularly)
            hash = (hash + 1) % tableSize;
        }

        // If the slot is empty, return null; otherwise, return the vertex
        return table[hash] == null ? null : table[hash];
    }

    // Method to get the index of a vertex in the hash table by its country name
    public int getVertexIndex(String key) {
        // Compute the hash index for the key
        int hash = getHash(key);

        // Search for the vertex using linear probing
        while (table[hash] != null &&
                !table[hash].getCountry().getCountryName().equals(key)) {
            hash = (hash + 1) % tableSize;
        }

        // If the slot is empty, return -1; otherwise, return the hash index
        return table[hash] == null ? -1 : hash;
    }

    // Private helper method to compute the hash value for a given key
    private int getHash(String key) {
        int hash = key.hashCode() % tableSize; // Compute hash using Java's hashCode method
        if (hash < 0) {
            hash += tableSize; // Ensure the hash is non-negative
        }
        return hash;
    }

    // Getter for the table size
    public int getTableSize() {
        return tableSize;
    }

    // Getter for the hash table array
    public Vertix[] getTable() {
        return table;
    }

    // Method to reset the visited state of all vertices in the table
    public void setAllVerticesToFalse() {
        for (Vertix vertix : table) {
            if (vertix != null) {
                vertix.setVisited(false); // Mark each vertex as unvisited
            }
        }
    }
}
