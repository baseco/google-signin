package com.reactnativegooglesignin;

import static com.reactnativegooglesignin.RNGoogleSigninModule.rejectWithNullActivity;
import static com.reactnativegooglesignin.Utils.getUserProperties;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;

@ReactModule(name = NativeOneTapSignInSpec.NAME)
public class RNOneTapSignInModule extends NativeOneTapSignInSpec {
    private static final int REQ_ONE_TAP = 2;

    private SignInClient oneTapClient;
    private PromiseWrapper promiseWrapper = new PromiseWrapper();

    public RNOneTapSignInModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(new RNOneTapSigninActivityEventListener());
        oneTapClient = Identity.getSignInClient(reactContext);
    }

    public static final String NO_SAVED_CREDENTIAL_FOUND = "NO_SAVED_CREDENTIAL_FOUND";
    public static final String ONE_TAP_START_FAILED = "ONE_TAP_START_FAILED";

    @Override
    public void signIn(ReadableMap params, Promise promise) {
        Activity activity = getCurrentActivity();

        if (activity == null) {
            rejectWithNullActivity(promise);
            return;
        }
        promiseWrapper.setPromiseWithInProgressCheck(promise, "signIn");

        BeginSignInRequest request = Utils.buildOneTapSignInRequest(params);
        oneTapClient.beginSignIn(request)
            .addOnSuccessListener(activity, result -> {
                try {
                    activity.startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                        null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(NativeOneTapSignInSpec.NAME, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    promiseWrapper.reject(ONE_TAP_START_FAILED, e);
                }
            })
            .addOnFailureListener(activity, e -> {
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                promiseWrapper.reject(NO_SAVED_CREDENTIAL_FOUND, e);
            });

    }

    @Override
    public void signOut(Promise promise) {
        oneTapClient
            .signOut().addOnSuccessListener(aVoid -> {
                promise.resolve(null);
            }).addOnFailureListener(e -> {
                promise.reject(e);
            });
    }

    private class RNOneTapSigninActivityEventListener extends BaseActivityEventListener {
        @Override
        public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent intent) {
             if (requestCode == REQ_ONE_TAP) {
                handleOneTapSignInResult(intent);
            }
        }
    }

    private void handleOneTapSignInResult(Intent data) {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            WritableMap userParams = getUserProperties(credential);
            promiseWrapper.resolve(userParams);
        } catch (ApiException e) {
            // TODO dupe
            int code = e.getStatusCode();
            String errorDescription = GoogleSignInStatusCodes.getStatusCodeString(code);
            promiseWrapper.reject(String.valueOf(code), errorDescription);
        }
    }
}
