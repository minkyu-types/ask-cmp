# Compose Multiplatform CI/CD 설정 가이드

이 프로젝트는 Compose Multiplatform(Android/iOS)을 위한 GitHub Actions 기반 CI/CD 파이프라인을 사용합니다.

## 📋 개요

CI/CD 파이프라인은 다음 4가지 주요 작업으로 구성됩니다:

1. **build-android**: Android 빌드 및 테스트
2. **build-ios**: iOS 빌드 및 테스트  
3. **deploy-android**: Google Play Store 내부 테스트 배포
4. **deploy-ios**: TestFlight 배포

## 🚀 워크플로우 트리거

워크플로우는 다음 경우에 자동으로 실행됩니다:

- `main` 또는 `develop` 브랜치에 push
- `main` 또는 `develop` 브랜치로의 Pull Request
- 수동 실행 (workflow_dispatch)

## 🤖 Android 빌드 & 배포

### 빌드 단계

1. JDK 17 설정
2. Gradle 캐싱
3. 유닛 테스트 실행
4. Debug APK 빌드
5. Release AAB 빌드
6. 빌드 아티팩트 업로드

### 배포 설정 (main 브랜치만)

배포를 위해 다음 GitHub Secrets을 설정해야 합니다:

#### 필수 Secrets

**PLAY_STORE_SERVICE_ACCOUNT_JSON**
- Google Play Console Service Account의 JSON 키
- 생성 방법:
  1. Google Play Console > 설정 > API 액세스
  2. 서비스 계정 생성 또는 선택
  3. JSON 키 다운로드
  4. 전체 JSON 내용을 Secret으로 등록

#### 코드 수정 필요

`.github/workflows/main.yml` 파일에서 다음을 수정하세요:

```yaml
packageName: com.yourpackage.name  # 실제 패키지명으로 변경
```

## 🍎 iOS 빌드 & 배포

### 빌드 단계

1. JDK 17 설정
2. Xcode 최신 안정 버전 설정
3. CocoaPods 설치
4. iOS Framework 빌드
5. iOS 시뮬레이터 테스트 실행
6. iOS 앱 빌드

### 배포 설정 (main 브랜치만)

배포를 위해 다음 GitHub Secrets을 설정해야 합니다:

#### 필수 Secrets

**IOS_CERTIFICATES_P12**
- iOS 배포 인증서를 Base64로 인코딩한 값
- 생성 방법:
  ```bash
  base64 -i YourCertificate.p12 | pbcopy
  ```

**IOS_CERTIFICATES_PASSWORD**
- P12 인증서의 비밀번호

**APPSTORE_ISSUER_ID**
- App Store Connect API Issuer ID
- App Store Connect > 사용자 및 액세스 > 키 탭에서 확인

**APPSTORE_API_KEY_ID**
- App Store Connect API Key ID
- App Store Connect에서 API 키 생성 시 발급

**APPSTORE_API_PRIVATE_KEY**
- App Store Connect API Private Key (p8 파일 내용)
- 생성 방법:
  ```bash
  cat AuthKey_XXXXXXXXXX.p8 | pbcopy
  ```

#### 코드 수정 필요

`.github/workflows/main.yml` 파일에서 다음을 수정하세요:

```yaml
bundle-id: com.yourpackage.name  # 실제 Bundle ID로 변경
```

#### exportOptions.plist 생성

`iosApp` 디렉토리에 `exportOptions.plist` 파일을 생성해야 합니다:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>method</key>
    <string>app-store</string>
    <key>teamID</key>
    <string>YOUR_TEAM_ID</string>
    <key>uploadBitcode</key>
    <false/>
    <key>uploadSymbols</key>
    <true/>
    <key>signingStyle</key>
    <string>automatic</string>
</dict>
</plist>
```

`YOUR_TEAM_ID`를 실제 Apple Developer Team ID로 변경하세요.

## ⚙️ GitHub Secrets 설정 방법

1. GitHub 저장소 페이지에서 **Settings** 클릭
2. 왼쪽 메뉴에서 **Secrets and variables > Actions** 클릭
3. **New repository secret** 버튼 클릭
4. Secret 이름과 값 입력 후 저장

## 🔄 프로젝트 구조 요구사항

워크플로우는 다음 프로젝트 구조를 가정합니다:

```
project-root/
├── .github/
│   └── workflows/
│       └── main.yml
├── androidApp/
│   └── build/outputs/
├── iosApp/
│   ├── iosApp.xcworkspace
│   ├── Podfile
│   └── exportOptions.plist
├── shared/
├── gradle/
├── gradlew
└── build.gradle.kts
```

프로젝트 구조가 다른 경우, 워크플로우 파일의 경로를 수정해야 합니다.

## 📦 빌드 아티팩트

각 빌드 후 다음 아티팩트가 생성됩니다:

### Android
- `android-apk`: Debug APK 파일
- `android-aab`: Release AAB 파일

Actions 탭에서 워크플로우 실행 결과를 확인하고 아티팩트를 다운로드할 수 있습니다.

## 🐛 문제 해결

### Android 빌드 실패

1. **Gradle 권한 오류**: `chmod +x gradlew` 단계가 실행되는지 확인
2. **테스트 실패**: 로컬에서 `./gradlew testDebugUnitTest` 실행하여 확인
3. **빌드 경로 오류**: 프로젝트 구조에 맞게 경로 수정

### iOS 빌드 실패

1. **CocoaPods 오류**: Podfile 위치 확인 (`iosApp/Podfile`)
2. **Xcode 버전 문제**: 프로젝트 요구사항에 맞는 Xcode 버전 지정
3. **Framework 링크 오류**: Gradle task 이름 확인
   - 시뮬레이터: `linkDebugFrameworkIosSimulatorArm64`
   - 실제 기기: `linkReleaseFrameworkIosArm64`

### 배포 실패

1. **Android**: Service Account 권한 확인 (Google Play Console에서 권한 부여 필요)
2. **iOS**: 
   - App Store Connect API 키 권한 확인
   - 인증서 및 프로비저닝 프로필 유효성 확인
   - Bundle ID 일치 여부 확인

## 📝 추가 설정

### 브랜치 전략

현재 설정:
- `main` 브랜치: 프로덕션 배포
- `develop` 브랜치: 개발 빌드 및 테스트

필요에 따라 다른 브랜치 전략을 사용하려면 워크플로우의 `on` 섹션을 수정하세요.

### 배포 트랙 변경

Android 배포 트랙을 변경하려면:

```yaml
track: internal  # internal, alpha, beta, production 중 선택
```

## 🔐 보안 권장사항

1. **절대로** 인증서, 키, 비밀번호를 코드에 직접 포함하지 마세요
2. 모든 민감한 정보는 GitHub Secrets으로 관리하세요
3. Service Account 및 API 키는 최소 권한 원칙을 따르세요
4. 정기적으로 인증서 및 키를 갱신하세요

## 📚 참고 자료

- [GitHub Actions 공식 문서](https://docs.github.com/en/actions)
- [Compose Multiplatform 공식 문서](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Google Play Console API 설정](https://developers.google.com/android-publisher/getting_started)
- [App Store Connect API](https://developer.apple.com/documentation/appstoreconnectapi)
