package Project3;

public class HashNode {

    // The vertex stored in this hash node
    private Vertix data;

    // A flag to indicate the state of the node 
    private char flag;

    // Constructor to initialize a HashNode with a vertex and its state flag
    public HashNode(Vertix data, char flag) {
        this.data = data; // Assign the vertex to the node
        this.flag = flag; // Set the state flag
    }

    // Getter for the vertex stored in the hash node
    public Vertix getData() {
        return data;
    }

    // Getter for the flag that indicates the state of the node
    public char getFlag() {
        return flag;
    }

    // Setter to update the vertex stored in the hash node
    public void setData(Vertix data) {
        this.data = data;
    }

    // Setter to update the flag that indicates the state of the node
    public void setFlag(char flag) {
        this.flag = flag;
    }

    // Overridden toString method to return a string representation of the node
    // This returns the vertex's string representation
    @Override
    public String toString() {
        return this.data + "";
    }
}
