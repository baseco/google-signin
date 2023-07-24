/**
 * @format
 */

import { AppRegistry } from 'react-native';
import App from './src/App';
import OneTapApp from './src/oneTap/OneTapApp';
import { name as appName } from './app.json';

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerComponent('google-one-tap-example', () => OneTapApp);
