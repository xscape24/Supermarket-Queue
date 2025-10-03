public class Cashier {
    String name;
    int cashierId;
    boolean isOccupied;

    public Cashier(String name, int cashierId) {
        this.name = name;
        this.cashierId = cashierId;
        this.isOccupied = false;
    }

    public void serveCustomer(Customer c) {
        isOccupied = true;
        System.out.println("Cashier " + name + " (ID: " + cashierId + ") is serving "
                           + c.getName() + " (Customer " + c.getId() 
                           + ") for " + c.getServiceTime() + " minutes.");
        try {
            Thread.sleep(1000);
        } catch(Exception e) {}
        isOccupied = false;
    }
}
