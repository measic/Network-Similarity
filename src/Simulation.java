import java.io.FileWriter;
import java.io.BufferedWriter;

public class Simulation {
    String simulationName;
    Agent[] agents;
    Graph graph;
    int agentNumber;
    int numberOfInterests;
    int rangePerInterest;
    int levelOfInterestNeeded;
    int maxEdgesPerNode;
    int maxGlobalEdges;
    int daysToRun;
    double probRandomEdges;
    //double probNeighborEdges;

    public Simulation(String simulationName, int agentNumber, int numberOfInterests, int rangePerInterest,
            int levelOfInterestNeeded, int maxEdgesPerNode, int maxGlobalEdges, int daysToRun,
            double probRandomEdges 
            //double probNeighborEdges
            ) {
        this.simulationName = simulationName;
        this.agentNumber = agentNumber;
        this.numberOfInterests = numberOfInterests;
        this.rangePerInterest = rangePerInterest;
        this.levelOfInterestNeeded = levelOfInterestNeeded;
        this.maxEdgesPerNode = maxEdgesPerNode;
        this.maxGlobalEdges = maxGlobalEdges;
        this.daysToRun = daysToRun;
        this.probRandomEdges = probRandomEdges;
        //this.probNeighborEdges = probNeighborEdges;

        generateAgents();
        createGraph();
    }

    private void generateAgents() {
        agents = new Agent[agentNumber];
        for (int i = 0; i < agentNumber; i++) {
            agents[i] = new Agent(numberOfInterests, rangePerInterest);
        }
    }

    private void createGraph() {
        graph = new Graph(agentNumber, maxEdgesPerNode, maxGlobalEdges);
    }

    public void conductSimulation() {
        for (int w = 0; w < daysToRun; w++) {
            graph.addRandomEdges(probRandomEdges);

            for (int i = 0; i < graph.getSize(); i++) {
                for (int j = i + 1; j < graph.getSize(); j++) {
                    if (!agents[i].similar(agents[j], levelOfInterestNeeded)) {
                        graph.removeEdge(i, j);
                    }
                }
            }
        }
    }

    public void exportNetwork() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(simulationName + "Network.net", false));
        writer.write("*Vertices " + graph.getSize());
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

        writer.newLine();
        writer.write("%Network details");
        writer.newLine();
        writer.write("%Number of vertices: " + graph.getSize());
        writer.newLine();
        writer.write("%Number of removed isolated vertices: " + (agentNumber - graph.getSize()));
        writer.newLine();
        writer.write("%Number of interests: " + numberOfInterests);
        writer.newLine();
        writer.write("%Range per interest: " + rangePerInterest);
        writer.newLine();
        writer.write("%Level of interest needed: " + levelOfInterestNeeded);
        writer.newLine();
        writer.write("%Max edges per node: " + maxEdgesPerNode);
        writer.newLine();
        writer.write("%Max global edges: " + maxGlobalEdges);
        writer.newLine();
        writer.write("%Days run: " + daysToRun);
        writer.newLine();
        writer.write("%Probability of random edges: " + probRandomEdges);
        //writer.newLine();
        //writer.write("%Probability of neighbor's edges: " + probNeighborEdges);
        writer.close();
    }

}
