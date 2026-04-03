import { registerWebModule, NativeModule } from 'expo';

import { ExpoPlayGamesModuleEvents } from './ExpoPlayGames.types';

class ExpoPlayGamesModule extends NativeModule<ExpoPlayGamesModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ExpoPlayGamesModule, 'ExpoPlayGamesModule');
