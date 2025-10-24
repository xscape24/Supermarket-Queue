import java.util.*;


class CheckoutQueue {
    int id;
    Queue<Customer> queue;

    public CheckoutQueue(int id) {
        this.id = id;
        this.queue = new LinkedList<>();
    }

    public void addCustomer(Customer c) {
        c.setQueueAssigned(id);
        queue.add(c);
        System.out.println("Customer " + c.getId() + " joined Queue " + id + " (Queue length: " + queue.size() + ")");
    }

    public void finishCustomer(Customer c) {
        queue.remove(c);
        System.out.println("Customer " + c.getId() + " finished at Queue " + id);
    }

    public int getQueueLength() {
        return queue.size();
    }
}
