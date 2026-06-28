package com.example.app_tt2.data.local.dictionary;

import com.example.app_tt2.data.remote.TranslationResultSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocalWordDictionary {

    private static final Map<String, String> EN_VI;
    private static final Map<String, String> VI_EN;
    private static final Set<String> KNOWN_WORDS;

    static {
        Map<String, String> enVi = new HashMap<>();
        putEnVi(enVi, "hello", "xin chào");
        putEnVi(enVi, "hi", "chào");
        putEnVi(enVi, "goodbye", "tạm biệt");
        putEnVi(enVi, "bye", "tạm biệt");
        putEnVi(enVi, "yes", "có");
        putEnVi(enVi, "no", "không");
        putEnVi(enVi, "not", "không");
        putEnVi(enVi, "thanks", "cảm ơn");
        putEnVi(enVi, "thank", "cảm ơn");
        putEnVi(enVi, "please", "làm ơn; vui lòng");
        putEnVi(enVi, "sorry", "xin lỗi");
        putEnVi(enVi, "book", "sách; đặt chỗ");
        putEnVi(enVi, "pen", "bút");
        putEnVi(enVi, "pencil", "bút chì");
        putEnVi(enVi, "school", "trường học");
        putEnVi(enVi, "student", "sinh viên; học sinh");
        putEnVi(enVi, "teacher", "giáo viên");
        putEnVi(enVi, "computer", "máy tính");
        putEnVi(enVi, "phone", "điện thoại");
        putEnVi(enVi, "water", "nước");
        putEnVi(enVi, "food", "thức ăn");
        putEnVi(enVi, "rice", "cơm; gạo");
        putEnVi(enVi, "house", "nhà");
        putEnVi(enVi, "home", "nhà; quê nhà");
        putEnVi(enVi, "room", "phòng");
        putEnVi(enVi, "car", "xe hơi; ô tô");
        putEnVi(enVi, "bike", "xe đạp; xe máy");
        putEnVi(enVi, "cat", "mèo");
        putEnVi(enVi, "dog", "chó");
        putEnVi(enVi, "bird", "chim");
        putEnVi(enVi, "fish", "cá");
        putEnVi(enVi, "love", "yêu; tình yêu");
        putEnVi(enVi, "like", "thích; giống như");
        putEnVi(enVi, "learn", "học");
        putEnVi(enVi, "study", "học; nghiên cứu");
        putEnVi(enVi, "work", "làm việc; công việc");
        putEnVi(enVi, "job", "công việc");
        putEnVi(enVi, "go", "đi");
        putEnVi(enVi, "come", "đến");
        putEnVi(enVi, "eat", "ăn");
        putEnVi(enVi, "drink", "uống");
        putEnVi(enVi, "sleep", "ngủ");
        putEnVi(enVi, "read", "đọc");
        putEnVi(enVi, "write", "viết");
        putEnVi(enVi, "speak", "nói");
        putEnVi(enVi, "listen", "nghe");
        putEnVi(enVi, "see", "nhìn thấy; xem");
        putEnVi(enVi, "watch", "xem; đồng hồ đeo tay");
        putEnVi(enVi, "open", "mở");
        putEnVi(enVi, "close", "đóng; gần");
        putEnVi(enVi, "big", "to; lớn");
        putEnVi(enVi, "small", "nhỏ");
        putEnVi(enVi, "good", "tốt");
        putEnVi(enVi, "bad", "xấu; tệ");
        putEnVi(enVi, "new", "mới");
        putEnVi(enVi, "old", "cũ; già");
        putEnVi(enVi, "hot", "nóng");
        putEnVi(enVi, "cold", "lạnh");
        putEnVi(enVi, "right", "đúng; bên phải; quyền");
        putEnVi(enVi, "left", "bên trái; rời đi");
        putEnVi(enVi, "light", "ánh sáng; nhẹ");
        putEnVi(enVi, "dark", "tối");
        putEnVi(enVi, "can", "có thể; lon");
        putEnVi(enVi, "road", "đường");
        putEnVi(enVi, "way", "cách; đường");
        putEnVi(enVi, "sugar", "đường ăn");
        putEnVi(enVi, "country", "đất nước; quốc gia");
        putEnVi(enVi, "city", "thành phố");
        putEnVi(enVi, "people", "người; mọi người");
        putEnVi(enVi, "friend", "bạn bè");
        putEnVi(enVi, "family", "gia đình");
        putEnVi(enVi, "father", "cha; bố");
        putEnVi(enVi, "mother", "mẹ");
        putEnVi(enVi, "brother", "anh trai; em trai");
        putEnVi(enVi, "sister", "chị gái; em gái");
        putEnVi(enVi, "day", "ngày");
        putEnVi(enVi, "night", "đêm");
        putEnVi(enVi, "time", "thời gian; lần");
        putEnVi(enVi, "money", "tiền");
        putEnVi(enVi, "name", "tên");
        putEnVi(enVi, "language", "ngôn ngữ");
        putEnVi(enVi, "english", "tiếng Anh");
        putEnVi(enVi, "vietnamese", "tiếng Việt; người Việt");
        putEnVi(enVi, "red", "màu đỏ");
        putEnVi(enVi, "green", "màu xanh lá");
        putEnVi(enVi, "blue", "màu xanh dương");
        putEnVi(enVi, "black", "màu đen");
        putEnVi(enVi, "white", "màu trắng");
        putEnVi(enVi, "yellow", "màu vàng");
        EN_VI = Collections.unmodifiableMap(enVi);

        Map<String, String> viEn = new HashMap<>();
        putViEn(viEn, "xin chào", "hello");
        putViEn(viEn, "chào", "hello; hi");
        putViEn(viEn, "tạm biệt", "goodbye; bye");
        putViEn(viEn, "có", "yes; have");
        putViEn(viEn, "không", "no; not");
        putViEn(viEn, "cảm ơn", "thanks; thank you");
        putViEn(viEn, "xin lỗi", "sorry");
        putViEn(viEn, "sách", "book");
        putViEn(viEn, "bút", "pen");
        putViEn(viEn, "bút chì", "pencil");
        putViEn(viEn, "trường", "school");
        putViEn(viEn, "trường học", "school");
        putViEn(viEn, "sinh viên", "student");
        putViEn(viEn, "học sinh", "student; pupil");
        putViEn(viEn, "giáo viên", "teacher");
        putViEn(viEn, "máy tính", "computer");
        putViEn(viEn, "điện thoại", "phone");
        putViEn(viEn, "nước", "water; country");
        putViEn(viEn, "đất nước", "country");
        putViEn(viEn, "quốc gia", "country; nation");
        putViEn(viEn, "thức ăn", "food");
        putViEn(viEn, "cơm", "rice; meal");
        putViEn(viEn, "gạo", "rice");
        putViEn(viEn, "nhà", "house; home");
        putViEn(viEn, "phòng", "room");
        putViEn(viEn, "xe", "vehicle; car");
        putViEn(viEn, "xe hơi", "car");
        putViEn(viEn, "ô tô", "car");
        putViEn(viEn, "xe đạp", "bike; bicycle");
        putViEn(viEn, "mèo", "cat");
        putViEn(viEn, "chó", "dog");
        putViEn(viEn, "chim", "bird");
        putViEn(viEn, "cá", "fish");
        putViEn(viEn, "yêu", "love");
        putViEn(viEn, "tình yêu", "love");
        putViEn(viEn, "thích", "like");
        putViEn(viEn, "học", "learn; study");
        putViEn(viEn, "nghiên cứu", "study; research");
        putViEn(viEn, "làm", "do; make; work");
        putViEn(viEn, "làm việc", "work");
        putViEn(viEn, "công việc", "work; job");
        putViEn(viEn, "đi", "go");
        putViEn(viEn, "đến", "come; arrive");
        putViEn(viEn, "ăn", "eat");
        putViEn(viEn, "uống", "drink");
        putViEn(viEn, "ngủ", "sleep");
        putViEn(viEn, "đọc", "read");
        putViEn(viEn, "viết", "write");
        putViEn(viEn, "nói", "speak; say");
        putViEn(viEn, "nghe", "listen; hear");
        putViEn(viEn, "xem", "watch; see");
        putViEn(viEn, "mở", "open");
        putViEn(viEn, "đóng", "close");
        putViEn(viEn, "to", "big; large");
        putViEn(viEn, "lớn", "big; large");
        putViEn(viEn, "nhỏ", "small");
        putViEn(viEn, "tốt", "good");
        putViEn(viEn, "xấu", "bad; ugly");
        putViEn(viEn, "tệ", "bad");
        putViEn(viEn, "mới", "new");
        putViEn(viEn, "cũ", "old");
        putViEn(viEn, "già", "old");
        putViEn(viEn, "nóng", "hot");
        putViEn(viEn, "lạnh", "cold");
        putViEn(viEn, "đúng", "right; correct");
        putViEn(viEn, "phải", "right; must");
        putViEn(viEn, "trái", "left; opposite");
        putViEn(viEn, "ánh sáng", "light");
        putViEn(viEn, "nhẹ", "light");
        putViEn(viEn, "đường", "road; way; sugar");
        putViEn(viEn, "đường ăn", "sugar");
        putViEn(viEn, "cách", "way; method");
        putViEn(viEn, "thành phố", "city");
        putViEn(viEn, "người", "person; people");
        putViEn(viEn, "bạn", "friend; you");
        putViEn(viEn, "gia đình", "family");
        putViEn(viEn, "cha", "father");
        putViEn(viEn, "bố", "father");
        putViEn(viEn, "mẹ", "mother");
        putViEn(viEn, "anh", "older brother; you; he");
        putViEn(viEn, "chị", "older sister; you; she");
        putViEn(viEn, "em", "younger sibling; you");
        putViEn(viEn, "ngày", "day");
        putViEn(viEn, "đêm", "night");
        putViEn(viEn, "thời gian", "time");
        putViEn(viEn, "tiền", "money");
        putViEn(viEn, "tên", "name");
        putViEn(viEn, "ngôn ngữ", "language");
        putViEn(viEn, "tiếng anh", "English");
        putViEn(viEn, "tiếng việt", "Vietnamese");
        putViEn(viEn, "đỏ", "red");
        putViEn(viEn, "xanh lá", "green");
        putViEn(viEn, "xanh dương", "blue");
        putViEn(viEn, "đen", "black");
        putViEn(viEn, "trắng", "white");
        putViEn(viEn, "vàng", "yellow");
        VI_EN = Collections.unmodifiableMap(viEn);

        Set<String> knownWords = new HashSet<>();
        knownWords.addAll(EN_VI.keySet());
        knownWords.addAll(VI_EN.keySet());
        KNOWN_WORDS = Collections.unmodifiableSet(knownWords);
    }

    public LocalWordDictionary() {
    }

    public static String translateSingleWord(String input, String sourceLanguage, String targetLanguage) {
        if (input == null) {
            return null;
        }

        String text = input.trim();
        if (text.isEmpty()) {
            return null;
        }

        String sourceCode = TranslationResultSelector.toMyMemoryCode(sourceLanguage);
        String targetCode = TranslationResultSelector.toMyMemoryCode(targetLanguage);

        if ("en".equals(sourceCode) && "vi".equals(targetCode)) {
            return EN_VI.get(text.toLowerCase(Locale.US));
        }

        if ("vi".equals(sourceCode) && "en".equals(targetCode)) {
            return VI_EN.get(key(text));
        }

        return null;
    }

    public static boolean isKnownWord(String word) {
        if (word == null) {
            return false;
        }

        String raw = word.trim().toLowerCase(Locale.US);
        String viKey = key(word);
        return EN_VI.containsKey(raw) || VI_EN.containsKey(viKey) || KNOWN_WORDS.contains(raw) || KNOWN_WORDS.contains(viKey);
    }

    public static boolean contains(String word) {
        return isKnownWord(word);
    }

    public static List<String> getSuggestions(String word) {
        return getSuggestions(word, 5);
    }

    public static List<String> getSuggestions(String word, int limit) {
        return findSuggestions(word, limit);
    }

    public static List<String> findSuggestions(String word) {
        return findSuggestions(word, 5);
    }

    public static List<String> findSuggestions(String word, int limit) {
        if (word == null || word.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String input = word.trim();
        String normalizedInput = normalizeForCompare(input);
        List<ScoredWord> scoredWords = new ArrayList<>();

        for (String candidate : KNOWN_WORDS) {
            String normalizedCandidate = normalizeForCompare(candidate);
            int distance = levenshtein(normalizedInput, normalizedCandidate);

            if (distance <= Math.max(1, normalizedInput.length() / 3)) {
                scoredWords.add(new ScoredWord(candidate, distance));
            }
        }

        Collections.sort(scoredWords, (first, second) -> {
            int compareDistance = Integer.compare(first.distance, second.distance);
            if (compareDistance != 0) {
                return compareDistance;
            }
            return first.word.compareTo(second.word);
        });

        List<String> result = new ArrayList<>();
        int max = Math.max(1, limit);

        for (ScoredWord scoredWord : scoredWords) {
            if (result.size() >= max) {
                break;
            }
            result.add(scoredWord.word);
        }

        return result;
    }

    public static Set<String> getAllWords() {
        return KNOWN_WORDS;
    }

    public static Set<String> getWords() {
        return KNOWN_WORDS;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return normalizeForCompare(value);
    }

    private static void putEnVi(Map<String, String> map, String key, String value) {
        map.put(key.toLowerCase(Locale.US), value);
    }

    private static void putViEn(Map<String, String> map, String key, String value) {
        map.put(key(key), value);
    }

    private static String key(String value) {
        return TranslationResultSelector.normalizeVietnameseKey(value);
    }

    private static String normalizeForCompare(String value) {
        return TranslationResultSelector.removeAccent(value)
                .replaceAll("[^a-z0-9]+", "")
                .toLowerCase(Locale.US)
                .trim();
    }

    private static int levenshtein(String first, String second) {
        int[][] dp = new int[first.length() + 1][second.length() + 1];

        for (int i = 0; i <= first.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= second.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                int cost = first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[first.length()][second.length()];
    }

    private static final class ScoredWord {
        final String word;
        final int distance;

        ScoredWord(String word, int distance) {
            this.word = word;
            this.distance = distance;
        }
    }
}
