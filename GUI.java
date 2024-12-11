/*visually displays graph and buttons and text fields
Louis M. Project4/11Dec
 */

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.LinkedHashSet;

public class GUI extends Pane {
    private LinkedHashSet<Vertex> vertices;
    private LinkedHashSet<Edge> edges;

    private Graph graph;
    private Pane graphPane;
    private HBox topControl;
    private HBox bottomControl;
    private TextArea messageBox;

    public GUI() { //constructor
        this.graph = new Graph();
        this.vertices = new LinkedHashSet<>();
        this.edges = new LinkedHashSet<>();

        graphPane = new Pane();

        topControl = topControls();
        bottomControl = functionButtons();
        mouseHandler();
    }

    private void mouseHandler() { //used to add vertices when mouse clicks
        graphPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String nvName = graph.getNextName();
                Vertex v = new Vertex(event.getX(), event.getY(), nvName);
                //add v to vertex and graph
                graph.addVertex(v);
                vertices.add(v);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                //remove vertex that get right-clicked
                Vertex clickedVertex = getClickedVertex(event.getX(),event.getY());
                if (clickedVertex != null) {
                    graph.removeVertex(clickedVertex.getName());
                    vertices.remove(clickedVertex);
                    edges.removeIf(edge -> edge.getStart().equals(clickedVertex) ||
                            edge.getEnd().equals(clickedVertex));
                }
            }
            updateGraph();
        });
    }

    private void updateVertices() { //add Vertices to GUI
        for (Vertex v : vertices) {
            Circle circle = new Circle(v.getX(), v.getY(), 5);


            //add label
            Text label = new Text(v.getX(), v.getY() -15, v.getName());
            graphPane.getChildren().addAll(circle, label);

        }
    }

    private void updateEdges() { //Updates edges displayed in GUI after button is pressed(?)
        for (Edge e : edges) {
            Line line = new Line(e.getStart().getX(), e.getStart().getY(),
                    e.getEnd().getX(), e.getEnd().getY());
            graphPane.getChildren().add(line);
        }
    }

    private Vertex getClickedVertex(double x, double y) {
        for (Vertex v : vertices) {
            double dx = x - v.getX();
            double dy = y - v.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance <= 10) { // Adjust this threshold to match the size of your vertex markers
                return v;
            }
        }
        return null; // No vertex was clicked
    }

    private void updateGraph() {
        graphPane.getChildren().clear();
        updateVertices();
        updateEdges();
    }

    private void setMessage(String message) {
        if (messageBox != null) {
            messageBox.setText(message);
        }
    }

    private HBox functionButtons() {
        Button depthFirstButton = new Button("Depth First Search");
        Button breadthFirstButton = new Button("Breadth First Search");
        Button hasCycleButton = new Button("Has Cycle?");
        Button isConnectedButton = new Button("Is Connected?");

        isConnectedButton.setOnAction(event -> {
            //check if graph is connected
            setMessage(graph.isConnected() ? "Connected" : "Not Connected");
        });
        depthFirstButton.setOnAction(event -> {
            LinkedHashSet<Vertex> dList = graph.depthFirstSearch();
            setMessage("DFS order: " + dList);
        });
        breadthFirstButton.setOnAction(event -> {
            LinkedHashSet<Vertex> bList = graph.breadthFirstSearch();
            setMessage("BFS order: " + bList);
        });
        hasCycleButton.setOnAction(event -> {
            setMessage(graph.hasCycle() ? "Has Cycles" : "No Cycles");
        });

        messageBox = new TextArea();
        messageBox.setPrefRowCount(1);
        messageBox.setEditable(false);
        messageBox.setFocusTraversable(false);
        HBox box = new HBox(10, isConnectedButton, hasCycleButton,
                depthFirstButton, breadthFirstButton, messageBox);
        box.setPadding(new Insets(10));
        return box;
    }

    private HBox topControls() {
        TextField v1NameField = new TextField();
        TextField v2NameField = new TextField();

        v1NameField.setPromptText("Vertex 1");
        v2NameField.setPromptText("Vertex 2");

        Button addEdge = new Button("Add Edge");
        addEdge.setOnAction(event -> {
            String v1Name = v1NameField.getText().trim().toUpperCase();
            String v2Name = v2NameField.getText().trim().toUpperCase();
            if (v1Name.isEmpty() || v2Name.isEmpty()) {
                setMessage("Please input both vertices");
                return;
            }
            if (graph.getVertexList().containsKey(v1Name) && graph.getVertexList().containsKey(v2Name)) {
                Vertex v1 = graph.getVertexList().get(v1Name);
                Vertex v2 = graph.getVertexList().get(v2Name);

                graph.addEdge(v1, v2);
                edges.add(new Edge(v1, v2, graph.geneName(v1.getName(), v2.getName())));
                updateGraph();
            } else {
                setMessage("Vertex not found");
            }
        });
        HBox box = new HBox(10, addEdge, v1NameField, v2NameField);
        box.setPadding(new Insets(10));
        return box;
    }

    public Pane getGraphPane() {
        return graphPane;
    }

    public HBox getEdgeControls() {
        return topControl;
    }

    public HBox buttonAndMessage() {
        return bottomControl;
    }

}