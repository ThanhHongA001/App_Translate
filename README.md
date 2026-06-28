# App_Translate

App_Translate là ứng dụng Android hỗ trợ dịch văn bản Anh - Việt, lưu lịch sử dịch, đánh giá chất lượng bản dịch, nhận dạng chữ từ ảnh, nhập văn bản bằng giọng nói, đọc kết quả dịch và gợi ý sửa từ nhập sai.

Dự án được xây dựng bằng Java trên Android Studio, tổ chức theo hướng phân lớp rõ ràng giữa giao diện, xử lý nghiệp vụ, repository, API remote và cơ sở dữ liệu local. Cách tổ chức này giúp mã nguồn dễ bảo trì, dễ mở rộng và phù hợp với bài tập lớn hoặc đồ án Android quy mô vừa.

## Mục tiêu dự án

Dự án hướng đến việc xây dựng một ứng dụng dịch ngôn ngữ có các chức năng thực tế thường gặp trong ứng dụng di động:

- Dịch văn bản giữa tiếng Anh và tiếng Việt.
- Lưu lại lịch sử các lần dịch để người dùng có thể xem lại.
- Xem chi tiết từng bản ghi lịch sử dịch.
- Xóa từng bản ghi hoặc xóa toàn bộ lịch sử dịch.
- Đánh giá bản dịch dựa trên độ chính xác ước lượng, văn phong và tốc độ xử lý.
- Nhận dạng văn bản từ ảnh bằng OCR.
- Nhập văn bản bằng giọng nói.
- Đọc kết quả dịch bằng Text To Speech.
- Gợi ý sửa từ nhập sai bằng từ điển cục bộ.
- Chuẩn bị module kết nối API xác thực thông qua Retrofit.

## Thông tin kỹ thuật

| Thành phần | Thông tin |
|---|---|
| Tên repository | App_Translate |
| Tên project | App_Translate |
| Package | com.example.app_tt2 |
| Ngôn ngữ lập trình | Java |
| Build system | Gradle Kotlin DSL |
| Android Gradle Plugin | 8.12.3 |
| compileSdk | 36 |
| minSdk | 24 |
| targetSdk | 36 |
| versionCode | 1 |
| versionName | 1.0 |
| Cơ sở dữ liệu local | Room Database |
| API dịch | MyMemory Translated API |
| OCR | Google ML Kit Text Recognition |
| Nhận dạng giọng nói | Android Speech Recognizer |
| Đọc văn bản | Android TextToSpeech |
| Kiến trúc xử lý | Activity, ViewModel, UseCase, Repository, DTO, Entity |

Ghi chú: repository đã đổi sang tên `App_Translate`, nhưng package code hiện tại vẫn là `com.example.app_tt2`. Điều này không gây lỗi nếu cấu hình Gradle và AndroidManifest vẫn khớp với mã nguồn.

## Công nghệ và thư viện sử dụng

| Nhóm | Thư viện / Công nghệ |
|---|---|
| UI Android | AppCompat, Material Components, ConstraintLayout, Activity |
| Lifecycle | ViewModel, LiveData |
| Network | Retrofit, Gson Converter, OkHttp Logging Interceptor |
| Local database | Room Runtime, Room Compiler |
| Danh sách dữ liệu | RecyclerView |
| OCR | Google ML Kit Text Recognition |
| Speech | Android Speech Recognizer, Android TextToSpeech |
| Kiểm thử | JUnit, AndroidX Test, Espresso |

## Chức năng chính

### 1. Dịch văn bản

Người dùng nhập văn bản, chọn ngôn ngữ nguồn và ngôn ngữ đích, sau đó bấm nút dịch. Ứng dụng gửi nội dung đến MyMemory API thông qua Retrofit và nhận kết quả trả về.

Luồng xử lý chính:

```text
TranslateActivity
→ TranslateViewModel
→ TranslateTextUseCase
→ TranslateRepositoryImpl
→ TranslateApiService
→ MyMemory API
→ TranslationResult
→ SaveTranslationUseCase
→ Room Database
→ Cập nhật giao diện
```

Các xử lý đã có:

- Kiểm tra nội dung đầu vào rỗng.
- Kiểm tra ngôn ngữ nguồn và ngôn ngữ đích.
- Không cho phép dịch khi hai ngôn ngữ trùng nhau.
- Hiển thị trạng thái đang dịch, lỗi hoặc dịch thành công.
- Lưu bản dịch thành công vào lịch sử.
- Tính thời gian dịch để phục vụ màn hình đánh giá.
- Lựa chọn kết quả dịch phù hợp từ dữ liệu trả về của API.

