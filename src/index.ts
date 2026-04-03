// Reexport the native module. On web, it will be resolved to ExpoPlayGamesModule.web.ts
// and on native platforms to ExpoPlayGamesModule.ts
export { default } from './ExpoPlayGamesModule';
export { default as ExpoPlayGamesView } from './ExpoPlayGamesView';
export * from  './ExpoPlayGames.types';
