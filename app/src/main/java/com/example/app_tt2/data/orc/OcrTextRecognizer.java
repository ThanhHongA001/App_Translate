package com.example.app_tt2.data.orc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class OcrTextRecognizer {

    private final TextRecognizer recognizer;

    public OcrTextRecognizer() {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    public interface OcrCallback {
        void onSuccess(String text);

        void onError(Exception exception);
    }

    public void recognizeFromBitmap(Bitmap bitmap, OcrCallback callback) {
        if (bitmap == null) {
            notifyError(callback, new IllegalArgumentException("Bitmap không hợp lệ."));
            return;
        }

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        recognize(inputImage, callback);
    }

    public void recognizeFromUri(Context context, Uri imageUri, OcrCallback callback) {
        if (context == null) {
            notifyError(callback, new IllegalArgumentException("Context không hợp lệ."));
            return;
        }

        if (imageUri == null) {
            notifyError(callback, new IllegalArgumentException("Đường dẫn ảnh không hợp lệ."));
            return;
        }

        try {
            InputImage inputImage = InputImage.fromFilePath(context, imageUri);
            recognize(inputImage, callback);
        } catch (IOException exception) {
            notifyError(callback, exception);
        }
    }

    private void recognize(InputImage inputImage, OcrCallback callback) {
        recognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    String result = extractText(text);

                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                })
                .addOnFailureListener(exception -> notifyError(callback, exception));
    }

    private String extractText(Text text) {
        if (text == null) {
            return "";
        }

        String result = text.getText();

        if (result == null) {
            return "";
        }

        return result.trim();
    }

    private void notifyError(OcrCallback callback, Exception exception) {
        if (callback != null) {
            callback.onError(exception);
        }
    }

    public void close() {
        recognizer.close();
    }
}