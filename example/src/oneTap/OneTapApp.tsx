import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Alert,
  Button,
  Image,
  ScrollView,
  SafeAreaView,
} from 'react-native';
import {
  GoogleOneTapSignIn,
  NativeModuleError,
  statusCodes,
} from '@react-native-google-signin/google-signin';
import type { User } from '@react-native-google-signin/google-signin';
// @ts-ignore see docs/CONTRIBUTING.md for details
import config from '../config';
import { OneTapUser } from '../../../src/OneTapSignIn';

type ErrorWithCode = Error & { code?: string };

type State = {
  error?: ErrorWithCode;
  userInfo?: OneTapUser;
};

const prettyJson = (value: any) => {
  return JSON.stringify(value, null, 2);
};
const PROFILE_IMAGE_SIZE = 150;

export default class GoogleSigninSampleApp extends Component<{}, State> {
  state = {
    userInfo: undefined,
    error: undefined,
  };

  render() {
    const { userInfo } = this.state;

    const body = userInfo ? (
      this.renderUserInfo(userInfo)
    ) : (
      <>
        <Button onPress={this._signIn} title="Sign in with One-tap!" />
        <Button onPress={this._createAccount} title="Sign up with One-tap!" />
      </>
    );
    return (
      <SafeAreaView style={[styles.pageContainer]}>
        <ScrollView contentContainerStyle={styles.container}>
          {body}
          {this.renderError()}
        </ScrollView>
      </SafeAreaView>
    );
  }

  renderUserInfo(userInfo: User) {
    return (
      <View style={styles.container}>
        <Text style={styles.welcomeText}>Welcome, {userInfo.user.name}</Text>
        <Text selectable style={{ color: 'black' }}>
          Your user info:{' '}
          {prettyJson({
            ...userInfo,
            idToken: `${userInfo.idToken?.slice(0, 5)}...`,
            user: {
              ...userInfo.user,
              photo: `${userInfo.user.photo?.slice(0, 5)}...`,
            },
          })}
        </Text>
        {userInfo.user.photo && (
          <Image
            style={{ width: PROFILE_IMAGE_SIZE, height: PROFILE_IMAGE_SIZE }}
            source={{ uri: userInfo.user.photo }}
          />
        )}

        <Button onPress={this._signOut} title="Log out" />
      </View>
    );
  }

  renderError() {
    const { error } = this.state;
    if (error !== undefined) {
      // @ts-ignore
      const text = `${error.toString()} ${error.code ? error.code : ''}`;
      return (
        <Text selectable style={{ color: 'black' }}>
          {text}
        </Text>
      );
    }
    return null;
  }

  _createAccount = async () => {
    try {
      const userInfo = await GoogleOneTapSignIn.createAccount({
        webClientId: config.webClientId,
      });
      this.setState({ userInfo, error: undefined });
    } catch (error) {
      const typedError = error as NativeModuleError;
      Alert.alert('Something went wrong', typedError.toString());
    }
  };

  _signIn = async () => {
    try {
      const userInfo = await GoogleOneTapSignIn.signIn({
        webClientId: config.webClientId,
      });
      this.setState({ userInfo, error: undefined });
    } catch (error) {
      const typedError = error as NativeModuleError;

      switch (typedError.code) {
        case statusCodes.NO_SAVED_CREDENTIAL_FOUND: {
          this._createAccount();
          break;
        }
        case statusCodes.SIGN_IN_CANCELLED:
          // sign in was cancelled
          Alert.alert('cancelled');
          break;
        case statusCodes.IN_PROGRESS:
          // operation (eg. sign in) already in progress
          Alert.alert('in progress');
          break;
        case statusCodes.PLAY_SERVICES_NOT_AVAILABLE:
          // android only
          Alert.alert('play services not available or outdated');
          break;
        default:
          Alert.alert('Something went wrong', typedError.toString());
          this.setState({
            error: typedError,
          });
      }
    }
  };

  _signOut = async () => {
    try {
      await GoogleOneTapSignIn.signOut();

      this.setState({ userInfo: undefined, error: undefined });
    } catch (error) {
      const typedError = error as NativeModuleError;

      this.setState({
        error: typedError,
      });
    }
  };
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    flexGrow: 1,
  },
  welcomeText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 20,
    color: 'black',
  },
  pageContainer: { flex: 1, backgroundColor: '#F5FCFF' },
});
