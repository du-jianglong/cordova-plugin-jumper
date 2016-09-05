package org.apache.cordova.jumper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class Jumper extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity mActivity = cordova.getActivity();
        if (action.equals("appGo")) {
            Log.d("plugin", "==============================" + args);
            Intent mIntent = new Intent(Intent.ACTION_MAIN);
            mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageInfo packageInfo = null;
            try {
                packageInfo = mActivity.getPackageManager().getPackageInfo(args.get(0).toString(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (packageInfo == null) {
                Toast.makeText(mActivity, "找不到该应用", Toast.LENGTH_SHORT).show();
                return false;
            }
            mIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> resolveInfoList = mActivity.getPackageManager().queryIntentActivities(mIntent, 0);
            ResolveInfo mResolveInfo = resolveInfoList.iterator().next();
            if (mResolveInfo != null) {
                String packageName = mResolveInfo.activityInfo.packageName;
                String activtyName = mResolveInfo.activityInfo.name;
                ComponentName mComponentName = new ComponentName(packageName, activtyName);
                mIntent.setComponent(mComponentName);
                mActivity.startActivity(mIntent);
            }
            return true;
        }
        return false;
    }

    public Jumper() {
        Log.d("plugin", "===========init============");
    }
}