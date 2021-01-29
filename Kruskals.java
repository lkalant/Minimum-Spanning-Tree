//  Levon Kalantarian
//  Uses book author's unmodified DisjSets.java class, and provided csv file, both included
//  Prints the initial edges, then edges with Kruskal's algorithm with the numbers of cities, edges, and total distances

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Kruskals {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("input.csv");
        Scanner filesScanner = new Scanner(file);

        Comparator<Edge> comparator = new edgeCompare();
        Hashtable<String, Integer> cities = new Hashtable<>(30);        // create a hash table with 30 initial capacity to look up city numbers by names.
        PriorityQueue<Edge> pq = new PriorityQueue<>(comparator);                  // create a priority queue for set of edges

        int cityCount = 0;
        int numEdges = 0;
        int totalEdgeValue = 0;

        System.out.println("\n===========Before Algorithm===========");
        System.out.println("Cities and distances");

        while (filesScanner.hasNextLine()) {
            String inputLine = filesScanner.nextLine();
            String[] lineStringArray = inputLine.split(",");                // parse new line into an array of strings

            cities.put(lineStringArray[0], cityCount++);                          // add first city from the new line as a new city to list of vertices

            for (int i = 2; i < lineStringArray.length; i += 2) {
                char nextChar = lineStringArray[i].charAt(0);
                if (Character.isDigit(nextChar)) {                                // make sure it's a number, then add to queue as new edge with distance from first vertex
                    int edgeValue = Integer.parseInt(lineStringArray[i]);
                    pq.add(new Edge(edgeValue, lineStringArray[0], lineStringArray[i - 1]));
                    totalEdgeValue += edgeValue;
                    System.out.println(++numEdges + "    " + lineStringArray[0] + ", " + lineStringArray[i - 1] + ", " + edgeValue);
                } else {                                                          // in case not a number, display error
                    System.out.println("Read error: line " + cityCount + ", item " + i + ":  Did not find expected digit");
                }
            }
        }

        System.out.println();
        System.out.println("Number of edges: " + numEdges);
        System.out.println("Number of cities: " + cities.size());
        System.out.println("Sum of all distances: " + totalEdgeValue);
        System.out.println();

        System.out.println("===========With Algorithm===========");
        System.out.println("Cities and distances:");
        kruskal(pq, cities);
    }

    static class edgeCompare implements Comparator<Edge> {
        public int compare(Edge a, Edge b) {
            return (a.getEdgeValue()) - (b.getEdgeValue());
        }
    }

    public static void kruskal(PriorityQueue<Edge> pq, Hashtable<String, Integer> cities) {

        int edgesAccepted = 0;                                      // start with no edges in our graph or tree yet
        DisjSets ds = new DisjSets(cities.size());                  // make a disjoint set for the number of vertices
        Edge e;

        int numFinalEdges = 0;
        int totalFinalEdgeValue = 0;

        while (edgesAccepted < cities.size() - 1) {
            e = pq.remove();                                        // get minimum edge = (u,v)
            int uset = ds.find(cities.get(e.getVertex1()));         // find set vertex 1 is in.      find on first vertex
            int vset = ds.find(cities.get(e.getVertex2()));         // find set vertex 2 is in.      find on second vertex

            if (uset != vset) {                                     // if not same set (not yet connected)
                edgesAccepted++;                      // accept the edge
                int newRoot = ds.union(uset, vset);   // connect them               modified DisjSets union function to return new root after union, to avoid running find later to find it

                totalFinalEdgeValue += e.getEdgeValue();
                System.out.println(++numFinalEdges + "    " + e.getVertex1() + ", " + e.getVertex2() + ", " + e.getEdgeValue());

                // update root after the merge
//                int newRoot = ds.find(uset);
                cities.replace(e.getVertex1(), newRoot);
                cities.replace(e.getVertex2(), newRoot);
            }
        }
        System.out.println();
        System.out.println("Number of edges: " + numFinalEdges);
        System.out.println("Number of cities: " + cities.size());
        System.out.println("Sum of all distances: " + totalFinalEdgeValue);
    }
}

class Edge {

    private int edgeValue;
    private String vertex1, vertex2;

    public Edge(int edgeValue, String vertex1, String vertex2) {
        this.edgeValue = edgeValue;
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public int getEdgeValue() {
        return edgeValue;
    }

    public String getVertex1() {
        return vertex1;
    }

    public String getVertex2() {
        return vertex2;
    }
}