package Project3;

public class Country {

    // Name of the country
    private String countryName;

    // Geographical longitude and latitude of the country
    private double longitude;
    private double latitude;

    // Cartesian coordinates (x, y) for additional calculations 
    private double x;
    private double y;

    // Constructor to initialize a Country with its name, latitude, and longitude
    public Country(String countryName, double longitude, double latitude) {
        setCountryName(countryName); // Set the country's name
        setLatitude(latitude);      // Set the latitude
        setLongitude(longitude);    // Set the longitude
    }

    // Getter for country name
    public String getCountryName() {
        return countryName;
    }

    // Setter for country name
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    // Getter for longitude
    public double getLongitude() {
        return longitude;
    }

    // Setter for longitude
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Getter for latitude
    public double getLatitude() {
        return latitude;
    }

    // Setter for latitude
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter for the x-coordinate in Cartesian space
    public double getX() {
        return x;
    }

    // Setter for the x-coordinate in Cartesian space
    public void setX(double x) {
        this.x = x;
    }

    // Getter for the y-coordinate in Cartesian space
    public double getY() {
        return y;
    }

    // Setter for the y-coordinate in Cartesian space
    public void setY(double y) {
        this.y = y;
    }
}
