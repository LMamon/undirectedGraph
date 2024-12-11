/*defines graph
Louis M. project4/11Dec
 */

import java.util.*;

//uses adjacency list
//using treeMap

public class Graph {
    private TreeMap<String, Vertex> vertexList;
    private TreeMap<String, Edge> edgeList;

    public Graph() { //constructor
        this.vertexList = new TreeMap<>();
        this.edgeList = new TreeMap<>();
    }

    public void addVertex(Vertex v) {
        String vlabel = genvName(vertexList.size());

        v.setName(vlabel);
        vertexList.put(vlabel, v);
        updateVertexNames();
    }

    public void removeVertex(String vLabel) {
        if (!vertexList.containsKey(vLabel)) {
            Vertex v = vertexList.remove(vLabel);

            edgeList.entrySet().removeIf(entry ->
                    entry.getValue().getStart().equals(v) || entry.getValue().getEnd().equals(v));
        }
    }

    private void updateVertexNames() {
        int idx = 0;
        TreeMap<String, Vertex> updatedVertexList = new TreeMap<>();

        for (Vertex v : vertexList.values()) {
            String newName = genvName(idx++); //gen new name
            updatedVertexList.put(newName, v); //add vertex to map with new name
        }
        //replace previous vertex list with updated one
        vertexList = updatedVertexList;
    }

    private String genvName(int idx) {
        StringBuilder label = new StringBuilder();
        while (idx >= 0) {
            label.insert(0, (char) ('A' + (idx % 26)));
            idx = (idx / 26) - 1;
        }
        return label.toString();
    }

    public String getNextName() {
        return genvName(vertexList.size());
    }

    public void addEdge(Vertex v1, Vertex v2) {
        String elabel = geneName(v1.getName(), v2.getName());
        if (!edgeList.containsKey(elabel)) {
            Edge edge = new Edge(v1, v2, elabel);
            edgeList.put(elabel, edge);
        } else {
            System.out.println("Edge already exists");
        }
    }

    public String geneName(String v1, String v2) {
        StringBuilder label = new StringBuilder();
        if (v1.compareTo(v2) < 0) {
            return v1 + v2;
        } else {
            return v2 + v1;
        }
    }

    public TreeMap<String, Vertex> getVertexList() {
        return vertexList;
    }

    public boolean isConnected() { //a graph is connected if all vertices are reachable
        if (vertexList.isEmpty()) {
            return true;
        }
        if (edgeList.isEmpty()) {
            return false;
        }

        Vertex sVertex = vertexList.values().iterator().next();
        LinkedHashSet<Vertex> visited = depthFirstSearch(sVertex);
        return visited.size() == vertexList.size();
    }

    public LinkedHashSet<Vertex> depthFirstSearch() {
        Vertex start = vertexList.get("A");
        if (start == null) {
            return new LinkedHashSet<>();
        }
        return depthFirstSearch(start);
    }

    public boolean hasCycle() { //check if graph has at least 1 cycle
        if (vertexList.isEmpty() || edgeList.isEmpty()) {
            return false;
        }
        LinkedHashSet<Vertex> visited = new LinkedHashSet<>();

        for (Vertex v : vertexList.values()) {
            if (!visited.contains(v)) {
                if (dfsCycle(v, null, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsCycle(Vertex v, Vertex parent, LinkedHashSet<Vertex> visited) { //checks for cycle
        visited.add(v);

        for (Edge e : edgeList.values()) { //check adjacent vertices
            Vertex adjVertex = null;
            if (e.getStart().equals(v)) {
                adjVertex = e.getEnd();
            } else if (e.getEnd().equals(v)) {
                adjVertex = e.getStart();
            }

            if (adjVertex != null) {
                if (!visited.contains(adjVertex)) {
                    //check neighbors that havent been visited
                    if (dfsCycle(adjVertex, v, visited)) {
                        return true;
                    }
                } else if (!adjVertex.equals(parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private LinkedHashSet<Vertex> depthFirstSearch(Vertex start) {
        LinkedHashSet<Vertex> visited = new LinkedHashSet<>();
        Stack<Vertex> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Vertex v = stack.pop();

            if (!visited.contains(v)) {
                visited.add(v);
                for (Edge e : edgeList.values()){ //push adjacent vertices to stack
                    if (e.getStart().equals(v) && !visited.contains(e.getEnd())) {
                        stack.push(e.getEnd());
                    } else if (e.getEnd().equals(v) && !visited.contains(e.getStart())) {
                        stack.push(e.getStart());
                    }
                }
            }
        }
        return visited;
    }

    public LinkedHashSet<Vertex> breadthFirstSearch() {
        Vertex start = vertexList.get("A");
        if (start == null) {
            return new LinkedHashSet<>();
        }
        return breadthFirstSearch(start);
    }

    private LinkedHashSet<Vertex> breadthFirstSearch(Vertex start) {
        LinkedHashSet<Vertex> visited = new LinkedHashSet<>();
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(start);

        while(!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (!visited.contains(v)) {
                visited.add(v);

                for (Edge e : edgeList.values()) {
                    if (e.getStart().equals(v) && !visited.contains(e.getEnd()) &&
                            !queue.contains(e.getEnd())) {
                        queue.add(e.getEnd());
                    } else if (e.getEnd().equals(v) && !visited.contains(e.getStart()) &&
                            !queue.contains(e.getStart())) {
                        queue.add(e.getStart());
                    }
                }
            }
        }
        return visited;
    }

}