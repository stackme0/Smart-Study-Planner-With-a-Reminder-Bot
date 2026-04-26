public class Task {
    String name;
    int priority;
    int deadline;
    boolean completed;
    String category;
    int difficulty;

    public Task(String name, int priority, int deadline, String category, int difficulty) {
        this.name = name;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.difficulty = difficulty;
        this.completed = false;
    }

    public void markComplete() {
        completed = true;
    }

    public void displayTask() {
        System.out.println(name + " | Priority: " + priority +
                " | Deadline: " + deadline +
                " | Category: " + category +
                " | Difficulty: " + difficulty +
                " | Done: " + completed);
    }
}