### 2. Đổi chiều ngôn ngữ

Ứng dụng hỗ trợ đổi vị trí ngôn ngữ nguồn và ngôn ngữ đích. Nếu đã có kết quả dịch trước đó, ứng dụng có thể đưa bản dịch vào ô nhập và dịch lại theo chiều ngược lại.

### 3. Sao chép kết quả dịch

Người dùng có thể sao chép kết quả dịch vào clipboard bằng nút sao chép. Nếu chưa có kết quả dịch, ứng dụng hiển thị thông báo phù hợp.

### 4. Lịch sử dịch

Mỗi bản dịch thành công được lưu vào Room Database. Người dùng có thể mở màn hình lịch sử để xem lại các bản dịch trước đó.

Dữ liệu lịch sử gồm:

| Trường | Ý nghĩa |
|---|---|
| id | Mã bản ghi tự tăng |
| sourceText | Văn bản gốc |
| translatedText | Văn bản đã dịch |
| sourceLang | Ngôn ngữ nguồn |
| targetLang | Ngôn ngữ đích |
| createdAt | Thời gian tạo |
| favorite | Trạng thái đánh dấu yêu thích |

Tên database:

```text
translation_history_db
```

Tên bảng:

```text
translation_history
```

### 5. Chi tiết lịch sử

Khi bấm vào một bản ghi trong danh sách lịch sử, ứng dụng mở màn hình chi tiết. Màn hình này hiển thị văn bản gốc, văn bản dịch, cặp ngôn ngữ và thời gian dịch.

Người dùng có thể xóa riêng từng bản ghi lịch sử.

### 6. Xóa toàn bộ lịch sử

Màn hình lịch sử có chức năng xóa toàn bộ dữ liệu đã lưu. Trước khi xóa, ứng dụng hiển thị hộp thoại xác nhận để tránh thao tác nhầm.

### 7. Đánh giá bản dịch

Ứng dụng có module đánh giá bản dịch dựa trên ba nhóm điểm:

| Tiêu chí | Trọng số |
|---|---:|
| Độ chính xác ước lượng | 50% |
| Văn phong | 30% |
| Tốc độ dịch | 20% |

Công thức tính điểm tổng quan:

```text
overallScore = accuracyScore * 0.5 + styleScore * 0.3 + speedScore * 0.2
```

Module đánh giá gồm các lớp:

```text
AccuracyEstimator
StyleEstimator
TranslationSpeedEstimator
TranslationEvaluator
EvaluateTranslationUseCase
TranslationEvaluation
```

Cách đánh giá hiện tại là heuristic, tức là ước lượng dựa trên độ dài văn bản, tỷ lệ giữa câu gốc và câu dịch, ký tự bất thường, khoảng trắng và thời gian xử lý. Đây không phải mô hình AI đánh giá ngữ nghĩa đầy đủ, nhưng phù hợp cho bài toán đánh giá cơ bản trong phạm vi ứng dụng học tập.

### 8. Nhận dạng chữ từ ảnh

Ứng dụng hỗ trợ OCR bằng Google ML Kit Text Recognition.

Người dùng có thể chọn một trong hai nguồn ảnh:

- Chụp ảnh trực tiếp bằng camera.
- Chọn ảnh từ thư viện.

Sau khi nhận dạng thành công, văn bản được đưa vào ô nhập để người dùng có thể dịch tiếp.

Lớp xử lý chính:

```text
data/ocr/OcrTextRecognizer.java
```

Nếu trong mã nguồn hiện tại thư mục đang đặt nhầm là `data/orc`, nên chuẩn hóa lại thành `data/ocr` và cập nhật các dòng `import` tương ứng để thống nhất với tên chức năng OCR.

### 9. Nhập văn bản bằng giọng nói

Ứng dụng sử dụng `RecognizerIntent.ACTION_RECOGNIZE_SPEECH` để nhận dạng giọng nói. Văn bản nhận dạng được sẽ được đưa vào ô nhập.

Ứng dụng có xử lý quyền microphone thông qua:

```text
android.permission.RECORD_AUDIO
```

### 10. Đọc kết quả dịch

Ứng dụng sử dụng Android `TextToSpeech` để đọc kết quả dịch. Ngôn ngữ đọc được ánh xạ thông qua lớp:

```text
utils/LanguageCodeMapper.java
```

Các ngôn ngữ được hỗ trợ chính gồm:

- Tiếng Việt.
- Tiếng Anh.

