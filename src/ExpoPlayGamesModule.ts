import { NativeModule, requireNativeModule } from 'expo';

import { ExpoPlayGamesModuleEvents } from './ExpoPlayGames.types';

declare class ExpoPlayGamesModule extends NativeModule<ExpoPlayGamesModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoPlayGamesModule>('ExpoPlayGames');
