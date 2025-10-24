import java.io.*;
import java.util.*;
class Simulation {
    int numCashiers;
     int numCustomers;
    int maxServiceTime;
    int maxArrivalInterval;
     List<CheckoutQueue> queues;
     List<Customer> customers;
     int totalServed = 0;
     double totalWaitTime = 0;
     double totalServiceTime = 0;
     double startTime;
    double endTime;

    public Simulation(int numCashiers, int numCustomers, int maxServiceTime, int maxArrivalInterval) {
        this.numCashiers = numCashiers;
        this.numCustomers = numCustomers;
        this.maxServiceTime = maxServiceTime;
        this.maxArrivalInterval = maxArrivalInterval;
        this.queues = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void runSimulation() {
        System.out.println("\n=== Supermarket Queue Simulation ===");
        startTime = System.currentTimeMillis();

        // Initialize queues
        for (int i = 0; i < numCashiers; i++) {
            queues.add(new CheckoutQueue(i + 1));
        }

        // Generate customers
        Random rand = new Random();
        int currentTime = 0;
        for (int i = 0; i < numCustomers; i++) {
            int arrivalTime = currentTime;
            int serviceTime = 2 + rand.nextInt(maxServiceTime);
            customers.add(new Customer(i + 1, arrivalTime, serviceTime));
            currentTime += rand.nextInt(maxArrivalInterval + 1);
        }

        // Process simulation
        for (Customer c : customers) {
            CheckoutQueue shortestQueue = getShortestQueue();
            shortestQueue.addCustomer(c);
            totalWaitTime += shortestQueue.getQueueLength();
            totalServiceTime += c.getServiceTime();
            totalServed++;
            simulateService(c, shortestQueue);
        }

        endTime = System.currentTimeMillis();
    }

    private void simulateService(Customer c, CheckoutQueue q) {
        try {
            Thread.sleep(c.getServiceTime() * 20L); // short sleep simulates work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        q.finishCustomer(c);
    }

    private CheckoutQueue getShortestQueue() {
        return queues.stream().min(Comparator.comparingInt(CheckoutQueue::getQueueLength)).orElse(queues.get(0));
    }

    public void printSummary() {
        double durationSec = (endTime - startTime) / 1000.0;
        System.out.println("");
        System.out.println("--- Simulation Summary ---");
        System.out.println("Cashiers: " + numCashiers);
        System.out.println("Customers Served: " + totalServed);
        System.out.println("Total Service Time: " + totalServiceTime + "s");
        System.out.println("Average Wait Time: " + (totalWaitTime / totalServed));
        System.out.println("Execution Time: " + durationSec + "s");
    }

    public void exportResults(String filename) {
        try (PrintWriter pw = new PrintWriter(new File(filename))) {
            pw.println("CustomerID,ArrivalTime,ServiceTime,QueueAssigned");
            for (Customer c : customers) {
                pw.println(c.getId() + "," + c.getArrivalTime() + "," + c.getServiceTime() + "," + c.getQueueAssigned());
            }
            System.out.println("Data exported to: " + filename);
        } catch (Exception e) {
            System.out.println("Error exporting results.");
        }
    }
}
