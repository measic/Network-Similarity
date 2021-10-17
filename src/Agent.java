import java.util.Random;

public class Agent {
    private int ID;
    private static int IDs = 0;
    private int[] interests;
    private int numberOfInterests;
    private int rangePerInterest;

    public Agent(int numberOfInterests, int rangePerInterest) {
        assignID();
        this.interests = new int[numberOfInterests];
        this.numberOfInterests = numberOfInterests;
        this.rangePerInterest = rangePerInterest;

        Random random = new Random();

        for (int i = 0; i < this.numberOfInterests; i++) {
            this.interests[i] = random.nextInt(rangePerInterest);
        }
    }

    private void assignID() {
        ID = IDs;
        IDs++;
    }

    public int getID() {
        return ID;
    }

    public int getInterest(int index) {
        return interests[index];
    }

    public int getNumberOfInterests() {
        return numberOfInterests;
    }

    public int getRangePerInterests() {
        return rangePerInterest;
    }

    public boolean similar(Agent otherAgent, int levelNeeded) {
        return similarityIndex(otherAgent) >= levelNeeded;
    }

    public double similarityIndex(Agent otherAgent) {
        int similarityCount = 0;
        for (int i = 0; i < numberOfInterests; i++) {
            if (interests[i] == otherAgent.getInterest(i)) {
                similarityCount++;
            }
        }
        return similarityCount;
    }

    public double averageWeightedInterest() {
        int sum = 0;
        for (int interest : interests) {
            sum += interest;
        }
        return (double) sum / (numberOfInterests * (rangePerInterest - 1));
    }

    public int averageWeightedInterestCategory() {
        double interest = averageWeightedInterest();
        if (interest <= 1.0/3) {
            return 1;
        } else if (interest <= 2.0/3) {
            return 2;
        } else {
            return 3;
        }
    }
}