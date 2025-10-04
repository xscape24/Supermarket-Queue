import java.util.LinkedList;
import java.util.Queue;

public class CashierQueue {
    Queue<Customer> queue;
    int queueNumber;

    public CashierQueue(int queueNumber) {
        this.queue = new LinkedList<>();
        this.queueNumber = queueNumber;
    }

    public void addCustomer(Customer customer) {
        queue.add(customer);
        System.out.println(customer.getName() + " (Customer " + customer.getId() 
                           + ") has joined Queue #" + queueNumber);
    }

    public Customer getNextCustomer() {
        if (!queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}