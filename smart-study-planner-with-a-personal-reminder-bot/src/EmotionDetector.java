 import java.util.*;

    public class EmotionDetector {

        private static Map<String, List<String>> emotionKeywords = new HashMap<>();

        static {
            emotionKeywords.put("tired", Arrays.asList("tired", "sleepy", "exhausted", "drained", "lazy"));
            emotionKeywords.put("stressed", Arrays.asList("stressed", "pressure", "overwhelmed", "anxious", "tense"));
            emotionKeywords.put("happy", Arrays.asList("happy", "good", "great", "excited", "motivated"));
            emotionKeywords.put("sad", Arrays.asList("sad", "down", "upset", "lonely", "depressed"));
        }

        public static String detectEmotion(String input) {
            input = input.toLowerCase();

            Map<String, Integer> scores = new HashMap<>();


            for (String emotion : emotionKeywords.keySet()) {
                scores.put(emotion, 0);
            }


            String[] words = input.split("\\s+");


            for (String word : words) {
                for (String emotion : emotionKeywords.keySet()) {
                    if (emotionKeywords.get(emotion).contains(word)) {
                        scores.put(emotion, scores.get(emotion) + 1);
                    }
                }
            }
            String detectedEmotion = "neutral";
            int maxScore = 0;

            for (String emotion : scores.keySet()) {
                if (scores.get(emotion) > maxScore) {
                    maxScore = scores.get(emotion);
                    detectedEmotion = emotion;
                }
            }

            return detectedEmotion;
        }
    }

