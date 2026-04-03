# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Is

An Expo module (not an app) that wraps Google Play Games Services v2 for Android. It exposes leaderboards, achievements, and sign-in to React Native apps. **Android-only** — `expo-module.config.json` declares `"platforms": ["android"]`, and the web stub (`src/ExpoPlayGamesModule.web.ts`) rejects all calls.

## Commands

```bash
npm run build       # Compile TypeScript → build/
npm run lint        # ESLint via expo-module-scripts
npm run test        # Jest via expo-module-scripts
npm run clean       # Remove build artifacts
npm run open:android  # Open example in Android Studio
```

To run the example app on a device/emulator:

```bash
cd example
npx expo run:android
```

## Architecture

```
src/
  ExpoPlayGames.types.ts     # Shared TypeScript types (PlayerInfo)
  ExpoPlayGamesModule.ts     # requireNativeModule declaration — native contract
  ExpoPlayGamesModule.web.ts # Web stub — all methods reject immediately
  index.ts                   # Public API surface re-exported as PlayGames object

android/
  build.gradle                                              # Depends on play-services-games-v2:19.0.0
  src/main/java/expo/modules/playgames/
    ExpoPlayGamesModule.kt    # Full Kotlin implementation
```

The JS layer (`src/index.ts`) wraps `ExpoPlayGamesModule.ts` in a plain `PlayGames` object. There is no view component — this module is API-only.

### Android implementation notes

- SDK is initialized in the `OnCreate` lifecycle hook via `PlayGamesSdk.initialize(activity)`
- `signOut()` is intentionally a no-op — Play Games v2 removed explicit sign-out; the user manages account access from the Play Games app
- `submitScore` accepts a `Double` on the Kotlin side (Expo coercion from JS `number`) and converts to `Long` for the SDK
- All methods reject via `promise.reject(errorCode, message, cause)` — error codes follow the pattern `OPERATION_FAILED` (e.g. `SIGN_IN_FAILED`, `SUBMIT_SCORE_FAILED`)

## Publishing

Versioning follows `MAJOR.MINOR.PATCH-beta.N` for pre-release, `MAJOR.MINOR.PATCH` for stable.

```bash
# Update version in package.json, then:
npm run build
npm publish --tag beta --access public   # pre-release
npm publish --access public              # stable
```

Requires an npm Granular Access Token with **Read and write** + **bypass 2FA** enabled.

## KaribouLab Context

This is a shared internal library used across KaribouLab games (starting with EchoStep). It lives alongside other products in `workspaces/`. Product-level tracking is in `../kariboulab-os/`.

---

## Consumer Integration Notes

Apps using this module must add their Play Games app ID to `AndroidManifest.xml`:

```xml
<meta-data android:name="com.google.android.gms.games.APP_ID"
           android:value="@string/app_id" />
```

This module's own `AndroidManifest.xml` is intentionally empty — the app is responsible for providing this.
