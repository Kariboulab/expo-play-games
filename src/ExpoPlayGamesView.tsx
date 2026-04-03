import { requireNativeView } from 'expo';
import * as React from 'react';

import { ExpoPlayGamesViewProps } from './ExpoPlayGames.types';

const NativeView: React.ComponentType<ExpoPlayGamesViewProps> =
  requireNativeView('ExpoPlayGames');

export default function ExpoPlayGamesView(props: ExpoPlayGamesViewProps) {
  return <NativeView {...props} />;
}
