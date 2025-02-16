package Project3;

public class LinkedListNode {
    private Edge edge;  // The edge stored in this node, representing a connection between two vertices
    private LinkedListNode next;  // Pointer to the next node in the linked list

    // Constructor to create a new node with a given edge
    public LinkedListNode(Edge edge) {
        this.edge = edge;  
    }

    // Getter method to retrieve the edge stored in the node
    public Edge getEdge() {
        return edge; 
    }

    // Setter method to assign a new edge to this node
    public void setEdge(Edge edge) {
        this.edge = edge; 
    }

    // Getter method to retrieve the next node in the linked list
    public LinkedListNode getNext() {
        return next;  
    }

    // Setter method to assign the next node in the list
    public void setNext(LinkedListNode next) {
        this.next = next;  
    }
}
