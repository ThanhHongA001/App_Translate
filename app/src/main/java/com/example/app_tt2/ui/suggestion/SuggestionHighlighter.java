package com.example.app_tt2.ui.suggestion;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SuggestionHighlighter {

    private static final Pattern WORD_PATTERN =
            Pattern.compile("[a-zA-ZÀ-ỹ]+");

    private final GetWordSuggestionsUseCase suggestionUseCase;

    SuggestionHighlighter(GetWordSuggestionsUseCase suggestionUseCase) {
        this.suggestionUseCase = suggestionUseCase;
    }

    List<WrongWordRange> highlightWrongWords(EditText editText) {
        List<WrongWordRange> wrongWords = new ArrayList<>();

        Editable editable = editText.getText();

        RedUnderlineSpan[] oldSpans = editable.getSpans(
                0,
                editable.length(),
                RedUnderlineSpan.class
        );

        for (RedUnderlineSpan span : oldSpans) {
            editable.removeSpan(span);
        }

        String input = editable.toString();
        Matcher matcher = WORD_PATTERN.matcher(input);

        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            if (!suggestionUseCase.isCorrectWord(word)) {
                editable.setSpan(
                        new RedUnderlineSpan(),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                wrongWords.add(new WrongWordRange(word, start, end));
            }
        }

        return wrongWords;
    }

    static class WrongWordRange {

        private final String word;
        private final int start;
        private final int end;

        WrongWordRange(String word, int start, int end) {
            this.word = word;
            this.start = start;
            this.end = end;
        }

        String getWord() {
            return word;
        }

        int getStart() {
            return start;
        }

        int getEnd() {
            return end;
        }

        boolean contains(int index) {
            return index >= start && index <= end;
        }
    }

    static class RedUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(android.text.TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(Color.RED);
            textPaint.setUnderlineText(true);
        }
    }
}