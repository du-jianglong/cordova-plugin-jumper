package org.apache.cordova.jumper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Jumper extends CordovaPlugin {
    Activity mActivity = null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mActivity = cordova.getActivity();
        if (action.equals("appGo")) {
            Log.d("JumperPlugins", "==============================" + args);
            //获取传入参数
            JSONObject mObject = args.getJSONObject(0);
            String appPackageName = mObject.getString("urlSchema");
            String downLoadUrl = mObject.getString("downloadUrl");
            //检测手机中是否存在apk
            PackageInfo packageInfo = null;
            try {
                packageInfo = mActivity.getPackageManager().getPackageInfo(appPackageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(mActivity, "找不到该应用", Toast.LENGTH_LONG).show();
            }
            if (packageInfo == null) {
                showUpdataDialog(downLoadUrl, "是否下载安装?");
                return false;
            } else {
                Intent mIntent = new Intent(Intent.ACTION_MAIN);
                mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
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

            }
            return true;
        }
        return false;
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        this.cordova.getActivity().startActivity(intent);
    }

    //下载APK
    protected void downLoadApk(final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this.cordova.getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    DownLoadManager mManager = new DownLoadManager();
                    File file = mManager.getFileFromServer(url, pd);
                    Log.d("JumperPlugins", url);
                    //用户体验
                    sleep(1500);
                    installApk(file);
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(mActivity, "下载安装失败", Toast.LENGTH_LONG).show();
                    pd.dismiss(); //结束掉进度条对话框
                    Looper.loop();
                }
            }
        }.start();

    }

    //提示下载框
    protected void showUpdataDialog(final String url, String message) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this.cordova.getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builer.setTitle("下载安装");
        Log.d("JumperPlugins", "显示消息" + message);
        builer.setMessage(message);
        //当点确定按钮时从服务器上下载 新的apk 然后安装װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("JumperPlugins", "下载apk,更新");
                downLoadApk(url);
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    //下载管理器
    class DownLoadManager {
        File getFileFromServer(String path, ProgressDialog pd) throws Exception {
            Log.d("JumperPlugins", "检查sd卡" + Environment.getExternalStorageState());
            //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d("JumperPlugins", "找到sd卡");
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                //获取到文件的大小
                pd.setMax(conn.getContentLength());
                InputStream is = conn.getInputStream();
                Log.d("JumperPlugins", "下载目标路径" + Environment.getExternalStorageDirectory());
                File file = null;
                FileOutputStream fos = null;
                try{
                    file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                    fos = new FileOutputStream(file);
                }catch (FileNotFoundException e){
                    file = new File(Environment.getDownloadCacheDirectory(), "updata.apk");
                    fos = new FileOutputStream(file);
                }
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    //获取当前下载量
                    pd.setProgress(total);
                }
                fos.close();
                bis.close();
                is.close();
                return file;
            } else {
                return null;
            }
        }

    }

    public Jumper() {
        Log.d("JumperPlugins", "===========init============");
    }
}
