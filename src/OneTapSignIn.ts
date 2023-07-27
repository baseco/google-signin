import { OneTapNativeModule } from './spec/NativeOneTapSignIn.android';
import { Platform } from 'react-native';

export type OneTapSignInParams = {
  webClientId: string;
  nonce?: string;
  autoSignIn?: boolean;
  filterByAuthorizedAccounts?: boolean;
  passwordRequestSupported?: boolean;
  idTokenRequestSupported?: boolean;
};
export type OneTapUser = {
  user: {
    id: string;
    name: string | null;
    givenName: string | null;
    familyName: string | null;
    photo: string | null;
  };
} & TokenOrPassword;

type TokenOrPassword =
  | {
      idToken: string;
      password: null;
    }
  | {
      idToken: null;
      password: string;
    };

const unsupportedPlatformError = new Error(
  'OneTapSignIn is now only available on Android',
);

const signIn = (params: OneTapSignInParams): Promise<OneTapUser> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(unsupportedPlatformError);
  }
  return OneTapNativeModule?.signIn({
    autoSignIn: true,
    filterByAuthorizedAccounts: true,
    passwordRequestSupported: true,
    idTokenRequestSupported: true,
    ...params,
  }) as Promise<OneTapUser>;
};
const createAccount = (params: OneTapSignInParams): Promise<OneTapUser> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(unsupportedPlatformError);
  }
  return OneTapNativeModule?.signIn({
    autoSignIn: false,
    filterByAuthorizedAccounts: false,
    passwordRequestSupported: false,
    idTokenRequestSupported: true,
    ...params,
  }) as Promise<OneTapUser>;
};

export const GoogleOneTapSignIn = {
  signIn,
  signOut: (): Promise<null> => {
    if (Platform.OS !== 'android') {
      return Promise.reject(unsupportedPlatformError);
    }
    return OneTapNativeModule? OneTapNativeModule.signOut() : null;
  },
  createAccount,
};
