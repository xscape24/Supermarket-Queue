public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Supermarket Queue Simulator.");

        Cashier cashier = new Cashier("Alice", 1);
        CashierQueue queue = new CashierQueue(1);

        Customer c1 = new Customer("John", 5);
        Customer c2 = new Customer("Mary", 7);
        Customer c3 = new Customer("David", 6);
        Customer c4 = new Customer("Sarah", 8);
        Customer c5 = new Customer("James", 4);

        queue.addCustomer(c1);
        queue.addCustomer(c2);
        queue.addCustomer(c3);
        queue.addCustomer(c4);
        queue.addCustomer(c5);

        System.out.println("\n--- Serving Customers ---");
        while(!queue.isEmpty()) {
            Customer next = queue.getNextCustomer();
            if(!(next == null)) {
                cashier.serveCustomer(next);
            }
        }

        System.out.println("\nSimulation Finished. All customers have been served!");
    }
}
