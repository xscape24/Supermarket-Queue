public class Main {
    public static void main(String[] args) {
        int numCashiers = 4;
        int numCustomers = 50;
        double meanService = 8.0;
        double meanInterarrival = 2.0;
        long seed = 12345L; //random number
        String runId = "run_001";

        Simulation sim = new Simulation(numCashiers, numCustomers, meanService, meanInterarrival, seed, runId);//instantiates Simulation Object
        sim.run();//Simulation starts 
        sim.printSummary();
        sim.exportResults(runId + "_results.csv");
        sim.exportEvents(runId + "_events.csv");
        sim.exportTimeseries(runId + "_timeseries.csv");
        sim.exportSummary(runId + "_summary.json");
        sim.exportConfig(runId + "_config.json");
    }
}