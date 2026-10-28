# APAAR OneView

**APAAR ID-Based Student Data Management and Verification Platform for AICTE-Approved Institutions**

APAAR OneView is a modern, unified academic record review and verification platform built with Kotlin and Jetpack Compose (Material Design 3). It streamlines student record ingestion, automated CSV validation, duplicate detection and collision triage, cross-institution academic history reviews, and verifiable audit logging.

---

## 📱 How to Download the APK from GitHub

You can download the pre-built APK directly from this repository without compiling any code:

1. Go to the **Actions** tab at the top of this GitHub repository.
2. Under **All workflows**, click on the latest workflow run (e.g., **Build & Export APK**).
3. Scroll down to the **Artifacts** section at the bottom of the summary page.
4. Click on **`APAAR-OneView-Debug-APK`** to download the ZIP file containing the installable `.apk` file.
5. Transfer or open the `.apk` file on your Android device (Android 7.0+ / API 24+) to install!

---

## 🚀 Building & Running Locally

### Prerequisites
- **JDK 17** or higher
- **Android Studio** (Ladybug / Iguana or newer recommended)
- **Android SDK API 34-36**

### Build via Command Line
```bash
# Clone the repository
git clone https://github.com/rickypraneet/<repository-name>.git
cd <repository-name>

# Make wrapper executable (macOS/Linux)
chmod +x gradlew

# Build the Debug APK
./gradlew assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 👥 Demo User Personas

| Role | Email | Password | Primary Capabilities |
| :--- | :--- | :--- | :--- |
| **Institution Administrator** | `admin@demo.apaaroneview.test` | `Demo@123` | Batch CSV upload, student registry, duplicate triage, institutional metrics |
| **Student** | `rahul@demo.apaaroneview.test` | `Demo@123` | Personal profile, multi-semester transcripts, credit tally, issue flagging |
| **Authorized Verifier** | `verifier@demo.apaaroneview.test` | `Demo@123` | Multi-state search, verification certificate generation, collision triage |

*Note: All data in this prototype is synthetic and intended for demonstration and evaluation.*

---

## 🛠 Project Architecture
- **Language:** Kotlin 2.0+
- **UI Toolkit:** Jetpack Compose with Material Design 3 (Edge-to-Edge)
- **Architecture:** Clean MVVM with Kotlin StateFlow & Coroutines
- **Testing:** Local JVM Robolectric tests (`./gradlew testDebugUnitTest`)
- **CI/CD:** Automated GitHub Actions build workflow (`.github/workflows/build-apk.yml`)
