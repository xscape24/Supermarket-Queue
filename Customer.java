public class Customer {
    String name;
    int customerId;
    int serviceTime;
    static int nextId = 1;

    public Customer(String name, int serviceTime) {
        this.name = name;
        this.serviceTime = serviceTime;
        this.customerId = nextId++;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return customerId;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}
