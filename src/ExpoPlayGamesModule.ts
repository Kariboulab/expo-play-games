import { NativeModule, requireNativeModule } from 'expo';

import { PlayerInfo } from './ExpoPlayGames.types';

declare class ExpoPlayGamesModule extends NativeModule {
  signIn(): Promise<boolean>;
  signOut(): Promise<void>;
  isSignedIn(): Promise<boolean>;
  getCurrentPlayer(): Promise<PlayerInfo>;

  submitScore(leaderboardId: string, score: number): Promise<boolean>;
  showLeaderboard(leaderboardId: string): Promise<void>;
  showAllLeaderboards(): Promise<void>;

  unlockAchievement(achievementId: string): Promise<boolean>;
  incrementAchievement(achievementId: string, steps: number): Promise<boolean>;
  showAchievements(): Promise<void>;
}

export default requireNativeModule<ExpoPlayGamesModule>('ExpoPlayGames');
