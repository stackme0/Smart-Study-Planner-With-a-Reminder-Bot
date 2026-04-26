public class ReminderThread extends Thread {

    private String emotion = "neutral";
    private boolean running = true;
    private boolean active = true;
    private boolean isUserTyping = false;

    private boolean isShowingReminder = false;

    public void updateEmotion(String newEmotion) {
        this.emotion = newEmotion;
    }

    public void stopReminder() {
        running = false;
    }

    public void toggleReminder() {
        active = !active;
        System.out.println(active ? " Reminders ON" : " Reminders OFF");
    }

    public void setUserTyping(boolean typing) {
        this.isUserTyping = typing;
    }

    public void run() {
        try {
            while (running) {

                int sleepTime = getSleepTime(emotion);
                Thread.sleep(sleepTime);

                if (!active || isUserTyping || isShowingReminder) continue;

                isShowingReminder = true;
                String message = getReminderMessage(emotion);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(null, message, "Personal Reminder", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    isShowingReminder = false;
                });
            }
        } catch (InterruptedException e) {
            System.out.println("Reminder stopped.");
        }
    }

    private int getSleepTime(String emotion) {
        return switch (emotion) {
            case "tired" -> 120000;
            case "stressed" -> 90000;
            case "happy" -> 60000;
            case "sad" -> 120000;
            default -> 90000;
        };
    }

    private String getReminderMessage(String emotion) {
        return switch (emotion) {
            case "tired" -> "Take it slow... even small progress matters.";
            case "stressed" -> "Breathe. One step at a time.";
            case "happy" -> "Great energy! Keep going.";
            case "sad" -> "It's okay... just try one small task.";
            default -> "Stay consistent. Even 10 minutes matters.";
        };
    }
}
