import java.io.*;
import java.util.*;

public class FileHandler {

    public static void saveTasks(List<Task> tasks) {
        try (PrintWriter pw = new PrintWriter("tasks.txt")) {
            for (Task t : tasks) {
                pw.println(t.name + "," + t.priority + "," + t.deadline + "," +
                        t.category + "," + t.difficulty + "," + t.completed);
            }
        } catch (Exception e) {
            System.out.println("Error saving tasks.");
        }
    }
    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                Task t = new Task(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        parts[3],
                        Integer.parseInt(parts[4])
                );

                t.completed = Boolean.parseBoolean(parts[5]);
                tasks.add(t);
            }
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
        return tasks;
    }
}
