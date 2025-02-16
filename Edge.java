package Project3;

public class Edge {
    private Vertix source;
    private Vertix destination;
    private double price; // Represents price.
    private double time;  // Represents time.
    private double dist;  // Represents distance.

    // Constructor to initialize source, destination, and weights
    public Edge(Vertix source, Vertix destination, double price, double time) {
        this.source = source;
        this.destination = destination;
        this.price = price;
        this.time = time;  
        this.dist = calculateDistance(); // Calculate the distance using Haversine formula
    }

    public Edge(Vertix source, Vertix destination) {
        this.source = source;
        this.destination = destination;
        this.dist = calculateDistance(); // Calculate the distance using Haversine formula
    }
   
   

    // Haversine formula to calculate distance
    private double calculateDistance() {
        double latSource = Math.toRadians(source.getCountry().getLatitude());
        double longSource = Math.toRadians(source.getCountry().getLongitude());
        double latDest = Math.toRadians(destination.getCountry().getLatitude());
        double longDest = Math.toRadians(destination.getCountry().getLongitude());

        double diffLat = latDest - latSource;
        double diffLong = longDest - longSource;

        double a = Math.pow(Math.sin(diffLat / 2), 2) +
                   Math.cos(latSource) * Math.cos(latDest) *
                   Math.pow(Math.sin(diffLong / 2), 2);

        double radius = 6371; // Earth's radius in kilometers
        return 2 * radius * Math.asin(Math.sqrt(a));
    }

    // Getters and Setters
    public Vertix getSource() {
        return source;
    }

    public void setSource(Vertix source) {
        this.source = source;
    }

    public Vertix getDestination() {
        return destination;
    }

    public void setDestination(Vertix destination) {
        this.destination = destination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }


}