Tùy thiết bị, một số ngôn ngữ có thể cần cài thêm dữ liệu Text To Speech.

### 11. Gợi ý sửa từ nhập sai

Ứng dụng có module gợi ý từ dựa trên từ điển cục bộ. Khi người dùng nhập từ không có trong từ điển, từ đó được gạch chân màu đỏ. Người dùng có thể bấm vào từ bị gạch đỏ để xem danh sách gợi ý.

Các lớp chính:

```text
LocalWordDictionary
GetWordSuggestionsUseCase
SuggestionHighlighter
SuggestionPopupController
WordSuggestion
```

Module gợi ý sử dụng khoảng cách Levenshtein để tìm từ gần đúng với từ đang nhập.

### 12. Module Auth API

Dự án có bổ sung module kết nối Auth API để phục vụ các chức năng xác thực hoặc tích hợp dịch vụ bên ngoài trong các phiên bản mở rộng.

Các lớp chính:

```text
AuthApiService
AuthRequestFactory
AuthRetrofitClient
AuthTokenStore
AuthServiceRepository
AuthServiceRepositoryImpl
```

Tài liệu sử dụng module này được đặt trong file:

```text
AUTH_API_USAGE.md
```

## Cấu trúc thư mục chính

```text
App_Translate
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── java/com/example/app_tt2
│           │   ├── data
│           │   │   ├── local
│           │   │   │   ├── AppDatabase.java
│           │   │   │   ├── dao/TranslationHistoryDao.java
│           │   │   │   ├── dictionary/LocalWordDictionary.java
│           │   │   │   └── entity/TranslationHistoryEntity.java
│           │   │   ├── remote
│           │   │   │   ├── RetrofitClient.java
│           │   │   │   ├── TranslationResultSelector.java
│           │   │   │   ├── api/TranslateApiService.java
│           │   │   │   ├── auth
│           │   │   │   │   ├── AuthApiService.java
│           │   │   │   │   ├── AuthRequestFactory.java
│           │   │   │   │   ├── AuthRetrofitClient.java
│           │   │   │   │   └── AuthTokenStore.java
│           │   │   │   └── dto
│           │   │   │       ├── MatchDto.java
│           │   │   │       ├── ResponseDataDto.java
│           │   │   │       └── TranslateResponseDto.java
│           │   │   ├── repository
│           │   │   │   ├── AuthServiceRepository.java
│           │   │   │   ├── AuthServiceRepositoryImpl.java
│           │   │   │   ├── TranslateRepository.java
│           │   │   │   └── TranslateRepositoryImpl.java
│           │   │   ├── ocr/OcrTextRecognizer.java
│           │   │   └── speech/TextToSpeechManager.java
│           │   ├── domain
│           │   │   ├── evaluator
│           │   │   ├── model
│           │   │   └── usecase
│           │   ├── ui
│           │   │   ├── evaluation
│           │   │   ├── history
│           │   │   ├── suggestion
│           │   │   └── translate
│           │   └── utils
│           └── res
│               ├── drawable
│               ├── layout
│               ├── mipmap
│               ├── values
│               └── xml
├── AUTH_API_USAGE.md
├── build.gradle.kts
├── gradle.properties
├── make_detailed_uml.py
├── settings.gradle.kts
└── README.md
```

## Mô tả các package chính

| Package | Vai trò |
|---|---|
| data/local | Cơ sở dữ liệu Room, DAO, Entity và từ điển cục bộ |
| data/remote | Retrofit client, API interface, DTO và xử lý dữ liệu remote |
| data/remote/api | Interface gọi API dịch |
| data/remote/auth | Các lớp phục vụ kết nối Auth API |
| data/repository | Lớp trung gian xử lý dữ liệu giữa domain và data source |
| data/ocr | Xử lý OCR bằng ML Kit |
| data/speech | Xử lý đọc văn bản bằng TextToSpeech |
| domain/model | Model nghiệp vụ của ứng dụng |
| domain/usecase | Các ca sử dụng chính của ứng dụng |
| domain/evaluator | Logic đánh giá chất lượng bản dịch |
| ui/translate | Màn hình dịch chính |
| ui/history | Màn hình lịch sử và chi tiết lịch sử |
| ui/evaluation | Màn hình đánh giá bản dịch |
| ui/suggestion | Gợi ý và highlight từ nhập sai |
| utils | Các hàm tiện ích dùng chung |

## API dịch

Ứng dụng sử dụng MyMemory Translated API.

Base URL:

```text
https://api.mymemory.translated.net/
```

