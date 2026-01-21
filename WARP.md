# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project overview

This is a single-module Android application (`:app`) named `Beacon`, built with the Android Gradle Plugin and Kotlin, using Jetpack Compose and Material 3 for the UI.

The project structure is standard for a modern Android app:
- Root Gradle configuration in `build.gradle.kts`, `settings.gradle.kts`, and `gradle.properties`.
- Main app module in `app/` with its own `build.gradle.kts`.
- Source sets under `app/src/main`, `app/src/test`, and `app/src/androidTest`.

## Common commands (run from repository root)

Use the Gradle wrapper (`gradlew` / `./gradlew`) for all builds and tests.

### Build

- Assemble debug APK for the app module:
  - PowerShell:
    - `./gradlew :app:assembleDebug`
  - `cmd.exe`:
    - `gradlew :app:assembleDebug`

- Assemble release APK (without changing signing config):
  - `./gradlew :app:assembleRelease`

### Tests

Unit and instrumentation tests use the default Android/Gradle setup.

- Run all unit tests for the app module:
  - `./gradlew :app:testDebugUnitTest`

- Run a single unit test class (example uses the existing `ExampleUnitTest`):
  - `./gradlew :app:testDebugUnitTest --tests "com.example.beacon.ExampleUnitTest"`

- Run all instrumentation (device/emulator) tests:
  - `./gradlew :app:connectedDebugAndroidTest`

### Lint and checks

- Run Android lint on the app module:
  - `./gradlew :app:lint`

- Run a broader verification pass (if you want a generic check target):
  - `./gradlew :app:check`

## High-level architecture

### Modules and configuration

- **Single app module**: All production code lives in `app/`.
- **Gradle configuration**:
  - Root `build.gradle.kts` applies plugins via a version catalog (`libs.plugins.*`), leaving actual plugin application to modules.
  - `settings.gradle.kts` configures repositories (`google()`, `mavenCentral()`) and includes the `:app` module.
  - `gradle.properties` sets standard AndroidX usage and Kotlin style; there are no custom Gradle behaviors specific to this project.
- **Android manifest** (`app/src/main/AndroidManifest.xml`):
  - Declares a single `MainActivity` as the launcher activity with `Theme.Beacon`.

### UI and theming

The app uses a Compose-first UI hosted by `MainActivity`.

- **Entry point**: `app/src/main/java/com/example/beacon/MainActivity.kt`.
  - `MainActivity` extends `ComponentActivity` and sets Compose content in `onCreate`.
  - `BeaconTheme` wraps the entire UI tree and provides Material 3 color schemes and typography.

- **Theming** (`app/src/main/java/com/example/beacon/ui/theme/`):
  - `Theme.kt` defines `BeaconTheme`, which selects between light/dark and (on Android 12+) dynamic color schemes using `dynamicDarkColorScheme` / `dynamicLightColorScheme`.
  - Other theme files (`Color.kt`, `Type.kt`) define color palettes and typography used by `MaterialTheme`.
  - The rest of the app should use `MaterialTheme` (colors, typography) rather than hard-coding styles wherever possible.

### Screen composition and state

Most of the current app behavior is implemented directly in `MainActivity.kt` as top-level composables and a small state model.

- **Severity model**:
  - `SeverityLevel` enum (`MINOR`, `MAJOR`, `DISASTER`) represents alert severity for SOS-like actions.

- **Activity-level state**:
  - `MainActivity` maintains a `mutableStateOf("P2P: Idle")` field for peer-to-peer status.
  - Helper methods like `isInternetAvailable()` and `isGpsEnabled()` are currently stubbed (`false` / `true`) and used to derive display strings for status chips.

- **Top-level screen**: `BeaconHomeScreen(...)` composable:
  - Accepts callbacks for `onSendAlert` and `onCustomMessage`, plus three status strings (`internetStatus`, `gpsStatus`, `p2pStatus`).
  - Manages UI state for the severity selection and bottom sheet visibility via `remember`.
  - Layout:
    - A row of **status chips** showing internet, GPS, and P2P status.
    - A large central **SOS button** that selects `DISASTER` severity and opens a confirmation sheet.
    - A column of **severity-specific buttons** (`Minor`, `Major`, `Disaster`) that each open the confirmation sheet with the corresponding severity.
    - A row of two **quick action buttons** (`Help Request`, `Custom Msg`) wired to the higher-level callbacks.
    - A placeholder **map preview** area implemented as a styled `Box`.
  - When a severity is selected and `showSheet` is true, it displays `AlertConfirmationSheet` as a Compose `ModalBottomSheet`.

- **Composable components** defined in `MainActivity.kt`:
  - `QuickActionButton(label, onClick)`: Standard Material 3 `Button` with rounded corners.
  - `SOSButton(onClick)`: Large circular red button with "SOS" text; used as the central call-to-action.
  - `StatusChip(text, color)`: Rounded chip-style indicator for connectivity and P2P statuses.
  - `SeverityButtons(onMinor, onMajor, onDisaster)`: Column of three severity-specific buttons wired to external callbacks.
  - `AlertConfirmationSheet(severity, onSend, onCancel)`: `ModalBottomSheet` summarizing the selected severity, a fixed message, and a map preview placeholder; exposes `onSend` and `onCancel` callbacks.
  - `SosSendDialog(onConfirm, onDismiss)`: An `AlertDialog`-based alternative confirmation UI for sending an SOS alert, not currently integrated into the main screen.

Overall, there is currently no separate data layer or navigation graph: business logic is minimal and mostly confined to `MainActivity`, with UI decomposed into a small set of composables in the same file and theming isolated under `ui/theme/`.

## Notes for future changes

- When adding new screens or features, prefer extracting composables into their own files and packages under `com.example.beacon` to keep `MainActivity.kt` focused on entry and high-level composition.
- If you introduce networking, persistence, or P2P logic, consider creating a simple domain/data layer (e.g., repository or use-case classes) instead of placing that logic directly in `MainActivity` or composables.