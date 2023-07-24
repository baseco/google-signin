
/**
 * This code was generated by [react-native-codegen](https://www.npmjs.com/package/react-native-codegen).
 *
 * @generated by codegen project: GenerateModuleJavaSpec.js
 *
 * @nolint
 */

package com.reactnativegooglesignin;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactModuleWithSpec;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.build.ReactBuildConfig;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class NativeGoogleSigninSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public static final String NAME = "RNGoogleSignin";

  public NativeGoogleSigninSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public @Nonnull String getName() {
    return NAME;
  }

  @ReactMethod
  @DoNotStrip
  public abstract void signIn(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void configure(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void addScopes(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void playServicesAvailable(boolean showPlayServicesUpdateDialog, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void signInSilently(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void signOut(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void revokeAccess(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void clearCachedAccessToken(String tokenString, Promise promise);

  @ReactMethod(isBlockingSynchronousMethod = true)
  @DoNotStrip
  public abstract boolean hasPreviousSignIn();

  @ReactMethod(isBlockingSynchronousMethod = true)
  @DoNotStrip
  public abstract @Nullable WritableMap getCurrentUser();

  @ReactMethod
  @DoNotStrip
  public abstract void getTokens(Promise promise);

  protected abstract Map<String, Object> getTypedExportedConstants();

  @Override
  @DoNotStrip
  public final @Nullable Map<String, Object> getConstants() {
    Map<String, Object> constants = getTypedExportedConstants();
    if (ReactBuildConfig.DEBUG || ReactBuildConfig.IS_INTERNAL_BUILD) {
      Set<String> obligatoryFlowConstants = new HashSet<>(Arrays.asList(
          "BUTTON_SIZE_ICON",
          "BUTTON_SIZE_STANDARD",
          "BUTTON_SIZE_WIDE",
          "IN_PROGRESS",
          "PLAY_SERVICES_NOT_AVAILABLE",
          "SIGN_IN_CANCELLED",
          "SIGN_IN_REQUIRED"
      ));
      Set<String> optionalFlowConstants = new HashSet<>();
      Set<String> undeclaredConstants = new HashSet<>(constants.keySet());
      undeclaredConstants.removeAll(obligatoryFlowConstants);
      undeclaredConstants.removeAll(optionalFlowConstants);
      if (!undeclaredConstants.isEmpty()) {
        throw new IllegalStateException(String.format("Native Module Flow doesn't declare constants: %s", undeclaredConstants));
      }
      undeclaredConstants = obligatoryFlowConstants;
      undeclaredConstants.removeAll(constants.keySet());
      if (!undeclaredConstants.isEmpty()) {
        throw new IllegalStateException(String.format("Native Module doesn't fill in constants: %s", undeclaredConstants));
      }
    }
    return constants;
  }
}
