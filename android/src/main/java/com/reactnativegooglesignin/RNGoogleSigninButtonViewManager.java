package com.reactnativegooglesignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.common.SignInButton;

@ReactModule(name = RNGoogleSigninButtonViewManager.MODULE_NAME)
public class RNGoogleSigninButtonViewManager extends SimpleViewManager<SignInButton> implements RNGoogleSigninButtonManagerInterface<SignInButton> {

    public static final String MODULE_NAME = "RNGoogleSigninButton";
    private final ViewManagerDelegate<SignInButton> mDelegate;

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    public RNGoogleSigninButtonViewManager() {
        mDelegate = new RNGoogleSigninButtonManagerDelegate(this);
    }

    @Nullable
    @Override
    protected ViewManagerDelegate<SignInButton> getDelegate() {
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            return mDelegate;
        } else {
            return null;
        }
    }

    @Override
    protected SignInButton createViewInstance(@NonNull final ThemedReactContext reactContext) {
        SignInButton button = new SignInButton(reactContext);
        button.setOnClickListener(v -> reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("RNGoogleSigninButtonClicked", null));
        return button;
    }
    @Override
    @ReactProp(name = "size")
    public void setSize(SignInButton button, int size) {
        button.setSize(size);
    }

    @Override
    @ReactProp(name = "disabled")
    public void setDisabled(SignInButton button, boolean disabled) {
        button.setEnabled(!disabled);
    }

    @Override
    @ReactProp(name = "color")
    public void setColor(SignInButton button, @Nullable String value) {
        if (value == null) {
            button.setColorScheme(SignInButton.COLOR_AUTO);
        } else {
            int color = "dark".equals(value) ? SignInButton.COLOR_DARK : SignInButton.COLOR_LIGHT;
            button.setColorScheme(color);
        }
    }
}