Endpoint:

```text
GET /get
```

Tham số:

| Tham số | Ý nghĩa | Ví dụ |
|---|---|---|
| q | Văn bản cần dịch | hello |
| langpair | Cặp ngôn ngữ nguồn và đích | en\|vi |

Ví dụ request:

```text
https://api.mymemory.translated.net/get?q=hello&langpair=en|vi
```

Kết quả trả về được ánh xạ vào các DTO:

```text
TranslateResponseDto
ResponseDataDto
MatchDto
```

## Quyền Android sử dụng

Trong `AndroidManifest.xml`, ứng dụng khai báo các quyền phục vụ kết nối mạng, ghi âm, camera và truy cập ảnh.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Ý nghĩa:

| Quyền | Mục đích |
|---|---|
| INTERNET | Gọi API dịch |
| RECORD_AUDIO | Nhập văn bản bằng giọng nói |
| CAMERA | Chụp ảnh để OCR |
| READ_EXTERNAL_STORAGE | Chọn ảnh từ thư viện trên các phiên bản Android cũ |
| WRITE_EXTERNAL_STORAGE | Tương thích thao tác file trên các phiên bản Android cũ |

Ứng dụng cũng khai báo `FileProvider` để tạo URI an toàn cho ảnh chụp tạm dùng trong OCR.

## Giao diện chính

Dự án hiện có các layout chính:

```text
activity_translate.xml
activity_history.xml
activity_history_detail.xml
activity_evaluation.xml
item_history.xml
item_translate_result.xml
item_evaluation_result.xml
```

Màn hình chính `TranslateActivity` gồm:

- Ô nhập văn bản.
- Chọn ngôn ngữ nguồn.
- Chọn ngôn ngữ đích.
- Nút dịch.
- Nút đổi chiều ngôn ngữ.
- Nút xóa nội dung.
- Nút sao chép kết quả.
- Nút mở OCR.
- Nút nhập giọng nói.
- Nút đọc kết quả dịch.
- Nút lịch sử.
- Menu chức năng mở rộng.

## Yêu cầu môi trường

Để chạy project, cần chuẩn bị:

- Android Studio bản mới.
- JDK 11 hoặc cao hơn.
- Gradle wrapper đi kèm project.
- Thiết bị Android hoặc Android Emulator.
- Kết nối Internet để gọi API dịch.

Khuyến nghị:

| Thành phần | Phiên bản |
|---|---|
| Android Studio | Bản mới tương thích AGP 8.x |
| JDK | 11 trở lên |
| Android SDK | Có SDK Platform 36 |
| Thiết bị chạy thử | Android 7.0 trở lên do minSdk = 24 |

## Cách chạy project trong Android Studio

Các bước chạy:

```text
1. Mở Android Studio.
2. Chọn Open.
3. Chọn thư mục App_Translate.
4. Chờ Gradle Sync hoàn tất.
5. Chọn thiết bị chạy thử hoặc emulator.
6. Bấm Run app.
```

Nếu Gradle chưa đồng bộ, bấm:

```text
File → Sync Project with Gradle Files
```

## Build APK debug

Chạy lệnh sau tại thư mục gốc project:

```powershell
.\gradlew assembleDebug
```

Sau khi build thành công, file APK nằm tại:

```text
app\build\outputs\apk\debug\app-debug.apk
```

Có thể mở nhanh thư mục APK bằng PowerShell:

```powershell
explorer .\app\build\outputs\apk\debug\
```

## Cài APK vào thiết bị bằng ADB

Nếu thiết bị đã bật USB Debugging, có thể cài APK bằng lệnh:

```powershell
adb install -r .\app\build\outputs\apk\debug\app-debug.apk
```

Nếu Windows không nhận lệnh `adb`, dùng đường dẫn đầy đủ đến Android SDK:

```powershell
"C:\Program Files (x86)\Android\android-sdk\platform-tools\adb.exe" install -r .\app\build\outputs\apk\debug\app-debug.apk
```

## Build APK release

Có thể tạo APK release trực tiếp trong Android Studio:

```text
Build → Generate Signed App Bundle or APK... → APK → Next
```

Nếu chưa có keystore, chọn `Create new...`, tạo keystore rồi build bản release.

File release thường nằm tại một trong các thư mục sau:

```text
app\release\
app\build\outputs\apk\release\
```

## Kiểm tra chức năng

Bảng kiểm thử cơ bản:

