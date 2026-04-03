# expo-play-games

Expo module wrapping **Google Play Games Services v2** for Android. Provides sign-in, leaderboards, and achievements via a clean TypeScript API.

**Android only.** iOS is not supported — all methods will throw on iOS/web.

---

## Installation

```sh
npm install expo-play-games
# or
pnpm add expo-play-games
```

No config plugin is required. The SDK is initialized automatically when the module loads.

---

## API

```ts
import { PlayGames } from "expo-play-games";
```

### Authentication

```ts
// Trigger the Play Games sign-in flow
const authenticated: boolean = await PlayGames.signIn();

// Check if the user is currently signed in
const isSignedIn: boolean = await PlayGames.isSignedIn();

// No-op on v2 — resolves immediately (user manages access from the Play Games app)
await PlayGames.signOut();

// Get the currently signed-in player's info
const player = await PlayGames.getCurrentPlayer();
// { playerId: string, displayName: string, title: string | null }
```

### Leaderboards

```ts
// Submit a score (score must be a non-negative integer)
const submitted: boolean = await PlayGames.submitScore("leaderboard_id", 12345);

// Open the Play Games leaderboard UI for a specific leaderboard
await PlayGames.showLeaderboard("leaderboard_id");

// Open the Play Games leaderboard UI showing all leaderboards
await PlayGames.showAllLeaderboards();
```

### Achievements

```ts
// Unlock a standard (non-incremental) achievement
const unlocked: boolean = await PlayGames.unlockAchievement("achievement_id");

// Increment an incremental achievement by N steps; resolves true if newly completed
const completed: boolean = await PlayGames.incrementAchievement(
  "achievement_id",
  1,
);

// Open the Play Games achievements UI
await PlayGames.showAchievements();
```

All methods throw a descriptive `Error` on failure — wrap calls in `try/catch`.

---

## Setup required in the consuming app

### 1. Google Play Console

1. Your app must exist in Play Console (even as a draft).
2. Go to **Grow → Play Games Services → Setup and management → Configuration**.
3. Create a new Play Games Services project and link it to your app.
4. Create the leaderboards and achievements you need — note their IDs.

### 2. Google Cloud Console

1. Open the Cloud project linked to your Play Games project.
2. Go to **APIs & Services → Credentials → Create Credential → OAuth 2.0 Client ID**.
3. Select **Android**, enter your app's package name and the **SHA-1 fingerprint** of your signing certificate.
   - Debug SHA-1: `keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android`
   - Release SHA-1: use the fingerprint from your upload key or Play-managed signing.
4. No `google-services.json` changes are needed — Play Games v2 uses the OAuth credential automatically.

### 3. App startup

`PlayGamesSdk.initialize()` is called automatically when the module loads (in `OnCreate`). You do not need to call anything manually before using the API.

If you want to silently sign in on app launch (recommended), call:

```ts
useEffect(() => {
  PlayGames.signIn().catch(() => {
    // User not signed in — show a manual sign-in button
  });
}, []);
```

### 4. Leaderboard / Achievement IDs

IDs are the string identifiers from Play Console (e.g. `CgkI...`). Pass them directly to the relevant methods.

---

## Not supported

- iOS - This module is specifically for Android.`

## Out of scope (v1)

- Saved games / cloud save
