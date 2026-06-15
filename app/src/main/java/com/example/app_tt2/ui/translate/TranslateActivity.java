package com.example.app_tt2.ui.translate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_tt2.R;
import com.example.app_tt2.data.orc.OcrTextRecognizer;
import com.example.app_tt2.data.speech.TextToSpeechManager;
import com.example.app_tt2.ui.evaluation.EvaluationActivity;
import com.example.app_tt2.ui.history.HistoryActivity;
import com.example.app_tt2.ui.suggestion.SuggestionPopupController;
import com.example.app_tt2.utils.Constants;
import com.example.app_tt2.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslateActivity extends AppCompatActivity {

    private EditText edtInputText;
    private TextView tvTranslatedText;
    private TextView tvStatus;

    private Spinner spinnerSourceLanguage;
    private Spinner spinnerTargetLanguage;

    private Button btnTranslate;
    private ImageButton btnClear;
    private ImageButton btnCopy;
    private ImageButton btnOcrImage;
    private ImageButton btnSpeakInput;
    private ImageButton btnSpeakResult;

    private ImageButton btnSwapLanguage;
    private ImageButton btnMenu;
    private ImageButton btnHistory;

    private ProgressBar progressBar;

    private TranslateViewModel viewModel;
    private SuggestionPopupController suggestionPopupController;

    private OcrTextRecognizer ocrTextRecognizer;
    private TextToSpeechManager textToSpeechManager;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;

    private ActivityResultLauncher<Intent> speechInputLauncher;
    private ActivityResultLauncher<String> audioPermissionLauncher;

    private Uri cameraImageUri;

    private final List<LanguageOption> languageOptions = new ArrayList<>();

    private long translationStartTime = 0L;
    private long lastTranslationDuration = 0L;
    private String lastSourceText = "";
    private String lastTranslatedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translate);

        initViews();
        initSuggestions();
        initOcrAndSpeech();
        initLanguages();
        initViewModel();
        initEvents();
        observeUiState();
    }

    private void initViews() {
        edtInputText = findViewById(R.id.translate_EdtInputText);

        tvTranslatedText = findViewById(R.id.translate_TvTranslatedText);
        tvStatus = findViewById(R.id.translate_TvResultLabel);

        spinnerSourceLanguage = findViewById(R.id.translate_TvSourceLanguage);
        spinnerTargetLanguage = findViewById(R.id.translate_TvTargetLanguage);

        btnTranslate = findViewById(R.id.translate_BtnTranslate);
        btnClear = findViewById(R.id.translate_BtnClear);
        btnCopy = findViewById(R.id.translate_BtnCopy);
        btnOcrImage = findViewById(R.id.translate_BtnOcrImage);
        btnSpeakInput = findViewById(R.id.translate_BtnSpeakInput);
        btnSpeakResult = findViewById(R.id.translate_BtnSpeakResult);

        btnSwapLanguage = findViewById(R.id.translate_BtnSwapLanguage);
        btnMenu = findViewById(R.id.translate_BtnMenu);
        btnHistory = findViewById(R.id.translate_BtnHistory);

        progressBar = findViewById(R.id.translate_ProgressBar);

        tvTranslatedText.setMovementMethod(new ScrollingMovementMethod());
        tvTranslatedText.setVerticalScrollBarEnabled(true);

        tvTranslatedText.setOnTouchListener((view, event) -> {
            view.getParent().requestDisallowInterceptTouchEvent(true);

            if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }

            return false;
        });
    }

    private void initSuggestions() {
        suggestionPopupController = new SuggestionPopupController(edtInputText);
    }

    private void initOcrAndSpeech() {
        ocrTextRecognizer = new OcrTextRecognizer();
        textToSpeechManager = new TextToSpeechManager(this);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        recognizeTextFromImage(uri);
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (Boolean.TRUE.equals(success) && cameraImageUri != null) {
                        recognizeTextFromImage(cameraImageUri);
                    } else {
                        Toast.makeText(
                                this,
                                "Không chụp được ảnh.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (Boolean.TRUE.equals(granted)) {
                        launchCameraForOcr();
                    } else {
                        Toast.makeText(
                                this,
                                "Bạn cần cấp quyền camera để chụp ảnh.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        speechInputLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK) {
                        tvStatus.setText("Đã hủy nhập giọng nói");
                        return;
                    }

                    Intent data = result.getData();

                    if (data == null) {
                        tvStatus.setText("Không nhận được dữ liệu giọng nói");

                        Toast.makeText(
                                this,
                                "Không nhận được dữ liệu giọng nói.",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    ArrayList<String> speechResults =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (speechResults == null || speechResults.isEmpty()) {
                        tvStatus.setText("Không nhận dạng được giọng nói");

                        Toast.makeText(
                                this,
                                "Không nhận dạng được giọng nói.",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    String recognizedText = speechResults.get(0);
                    appendRecognizedSpeechToInput(recognizedText);
                }
        );

        audioPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (Boolean.TRUE.equals(granted)) {
                        launchSpeechInput();
                    } else {
                        Toast.makeText(
                                this,
                                "Bạn cần cấp quyền micro để nhập văn bản bằng giọng nói.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void initLanguages() {
        languageOptions.clear();

        languageOptions.add(LanguageOption.english());
        languageOptions.add(LanguageOption.vietnamese());

        ArrayAdapter<LanguageOption> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                languageOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceLanguage.setAdapter(adapter);
        spinnerTargetLanguage.setAdapter(adapter);

        spinnerSourceLanguage.setSelection(0);
        spinnerTargetLanguage.setSelection(1);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TranslateViewModel.class);
    }

    private void initEvents() {
        btnTranslate.setOnClickListener(v -> translateText());

        btnSwapLanguage.setOnClickListener(v -> swapLanguageAndTranslateAgain());

        btnClear.setOnClickListener(v -> clearTranslateScreen());

        btnHistory.setOnClickListener(v -> openHistoryScreen());

        btnMenu.setOnClickListener(v -> showPopupMenu());

        btnCopy.setOnClickListener(v -> copyTranslatedText());

        btnOcrImage.setOnClickListener(v -> showOcrSourceChooser());

        btnSpeakInput.setOnClickListener(v -> openSpeechInput());

        btnSpeakResult.setOnClickListener(v -> speakTranslatedText());
    }

    private void translateText() {
        String inputText = edtInputText.getText().toString().trim();

        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(
                    this,
                    "Vui lòng nhập nội dung cần dịch.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        LanguageOption sourceLanguage =
                (LanguageOption) spinnerSourceLanguage.getSelectedItem();

        LanguageOption targetLanguage =
                (LanguageOption) spinnerTargetLanguage.getSelectedItem();

        if (sourceLanguage == null || targetLanguage == null) {
            Toast.makeText(
                    this,
                    "Vui lòng chọn ngôn ngữ dịch.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        translationStartTime = System.currentTimeMillis();
        lastSourceText = inputText;
        lastTranslatedText = "";
        lastTranslationDuration = 0L;

        viewModel.translate(inputText, sourceLanguage, targetLanguage);
    }

    private void swapLanguageAndTranslateAgain() {
        int sourcePosition = spinnerSourceLanguage.getSelectedItemPosition();
        int targetPosition = spinnerTargetLanguage.getSelectedItemPosition();

        spinnerSourceLanguage.setSelection(targetPosition);
        spinnerTargetLanguage.setSelection(sourcePosition);

        if (!TextUtils.isEmpty(lastTranslatedText)) {
            edtInputText.setText(lastTranslatedText);
            edtInputText.setSelection(edtInputText.getText().length());

            if (suggestionPopupController != null) {
                suggestionPopupController.refresh();
            }

            tvTranslatedText.setText("");
            tvStatus.setText("Đang dịch lại...");

            translateText();
        }
    }

    private void clearTranslateScreen() {
        edtInputText.setText("");
        tvTranslatedText.setText("");
        tvStatus.setText("Đã xóa nội dung");

        translationStartTime = 0L;
        lastTranslationDuration = 0L;
        lastSourceText = "";
        lastTranslatedText = "";

        if (textToSpeechManager != null) {
            textToSpeechManager.stop();
        }

        if (suggestionPopupController != null) {
            suggestionPopupController.refresh();
        }
    }

    private void copyTranslatedText() {
        String translatedText = tvTranslatedText.getText().toString().trim();

        if (TextUtils.isEmpty(translatedText)) {
            Toast.makeText(
                    this,
                    "Chưa có nội dung để sao chép.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData =
                ClipData.newPlainText(
                        "translated_text",
                        translatedText
                );

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(
                this,
                "Đã sao chép kết quả dịch.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showOcrSourceChooser() {
        String[] options = {
                "Chụp ảnh trực tiếp",
                "Chọn ảnh từ thư viện"
        };

        new AlertDialog.Builder(this)
                .setTitle("Nhận dạng chữ từ ảnh")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCameraForOcr();
                    } else {
                        openImagePickerForOcr();
                    }
                })
                .show();
    }

    private void openImagePickerForOcr() {
        imagePickerLauncher.launch("image/*");
    }

    private void openCameraForOcr() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCameraForOcr();
            return;
        }

        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void launchCameraForOcr() {
        try {
            cameraImageUri = createCameraImageUri();
            cameraLauncher.launch(cameraImageUri);
        } catch (IOException exception) {
            Toast.makeText(
                    this,
                    "Không thể tạo file ảnh: " + exception.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private Uri createCameraImageUri() throws IOException {
        File imageDirectory = new File(getCacheDir(), "ocr_images");

        if (!imageDirectory.exists()) {
            boolean created = imageDirectory.mkdirs();

            if (!created) {
                throw new IOException("Không thể tạo thư mục lưu ảnh tạm.");
            }
        }

        File imageFile = File.createTempFile(
                "ocr_",
                ".jpg",
                imageDirectory
        );

        return FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                imageFile
        );
    }

    private void recognizeTextFromImage(Uri imageUri) {
        if (ocrTextRecognizer == null) {
            Toast.makeText(
                    this,
                    "Bộ nhận dạng OCR chưa sẵn sàng.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        setOcrLoading(true);
        tvStatus.setText("Đang nhận dạng chữ từ ảnh...");

        ocrTextRecognizer.recognizeFromUri(
                this,
                imageUri,
                new OcrTextRecognizer.OcrCallback() {
                    @Override
                    public void onSuccess(String text) {
                        runOnUiThread(() -> {
                            setOcrLoading(false);

                            if (TextUtils.isEmpty(text)) {
                                tvStatus.setText("Không nhận dạng được chữ");

                                Toast.makeText(
                                        TranslateActivity.this,
                                        "Không tìm thấy văn bản trong ảnh.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }

                            edtInputText.setText(text);
                            edtInputText.setSelection(edtInputText.getText().length());

                            if (suggestionPopupController != null) {
                                suggestionPopupController.refresh();
                            }

                            tvStatus.setText("Đã nhận dạng văn bản từ ảnh");
                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        runOnUiThread(() -> {
                            setOcrLoading(false);
                            tvStatus.setText("Lỗi OCR");

                            Toast.makeText(
                                    TranslateActivity.this,
                                    "Không thể nhận dạng ảnh: " + exception.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    private void openSpeechInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            launchSpeechInput();
            return;
        }

        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    private void launchSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        String languageCode = getSourceLanguageCode();

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói nội dung cần dịch");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        try {
            tvStatus.setText("Đang nghe giọng nói...");
            speechInputLauncher.launch(intent);
        } catch (ActivityNotFoundException exception) {
            tvStatus.setText("Thiết bị không hỗ trợ nhận dạng giọng nói");

            Toast.makeText(
                    this,
                    "Thiết bị không hỗ trợ nhận dạng giọng nói.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void appendRecognizedSpeechToInput(String recognizedText) {
        if (TextUtils.isEmpty(recognizedText)) {
            tvStatus.setText("Không nhận dạng được giọng nói");
            return;
        }

        String currentText = edtInputText.getText().toString().trim();
        String finalText;

        if (TextUtils.isEmpty(currentText)) {
            finalText = recognizedText.trim();
        } else {
            finalText = currentText + " " + recognizedText.trim();
        }

        edtInputText.setText(finalText);
        edtInputText.setSelection(edtInputText.getText().length());

        if (suggestionPopupController != null) {
            suggestionPopupController.refresh();
        }

        tvStatus.setText("Đã nhập văn bản bằng giọng nói");
    }

    private void setOcrLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

        btnOcrImage.setEnabled(!loading);
        btnTranslate.setEnabled(!loading);
        btnClear.setEnabled(!loading);
        btnCopy.setEnabled(!loading);
        btnSpeakInput.setEnabled(!loading);
        btnSpeakResult.setEnabled(!loading);
        btnSwapLanguage.setEnabled(!loading);
        btnMenu.setEnabled(!loading);
        btnHistory.setEnabled(!loading);
    }

    private void speakTranslatedText() {
        String translatedText = tvTranslatedText.getText().toString().trim();

        if (TextUtils.isEmpty(translatedText)) {
            Toast.makeText(
                    this,
                    "Chưa có kết quả dịch để đọc.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        boolean success = textToSpeechManager != null
                && textToSpeechManager.speak(translatedText, getTargetLanguageCode());

        if (!success) {
            Toast.makeText(
                    this,
                    "Không thể đọc kết quả dịch. Thiết bị chưa sẵn sàng hoặc ngôn ngữ không được hỗ trợ.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private String getSourceLanguageCode() {
        LanguageOption languageOption =
                (LanguageOption) spinnerSourceLanguage.getSelectedItem();

        return resolveLanguageCode(languageOption, "en-US");
    }

    private String getTargetLanguageCode() {
        LanguageOption languageOption =
                (LanguageOption) spinnerTargetLanguage.getSelectedItem();

        return resolveLanguageCode(languageOption, "vi-VN");
    }

    private String resolveLanguageCode(LanguageOption languageOption, String fallbackCode) {
        if (languageOption == null) {
            return fallbackCode;
        }

        String reflectedCode = getCodeByReflection(languageOption);

        if (!TextUtils.isEmpty(reflectedCode)) {
            return reflectedCode;
        }

        String label = languageOption.toString();

        if (label == null) {
            return fallbackCode;
        }

        String lowerLabel = label.toLowerCase(Locale.US);

        if (lowerLabel.contains("vi")
                || lowerLabel.contains("vietnam")
                || lowerLabel.contains("tiếng việt")
                || lowerLabel.contains("tieng viet")) {
            return "vi-VN";
        }

        if (lowerLabel.contains("en")
                || lowerLabel.contains("english")
                || lowerLabel.contains("tiếng anh")
                || lowerLabel.contains("tieng anh")
                || lowerLabel.contains("anh")) {
            return "en-US";
        }

        return fallbackCode;
    }

    private String getCodeByReflection(LanguageOption languageOption) {
        String[] methodNames = {
                "getCode",
                "getLanguageCode",
                "getApiCode",
                "getValue"
        };

        for (String methodName : methodNames) {
            try {
                Method method = languageOption.getClass().getMethod(methodName);
                Object value = method.invoke(languageOption);

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

    private void openHistoryScreen() {
        Intent intent = new Intent(
                TranslateActivity.this,
                HistoryActivity.class
        );

        startActivity(intent);
    }

    private void openEvaluationScreen() {
        if (TextUtils.isEmpty(lastSourceText) || TextUtils.isEmpty(lastTranslatedText)) {
            Toast.makeText(
                    this,
                    "Chưa có bản dịch để đánh giá.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent intent = new Intent(
                TranslateActivity.this,
                EvaluationActivity.class
        );

        intent.putExtra(Constants.EXTRA_SOURCE_TEXT, lastSourceText);
        intent.putExtra(Constants.EXTRA_TRANSLATED_TEXT, lastTranslatedText);
        intent.putExtra(Constants.EXTRA_TRANSLATION_DURATION, lastTranslationDuration);

        startActivity(intent);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add("Lịch sử dịch");
        popupMenu.getMenu().add("Đánh giá bản dịch");
        popupMenu.getMenu().add("Nhận dạng chữ từ ảnh");
        popupMenu.getMenu().add("Nhập văn bản bằng giọng nói");
        popupMenu.getMenu().add("Đọc kết quả dịch");
        popupMenu.getMenu().add("Kiểm tra gợi ý từ");
        popupMenu.getMenu().add("Xóa nội dung");
        popupMenu.getMenu().add("Giới thiệu");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Lịch sử dịch")) {
                openHistoryScreen();
                return true;
            }

            if (title.equals("Đánh giá bản dịch")) {
                openEvaluationScreen();
                return true;
            }

            if (title.equals("Nhận dạng chữ từ ảnh")) {
                showOcrSourceChooser();
                return true;
            }

            if (title.equals("Nhập văn bản bằng giọng nói")) {
                openSpeechInput();
                return true;
            }

            if (title.equals("Đọc kết quả dịch")) {
                speakTranslatedText();
                return true;
            }

            if (title.equals("Kiểm tra gợi ý từ")) {
                if (suggestionPopupController != null) {
                    suggestionPopupController.refresh();
                }

                Toast.makeText(
                        this,
                        "Đã kiểm tra từ nghi sai. Bấm vào từ gạch đỏ để xem gợi ý.",
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }

            if (title.equals("Xóa nội dung")) {
                clearTranslateScreen();
                return true;
            }

            if (title.equals("Giới thiệu")) {
                Toast.makeText(
                        this,
                        "Ứng dụng dịch Anh - Việt có OCR ảnh, nhập giọng nói, đọc kết quả dịch, lưu lịch sử, đánh giá bản dịch và gợi ý sửa từ nhập sai.",
                        Toast.LENGTH_LONG
                ).show();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void observeUiState() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) {
                return;
            }

            progressBar.setVisibility(
                    state.isLoading() ? View.VISIBLE : View.GONE
            );

            btnTranslate.setEnabled(!state.isLoading());
            btnClear.setEnabled(!state.isLoading());
            btnCopy.setEnabled(!state.isLoading());
            btnOcrImage.setEnabled(!state.isLoading());
            btnSpeakInput.setEnabled(!state.isLoading());
            btnSpeakResult.setEnabled(!state.isLoading());
            btnSwapLanguage.setEnabled(!state.isLoading());
            btnMenu.setEnabled(!state.isLoading());
            btnHistory.setEnabled(!state.isLoading());

            if (state.isLoading()) {
                tvStatus.setText("Đang dịch...");
                return;
            }

            if (state.hasError()) {
                tvStatus.setText("Lỗi");
                tvTranslatedText.setText("");

                lastTranslatedText = "";
                lastTranslationDuration = 0L;

                Toast.makeText(
                        this,
                        state.getErrorMessage(),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (state.hasResult()) {
                tvStatus.setText("Dịch thành công");

                lastTranslationDuration =
                        System.currentTimeMillis() - translationStartTime;

                lastTranslatedText = state.getResult().getTranslatedText();

                tvTranslatedText.setText(lastTranslatedText);
            } else {
                tvStatus.setText("Sẵn sàng");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeechManager != null) {
            textToSpeechManager.shutdown();
        }

        if (ocrTextRecognizer != null) {
            ocrTextRecognizer.close();
        }

        super.onDestroy();
    }
}