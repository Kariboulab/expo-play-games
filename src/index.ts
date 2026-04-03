import ExpoPlayGamesModule from './ExpoPlayGamesModule';

export type { PlayerInfo } from './ExpoPlayGames.types';

const PlayGames = {
  signIn: (): Promise<boolean> => ExpoPlayGamesModule.signIn(),
  signOut: (): Promise<void> => ExpoPlayGamesModule.signOut(),
  isSignedIn: (): Promise<boolean> => ExpoPlayGamesModule.isSignedIn(),
  getCurrentPlayer: () => ExpoPlayGamesModule.getCurrentPlayer(),

  submitScore: (leaderboardId: string, score: number): Promise<boolean> =>
    ExpoPlayGamesModule.submitScore(leaderboardId, score),
  showLeaderboard: (leaderboardId: string): Promise<void> =>
    ExpoPlayGamesModule.showLeaderboard(leaderboardId),
  showAllLeaderboards: (): Promise<void> => ExpoPlayGamesModule.showAllLeaderboards(),

  unlockAchievement: (achievementId: string): Promise<boolean> =>
    ExpoPlayGamesModule.unlockAchievement(achievementId),
  incrementAchievement: (achievementId: string, steps: number): Promise<boolean> =>
    ExpoPlayGamesModule.incrementAchievement(achievementId, steps),
  showAchievements: (): Promise<void> => ExpoPlayGamesModule.showAchievements(),
};

export { PlayGames };
export default PlayGames;
