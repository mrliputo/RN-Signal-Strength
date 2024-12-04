package com.infosignalstrength;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class InfoSignalStrengthModule extends ReactContextBaseJavaModule {

  private TelephonyManager telephonyManager;

  private static final String NAME = "InfoSignalStrength";

  public InfoSignalStrengthModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void getCurrentSignalStrength(Promise promise) {
    telephonyManager = (TelephonyManager) getReactApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    getCellSignalLevel(promise);
  }


  @ReactMethod
  public void getTotalRxTxBytes(Promise promise) {
    long startTime = System.currentTimeMillis();
    long RxBytes = TrafficStats.getTotalRxBytes();
    long TxBytes = TrafficStats.getTotalTxBytes();


    WritableMap speedInfo = new WritableNativeMap();
    speedInfo.putDouble("rxBytes", RxBytes);
    speedInfo.putDouble("txBytes", TxBytes);
    speedInfo.putDouble("time", startTime);


    promise.resolve(speedInfo);
  }

  private void getCellSignalLevel(Promise promise) {
    // API level 23 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      promise.resolve(telephonyManager.getSignalStrength().getLevel());
    } else {
      getCellSignalLevelLegacy(promise);
    }
  }

  // For API level 22 and below
  private void getCellSignalLevelLegacy(final Promise promise) {
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
      @Override
      public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        int signalLevel = getSignalLevelLegacy(signalStrength);
        promise.resolve(signalLevel);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
      }
    };
    telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
  }

  private int getSignalLevelLegacy(SignalStrength signalStrength) {
    int signalStrengthValue;
    if (signalStrength.isGsm()) {
      // For GSM networks
      signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
    } else {
      // For CDMA networks
      signalStrengthValue = signalStrength.getCdmaDbm();
    }

    // Map the signal strength value to a level manually
    if (signalStrengthValue >= -70) {
      return 44;
    } else if (signalStrengthValue >= -85) {
      return 33;
    } else if (signalStrengthValue >= -100) {
      return 22;
    } else if (signalStrengthValue >= -110) {
      return 11;
    } else {
      return 00;
    }
  }
}
