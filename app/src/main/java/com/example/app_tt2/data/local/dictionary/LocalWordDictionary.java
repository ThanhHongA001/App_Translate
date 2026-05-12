package com.example.app_tt2.data.local.dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocalWordDictionary {

    private final Set<String> englishWords = new HashSet<>();
    private final Set<String> vietnameseWords = new HashSet<>();

    public LocalWordDictionary() {
        initEnglishWords();
        initVietnameseWords();
    }

    private void initEnglishWords() {
        englishWords.addAll(Arrays.asList(
                "i", "you", "he", "she", "it", "we", "they",
                "me", "him", "her", "us", "them",
                "my", "your", "his", "their", "our",
                "a", "an", "the",
                "am", "is", "are", "was", "were", "be", "been", "being",
                "do", "does", "did", "done",
                "have", "has", "had",
                "can", "could", "will", "would", "shall", "should", "may", "might", "must",
                "go", "goes", "went", "gone", "come", "came",
                "eat", "drink", "sleep", "study", "learn", "read", "write",
                "speak", "listen", "watch", "see", "look",
                "make", "take", "give", "get", "use", "work",
                "like", "love", "want", "need", "know", "think",
                "good", "bad", "new", "old", "big", "small",
                "beautiful", "important", "different", "easy", "hard",
                "hello", "hi", "thanks", "thank", "sorry",
                "book", "school", "student", "teacher", "friend",
                "home", "house", "family", "food", "water",
                "today", "tomorrow", "yesterday",
                "morning", "afternoon", "evening", "night",
                "english", "vietnamese", "translation", "translate",
                "computer", "phone", "internet", "application", "project"
        ));
    }

    private void initVietnameseWords() {
        vietnameseWords.addAll(Arrays.asList(
                "tôi", "mình", "bạn", "anh", "chị", "em", "họ",
                "là", "của", "và", "hoặc", "nhưng", "vì", "nên",
                "có", "không", "được", "bị", "đang", "đã", "sẽ",
                "đi", "đến", "về", "ăn", "uống", "ngủ", "học",
                "đọc", "viết", "nói", "nghe", "xem", "nhìn",
                "làm", "dùng", "cần", "muốn", "biết", "nghĩ",
                "tốt", "xấu", "mới", "cũ", "lớn", "nhỏ",
                "đẹp", "quan", "trọng", "khác", "dễ", "khó",
                "xin", "chào", "cảm", "ơn", "lỗi",
                "sách", "trường", "sinh", "viên", "giáo", "viên",
                "bạn", "nhà", "gia", "đình", "thức", "ăn", "nước",
                "hôm", "nay", "ngày", "mai", "hôm", "qua",
                "sáng", "trưa", "chiều", "tối",
                "tiếng", "anh", "việt", "dịch", "bản",
                "máy", "tính", "điện", "thoại", "mạng", "ứng", "dụng", "dự", "án"
        ));
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }

        String normalized = normalize(word);

        if (normalized.isEmpty()) {
            return true;
        }

        return englishWords.contains(normalized) || vietnameseWords.contains(normalized);
    }

    public List<String> getAllWords() {
        List<String> result = new ArrayList<>();
        result.addAll(englishWords);
        result.addAll(vietnameseWords);
        return result;
    }

    public String normalize(String word) {
        if (word == null) {
            return "";
        }

        return word
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("^[^a-zA-ZÀ-ỹ]+", "")
                .replaceAll("[^a-zA-ZÀ-ỹ]+$", "");
    }
}