package Project3;

public class LinkedList {

    private LinkedListNode first;
    private int size; // Number of elements in the list
    private Vertix vertix;
    private double cost;
    private double distance; // Accumulated distance
    private double time;     // Accumulated time
    private LinkedList path; // Path as a linked list

    // Default constructor
    public LinkedList() {
        this.first = null;
        this.size = 0;
        this.path = null; // Avoid circular reference
    }

    // Getters and setters
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LinkedList getPath() {
        // Ensure path is not null before returning
        if (this.path == null) {
            this.path = new LinkedList();
        }
        return this.path;
    }

    public void setPath(LinkedList path) {
        this.path = path;
    }

    public Vertix getVertix() {
        return vertix;
    }

    public void setVertix(Vertix vertix) {
        this.vertix = vertix;
    }

    public LinkedListNode getFirstNode() {
        return first;
    }

    public void setFirstNode(LinkedListNode first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return first == null;
    }

    // Add an edge to the start of the list
    public void addFirst(Edge edge) {
        LinkedListNode temp = new LinkedListNode(edge);
        if (first == null) {
            first = temp;
        } else {
            temp.setNext(first);
            first = temp;
        }
        size++;
    }

    // Add an edge to the end of the list
    public void addLast(Edge edge) {
        LinkedListNode temp = new LinkedListNode(edge);
        if (first == null) {
            first = temp;
        } else {
            LinkedListNode current = first;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(temp);
        }
        size++;
    }

    // Get an edge by its name
    public Edge get(String name) {
        LinkedListNode current = getNode(name);
        return current == null ? null : current.getEdge();
    }

    // Get a node by its name
    public LinkedListNode getNode(String name) {
        LinkedListNode current = first;
        while (current != null) {
            if (current.getEdge().getDestination().getCountry().getCountryName().equals(name)) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    // Get the edge at the first node
    public Edge getFirst() {
        return first == null ? null : first.getEdge();
    }

    // Get the edge at the last node
    public Edge getLast() {
        if (first == null) return null;

        LinkedListNode current = first;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getEdge();
    }

    // Print the list
    public void printList() {
        LinkedListNode current = first;
        while (current != null) {
            System.out.println(current.getEdge());
            current = current.getNext();
        }
    }

    // Add all elements from another LinkedList to this list
    public void addAll(LinkedList list) {
        if (list == null || list.isEmpty()) return;

        LinkedListNode current = list.getFirstNode();
        while (current != null) {
            this.addLast(current.getEdge());
            current = current.getNext();
        }
    }

    // Get a node by its index
    public LinkedListNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        LinkedListNode current = first;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    // Get a copy of the current path
    public LinkedList getPath2() {
        if (this.path == null) {
            return new LinkedList(); // Return empty LinkedList if path is null
        }

        LinkedList pathCopy = new LinkedList();
        LinkedListNode currentNode = this.path.getFirstNode();
        while (currentNode != null) {
            pathCopy.addLast(currentNode.getEdge());
            currentNode = currentNode.getNext();
        }
        return pathCopy;
    }
}