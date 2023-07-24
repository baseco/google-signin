import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  // using Object to be compatible with paper
  signIn(params: Object): Promise<Object>;
  signOut(): Promise<null>;
}

export const OneTapNativeModule =
  TurboModuleRegistry.getEnforcing<Spec>('RNOneTapSignIn');
