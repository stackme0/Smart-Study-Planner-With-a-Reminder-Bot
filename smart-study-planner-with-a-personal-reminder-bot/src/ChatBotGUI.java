import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatBotGUI {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ChatBotGUI() {
        frame = new JFrame("Smart Study Bot");
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setFocusPainted(false);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        appendBotMessage("Hello! How are you feeling today? I can help you sort your tasks based on your mood.");
    }

    public void show() {
        frame.setVisible(true);
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        appendUserMessage(text);
        inputField.setText("");

        // Basic Bot logic
        String emotion = EmotionDetector.detectEmotion(text);
        String botResponse = ReminderBot.getResponse(emotion);
        
        appendBotMessage(botResponse);
        
        // Suggest top task if available
        Task task = SmartPlannerGUI.scheduler.getTopTask(emotion);
        if (task != null) {
            String suggestion = ReminderBot.getSmartSuggestion(emotion, task);
            String categoryTip = ReminderBot.getCategoryTip(task);
            appendBotMessage("Based on your mood, I recommend working on: " + task.name + "\n" + suggestion + "\n" + categoryTip);
        }
    }

    private void appendUserMessage(String message) {
        chatArea.append("You: " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void appendBotMessage(String message) {
        chatArea.append("Bot: " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
