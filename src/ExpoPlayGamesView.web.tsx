import * as React from 'react';

import { ExpoPlayGamesViewProps } from './ExpoPlayGames.types';

export default function ExpoPlayGamesView(props: ExpoPlayGamesViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
