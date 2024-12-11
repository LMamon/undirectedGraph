//defines edge class
//Louis M. Project4/11Dec

public class Edge {
    private Vertex start;
    private Vertex end;
    private String label;

    public Edge(Vertex start, Vertex end, String label) {
        this.start = start;
        this.end = end;
        this.label = label;
    }
    public Vertex getStart() {
        return start;
    }
    public Vertex getEnd() {
        return end;
    }

}