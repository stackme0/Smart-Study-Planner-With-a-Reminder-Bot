import java.util.*;

    public class ReminderBot {

        private static Map<String, List<String>> responses = new HashMap<>();

        static {
            responses.put("tired", Arrays.asList(
                    "You've been pushing yourself... take a short break.",
                    "Let's slow down a bit. Maybe start with something light?",
                    "Even machines need rest. You deserve one too.",
                    "Try doing just one small task. No pressure."
            ));

            responses.put("stressed", Arrays.asList(
                    "Hey... breathe. One step at a time.",
                    "You don't have to do everything right now.",
                    "Let's break your work into smaller pieces.",
                    "You're stronger than this moment."
            ));

            responses.put("happy", Arrays.asList(
                    "Love this energy! Let's use it well.",
                    "You're doing amazing. Keep going!",
                    "Perfect time to finish your tasks!",
                    "This momentum? Don't waste it."
            ));

            responses.put("sad", Arrays.asList(
                    "It's okay to feel this way. Start small.",
                    "You don't have to rush. Just begin with one task.",
                    "I'm here... let's take it slow together.",
                    "Even a little progress is still progress."
            ));

            responses.put("neutral", Arrays.asList(
                    "Let's focus and get something done.",
                    "A small step now can save time later.",
                    "Start with your easiest task first.",
                    "Consistency beats motivation."
            ));
        }

        public static String getResponse(String emotion) {
            Random rand = new Random();

            List<String> msgs = responses.getOrDefault(emotion, responses.get("neutral"));

            return msgs.get(rand.nextInt(msgs.size()));
        }
        public static String getSmartSuggestion(String emotion,Task task) {

            return switch (emotion) {
                case "tired" -> " Suggestion: Revise notes or do light reading.";
                case "stressed" -> " Suggestion: Break your task into smaller parts.";
                case "happy" -> " Suggestion: Try completing your hardest task now.";
                case "sad" -> " Suggestion: Start with an easy subject.";
                default -> " Suggestion: Pick one task and start now.";
            };
        }
        public static String getCategoryTip(Task task) {

            if (task == null) return "";
            return switch (task.category.toLowerCase()) {
                case "study" -> " Focus deeply, avoid distractions.";
                case "revision" -> " Revise actively, not passively.";
                case "practice" -> " Practice questions for better retention.";
                default -> " Stay consistent.";
            };
        }
    }


