package com.example.app_tt2.ui.suggestion;

public class WordSuggestion {

    private final String wrongWord;
    private final String suggestedWord;
    private final int startIndex;
    private final int endIndex;
    private final int distance;

    public WordSuggestion(
            String wrongWord,
            String suggestedWord,
            int startIndex,
            int endIndex,
            int distance
    ) {
        this.wrongWord = wrongWord;
        this.suggestedWord = suggestedWord;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.distance = distance;
    }

    public String getWrongWord() {
        return wrongWord;
    }

    public String getSuggestedWord() {
        return suggestedWord;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getDistance() {
        return distance;
    }
}