| Nhóm chức năng | Cách kiểm tra | Kết quả mong đợi |
|---|---|---|
| Dịch văn bản | Nhập `hello`, chọn English → Tiếng Việt, bấm dịch | Hiển thị bản dịch tiếng Việt |
| Chống input rỗng | Không nhập gì và bấm dịch | Hiển thị thông báo yêu cầu nhập nội dung |
| Chống trùng ngôn ngữ | Chọn cùng ngôn ngữ nguồn và đích | Hiển thị thông báo lỗi |
| Lưu lịch sử | Dịch thành công rồi mở lịch sử | Có bản ghi mới trong danh sách |
| Xem chi tiết lịch sử | Bấm vào một bản ghi lịch sử | Mở màn hình chi tiết |
| Xóa lịch sử | Xóa một bản ghi hoặc xóa toàn bộ | Dữ liệu được xóa khỏi danh sách |
| OCR ảnh | Chọn ảnh có chữ rõ ràng | Văn bản được đưa vào ô nhập |
| Chụp ảnh OCR | Cấp quyền camera và chụp ảnh | Ảnh được nhận dạng chữ |
| Nhập giọng nói | Cấp quyền micro và nói thử | Văn bản được đưa vào ô nhập |
| Đọc kết quả dịch | Dịch xong và bấm nút loa | Thiết bị đọc bản dịch |
| Gợi ý từ sai | Nhập từ sai chính tả | Từ sai được gạch đỏ và có popup gợi ý |
| Đánh giá bản dịch | Dịch xong và mở màn hình đánh giá | Hiển thị điểm và nhận xét |

## Cập nhật code lên GitHub

Repository hiện tại:

```text
https://github.com/ThanhHongA001/App_Translate
```

Nếu máy vẫn đang trỏ nhầm sang repo cũ `App_TT2`, sửa lại remote bằng lệnh:

```powershell
git remote set-url origin https://github.com/ThanhHongA001/App_Translate.git
```

Kiểm tra remote:

```powershell
git remote -v
```

Kết quả đúng phải có dạng:

```text
origin  https://github.com/ThanhHongA001/App_Translate.git (fetch)
origin  https://github.com/ThanhHongA001/App_Translate.git (push)
```

Sau đó cập nhật code:

```powershell
git add .
git commit -m "Update project source code"
git push -u origin main
```

Nếu không có thay đổi mới để commit, Git có thể báo `nothing to commit`. Khi đó chỉ cần push:

```powershell
git push -u origin main
```

## Hạn chế hiện tại

Một số giới hạn kỹ thuật của phiên bản hiện tại:

- API MyMemory cần Internet và có thể bị giới hạn tần suất request.
- Độ chính xác dịch phụ thuộc vào API bên ngoài.
- OCR phụ thuộc vào chất lượng ảnh, ánh sáng, độ nghiêng và độ rõ của chữ.
- Text To Speech phụ thuộc vào dữ liệu giọng nói có sẵn trên thiết bị.
- Speech Recognizer phụ thuộc vào dịch vụ nhận dạng giọng nói của thiết bị.
- Module đánh giá bản dịch là đánh giá heuristic, chưa phải đánh giá ngữ nghĩa bằng mô hình NLP chuyên sâu.
- Từ điển gợi ý là từ điển cục bộ, số lượng từ còn giới hạn.
- Giao diện chọn ngôn ngữ hiện tập trung vào English và Tiếng Việt.

## Hướng phát triển

Các hướng có thể mở rộng trong các phiên bản sau:

- Thêm nhiều cặp ngôn ngữ hơn.
- Bổ sung chế độ tự động phát hiện ngôn ngữ nguồn.
- Tăng kích thước từ điển cục bộ.
- Thêm tìm kiếm trong lịch sử dịch.
- Thêm đánh dấu yêu thích cho bản dịch.
- Thêm chỉnh sửa bản ghi lịch sử.
- Tối ưu giao diện cho tablet.
- Thêm kiểm thử unit test cho usecase và evaluator.
- Thêm kiểm thử UI bằng Espresso.
- Chuẩn hóa package `data/orc` thành `data/ocr` nếu trong mã nguồn vẫn còn tên thư mục cũ.
- Tách cấu hình API ra file riêng nếu ứng dụng mở rộng.
- Hoàn thiện module Auth API nếu ứng dụng cần đăng nhập hoặc xác thực người dùng.

## Tác giả

Repository GitHub: Bùi Thanh Hồng

```text
https://github.com/ThanhHongA001/App_Translate
```

Dự án được xây dựng phục vụ học tập và thực hành phát triển ứng dụng Android bằng Java.
