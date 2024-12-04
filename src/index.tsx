import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'info-signal-strength' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const InfoSignalStrength = NativeModules.InfoSignalStrength
  ? NativeModules.InfoSignalStrength
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function getCurrentSignalStrength(): Promise<number> {
  return InfoSignalStrength.getCurrentSignalStrength();
}

export function getTotalRxTxBytes(): Promise<object> {
  return InfoSignalStrength.getTotalRxTxBytes();
}
