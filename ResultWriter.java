import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    String base;

    public ResultWriter(String base) {
        this.base = base;
    }

    public void writeResults(int customers, int cashiers, double avgWait, double avgService) {
        try {
            FileWriter fw = new FileWriter(base + "_results.csv");
            fw.write("customers,cashiers,avg_wait_seconds,avg_service_seconds\n");
            fw.write(customers + "," + cashiers + "," + Math.round(avgWait * 100.0) / 100.0 + "," + Math.round(avgService * 100.0) / 100.0 + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSummary(String json) {
        try {
            FileWriter fw = new FileWriter(base + "_summary.json");
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEvents(String text) {
        try {
            FileWriter fw = new FileWriter(base + "_events.csv");
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTimeseries(String text) {
        try {
            FileWriter fw = new FileWriter(base + "_timeseries.csv");
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConfig(String json) {
        try {
            FileWriter fw = new FileWriter(base + "_config.json");
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}