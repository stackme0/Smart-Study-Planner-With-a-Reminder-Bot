import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SmartPlannerGUI {

    static Scheduler scheduler = new Scheduler();
    static ReminderThread reminder = new ReminderThread();
    static JPanel taskListPanel;

    public static void main(String[] args) {
        scheduler.tasks = FileHandler.loadTasks();
        reminder.start();

        JFrame frame = new JFrame("Smart Study Planner");
        frame.setSize(950, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 650));
        sidebar.setBackground(new Color(35, 39, 42)); // Modern dark charcoal
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 0, 0, 0));

        JLabel logo = new JLabel("Smart Planner");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        sidebar.add(createNavButton("Add Task", "add"));
        sidebar.add(createNavButton("Mark Complete", "done"));
        sidebar.add(createNavButton("Delete Task", "delete"));
        sidebar.add(createNavButton("Smart Timetable", "timetable"));
        sidebar.add(createNavButton("Chat Bot", "chat"));
        sidebar.add(createNavButton("Generate Report", "report"));
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createNavButton("Exit", "exit"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Main Content Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(245, 246, 250)); // Soft modern white/gray

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)),
            new EmptyBorder(25, 30, 25, 30)
        ));
        
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(45, 52, 54));
        headerPanel.add(title, BorderLayout.WEST);

        mainArea.add(headerPanel, BorderLayout.NORTH);

        // Task Display List
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(new Color(245, 246, 250));
        taskListPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainArea.add(scrollPane, BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainArea, BorderLayout.CENTER);

        updateTaskDisplay();

        frame.setVisible(true);
    }

    static JButton createNavButton(String text, String action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setForeground(new Color(185, 187, 190));
        btn.setBackground(new Color(35, 39, 42));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(240, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(55, 60, 65));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(35, 39, 42));
                btn.setForeground(new Color(185, 187, 190));
            }
        });

        btn.addActionListener(e -> {
            switch (action) {
                case "add": addTask(); break;
                case "delete": deleteTask(); break;
                case "done": markComplete(); break;
                case "timetable": generateTimetableWindow(); break;
                case "chat": new ChatBotGUI().show(); break;
                case "report": 
                    String report = ReportGenerator.generateReport((ArrayList<Task>) scheduler.tasks);
                    JOptionPane.showMessageDialog(null, report, "Report", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "exit": System.exit(0); break;
            }
        });
        return btn;
    }

    static void updateTaskDisplay() {
        taskListPanel.removeAll();
        
        if (scheduler.tasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasks available. Add some tasks to get started!");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
            emptyLabel.setForeground(new Color(150, 150, 150));
            taskListPanel.add(emptyLabel);
        } else {
            int index = 1;
            for (Task t : scheduler.tasks) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                card.setMaximumSize(new Dimension(850, 100));

                JLabel nameLabel = new JLabel(index + ". " + t.name);
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
                nameLabel.setForeground(t.completed ? new Color(160, 160, 160) : new Color(45, 52, 54));

                String details = String.format("Priority: %s   |   Deadline: %d days   |   Difficulty: %s", 
                    getPriority(t.priority), t.deadline, getDifficulty(t.difficulty));
                JLabel detailsLabel = new JLabel(details);
                detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                detailsLabel.setForeground(new Color(130, 130, 130));

                JPanel infoPanel = new JPanel(new GridLayout(2, 1, 8, 8));
                infoPanel.setBackground(Color.WHITE);
                infoPanel.add(nameLabel);
                infoPanel.add(detailsLabel);

                JLabel statusLabel = new JLabel(t.completed ? "DONE" : "PENDING");
                statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                statusLabel.setForeground(t.completed ? new Color(39, 174, 96) : new Color(225, 112, 85));
                
                card.add(infoPanel, BorderLayout.CENTER);
                card.add(statusLabel, BorderLayout.EAST);

                taskListPanel.add(card);
                taskListPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                index++;
            }
        }
        
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    static void addTask() {
        String name = JOptionPane.showInputDialog("Task name:");
        if (name == null || name.trim().isEmpty()) return;

        try {
            int priority = Integer.parseInt(JOptionPane.showInputDialog("Priority (1-High, 2-Medium, 3-Low):"));
            int deadline = Integer.parseInt(JOptionPane.showInputDialog("Deadline (days):"));
            String category = JOptionPane.showInputDialog("Category:");
            int difficulty = Integer.parseInt(JOptionPane.showInputDialog("Difficulty (1-Easy, 2-Medium, 3-Hard):"));

            scheduler.addTask(new Task(name, priority, deadline, category, difficulty));
            FileHandler.saveTasks(scheduler.tasks);
            updateTaskDisplay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Task not added.");
        }
    }

    static void deleteTask() {
        String input = JOptionPane.showInputDialog("Enter task number to delete:");
        if (input == null) return;
        try {
            int index = Integer.parseInt(input);
            scheduler.deleteTask(index - 1);
            updateTaskDisplay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number");
        }
    }

    static void markComplete() {
        String input = JOptionPane.showInputDialog("Enter task number to mark complete:");
        if (input == null) return;
        try {
            int index = Integer.parseInt(input);
            scheduler.markTaskDone(index - 1);
            FileHandler.saveTasks(scheduler.tasks);
            updateTaskDisplay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number");
        }
    }

    static void generateTimetableWindow() {
        String mood = JOptionPane.showInputDialog("How are you feeling? (e.g., tired, stressed, motivated)");
        if (mood == null || mood.trim().isEmpty()) return;

        String emotion = EmotionDetector.detectEmotion(mood);
        List<Task> sortedTasks = scheduler.generateTimetable(emotion);

        JFrame taskFrame = new JFrame("Smart Timetable");
        taskFrame.setSize(550, 500);
        taskFrame.setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 15));
        area.setMargin(new Insets(20, 20, 20, 20));

        StringBuilder sb = new StringBuilder();
        sb.append("Detected Mood: ").append(emotion.toUpperCase()).append("\n\n");
        sb.append("Here is your recommended study schedule:\n");
        sb.append("--------------------------------------------------\n\n");

        if (sortedTasks.isEmpty()) {
            sb.append("No pending tasks available.\n");
        } else {
            int i = 1;
            for (Task t : sortedTasks) {
                sb.append(i++).append(". ").append(t.name).append("\n");
                sb.append("   Priority   : ").append(getPriority(t.priority)).append("\n");
                sb.append("   Deadline   : ").append(t.deadline).append(" days\n");
                sb.append("   Difficulty : ").append(getDifficulty(t.difficulty)).append("\n\n");
            }
        }

        area.setText(sb.toString());
        taskFrame.add(new JScrollPane(area));
        taskFrame.setVisible(true);
    }

    static String getPriority(int p) { return p == 1 ? "High" : (p == 2 ? "Medium" : "Low"); }
    static String getDifficulty(int d) { return d == 1 ? "Easy" : (d == 2 ? "Medium" : "Hard"); }
}