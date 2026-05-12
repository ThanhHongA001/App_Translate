package com.example.app_tt2.domain.evaluator;

import com.example.app_tt2.domain.model.TranslationEvaluation;

public class TranslationEvaluator {

    private final AccuracyEstimator accuracyEstimator;
    private final StyleEstimator styleEstimator;
    private final TranslationSpeedEstimator speedEstimator;

    public TranslationEvaluator() {
        this.accuracyEstimator = new AccuracyEstimator();
        this.styleEstimator = new StyleEstimator();
        this.speedEstimator = new TranslationSpeedEstimator();
    }

    public TranslationEvaluation evaluate(
            String sourceText,
            String translatedText,
            long durationMillis
    ) {
        int accuracyScore = accuracyEstimator.estimate(sourceText, translatedText);
        int styleScore = styleEstimator.estimate(translatedText);
        int speedScore = speedEstimator.estimate(durationMillis);

        int overallScore = (int) Math.round(
                accuracyScore * 0.5 +
                        styleScore * 0.3 +
                        speedScore * 0.2
        );

        String comment = buildComment(
                accuracyScore,
                styleScore,
                speedScore,
                overallScore
        );

        return new TranslationEvaluation(
                accuracyScore,
                styleScore,
                speedScore,
                overallScore,
                comment,
                System.currentTimeMillis()
        );
    }

    private String buildComment(
            int accuracyScore,
            int styleScore,
            int speedScore,
            int overallScore
    ) {
        StringBuilder builder = new StringBuilder();

        builder.append("Điểm tổng quan: ").append(overallScore).append("/100\n");
        builder.append("Độ chính xác ước lượng: ").append(accuracyScore).append("/100\n");
        builder.append("Văn phong: ").append(styleScore).append("/100\n");
        builder.append("Tốc độ dịch: ").append(speedScore).append("/100\n\n");

        if (overallScore >= 85) {
            builder.append("Bản dịch có chất lượng tốt, nội dung ổn định và tốc độ xử lý nhanh.");
        } else if (overallScore >= 70) {
            builder.append("Bản dịch ở mức khá, có thể sử dụng nhưng vẫn nên kiểm tra lại ngữ cảnh.");
        } else if (overallScore >= 50) {
            builder.append("Bản dịch ở mức trung bình, nên chỉnh sửa lại câu chữ trước khi sử dụng.");
        } else {
            builder.append("Bản dịch có chất lượng thấp hoặc dữ liệu đầu vào chưa phù hợp.");
        }

        return builder.toString();
    }
}