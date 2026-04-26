import java.util.*;

public class Scheduler {

    List<Task> tasks = new ArrayList<>();

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void showTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.print(i + ". ");
            tasks.get(i).displayTask();
        }
    }

    public int calculateScore(Task t, String emotion) {
        int score = 0;
        
        // Base score off deadline
        score += (100 - t.deadline * 2);

        // General priority weight
        if (t.priority == 1) score += 15;
        else if (t.priority == 2) score += 10;
        else if (t.priority == 3) score += 5;

        // Smart mood modifiers
        if (emotion.contains("tired") || emotion.contains("sad") || emotion.contains("unmotivated")) {
            if (t.difficulty == 1) score += 50; // Heavily prioritize easy tasks
            if (t.difficulty == 3) score -= 50; // Penalize hard tasks
            if (t.priority == 3) score += 10;
        }
        else if (emotion.contains("stress") || emotion.contains("stressed")) {
            if (t.difficulty == 1) score += 30;
            if (t.difficulty == 2) score += 15;
            if (t.difficulty == 3) score -= 40; // Penalize hard tasks
        }
        else if (emotion.contains("motivated") || emotion.contains("productive") || emotion.contains("happy")) {
            if (t.difficulty == 3) score += 30; // Capitalize on good mood for hard tasks
            if (t.priority == 1) score += 20;
        }

        return score;
    }

    public Task getTopTask(String emotion) {
        Task best = null;
        int bestScore = Integer.MIN_VALUE;

        for (Task t : tasks) {
            if (t.completed) continue;
            int score = calculateScore(t, emotion);
            if (score > bestScore) {
                bestScore = score;
                best = t;
            }
        }
        return best;
    }

    public List<Task> generateTimetable(String emotion) {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.completed) {
                pendingTasks.add(t);
            }
        }
        pendingTasks.sort((t1, t2) -> {
            int score1 = calculateScore(t1, emotion);
            int score2 = calculateScore(t2, emotion);
            return Integer.compare(score2, score1);
        });
        return pendingTasks;
    }

    public void markTaskDone(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markComplete();
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            FileHandler.saveTasks(tasks);
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Invalid task number.");
        }
    }
}
