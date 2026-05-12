package com.example.app_tt2.ui.suggestion;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class SuggestionPopupController {

    private final EditText editText;
    private final GetWordSuggestionsUseCase suggestionUseCase;
    private final SuggestionHighlighter highlighter;

    private final List<SuggestionHighlighter.WrongWordRange> wrongWordRanges =
            new ArrayList<>();

    private boolean isReplacingText = false;

    public SuggestionPopupController(EditText editText) {
        this.editText = editText;
        this.suggestionUseCase = new GetWordSuggestionsUseCase();
        this.highlighter = new SuggestionHighlighter(suggestionUseCase);

        initTextWatcher();
        initTouchListener();
    }

    public void refresh() {
        wrongWordRanges.clear();
        wrongWordRanges.addAll(highlighter.highlightWrongWords(editText));
    }

    private void initTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isReplacingText) {
                    return;
                }

                refresh();
            }
        });
    }

    private void initTouchListener() {
        editText.setOnTouchListener((view, event) -> {
            if (event.getAction() != MotionEvent.ACTION_UP) {
                return false;
            }

            int offset = editText.getOffsetForPosition(
                    event.getX(),
                    event.getY()
            );

            SuggestionHighlighter.WrongWordRange clickedRange =
                    findClickedWrongWord(offset);

            if (clickedRange != null) {
                showSuggestionPopup(clickedRange);
                return true;
            }

            return false;
        });
    }

    private SuggestionHighlighter.WrongWordRange findClickedWrongWord(int offset) {
        for (SuggestionHighlighter.WrongWordRange range : wrongWordRanges) {
            if (range.contains(offset)) {
                return range;
            }
        }

        return null;
    }

    private void showSuggestionPopup(
            SuggestionHighlighter.WrongWordRange wrongWordRange
    ) {
        List<WordSuggestion> suggestions =
                suggestionUseCase.getSuggestions(
                        wrongWordRange.getWord(),
                        wrongWordRange.getStart(),
                        wrongWordRange.getEnd()
                );

        if (suggestions.isEmpty()) {
            Toast.makeText(
                    editText.getContext(),
                    "Chưa có gợi ý phù hợp.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        PopupMenu popupMenu = new PopupMenu(editText.getContext(), editText);

        for (WordSuggestion suggestion : suggestions) {
            popupMenu.getMenu().add(suggestion.getSuggestedWord());
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedSuggestion = item.getTitle().toString();
            replaceWrongWord(wrongWordRange, selectedSuggestion);
            return true;
        });

        popupMenu.show();
    }

    private void replaceWrongWord(
            SuggestionHighlighter.WrongWordRange wrongWordRange,
            String replacement
    ) {
        Editable editable = editText.getText();

        if (wrongWordRange.getStart() < 0
                || wrongWordRange.getEnd() > editable.length()
                || wrongWordRange.getStart() >= wrongWordRange.getEnd()) {
            return;
        }

        isReplacingText = true;

        editable.replace(
                wrongWordRange.getStart(),
                wrongWordRange.getEnd(),
                replacement
        );

        editText.setSelection(
                Math.min(
                        wrongWordRange.getStart() + replacement.length(),
                        editable.length()
                )
        );

        isReplacingText = false;

        refresh();
    }
}