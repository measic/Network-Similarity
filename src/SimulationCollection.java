import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class SimulationCollection {
    int numTrials = 10_000;
    String simulationsName = "Baseline";

    // default parameters
    int agentNumber = 832;
    int numberOfInterests = 10;
    int rangePerInterest = 2;
    int levelOfInterestNeeded = 7;
    int maxEdgesPerNode = Integer.MAX_VALUE;
    int maxGlobalEdges = Integer.MAX_VALUE;
    int daysToRun = 6;
    double probRandomEdges = (2.0*477)/(832*831);



    boolean exportNetwork = false;

    // data
    private double[] averageDegree = new double[numTrials];
    private int[] numberOfConnectedComponents = new int[numTrials];
    //private double[] averageShortestPathLength = new double[numTrials];
    private double[] density = new double[numTrials];
    private int[] cycleBasisCount = new int[numTrials];
    private double[] clusteringCoefficientAverage = new double[numTrials];
    private int[] diameter = new int[numTrials];
    private ArrayList<int[]> edgeDistribution = new ArrayList<int[]>();
    //private double[] modularity = new double[numTrials];
    private double[] assortativityCoefficient = new double[numTrials];
    private int[] nodeCount = new int[numTrials];

    public void addSimulations() throws Exception {
        int aveNodeCount = 0;
        double aveDegree = 0;
        double aveDensity = 0;
        int aveCycle = 0;
        double aveClust = 0;
        int aveDiameter = 0;
        double aveAssort = 0;
        int aveComp = 0;

        for (int i = 0; i < numTrials; i++) {
            // change parameters here

            //
            Simulation sim = new Simulation(i + simulationsName, agentNumber, numberOfInterests, rangePerInterest,
                    levelOfInterestNeeded, maxEdgesPerNode, maxGlobalEdges, daysToRun, probRandomEdges
                    //probNeighborEdges
                    );
            sim.conductSimulation();
            sim.graph.removeIsolated();
            if (exportNetwork) {sim.exportNetwork();}
            conductAnalysis(sim, i);

            System.out.println("Conducted " + (i + 1) + "/" + numTrials + " simulations.");

            aveNodeCount += sim.graph.getSize();
            aveDegree += averageDegree[i];
            aveDensity += density[i];
            aveCycle += cycleBasisCount[i];
            aveClust += clusteringCoefficientAverage[i];
            aveDiameter += diameter[i];
            aveAssort += assortativityCoefficient[i];
            aveComp += numberOfConnectedComponents[i];

            System.out.println("Node: " + aveNodeCount / (i+1.0));
            System.out.println("Deg: " +aveDegree / (i+1.0));
            System.out.println("Dens: " +aveDensity / (i+1.0));
            System.out.println("Cycle: " +aveCycle / (i+1.0));
            System.out.println("Clust: " +aveClust / (i+1.0));
            System.out.println("Diam: " +aveDiameter / (i+1.0));
            System.out.println("Assort: " +aveAssort / (i+1.0));
            System.out.println("Comp: " +aveComp / (i+1.0));
        }
    }

    // public void conductSimulations() {
    //     int averageNodeCount = 0;
        
    //     for (int i = 0; i < numTrials; i++) {
    //         sim.conductSimulation();
    //         sim.graph.removeIsolated();
    //         System.out.println("Conducted " + (i + 1) + "/" + numTrials + " simulations.");

            
    //         averageNodeCount += sim.graph.getSize();
    //         System.out.println(averageNodeCount / (i+1.0));
    //     }
    // }

    public void conductAnalysis(Simulation sim, int i) {
            averageDegree[i] = sim.graph.averageDegree();
            numberOfConnectedComponents[i] = sim.graph.numberOfConnectedComponents();
            //averageShortestPathLength[i] = sim.graph.averageShortestPathLength();
            density[i] = sim.graph.density();
            cycleBasisCount[i] = sim.graph.cycleBasisCount();
            clusteringCoefficientAverage[i] = sim.graph.clusteringCoefficientAverage();
            diameter[i] = sim.graph.diameter();
            edgeDistribution.add(sim.graph.edgeDistribution());
            //modularity[i] = sim.graph.modularity(sim.agents);
            assortativityCoefficient[i] = sim.graph.assortativityCoefficient();
            nodeCount[i] = sim.graph.getSize();
    }

    public void exportAverageDegree() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "AverageDegree.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + averageDegree[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportNodeCount() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "NodeCount.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + nodeCount[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportNumberOfConnectedComponents() throws Exception {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(simulationsName + "NumberOfConnectedComponents.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + numberOfConnectedComponents[i]);
            writer.newLine();
        }
        writer.close();
    }

    // public void exportAverageShortestPathLength() throws Exception {
    //     BufferedWriter writer = new BufferedWriter(
    //             new FileWriter(simulationsName + "AverageShortestPathLength.txt", false));
    //     for (int i = 0; i < numTrials; i++) {
    //         writer.write("" + averageShortestPathLength[i]);
    //         writer.newLine();
    //     }
    //     writer.close();
    // }

    public void exportDensity() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "Density.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + density[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportCycleBasisCount() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "CycleBasisCount.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + cycleBasisCount[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportClusteringCoefficientAverage() throws Exception {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(simulationsName + "ClusteringCoefficientAverage.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + clusteringCoefficientAverage[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportDiameter() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "Diameter.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + diameter[i]);
            writer.newLine();
        }
        writer.close();
    }

    public void exportEdgeDistribution() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "EdgeDistribution.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write(simulationsName + i);
            writer.newLine();
            for (int j = 0; j < edgeDistribution.get(i).length; j++) {
                if (edgeDistribution.get(i)[j] != 0) {
                    writer.write(j + " " + edgeDistribution.get(i)[j]);
                    writer.newLine();
                }
            }
            writer.newLine();
        }
        writer.close();
    }

    // public void exportModularity() throws Exception {
    //     BufferedWriter writer = new BufferedWriter(new FileWriter(simulationsName + "Modularity.txt", false));
    //     for (int i = 0; i < numTrials; i++) {
    //         writer.write("" + modularity[i]);
    //         writer.newLine();
    //     }
    //     writer.close();
    // }

    public void exportAssortativityCoefficient() throws Exception {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(simulationsName + "AssortativityCoefficient.txt", false));
        for (int i = 0; i < numTrials; i++) {
            writer.write("" + assortativityCoefficient[i]);
            writer.newLine();
        }
        writer.close();
    }
}