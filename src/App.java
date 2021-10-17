
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class App {
    public static void main(String[] args) throws Exception {
        // ArrayList<ArrayList<Integer>> list = matrixFromFile();
        // Graph bearman = new Graph(list.size(), Integer.MAX_VALUE, Integer.MAX_VALUE);
        // for (int i = 0; i < list.size(); i++) {
        //     for (int j = i + 1; j < list.size(); j++) {
        //         if (list.get(i).get(j) == 1) {
        //             bearman.addEdge(i, j);
        //         }
        //     }
        // }
        // // System.out.println(bearman.averageDegree());
        // // System.out.println(bearman.numberOfConnectedComponents());
        // // System.out.println(bearman.averageShortestPathLength());
        // // System.out.println(bearman.density());
        // // System.out.println(bearman.cycleBasisCount());
        // // System.out.println(bearman.clusteringCoefficientAverage());
        // // System.out.println(bearman.diameter());
        //System.out.println(bearman.assortativityCoefficient());
        // int[] dist = bearman.edgeDistribution();
        // BufferedWriter writer = new BufferedWriter(new FileWriter("dist.txt", false));
        // for (int i = 0; i < dist.length; i++) {

        //     writer.write(i + " " + dist[i]);
        //     writer.newLine();

            
        // }
        //writer.close();
        //exportNetwork(bearman);
        


        
        SimulationCollection c = new SimulationCollection();
        c.addSimulations();


        c.exportAverageDegree();
        c.exportNumberOfConnectedComponents();
        //c.exportAverageShortestPathLength();
        c.exportDensity();
        c.exportCycleBasisCount();
        c.exportClusteringCoefficientAverage();
        c.exportDiameter();
        c.exportEdgeDistribution();
        //c.exportModularity();
        c.exportAssortativityCoefficient();
        c.exportNodeCount();
        
    }

    public static ArrayList<ArrayList<Integer>> matrixFromFile() throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
        Scanner input = new Scanner(new File("bearman.txt"));
        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            ArrayList<Integer> col = new ArrayList<Integer>();
            while (colReader.hasNextInt()) {
                col.add(colReader.nextInt());
            }
            a.add(col);
        }
        return a;
    }

    public static void exportNetwork(Graph graph) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("bearman.net", false));
        writer.write("*Vertices 573");
        writer.newLine();

        writer.write("*Edges");
        writer.newLine();
        for (int i = 0; i < graph.getSize(); i++) {
            for (int j = i + 1; j < graph.getSize(); j++) {
                if (graph.edgeExists(i, j)) {
                    writer.write((i + 1) + " " + (j + 1));
                    writer.newLine();
                }
            }
        }
        writer.close();
    }
}
