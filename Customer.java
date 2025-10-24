class Customer {
     int id;
     int arrivalTime;
     int serviceTime;
    int queueAssigned;

    public Customer(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setQueueAssigned(int q) {
        this.queueAssigned = q;
    }

    public int getQueueAssigned() {
        return queueAssigned;
    }
}
