
public class Main {

    public static void main(String[] args) {
        // ======= CONFIGURATION =======
        int numCashiers = 4;
        int numCustomers = 50;
        int maxServiceTime = 8; // seconds
        int maxArrivalInterval = 4; // seconds between arrivals

        Simulation simulation = new Simulation(numCashiers, numCustomers, maxServiceTime, maxArrivalInterval);
        simulation.runSimulation();
        simulation.printSummary();
        simulation.exportResults("run_001_results.csv");
    }
}
