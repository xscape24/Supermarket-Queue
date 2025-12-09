import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulation {
     int numCashiers;
    int numCustomers;
    double meanService;
    double meanInterarrival;
     long seed;
    String runId;

    List<CheckoutQueue> queues = new ArrayList<>();
     List<Customer> customers = new ArrayList<>();
   List<Event> events = new ArrayList<>();
    List<Snapshot> timeseries = new ArrayList<>();
   StringBuilder eventsText = new StringBuilder("CustomerID,Type,Time,Service,Queue,Extra\n");
    StringBuilder timeseriesText = new StringBuilder("time,total");
    PriorityQueue<Departure> departures = new PriorityQueue<>(Comparator.comparingDouble(d -> d.time));

    private double lastEventTime = 0.0;
    private double startWall;
    private double endWall;

    public Simulation(int numCashiers, int numCustomers, double meanService, double meanInterarrival, long seed, String runId) {
        this.numCashiers = numCashiers;
        this.numCustomers = numCustomers;
        this.meanService = meanService;
        this.meanInterarrival = meanInterarrival;
        this.seed = seed;
        this.runId = runId;
        for (int i = 1; i <= numCashiers; i++) {
            queues.add(new CheckoutQueue(i));
            timeseriesText.append(",q").append(i);
        }
        for (int i = 1; i <= numCashiers; i++) timeseriesText.append(",busy").append(i);
        timeseriesText.append("\n");
    }

    public void run() {
        startWall = System.currentTimeMillis();
        generateCustomers();
        double nextSnapshot = 0.0;
        int idx = 0;
        while (idx < customers.size() || !departures.isEmpty()) {
            double nextArrival = idx < customers.size() ? customers.get(idx).getArrivalTime() : Double.POSITIVE_INFINITY;
            double nextDeparture = departures.isEmpty() ? Double.POSITIVE_INFINITY : departures.peek().time;
            double next = Math.min(nextArrival, nextDeparture);

            while (nextSnapshot <= next) {
                recordSnapshot(nextSnapshot);
                nextSnapshot += 1.0;
            }

            lastEventTime = next;

            if (nextArrival <= nextDeparture) {
                Customer c = customers.get(idx++);
                handleArrival(c);
            } else {
                Departure d = departures.poll();
                handleDeparture(d);
            }
        }

        if (timeseries.isEmpty() || timeseries.get(timeseries.size()-1).time < lastEventTime) {
            double t = timeseries.isEmpty() ? 0.0 : timeseries.get(timeseries.size()-1).time + 1.0;
            while (t <= lastEventTime + 1.0) {
                recordSnapshot(t);
                t += 1.0;
            }
        }

        endWall = System.currentTimeMillis();
        System.out.println("Supermarket Simulator Finished.");
    }

    public void generateCustomers() {
        Random r = new Random(seed);
        double t = 0.0;
        for (int i = 1; i <= numCustomers; i++) {
            double inter = -meanInterarrival * Math.log(1.0 - r.nextDouble());
            t += inter;
            double serv = -meanService * Math.log(1.0 - r.nextDouble());
            if (serv < 0.1) serv = 0.1;
            Customer c = new Customer(i, round(t), round(serv));
            customers.add(c);
            customers.add(c);
            events.add(new Event(i, "arrival", c.getArrivalTime(), c.getServiceTime(), -1, ""));
            eventsText.append(c.getId()).append(",arrival,").append(format(c.getArrivalTime())).append(",").append(format(c.getServiceTime())).append(",").append(-1).append(",\n");
        }
    }

public  void handleArrival(Customer c) {
        CheckoutQueue q = queues.get(0);
        for (CheckoutQueue qq : queues) {
            if (qq.getQueueLength() < q.getQueueLength()) q = qq;
        }
        q.addCustomer(c);
        events.add(new Event(c.getId(), "assigned", c.getArrivalTime(), c.getServiceTime(), q.getId(), ""));
        eventsText.append(c.getId()).append(",assigned,").append(format(c.getArrivalTime())).append(",").append(format(c.getServiceTime())).append(",").append(q.getId()).append(",\n");
        if (q.getCashierBusyUntil() <= c.getArrivalTime() && q.peek() != null && q.peek().getId() == c.getId()) {
            startService(q, c, c.getArrivalTime());
        }
    }

    public void startService(CheckoutQueue q, Customer c, double start) {
        c.setServiceStart(start);
        double dep = round(start + c.getServiceTime());
        c.setDepartureTime(dep);
        q.setCashierBusyUntil(dep);
        q.addBusy(c.getServiceTime());
        departures.add(new Departure(q.getId(), c, dep));
        events.add(new Event(c.getId(), "service_start", start, c.getServiceTime(), q.getId(), ""));
        eventsText.append(c.getId()).append(",service_start,").append(format(start)).append(",").append(format(c.getServiceTime())).append(",").append(q.getId()).append(",\n");
    }

    private void handleDeparture(Departure d) {
        CheckoutQueue q = queues.get(d.queueId - 1);
        Customer head = q.peek();
        if (head != null && head.getId() == d.customer.getId()) {
            q.poll();
        } else {
            List<Customer> temp = new ArrayList<>();
            Customer found = null;
            Customer p;
            while ((p = q.poll()) != null) {
                if (p.getId() == d.customer.getId()) { found = p; break; }
                temp.add(p);
            }
            for (Customer c : temp) q.addCustomer(c);
        }
        events.add(new Event(d.customer.getId(), "departure", d.time, d.customer.getServiceTime(), d.queueId, ""));
        eventsText.append(d.customer.getId()).append(",departure,").append(format(d.time)).append(",").append(format(d.customer.getServiceTime())).append(",").append(d.queueId).append(",\n");
        double wait = d.customer.getServiceStart() - d.customer.getArrivalTime();
        wait = Math.max(0.0, wait);
        System.out.println("Customer " + d.customer.getId() + " waited " + format(wait) + " seconds and was served from " + format(d.customer.getServiceStart()) + " seconds to " + format(d.customer.getDepartureTime()) + " seconds.");
        Customer next = q.peek();
        if (next != null) {
            startService(q, next, d.time);
        } else {
            q.setCashierBusyUntil(d.time);
        }
    }

    private void recordSnapshot(double t) {
        int total = 0;
        List<Integer> lengths = new ArrayList<>();//length of each cashier's queue in snapshot
        List<Integer> busy = new ArrayList<>();
        for (CheckoutQueue q : queues) {
            int l = q.getQueueLength();
            total += l;
            lengths.add(l);
            busy.add(q.getCashierBusyUntil() > t ? 1 : 0);
        }
        timeseries.add(new Snapshot(round(t), total, lengths, busy));
        StringBuilder l = new StringBuilder();
        l.append(format(t)).append(",").append(total);
        for (int v : lengths) l.append(",").append(v);
        for (int v : busy) l.append(",").append(v);
        timeseriesText.append(l.toString()).append("\n");
    }

    public void printSummary() {
        double totalService = 0.0;
        double totalWait = 0.0;
        int started = 0;
        int departed = 0;
        for (Event e : events) {
            if ("service_start".equals(e.type)) {
                totalService += e.service;
                started++;
                double arr = getArrival(e.customerId);
                totalWait += (e.time - arr);
            }
            if ("departure".equals(e.type)) departed++;
        }
        double avgWait = started == 0 ? 0.0 : totalWait / started; //calculates average wait time
        double avgService = started == 0 ? 0.0 : totalService / started;//calculates average wait time
        double avgTimeInSystem = timeseries.stream().mapToDouble(s -> s.total).average().orElse(0.0);//calculates average  time customer spends in system
        double lambda = customers.size() / Math.max(1.0, lastEventTime);//average number of customers at one time


        System.out.println("Cashiers: " + numCashiers + ".");
        System.out.println("Customers: " + numCustomers + ".");
        System.out.println("Served: " + departed + ".");
        System.out.println("Average wait: " + format(avgWait) + " seconds.");
        System.out.println("Average service: " + format(avgService) + " seconds.");
        System.out.println("L (avg in system): " + format(avgTimeInSystem) + ".");
        System.out.println("Lambda * W: " + format(lambda * avgWait) + ".");
        System.out.println("Wall seconds: " + format((endWall - startWall) / 1000.0) + " seconds.");
        for (CheckoutQueue q : queues) {
            double util = lastEventTime == 0.0 ? 0.0 : q.getBusyTime() / Math.max(1.0, lastEventTime);
            System.out.println("cashier-" + q.getId() + " util: " + format(util) + ".");
        }
    }

    public void exportResults(String file) {//exports data to results file
        try (PrintWriter pw = new PrintWriter(new File(file))) {
            pw.println("CustomerID,Arrival,ServiceStart,Departure,Service,Queue");
            for (Customer c : customers) {
                pw.println(c.getId() + "," + format(c.getArrivalTime()) + "," + format(c.getServiceStart()) + "," + format(c.getDepartureTime()) + "," + format(c.getServiceTime()) + "," + c.getQueueAssigned());
            }
            System.out.println("Results saved to: " + file + ".");
        } catch (Exception e) { System.out.println("results export fail."); }
    }

    public void exportEvents(String file) {//exports data to events file
        try (PrintWriter pw = new PrintWriter(new File(file))) {
            pw.print(eventsText.toString());
            System.out.println("Events saved to: " + file + ".");
        } catch (Exception e) { System.out.println("events export fail."); }
    }

    public void exportTimeseries(String file) {//exports data to timeseries file
        try (PrintWriter pw = new PrintWriter(new File(file))) {
            pw.print(timeseriesText.toString());
            System.out.println("Timeseries saved to: " + file + ".");
        } catch (Exception e) { System.out.println("timeseries export fail."); }
    }

    public void exportSummary(String file) {//exports data to summary file
        try (PrintWriter pw = new PrintWriter(new File(file))) {
            double totalService = 0.0;
            double totalWait = 0.0;
            int started = 0;
            for (Event e : events) {
                if ("service_start".equals(e.type)) {
                    totalService += e.service;
                    started++;
                    totalWait += (e.time - getArrival(e.customerId));
                }
            }
            double avgWait = started == 0 ? 0.0 : totalWait / started;
            double avgService = started == 0 ? 0.0 : totalService / started;
            pw.println("{");
            pw.println("\"runId\":\"" + runId + "\",");
            pw.println("\"numCashiers\":" + numCashiers + ",");
            pw.println("\"numCustomers\":" + numCustomers + ",");
            pw.println("\"averageWait\":" + format(avgWait) + ",");
            pw.println("\"averageService\":" + format(avgService) + ",");
            pw.println("\"totalServed\":" + events.stream().filter(e -> "departure".equals(e.type)).count() + ",");
            pw.println("\"wallSeconds\":" + format((endWall - startWall) / 1000.0));
            pw.println("}");
            System.out.println("Summary saved to: " + file + ".");
        } catch (Exception e) { System.out.println("summary export fail."); }
    }

    public void exportConfig(String file) { //exports data to config file
        try (PrintWriter pw = new PrintWriter(new File(file))) {
            pw.println("{");
            pw.println("\"runId\":\"" + runId + "\",");
            pw.println("\"numCashiers\":" + numCashiers + ",");
            pw.println("\"numCustomers\":" + numCustomers + ",");
            pw.println("\"meanService\":" + meanService + ",");
            pw.println("\"meanInterarrival\":" + meanInterarrival + ",");
            pw.println("\"seed\":" + seed);
            pw.println("}");
            System.out.println("Config saved to: " + file + ".");
        } catch (Exception e) { System.out.println("config export fail."); }
    }

    private double getArrival(int id) {
        for (Customer c : customers) if (c.getId() == id) return c.getArrivalTime();
        return 0.0;
    }

    private static double round(double d) { return Math.round(d * 1000.0) / 1000.0; }
    private static String format(double d) { if (d < 0) return ""; return String.format("%.2f", d); }




    static class Event {
        int customerId;
        String type;
        double time;
        double service;
        int queue;
        String extra;
        Event(int customerId, String type, double time, double service, int queue, String extra) {
            this.customerId = customerId; this.type = type; this.time = time; this.service = service; this.queue = queue; this.extra = extra;
        }
    }

    static class Departure {
        int queueId;
        Customer customer;
        double time;
        Departure(int q, Customer c, double t) { queueId = q; customer = c; time = t; }
    }

    static class Snapshot {
        double time;
        int total;
        List<Integer> lengths;
        List<Integer> busy;
        Snapshot(double time, int total, List<Integer> lengths, List<Integer> busy) { this.time = time; this.total = total; this.lengths = lengths; this.busy = busy; }
    }
}