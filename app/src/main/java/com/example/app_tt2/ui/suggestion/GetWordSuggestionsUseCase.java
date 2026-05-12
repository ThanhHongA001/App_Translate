package com.example.app_tt2.ui.suggestion;

import com.example.app_tt2.data.local.dictionary.LocalWordDictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GetWordSuggestionsUseCase {

    private static final int MAX_SUGGESTION_COUNT = 5;

    private final LocalWordDictionary dictionary;

    public GetWordSuggestionsUseCase() {
        this.dictionary = new LocalWordDictionary();
    }

    public boolean isCorrectWord(String word) {
        return dictionary.contains(word);
    }

    public List<WordSuggestion> getSuggestions(
            String wrongWord,
            int startIndex,
            int endIndex
    ) {
        List<WordSuggestion> result = new ArrayList<>();

        if (wrongWord == null || wrongWord.trim().isEmpty()) {
            return result;
        }

        String normalizedWrongWord = dictionary.normalize(wrongWord);

        if (normalizedWrongWord.length() <= 1) {
            return result;
        }

        List<String> allWords = dictionary.getAllWords();

        for (String candidate : allWords) {
            int distance = calculateLevenshteinDistance(
                    normalizedWrongWord,
                    dictionary.normalize(candidate)
            );

            int maxDistance = getAllowedDistance(normalizedWrongWord.length());

            if (distance <= maxDistance) {
                result.add(new WordSuggestion(
                        wrongWord,
                        candidate,
                        startIndex,
                        endIndex,
                        distance
                ));
            }
        }

        result.sort(
                Comparator
                        .comparingInt(WordSuggestion::getDistance)
                        .thenComparing(WordSuggestion::getSuggestedWord)
        );

        if (result.size() > MAX_SUGGESTION_COUNT) {
            return new ArrayList<>(result.subList(0, MAX_SUGGESTION_COUNT));
        }

        return result;
    }

    private int getAllowedDistance(int length) {
        if (length <= 3) {
            return 1;
        }

        if (length <= 6) {
            return 2;
        }

        return 3;
    }

    private int calculateLevenshteinDistance(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();

        int[][] dp = new int[firstLength + 1][secondLength + 1];

        for (int i = 0; i <= firstLength; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= secondLength; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= firstLength; i++) {
            for (int j = 1; j <= secondLength; j++) {
                int cost = first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[firstLength][secondLength];
    }
}