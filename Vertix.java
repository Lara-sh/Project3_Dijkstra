package Project3;

public class Vertix {
    private Country country;
    private LinkedList vertices = new LinkedList();
    private boolean visited;

    // Constructor
    public Vertix(Country country) {
        this.country = country;
        this.visited = false;
    }

    // Getter for Country
    public Country getCountry() {
        return country;
    }

    // Setter for Country
    public void setCountry(Country country) {
        this.country = country;
    }

    // Getter for Vertices (Edges)
    public LinkedList getVertices() {
        return vertices;
    }

    // Setter for Vertices (Edges)
    public void setVertices(LinkedList vertices) {
        this.vertices = vertices;
    }

    // Getter for Visited
    public boolean isVisited() {
        return visited;
    }

    // Setter for Visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    // Override hashCode to ensure correct usage in hash-based collections
    @Override
    public int hashCode() {
        return country.getCountryName().hashCode();
    }

    // Override equals to ensure correct comparison of Vertix objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertix vertix = (Vertix) obj;
        return country.getCountryName().equals(vertix.country.getCountryName());
    }
}