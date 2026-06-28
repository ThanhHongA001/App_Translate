package com.example.app_tt2.data.remote;

import com.example.app_tt2.data.remote.dto.MatchDto;
import com.example.app_tt2.data.remote.dto.TranslateResponseDto;

import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.Locale;

public final class TranslationResultSelector {

    private TranslationResultSelector() {
    }

    public static String selectBest(TranslateResponseDto dto,
                                    String originalText,
                                    String sourceLanguage,
                                    String targetLanguage) {
        if (dto == null) {
            return "";
        }

        String input = clean(originalText);
        String sourceCode = toMyMemoryCode(sourceLanguage);
        String targetCode = toMyMemoryCode(targetLanguage);
        boolean singleWord = isSingleWord(input);

        Candidate best = null;

        if (dto.getMatches() != null) {
            for (MatchDto match : dto.getMatches()) {
                if (match == null) {
                    continue;
                }

                String translation = clean(match.getTranslation());
                if (!isUsableTranslation(translation, input)) {
                    continue;
                }

                double score = normalizeScore(match.getMatchScore());

                boolean sourceOk = sameLang(match.getSource(), sourceCode);
                boolean targetOk = sameLang(match.getTarget(), targetCode);
                boolean exactSegment = equalsLoose(match.getSegment(), input);

                if (sourceOk) {
                    score += 0.15;
                } else {
                    score -= 0.70;
                }

                if (targetOk) {
                    score += 0.15;
                } else {
                    score -= 0.70;
                }

                if (exactSegment) {
                    score += 0.35;
                } else if (singleWord) {
                    score -= 0.50;
                }

                if (singleWord && hasManyWords(translation)) {
                    score -= 0.18;
                }

                if (singleWord && translation.length() > 80) {
                    score -= 0.30;
                }

                if (best == null || score > best.score) {
                    best = new Candidate(translation, score);
                }
            }
        }

        if (best != null && (!singleWord || best.score >= 0.80)) {
            return best.value;
        }

        if (dto.getResponseData() != null) {
            String translatedText = clean(dto.getResponseData().getTranslatedText());
            if (isUsableTranslation(translatedText, input)) {
                return translatedText;
            }
        }

        return best != null ? best.value : "";
    }

    public static String resolveLanguageCode(Object languageObject, String fallbackCode) {
        if (languageObject == null) {
            return toMyMemoryCode(fallbackCode);
        }

        if (languageObject instanceof String) {
            return toMyMemoryCode((String) languageObject);
        }

        String reflectedCode = readCodeByReflection(languageObject);
        if (!reflectedCode.isEmpty()) {
            return toMyMemoryCode(reflectedCode);
        }

        String label = languageObject.toString();
        if (label == null) {
            return toMyMemoryCode(fallbackCode);
        }

        return toMyMemoryCode(label);
    }

    public static String toMyMemoryCode(String languageCodeOrLabel) {
        if (languageCodeOrLabel == null) {
            return "";
        }

        String value = languageCodeOrLabel.trim().toLowerCase(Locale.US);
        if (value.isEmpty()) {
            return "";
        }

        if (value.contains("vi")
                || value.contains("vietnam")
                || value.contains("tiếng việt")
                || value.contains("tieng viet")
                || value.contains("vietnamese")) {
            return "vi";
        }

        if (value.contains("en")
                || value.contains("english")
                || value.contains("tiếng anh")
                || value.contains("tieng anh")) {
            return "en";
        }

        return baseLang(value);
    }

    public static boolean isSingleWord(String text) {
        String value = clean(text);
        if (value.isEmpty()) {
            return false;
        }

        return !value.matches(".*\\s+.*");
    }

    public static String clean(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("&#39;", "'")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String normalizeVietnameseKey(String input) {
        if (input == null) {
            return "";
        }

        return Normalizer
                .normalize(input.trim(), Normalizer.Form.NFC)
                .toLowerCase(new Locale("vi", "VN"));
    }

    public static String removeAccent(String input) {
        if (input == null) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("đ", "d")
                .replace("Đ", "D")
                .toLowerCase(Locale.US)
                .trim();
    }

    private static String readCodeByReflection(Object languageObject) {
        String[] methodNames = {
                "getCode",
                "getLanguageCode",
                "getApiCode",
                "getValue",
                "getLocaleCode"
        };

        for (String methodName : methodNames) {
            try {
                Method method = languageObject.getClass().getMethod(methodName);
                Object value = method.invoke(languageObject);
                if (value != null) {
                    String code = value.toString().trim();
                    if (!code.isEmpty()) {
                        return code;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return "";
    }

    private static boolean isUsableTranslation(String translation, String input) {
        if (translation == null || translation.trim().isEmpty()) {
            return false;
        }

        if (equalsLoose(translation, input)) {
            return false;
        }

        String lower = translation.toLowerCase(Locale.US);
        return !lower.contains("null")
                && !lower.contains("mymemory warning")
                && !lower.contains("quota")
                && !lower.contains("invalid language pair");
    }

    private static boolean sameLang(String apiLang, String expectedBaseLang) {
        return baseLang(apiLang).equals(baseLang(expectedBaseLang));
    }

    private static String baseLang(String code) {
        if (code == null) {
            return "";
        }

        String value = code.trim().toLowerCase(Locale.US);
        int dash = value.indexOf('-');
        int underscore = value.indexOf('_');
        int cut = -1;

        if (dash >= 0 && underscore >= 0) {
            cut = Math.min(dash, underscore);
        } else if (dash >= 0) {
            cut = dash;
        } else if (underscore >= 0) {
            cut = underscore;
        }

        return cut >= 0 ? value.substring(0, cut) : value;
    }

    private static boolean equalsLoose(String a, String b) {
        return removeAccent(clean(a)).equals(removeAccent(clean(b)));
    }

    private static boolean hasManyWords(String text) {
        String value = clean(text);
        if (value.isEmpty()) {
            return false;
        }

        String[] parts = value.split("\\s+");
        return parts.length >= 6;
    }

    private static double normalizeScore(double value) {
        if (value < 0.0) {
            return 0.0;
        }

        if (value > 1.0) {
            return value / 100.0;
        }

        return value;
    }

    private static final class Candidate {
        final String value;
        final double score;

        Candidate(String value, double score) {
            this.value = value;
            this.score = score;
        }
    }
}