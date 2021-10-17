import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Graph {
    private ArrayList<ArrayList<Boolean>> graph;
    private int size;
    private final int maxEdgesPerNode;
    private final int maxGlobalEdges;
    private int totalEdgeCount;

    public Graph(int size, int maxEdgesPerNode, int maxGlobalEdges) {
        this.size = size;
        this.graph = new ArrayList<ArrayList<Boolean>>(size);

        setEmptyGraph();

        this.maxEdgesPerNode = maxEdgesPerNode;
        this.maxGlobalEdges = maxGlobalEdges;
    }

    private void setEmptyGraph() {
        ArrayList<Boolean> newArray = new ArrayList<Boolean>(size);

        for (int i : range(0, size, false)) {
            newArray.add(false);
        }

        for (int i : range(0, size, false)) {
            this.graph.add((ArrayList<Boolean>) newArray.clone());
        }
    }

    /*
    private ArrayList<int[]> getUnmatchedPairs() {
        ArrayList<int[]> pairs = new ArrayList<>();

        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (!edgeExists(i, j)) {
                    pairs.add(new int[] { i, j });
                }
            }
        }

        Collections.shuffle(pairs);

        return pairs;
    }
    */

    public void addRandomEdges(double probability) {
        for (int i: range(0, size, false)) {
            for (int j: range(i + 1, size, false)) {
                if (Math.random() < probability) {
                    addEdge(i, j);
                }
            }
        }

        


        // ArrayList<int[]> pairs = getUnmatchedPairs();

        // for (int[] pair : pairs) {
        //     if (Math.random() <= probability) {
        //         addEdge(pair[0], pair[1]);
        //     }
        // }
    }

    /*
    public void addNeighborEdges(double probability) {
        for (int i : range(0, size, true)) {
            HashSet<Integer> neighbors = getNeighbors(i);
            HashSet<Integer> neighborsOfNeighbors = new HashSet<>();

            for (Integer neighbor : neighbors) {
                neighborsOfNeighbors.addAll(getNeighbors(neighbor));
            }

            if (neighborsOfNeighbors.contains(i)) {
                neighborsOfNeighbors.remove(i);
            }

            for (Integer neighbor : neighborsOfNeighbors) {
                if (Math.random() <= probability) {
                    addEdge(i, neighbor);
                }
            }
        }
    }
    */

    public HashSet<Integer> getNeighbors(int vertex) {
        HashSet<Integer> neighbors = new HashSet<Integer>();
        for (int i = 0; i < size; i++) {
            if (graph.get(vertex).get(i)) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    public List<Integer> range(int start, int end, boolean shuffle) {
        List<Integer> numbers = IntStream.rangeClosed(start, end - 1).boxed().collect(Collectors.toList());
        if (shuffle) {
            Collections.shuffle(numbers);
        }
        return numbers;
    }

    public void addEdge(int vertex1, int vertex2) {
        if (!edgeExists(vertex1, vertex2) && totalEdgeCount < maxGlobalEdges && degree(vertex1) < maxEdgesPerNode
                && degree(vertex2) < maxEdgesPerNode) {
            graph.get(vertex1).set(vertex2, true);
            graph.get(vertex2).set(vertex1, true);
            totalEdgeCount++;
        }
    }

    public void removeEdge(int vertex1, int vertex2) {
        if (edgeExists(vertex1, vertex2)) {
            graph.get(vertex1).set(vertex2, false);
            graph.get(vertex2).set(vertex1, false);
            totalEdgeCount--;
        }
    }

    public boolean edgeExists(int vertex1, int vertex2) {
        return graph.get(vertex1).get(vertex2) && graph.get(vertex2).get(vertex1);
    }

    public int getSize() {
        return size;
    }

    // Statistics
    public int degree(int vertex) {
        int count = 0;
        for (Boolean value : graph.get(vertex)) {
            if (value) {
                count++;
            }
        }
        return count;
    }

    public int getIsolatedCount() {
        int count = 0;
        for (int i : range(0, size, false)) {
            if (degree(i) == 0) {
                count++;
            }
        }
        return count;
    }

    public void removeIsolated() {
        ArrayList<Integer> isolated = new ArrayList<Integer>(size/2);

        for (int i : range(0, size, false)) {
            if (degree(i) == 0) {
                isolated.add(i);
            }
        }

        for (int i = size - 1; i>= 0; i--) {
            if (isolated.contains(i)) {
                graph.remove(i);
            }
        }

        for (ArrayList<Boolean> list : graph) {
            for (int i = size - 1; i>= 0; i--) {
                if (isolated.contains(i)) {
                    list.remove(i);
                }
            }
        }
        this.size = graph.size();
    }

    public double averageDegree() {
        return Double.parseDouble(String.format("%f", 2.0 * totalEdgeCount / (double) size));
    }

    public int numberOfConnectedComponents() {
        boolean[] visited = new boolean[size];
        int count = 0;
        for (int vertex : range(0, size, false)) {
            if (!visited[vertex]) {
                dfs(vertex, visited);
                count++;
            }
        }
        return count;
    }

    private void dfs(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for (int neighbor : getNeighbors(vertex)) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited);
            }
        }
    }

    private int[] bfs_dist(int source) {
        int[] distance = new int[size];
        for (int i : range(0, size, false)) {
            distance[i] = Integer.MAX_VALUE;
        }

        ArrayList<Integer> queue = new ArrayList<Integer>(size);

        queue.add(source);
        distance[source] = 0;

        while (!queue.isEmpty()) {
            int curr = queue.remove(0);

            for (int neighbor : getNeighbors(curr)) {
                if (distance[neighbor] == Integer.MAX_VALUE) {
                    distance[neighbor] = distance[curr] + 1;
                    queue.add(neighbor);
                }
            }
        }

        return distance;
    }

    private int[][] shortestPathPairs() {
        int[][] pairs = new int[size][size];

        for (int i : range(0, size, false)) {
            pairs[i] = bfs_dist(i);
        }

        return pairs;
    }

    /*
    public double averageShortestPathLength() {
        // not accurate
        int[][] shortestPathPairs = shortestPathPairs();
        int sumShortestPathPairs = 0;
        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (shortestPathPairs[i][j] != Integer.MAX_VALUE) {
                    sumShortestPathPairs += shortestPathPairs[i][j];
                }
            }
        }
        return Double.parseDouble(String.format("%f", ((double) sumShortestPathPairs) / (size * (size - 1))));
    }
    */

    public double density() {
        return Double.parseDouble(String.format("%f", (2.0 * totalEdgeCount) / (size * (size - 1))));
    }

    public int cycleBasisCount() {
        // it is just edges minus vertices plus connected components
        return totalEdgeCount - (size - numberOfConnectedComponents());
    }

    public double clusteringCoefficientNode(int vertex) {
        HashSet<Integer> neighborsTotal = getNeighbors(vertex);
        HashSet<Integer> neighborsLeft = new HashSet<Integer>(neighborsTotal);
        int neighborEdgeCount = 0;
        for (int neighbor1 : neighborsTotal) {
            neighborsLeft.remove(neighbor1);
            for (int neighbor2 : neighborsLeft) {
                if (edgeExists(neighbor1, neighbor2)) {
                    neighborEdgeCount++;
                }
            }
        }
        int degreeVertex = degree(vertex);
        return neighborEdgeCount == 0 ? 0 : (2.0 * neighborEdgeCount) / (degreeVertex * (degreeVertex - 1));
    }

    public double clusteringCoefficientAverage() {
        double sum = 0;
        for (int vertex : range(0, size, false)) {
            sum += clusteringCoefficientNode(vertex);
        }
        return Double.parseDouble(String.format("%f", sum / size));
    }

    public int diameter() {
        int[][] shortestPathPairs = shortestPathPairs();
        int maxShortest = 0;
        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (shortestPathPairs[i][j] != Integer.MAX_VALUE && shortestPathPairs[i][j] > maxShortest) {
                    maxShortest = shortestPathPairs[i][j];
                }
            }
        }
        return maxShortest;
    }

    
    public int[] edgeDistribution() {
        int[] edgeDistribution = new int[totalEdgeCount + 1];
        for (int i : range(0, size, false)) {
            edgeDistribution[getNeighbors(i).size()] += 1;
        }
        return edgeDistribution;
    }
    
    /*
    public double modularity(Agent[] agents) {
        if (totalEdgeCount == 0) {
            return 0.0;
        }

        double sum = 0;
        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (agents[i].averageWeightedInterestCategory() == agents[j].averageWeightedInterestCategory()) {
                    sum += ((edgeExists(i, j) ? 1 : 0) - (degree(i) * degree(j) / (2 * totalEdgeCount)));
                }
            }
        }
        return Double.parseDouble(String.format("%f", sum / totalEdgeCount));
    }
    */

    public double assortativityCoefficient() {
        ArrayList<double[]> pairs = new ArrayList<>();

        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (edgeExists(i, j)) {
                    pairs.add(new double[] { degree(i), degree(j) });
                }
            }
        }

        double xBar = pairAverage(pairs, 0);
        double yBar = pairAverage(pairs, 1);

        double q = 0, pSqrd = 0, rSqrd = 0;

        int pos = 0;

        for (int i : range(0, size, false)) {
            for (int j : range(i + 1, size, false)) {
                if (edgeExists(i, j)) {
                    double xi = pairs.get(pos)[0], yi = pairs.get(pos)[1];

                    q += (xi - xBar) * (yi - yBar);
                    pSqrd += Math.pow((xi - xBar), 2);
                    rSqrd += Math.pow((yi - yBar), 2);

                    pos += 1;
                }
            }
        }
        return Double.parseDouble(String.format("%f", q / (Math.sqrt(pSqrd) * Math.sqrt(rSqrd))));
    }

    private double pairAverage(ArrayList<double[]> pairs, int type) {
        double sum = 0;
        for (double[] pair : pairs) {
            sum += pair[type];
        }
        return sum / pairs.size();
    }


}