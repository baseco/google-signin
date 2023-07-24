import React, { useEffect } from 'react';

import {
  Platform,
  DeviceEventEmitter,
  StyleSheet,
  useColorScheme,
} from 'react-native';
import type { GoogleSigninButtonProps } from './types';
import RNGoogleSigninButton from './spec/SignInButtonNativeComponent';
import { NativeModule } from './spec/NativeGoogleSignin';

const { BUTTON_SIZE_WIDE, BUTTON_SIZE_ICON, BUTTON_SIZE_STANDARD } =
  NativeModule.getConstants();

export const GoogleSigninButton = ({
  onPress,
  style,
  color,
  size = BUTTON_SIZE_STANDARD,
  ...rest
}: GoogleSigninButtonProps) => {
  useEffect(() => {
    if (Platform.OS === 'ios') {
      return;
    }
    const clickListener = DeviceEventEmitter.addListener(
      'RNGoogleSigninButtonClicked',
      () => {
        onPress?.();
      },
    );
    return () => {
      clickListener.remove();
    };
  }, [onPress]);

  const activeColorScheme = useColorScheme();
  const usedColor = color ?? activeColorScheme ?? 'light';

  const recommendedSize = (() => {
    switch (size) {
      case BUTTON_SIZE_ICON:
        return styles.iconSize;
      case BUTTON_SIZE_WIDE:
        return styles.wideSize;
      default:
        return styles.standardSize;
    }
  })();

  return (
    <RNGoogleSigninButton
      {...rest}
      size={size}
      onPress={onPress}
      color={usedColor}
      style={StyleSheet.compose(recommendedSize, style)}
    />
  );
};

GoogleSigninButton.Size = {
  Icon: BUTTON_SIZE_ICON,
  Standard: BUTTON_SIZE_STANDARD,
  Wide: BUTTON_SIZE_WIDE,
} as const;

GoogleSigninButton.Color = {
  Dark: 'dark',
  Light: 'light',
} as const;

// sizes according to https://developers.google.com/identity/sign-in/ios/reference/Classes/GIDSignInButton
const styles = StyleSheet.create({
  iconSize: {
    width: 48,
    height: 48,
  },
  standardSize: { width: 230, height: 48 },
  wideSize: { width: 312, height: 48 },
});
