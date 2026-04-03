import { NativeModule, registerWebModule } from 'expo';

const NOT_SUPPORTED = 'expo-play-games is Android-only and is not supported on this platform.';

class ExpoPlayGamesModule extends NativeModule {
  signIn(): Promise<boolean> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  signOut(): Promise<void> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  isSignedIn(): Promise<boolean> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  getCurrentPlayer(): Promise<never> { return Promise.reject(new Error(NOT_SUPPORTED)); }

  submitScore(_leaderboardId: string, _score: number): Promise<boolean> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  showLeaderboard(_leaderboardId: string): Promise<void> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  showAllLeaderboards(): Promise<void> { return Promise.reject(new Error(NOT_SUPPORTED)); }

  unlockAchievement(_achievementId: string): Promise<boolean> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  incrementAchievement(_achievementId: string, _steps: number): Promise<boolean> { return Promise.reject(new Error(NOT_SUPPORTED)); }
  showAchievements(): Promise<void> { return Promise.reject(new Error(NOT_SUPPORTED)); }
}

export default registerWebModule(ExpoPlayGamesModule, 'ExpoPlayGames');
