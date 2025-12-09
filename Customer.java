public class Customer {
    int id;
    double arrivalTime;
    double serviceTime;
    int queueAssigned = -1;
    double serviceStart = -1.0;
    double departureTime = -1.0;
    
    public Customer(int id, double arrivalTime, double serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    
    public int getId() { return id; }
    public double getArrivalTime() { return arrivalTime; }
    public double getServiceTime() { return serviceTime; }
    public void setQueueAssigned(int queueNum) { this.queueAssigned = queueNum; }
    public int getQueueAssigned() { return queueAssigned; }
    public void setServiceStart(double time) { serviceStart = time; }
    public double getServiceStart() { return serviceStart; }
    public void setDepartureTime(double time) { departureTime = time; }
    public double getDepartureTime() { return departureTime; }
}