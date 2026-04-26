import java.util.*;

public class ReportGenerator {

    public static String generateReport(ArrayList<Task> tasks) {

        int total = tasks.size();
        int completed = 0;
        int pending = 0;

        for (Task t : tasks) {
            if (t.completed) completed++;
            else pending++;
        }

        StringBuilder report = new StringBuilder();

        report.append("SMART STUDY REPORT\n");
        report.append("--------------------------\n");
        report.append("Total Tasks : ").append(total).append("\n");
        report.append("Completed : ").append(completed).append("\n");
        report.append("Pending : ").append(pending).append("\n");

        if(total > 0){
            int percent = (completed * 100) / total;
            report.append("Progress : ").append(percent).append("%\n");
        }

        return report.toString();
    }
}
