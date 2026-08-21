name: Build Android APK

on:
  push:
    branches:
      - main
      - master

  pull_request:
    branches:
      - main
      - master

  workflow_dispatch:

permissions:
  contents: read

jobs:
  build:
    name: Build Jungle Math APK
    runs-on: ubuntu-latest

    steps:

      # --------------------------------------------------
      # 1. Checkout source code
      # --------------------------------------------------
      - name: Checkout repository
        uses: actions/checkout@v4

      # --------------------------------------------------
      # 2. Install Java 17
      # --------------------------------------------------
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: gradle

      # --------------------------------------------------
      # 3. Install Android SDK
      # --------------------------------------------------
      - name: Set up Android SDK
        uses: android-actions/setup-android@v3

      # --------------------------------------------------
      # 4. Install required Android SDK components
      # --------------------------------------------------
      - name: Install Android SDK components
        run: |
          yes | sdkmanager --licenses || true

          sdkmanager \
            "platform-tools" \
            "platforms;android-36" \
            "build-tools;36.0.0"

      # --------------------------------------------------
      # 5. Find Android project
      # --------------------------------------------------
      - name: Locate Android project
        shell: bash
        run: |
          echo "Current directory:"
          pwd

          echo ""
          echo "Repository contents:"
          ls -la

          echo ""
          echo "Searching for settings.gradle.kts:"
          find . -maxdepth 3 -name "settings.gradle.kts" -print

          echo ""
          echo "Searching for app module:"
          find . -maxdepth 4 -type d -name "app" -print

      # --------------------------------------------------
      # 6. Determine project directory
      # --------------------------------------------------
      - name: Determine project directory
        id: project
        shell: bash
        run: |
          if [ -f "settings.gradle.kts" ] && [ -d "app" ]; then
            echo "dir=." >> "$GITHUB_OUTPUT"
            echo "Android project is repository root."
          elif [ -f "-Basic-Elementary-Math-App/settings.gradle.kts" ] && \
               [ -d "-Basic-Elementary-Math-App/app" ]; then
            echo "dir=-Basic-Elementary-Math-App" >> "$GITHUB_OUTPUT"
            echo "Android project is inside -Basic-Elementary-Math-App."
          else
            echo "ERROR: Android project could not be located."
            exit 1
          fi

      # --------------------------------------------------
      # 7. Generate the debug keystore required by
      #    app/build.gradle.kts
      # --------------------------------------------------
      - name: Generate debug keystore
        working-directory: ${{ steps.project.outputs.dir }}
        shell: bash
        run: |
          if [ ! -f "debug.keystore" ]; then
            echo "Generating debug.keystore..."

            keytool -genkeypair \
              -v \
              -keystore debug.keystore \
              -storepass android \
              -alias androiddebugkey \
              -keypass android \
              -keyalg RSA \
              -keysize 2048 \
              -validity 10000 \
              -dname "CN=Android Debug,O=Android,C=US"

            echo "debug.keystore created."
          else
            echo "debug.keystore already exists."
          fi

          ls -lh debug.keystore

      # --------------------------------------------------
      # 8. Create .env if the project expects it
      # --------------------------------------------------
      - name: Prepare environment configuration
        working-directory: ${{ steps.project.outputs.dir }}
        shell: bash
        run: |
          if [ ! -f ".env" ]; then
            if [ -f ".env.example" ]; then
              cp .env.example .env
              echo ".env created from .env.example."
            else
              touch .env
              echo "Empty .env created."
            fi
          fi

      # --------------------------------------------------
      # 9. Check Gradle project
      # --------------------------------------------------
      - name: Verify Gradle project
        working-directory: ${{ steps.project.outputs.dir }}
        shell: bash
        run: |
          test -f settings.gradle.kts
          test -f build.gradle.kts
          test -f app/build.gradle.kts

          echo "Gradle project verified."

      # --------------------------------------------------
      # 10. Set up Gradle 9.3.1
      # --------------------------------------------------
      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '9.3.1'

      # --------------------------------------------------
      # 11. Show versions
      # --------------------------------------------------
      - name: Show build environment
        working-directory: ${{ steps.project.outputs.dir }}
        shell: bash
        run: |
          java -version
          gradle --version
          sdkmanager --list_installed | head -100

      # --------------------------------------------------
      # 12. Clean project
      # --------------------------------------------------
      - name: Clean project
        working-directory: ${{ steps.project.outputs.dir }}
        run: |
          gradle --no-daemon clean

      # --------------------------------------------------
      # 13. Build DEBUG APK
      # --------------------------------------------------
      - name: Build Debug APK
        working-directory: ${{ steps.project.outputs.dir }}
        run: |
          gradle \
            --no-daemon \
            --stacktrace \
            --info \
            assembleDebug

      # --------------------------------------------------
      # 14. Verify APK exists
      # --------------------------------------------------
      - name: Verify APK
        working-directory: ${{ steps.project.outputs.dir }}
        shell: bash
        run: |
          APK="app/build/outputs/apk/debug/app-debug.apk"

          if [ ! -f "$APK" ]; then
            echo "ERROR: APK was not created."
            echo ""
            echo "Available APK files:"
            find app/build -type f -name "*.apk" -print || true
            exit 1
          fi

          echo "APK successfully created:"
          ls -lh "$APK"

      # --------------------------------------------------
      # 15. Run Android Lint
      # --------------------------------------------------
      - name: Run Android Lint
        working-directory: ${{ steps.project.outputs.dir }}
        run: |
          gradle \
            --no-daemon \
            lintDebug

      # --------------------------------------------------
      # 16. Upload APK
      # --------------------------------------------------
      - name: Upload Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: jungle-math-debug-apk
          path: ${{ steps.project.outputs.dir }}/app/build/outputs/apk/debug/app-debug.apk
          if-no-files-found: error
          retention-days: 14

      # --------------------------------------------------
      # 17. Upload lint report if available
      # --------------------------------------------------
      - name: Upload lint report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: jungle-math-lint-report
          path: |
            ${{ steps.project.outputs.dir }}/app/build/reports/lint-results-debug.html
            ${{ steps.project.outputs.dir }}/app/build/reports/lint-results-debug.xml
          if-no-files-found: ignore
          retention-days: 14
