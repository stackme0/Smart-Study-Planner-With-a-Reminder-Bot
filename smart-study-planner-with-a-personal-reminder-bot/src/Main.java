import java.util.*;

public class Main {
    public static void main(String[] args) {

        ReminderThread reminder = new ReminderThread();
        reminder.start();

        Scanner sc = new Scanner(System.in);
        Scheduler scheduler = new Scheduler();

        scheduler.tasks = FileHandler.loadTasks();

        System.out.println("✨ Smart Study Planner Bot ✨");

        while (true) {

            System.out.println("\n1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Tasks");
            System.out.println("4. Mark Task Complete");
            System.out.println("5. Get Smart Suggestion");
            System.out.println("6. Show Progress");
            System.out.println("7. Start Pomodoro");
            System.out.println("8. Generate Report");
            System.out.println("9. Exit");
            System.out.println("10. Toggle Reminders");

            System.out.print("Choose: ");
            reminder.setUserTyping(true);
            int choice = sc.nextInt();
            reminder.setUserTyping(false);
            sc.nextLine();

            if (choice == 1) {
                reminder.setUserTyping(true);
                System.out.print("Task name: ");
                String name = sc.nextLine();
                reminder.setUserTyping(false);

                reminder.setUserTyping(true);
                System.out.print("Priority (1-High, 2-Medium, 3-Low): ");
                int p = sc.nextInt();
                reminder.setUserTyping(false);

                reminder.setUserTyping(true);
                System.out.print("Deadline (days): ");
                int d = sc.nextInt();
                reminder.setUserTyping(false);

                sc.nextLine();
                reminder.setUserTyping(true);
                System.out.print("Category: ");
                String cat = sc.nextLine();
                reminder.setUserTyping(false);

                reminder.setUserTyping(true);
                System.out.print("Difficulty(1-Easy , 2-Medium , 3-Hard): ");
                int diff = sc.nextInt();
                reminder.setUserTyping(false);

                scheduler.addTask(new Task(name, p, d, cat , diff ));
            }

            else if (choice == 2) {
                scheduler.showTasks();
            }
            else if (choice == 3) {
                scheduler.showTasks();

                reminder.setUserTyping(true);
                System.out.print("Enter task number to delete: ");
                int index = sc.nextInt();
                reminder.setUserTyping(false);

                scheduler.deleteTask(index - 1);
            }

            else if (choice == 4) {
                scheduler.showTasks();
                reminder.setUserTyping(true);
                System.out.print("Enter task index: ");
                int i = sc.nextInt();
                reminder.setUserTyping(false);
                scheduler.markTaskDone(i);
            }

            else if (choice == 5) {

                sc.nextLine();
                reminder.setUserTyping(true);
                System.out.print("How are you feeling? ");
                String input = sc.nextLine();
                reminder.setUserTyping(false);

                String emotion = EmotionDetector.detectEmotion(input);
                reminder.updateEmotion(emotion);
                Task task = scheduler.getTopTask(emotion);
                if (task == null) {
                    System.out.println("No tasks available. Please add tasks first.");
                    continue;
                }
                String response = ReminderBot.getResponse(emotion);
                String suggestion = ReminderBot.getSmartSuggestion(emotion, task);

                reminder.setUserTyping(true);
                System.out.println("\n🤖 Bot: " + response);
                System.out.println(suggestion);
                System.out.println(ReminderBot.getCategoryTip(task));
                reminder.setUserTyping(false);
            }

            else if (choice == 6) {
                ProductivityTracker.showProgress(scheduler.tasks);
            }

            else if (choice == 7) {
                ReportGenerator.generateReport((ArrayList<Task>) scheduler.tasks);
            }

            else if (choice == 9) {
                FileHandler.saveTasks(scheduler.tasks);
                System.out.println("Goodbye 👋");
                break;
            }
            else if (choice == 10) {
                reminder.toggleReminder();
            }
        }
        sc.close();
    }
}
