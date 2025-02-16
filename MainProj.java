package Project3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainProj extends Scene {
	private Graph graph;  // The graph object representing the network of countries and connections
    private static int numberOfVertices;  // Number of vertices (countries)
    private static int numberOfEdges;  // Number of edges (connections between countries)
    private final File file;  // File containing the country and edge data
    private int timeClicked = 0;  // Keeps track of clicks to select source and destination
    private BorderPane bp = new BorderPane();  // The layout container for the scene
    private final double mapWidth = 960;  // Width of the map to be displayed
    private final double mapHeight = 480;  // Height of the map to be displayed
    private final Stage stage;  // The stage for the JavaFX application
    private final Scene scene;  // The current scene for the application
    // ComboBoxes for selecting source, destination, and filter options 
    private final ComboBox<String> sourceComboBox = new ComboBox<>();
    private final ComboBox<String> destinationComboBox = new ComboBox<>();
    private final ComboBox<String> filterComboBox = new ComboBox<>();
    
    public MainProj(Stage stage, Scene scene, File file) {
        super(new BorderPane(), 1200, 650);  // Initialize the Scene with a BorderPane root
        this.stage = stage;
        this.scene = scene;
        this.bp = (BorderPane) this.getRoot();
        this.file = file;
        		
        readFile();  // Read data 
        SceneMap(); // Call the javafx method
    }

    public void readFile() {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line = bufferedReader.readLine();
            if (line == null || line.trim().isEmpty()) {
                throw new IllegalArgumentException("The file is empty or improperly formatted.");
            }

            line = line.trim();
            if (line.startsWith("\uFEFF")) {
                line = line.substring(1); // Remove BOM if present
            }

            // Read the first line containing the number of vertices and edges
            String[] firstLine = line.split(",");
            if (firstLine.length != 2) {
                throw new IllegalArgumentException("The first line must contain exactly two integers separated by a comma.");
            }

            numberOfVertices = Integer.parseInt(firstLine[0].trim());
            numberOfEdges = Integer.parseInt(firstLine[1].trim());
            graph = new Graph(numberOfVertices);

            for (int i = 0; i < numberOfVertices; i++) {
                line = bufferedReader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    throw new IllegalArgumentException("Insufficient vertex data in the file.");
                }

                String[] tokens = line.split(",");
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("Each vertex line must contain a name, latitude, and longitude. Error in line: " + line);
                }

                String countryName = tokens[0].trim();
                double latitude = Double.parseDouble(tokens[1].trim());
                double longitude = Double.parseDouble(tokens[2].trim());

                Country country = new Country(countryName, longitude, latitude);
                country.setX(((longitude + 180.0) / 360.0) * mapWidth);
                country.setY(((90.0 - latitude) / 180.0) * mapHeight);

                Vertix vertix = new Vertix(country);
                graph.addVertix(vertix);


                // Add country to combo boxes for the user interface
                sourceComboBox.getItems().add(countryName);
                destinationComboBox.getItems().add(countryName);
            }

            // Read the edge data
            int edgeCount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(",");
                if (tokens.length != 4) {
                    throw new IllegalArgumentException("Each edge line must contain source, destination, price, and time. Error in line: " + line);
                }

                String sourceName = tokens[0].trim();
                String destinationName = tokens[1].trim();
                double price = Double.parseDouble(tokens[2].trim().replace("$", "")); 
                double time = Double.parseDouble(tokens[3].trim());

                Vertix source = graph.getVertix(sourceName);
                Vertix destination = graph.getVertix(destinationName);

                if (source == null || destination == null) {
                    throw new IllegalArgumentException("Edge references undefined vertices: " + sourceName + " or " + destinationName);
                }

                Edge edge = new Edge(source, destination, price, time);
                source.getVertices().addLast(edge);
                edgeCount++;
            }

            // Verify the number of edges read
            if (edgeCount != numberOfEdges) {
                throw new IllegalArgumentException("Mismatch between declared number of edges and actual edges read. Expected: " + numberOfEdges + ", but read: " + edgeCount);
            }

            // Add filter options to combo box
            filterComboBox.getItems().addAll("Distance", "Price", "Time");

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("File format error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private ScrollPane getMap() {
        // Load the map image
        Image mapImage = new Image(getClass().getResource("/Project3/natural_earth.jpg").toExternalForm());

        // Create an ImageView to display the map
        ImageView map = new ImageView(mapImage);

        // Set the new map size with respect to the defined width and height
        map.setFitWidth(mapWidth);
        map.setFitHeight(mapHeight);
        map.setPreserveRatio(true); // Maintain aspect ratio of the image

        // Optionally, add a style class to the image (if you have CSS styling)
        map.getStyleClass().add("map-image");

        // Create a pane to hold the map image
        Pane pane = new Pane(map);

        // Add countries to combo boxes and display their locations on the map
        Vertix[] nodes = graph.getHashTable().getTable();
        for (Vertix vertix : nodes) {
            if (vertix != null) {
                String countryName = vertix.getCountry().getCountryName();
                double latitude = vertix.getCountry().getLatitude();
                double longitude = vertix.getCountry().getLongitude();

                // Convert latitude and longitude to map coordinates
                double x = (longitude + 180.0) / 360.0 * mapWidth;
                double y = (90.0 - latitude) / 180.0 * mapHeight;

                // Add circles to represent countries on the map
                Circle marker = new Circle(x, y, 5, Color.YELLOW);
                marker.setStroke(Color.BLACK);
                marker.setStrokeWidth(1);

                // Add labels to represent country names
                Label countryLabel = new Label(countryName);
                countryLabel.setTextFill(Color.BLACK);
                countryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                countryLabel.setLayoutX(x - 15);
                countryLabel.setLayoutY(y - 15);

                // Add click event to the marker
                marker.setOnMouseClicked(e -> {
                    // First click: Set source combo box value
                    if (timeClicked == 0) {
                        sourceComboBox.setValue(countryName);
                        timeClicked = 1;
                    }
                    // Second click: Set target combo box value
                    else if (timeClicked == 1) {
                    	destinationComboBox.setValue(countryName);
                        timeClicked = 0; // Reset click count after setting both source and target
                    }
                });

                // Add the marker and label to the pane
                pane.getChildren().addAll(marker, countryLabel);
            }
        }

        // Create a ScrollPane to make the map scrollable
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private void drawPathOnMap(LinkedList path, ScrollPane mapPane, double maxWidth, double maxHeight) {
        // Ensure mapContent is initialized and cast to Pane
        Pane mapContent = (Pane) mapPane.getContent();
        if (mapContent == null) {
            mapContent = new Pane();
            mapPane.setContent(mapContent);
        }

        for (int i = 0; i < path.getSize(); i++) {
            Edge edge = path.getNodeByIndex(i).getEdge();

            Vertix sourceVertex = edge.getSource();
            Vertix destinationVertex = edge.getDestination();

            Country sourceCountry = sourceVertex.getCountry();
            Country destinationCountry = destinationVertex.getCountry();

            // Calculate coordinates
            double x1 = ((sourceCountry.getLongitude() + 180) / 360) * maxWidth;
            double y1 = ((90 - sourceCountry.getLatitude()) / 180) * maxHeight;

            double x2 = ((destinationCountry.getLongitude() + 180) / 360) * maxWidth;
            double y2 = ((90 - destinationCountry.getLatitude()) / 180) * maxHeight;

            // Create and configure the line
            Line line = new Line();
            line.setStartX(x1);
            line.setStartY(y1);
            line.setEndX(x2);
            line.setEndY(y2);
            line.setManaged(false);
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);

            // Add the line to the pane
            mapContent.getChildren().add(line);
        }
    }

    private void SceneMap() {

        // Create a ScrollPane for the map
        ScrollPane mapPane = getMap();

        // Create a VBox to hold the map and any other components (if needed)
        VBox tableBox = new VBox(10, mapPane);
        tableBox.setAlignment(Pos.CENTER);

        // Create a title label for the map with yellow color
        Label titleLabel = new Label("Let's travel around the world! ✈🌎");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.YELLOW);  // Set the text color to yellow

        // Add the drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.GRAY);
        titleLabel.setEffect(dropShadow);

        // Add padding and background color to the title label
        titleLabel.setPadding(new Insets(10));
        titleLabel.setBackground(new Background(new BackgroundFill(
                Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY
        )));

        // Create a StackPane to wrap the label for better control over background and alignment
        StackPane titlePane = new StackPane(titleLabel);
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setPadding(new Insets(10));

        // Add a background to the StackPane
        titlePane.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(10), Insets.EMPTY
        )));

        // Center the label
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // Add the title label above the map
        VBox mapBox = new VBox(10, titlePane, tableBox);
        mapBox.setAlignment(Pos.CENTER);

        // Assuming bp is a BorderPane, set its padding and add the VBox as a left component
        bp.setPadding(new Insets(15));
        BorderPane.setMargin(mapBox, new Insets(0, 0, 0, 20));
        bp.setLeft(mapBox);

        // Create labels and combo boxes for source, target, and filter, with yellow labels
        Label sourceLabel = new Label("Source: ");
        sourceLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        sourceLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        sourceComboBox.setPrefWidth(200);
        sourceComboBox.setPromptText("Select a country");

        HBox sourceBox = new HBox(5, sourceLabel, sourceComboBox);
        sourceBox.setAlignment(Pos.CENTER);

        Label targetLabel = new Label("Target: ");
        targetLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        targetLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        destinationComboBox.setPrefWidth(200);
        destinationComboBox.setPromptText("Select a country");

        HBox targetBox = new HBox(5, targetLabel, destinationComboBox);
        targetBox.setAlignment(Pos.CENTER);

        Label filterLabel = new Label("Filter: ");
        filterLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        filterLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        filterComboBox.setPrefWidth(200);
        filterComboBox.setPromptText("Choose a way");

        HBox filterBox = new HBox(5, filterLabel, filterComboBox);
        filterBox.setAlignment(Pos.CENTER);

        // Create the "Run" button with yellow text
        Button runButton = new Button("Run");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setTextFill(Color.BLACK);  // Set button text to yellow

        // Create the path label and text area
        Label pathLabel = new Label("Path: ");
        pathLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        pathLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        TextArea pathArea = new TextArea();
        pathArea.setEditable(false);

        // Create distance label and text field
        Label distanceLabel = new Label("Distance: ");
        distanceLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        distanceLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        TextField distanceField = new TextField();
        distanceField.setEditable(false);

        // Create cost label and text field
        Label costLabel = new Label("Price: ");
        costLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        costLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        TextField costField = new TextField();
        costField.setEditable(false);

        // Create time label and text field
        Label timeLabel = new Label("Time: ");
        timeLabel.setTextFill(Color.YELLOW);  // Set label text to yellow
        timeLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, new CornerRadii(5), Insets.EMPTY // Set dark gray background for label
        )));
        TextField timeField = new TextField();
        timeField.setEditable(false);

        // Create a VBox to hold the right-side components
        VBox rightBox = new VBox(10, sourceBox, targetBox, filterBox, runButton, pathLabel, pathArea,
                distanceLabel, distanceField, costLabel, costField, timeLabel, timeField);

        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(10));

        // Add the rightBox to the right side of the BorderPane
        bp.setRight(rightBox);

     // Set the action for the "Run" button
        runButton.setOnAction(e -> {
            // Retrieve user inputs
            String source = sourceComboBox.getValue();
            String destination = destinationComboBox.getValue();
            String weightType = filterComboBox.getValue(); // e.g., "price", "time", "distance"

            // Validate user inputs
            if (source == null || destination == null || weightType == null) {
                pathArea.setText("Please select source, destination, and filter type.");
                return;
            }

            // Call the getResult method to find the shortest path based on the selected weight type
            LinkedList result = graph.getResult(163, source, destination, weightType);

            if (result == null || result.isEmpty()) {
                // Handle case where no path is found
                pathArea.setText("No path found between " + source + " and " + destination);
                return;
            }

            // Retrieve and display distance, price, and time details
            LinkedList distanceResult = graph.getResult(163, source, destination, "distance");
            LinkedList priceResult = graph.getResult(163,source, destination, "price");
            LinkedList timeResult = graph.getResult(163, source, destination, "time");

            if (distanceResult != null) {
                distanceField.setText(String.format("%.2f", distanceResult.getDistance()));
            } else {
                distanceField.setText("N/A");
            }

            if (priceResult != null) {
                costField.setText(String.format("%.2f", priceResult.getCost()));
            } else {
                costField.setText("N/A");
            }

            if (timeResult != null) {
                timeField.setText(String.format("%.2f", timeResult.getTime()));
            } else {
                timeField.setText("N/A");
            }

            // Display the path details
            pathArea.setText("");  // Clear the text area first
            StringBuilder pathString = new StringBuilder();
            LinkedList path = result;

            if (path != null && !path.isEmpty()) {
                LinkedListNode currentNode = path.getFirstNode();
                while (currentNode != null) {
                    Edge edge = currentNode.getEdge();

                    // Validate that edge details are correct
                    if (edge != null && edge.getSource() != null && edge.getDestination() != null) {
                        String sourceCountry = edge.getSource().getCountry().getCountryName();
                        String destinationCountry = edge.getDestination().getCountry().getCountryName();

                        if (!sourceCountry.equals(destinationCountry)) {
                            pathString.append("From ")
                                .append(sourceCountry)
                                .append(" to ")
                                .append(destinationCountry)
                                .append(" with ")
                                .append(weightType)
                                .append(": ")
                                .append(getEdgeWeight(edge, weightType))
                                .append("\n");
                        }
                    }
                    currentNode = currentNode.getNext();
                }

                // Display the path details in the pathArea
                pathArea.setText(pathString.toString());

                // Draw the path on the map
                drawPathOnMap(path, mapPane, mapWidth, mapHeight);
            } else {
                pathArea.setText("Path is empty or invalid.");
            }
        });
    }
        // Helper method to get the weight of an edge based on the selected criterion
        private String getEdgeWeight(Edge edge, String weightType) {
            switch (weightType.toLowerCase()) {
                case "distance":
                    return String.format("%.2f", edge.getDist());
                case "price":
                    return String.format("%.2f", edge.getPrice());
                case "time":
                    return String.format("%.2f", edge.getTime());
                default:
                    return "Unknown weight type";
            }
        }
}