package com.example.app_tt2.data.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.example.app_tt2.utils.LanguageCodeMapper;

import java.util.Locale;

public class TextToSpeechManager implements TextToSpeech.OnInitListener {

    private final Context appContext;
    private TextToSpeech textToSpeech;

    private boolean ready = false;
    private Locale currentLocale = Locale.US;

    public TextToSpeechManager(Context context) {
        appContext = context.getApplicationContext();
        textToSpeech = new TextToSpeech(appContext, this);
    }

    @Override
    public void onInit(int status) {
        ready = status == TextToSpeech.SUCCESS;

        if (ready && textToSpeech != null) {
            textToSpeech.setLanguage(currentLocale);
        }
    }

    public boolean isReady() {
        return ready && textToSpeech != null;
    }

    public boolean speak(String text, String languageCode) {
        if (!isReady()) {
            return false;
        }

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        Locale locale = LanguageCodeMapper.toLocale(languageCode);
        int languageResult = textToSpeech.setLanguage(locale);

        if (languageResult == TextToSpeech.LANG_MISSING_DATA
                || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            return false;
        }

        currentLocale = locale;

        String utteranceId = "tts_" + System.currentTimeMillis();

        int speakResult = textToSpeech.speak(
                text.trim(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                utteranceId
        );

        return speakResult == TextToSpeech.SUCCESS;
    }

    public boolean setLanguage(String languageCode) {
        if (!isReady()) {
            currentLocale = LanguageCodeMapper.toLocale(languageCode);
            return false;
        }

        Locale locale = LanguageCodeMapper.toLocale(languageCode);
        int result = textToSpeech.setLanguage(locale);

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            return false;
        }

        currentLocale = locale;
        return true;
    }

    public boolean setSpeechRate(float rate) {
        if (!isReady()) {
            return false;
        }

        float safeRate = Math.max(0.1f, Math.min(rate, 2.0f));
        return textToSpeech.setSpeechRate(safeRate) == TextToSpeech.SUCCESS;
    }

    public boolean setPitch(float pitch) {
        if (!isReady()) {
            return false;
        }

        float safePitch = Math.max(0.1f, Math.min(pitch, 2.0f));
        return textToSpeech.setPitch(safePitch) == TextToSpeech.SUCCESS;
    }

    public boolean isLanguageSupported(String languageCode) {
        if (!isReady()) {
            return false;
        }

        Locale locale = LanguageCodeMapper.toLocale(languageCode);
        int result = textToSpeech.isLanguageAvailable(locale);

        return result == TextToSpeech.LANG_AVAILABLE
                || result == TextToSpeech.LANG_COUNTRY_AVAILABLE
                || result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void shutdown() {
        ready = false;

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }
}