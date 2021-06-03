package com.thronie.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.content.pm.PackageManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;


public class OpenIntentModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Promise intentPromise;
    private Context ctx;

    public OpenIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.ctx = reactContext.getApplicationContext();
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "OpenIntent";
    }

    @ReactMethod
    public void openLink(String url, String packageName, final Promise promise) {
        // TODO: Implement some actually useful functionality
        intentPromise = promise;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (packageName != null) {
            intent.setPackage(packageName);
        }
        try {
            this.reactContext.startActivityForResult(intent, 5864, null);
        } catch (Exception e) {
            intentPromise.reject("Failed", "NO APP FOUND");
        }
    }

    @ReactMethod
    public void isPackageInstalled(String packageName, Callback cb) {
        PackageManager pm = this.ctx.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            cb.invoke(true);
        } catch (Exception e) {
            cb.invoke(false);
        }
    }

    @ReactMethod
    public void getApps(Promise promise) {
        try {
            PackageManager pm = this.reactContext.getPackageManager();
            List < PackageInfo > pList = pm.getInstalledPackages(0);
            WritableArray list = Arguments.createArray();
            for (int i = 0; i < pList.size(); i++) {
                PackageInfo packageInfo = pList.get(i);
                WritableMap appInfo = Arguments.createMap();

                appInfo.putString("packageName", packageInfo.packageName);
                appInfo.putString("versionName", packageInfo.versionName);
                appInfo.putDouble("versionCode", packageInfo.versionCode);
                appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                appInfo.putString("icon", Utility.convert(icon));

                String apkDir = packageInfo.applicationInfo.publicSourceDir;
                appInfo.putString("apkDir", apkDir);

                File file = new File(apkDir);
                double size = file.length();
                appInfo.putDouble("size", size);

                list.pushMap(appInfo);
            }
            promise.resolve(list);
        } catch (Exception ex) {
            promise.reject(ex);
        }
    }

    @ReactMethod
    public void getNonSystemApps(Promise promise) {
        collectNonSystemApps(true, promise);
    }

    @ReactMethod
    public void getNonSystemApps(boolean iconRequired, Promise promise) {
        collectNonSystemApps(iconRequired, promise);
    }

    @ReactMethod
    public void getSystemApps(Promise promise) {
        try {
            PackageManager pm = this.reactContext.getPackageManager();
            List < PackageInfo > pList = pm.getInstalledPackages(0);
            WritableArray list = Arguments.createArray();
            for (int i = 0; i < pList.size(); i++) {
                PackageInfo packageInfo = pList.get(i);
                WritableMap appInfo = Arguments.createMap();

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    appInfo.putString("packageName", packageInfo.packageName);
                    appInfo.putString("versionName", packageInfo.versionName);
                    appInfo.putDouble("versionCode", packageInfo.versionCode);
                    appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                    appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                    appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                    Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                    appInfo.putString("icon", Utility.convert(icon));

                    String apkDir = packageInfo.applicationInfo.publicSourceDir;
                    appInfo.putString("apkDir", apkDir);

                    File file = new File(apkDir);
                    double size = file.length();
                    appInfo.putDouble("size", size);

                    list.pushMap(appInfo);
                }
            }
            promise.resolve(list);
        } catch (Exception ex) {
            promise.reject(ex);
        }

    }

    private void collectNonSystemApps(boolean iconRequired, Promise promise) {
        try {
            PackageManager pm = this.reactContext.getPackageManager();
            List < PackageInfo > pList = pm.getInstalledPackages(0);
            WritableArray list = Arguments.createArray();
            for (int i = 0; i < pList.size(); i++) {
                PackageInfo packageInfo = pList.get(i);
                WritableMap appInfo = Arguments.createMap();

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appInfo.putString("packageName", packageInfo.packageName);
                    appInfo.putString("versionName", packageInfo.versionName);
                    appInfo.putDouble("versionCode", packageInfo.versionCode);
                    appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                    appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                    appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                    if (iconRequired) {
                        Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                        appInfo.putString("icon", Utility.convert(icon));
                    }

                    String apkDir = packageInfo.applicationInfo.publicSourceDir;
                    appInfo.putString("apkDir", apkDir);

                    File file = new File(apkDir);
                    double size = file.length();
                    appInfo.putDouble("size", size);

                    list.pushMap(appInfo);
                }
            }
            promise.resolve(list);
        } catch (Exception ex) {
            promise.reject(ex);
        }
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        //listener for activity
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (intentPromise != null) {
                if (data != null) {
                    String res = data.getStringExtra("response");
                    intentPromise.resolve(res);
                } else {
                    intentPromise.reject("Failed", "");
                }
            }
        }
    };
}