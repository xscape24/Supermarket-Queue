import java.util.LinkedList;
import java.util.Queue;

public class CheckoutQueue {
     int id;
     final Queue<Customer> queue;
     double busyTime = 0.0;
     double cashierBusyUntil = 0.0;//time cashier will finish serving current customer

    public CheckoutQueue(int id) {
        this.id = id;
        this.queue = new LinkedList<>();
    }

    public int getId() { return id; }

    public void addCustomer(Customer c) {
        c.setQueueAssigned(id);
        queue.add(c);
    }

    public Customer poll() {
        return queue.poll();
    }

    public Customer peek() {
        return queue.peek();
    }

    public int getQueueLength() {
        return queue.size();
    }

    public void addBusy(double time) {
        this.busyTime += time;
    }

    public double getBusyTime() {
        return this.busyTime;
    }

    public double getCashierBusyUntil() {
        return this.cashierBusyUntil;
    }

    public void setCashierBusyUntil(double time) {
        this.cashierBusyUntil = time;
    }
}