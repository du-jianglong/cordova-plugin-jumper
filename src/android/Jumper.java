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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Jumper extends CordovaPlugin {
    //下载是否成功
    private boolean isDownload = false;
    //下载缓存
    private File cacheFile = null;
    //App名称
    private String appPackageName = "";
    private static final String TAG = "JumperPlugins";
    private Activity mActivity = null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mActivity = cordova.getActivity();
        if (action.equals("appGo")) {
            Log.d(TAG, "==============================" + args);
            //获取传入参数
            JSONObject mObject = args.getJSONObject(0);
            appPackageName = mObject.getString("urlSchema");
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
                //在新栈中打开应用
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                List<ResolveInfo> resolveInfoList = mActivity.getPackageManager().queryIntentActivities(mIntent, 0);
                ResolveInfo mResolveInfo = resolveInfoList.iterator().next();
                if (mResolveInfo != null) {
                    //获取跳转目标应用的包名和activity名
                    String packageName = mResolveInfo.activityInfo.packageName;
                    String activtyName = mResolveInfo.activityInfo.name;
                    //将包名和activity名打包，准备回调
                    Map<String,String> callbackInfo = new HashMap<String, String>();
                    callbackInfo.put("packageName",packageName);
                    callbackInfo.put("activtyName",activtyName);
                    JSONObject callbackJson = new JSONObject(callbackInfo);
                    //准备跳转应用
                    ComponentName mComponentName = new ComponentName(packageName, activtyName);
                    mIntent.setComponent(mComponentName);
                    //跳转应用
                    mActivity.startActivity(mIntent);
                    //回调
                    callbackContext.success(callbackJson);
                }

            }
            return true;
        }
        if(action.equals("appLiteGo")){
            Intent mIntent = new Intent(mActivity,WebViewActivity.class);
            mIntent.putExtra("url",args.getString(0));
            mActivity.startActivity(mIntent);
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
        cacheFile = file;
        isDownload = true;
        this.cordova.getActivity().startActivity(intent);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        Log.d(TAG,"校验安装状态是否成功:"+isDownload);
        if(isDownload){
            PackageInfo packageInfo = null;
            try {
                packageInfo = mActivity.getPackageManager().getPackageInfo(appPackageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(packageInfo ==null){
                isDownload = false;
                Intent intent = new Intent();
                //执行动作
                intent.setAction(Intent.ACTION_VIEW);
                //执行的数据类型
                intent.setDataAndType(Uri.fromFile(cacheFile), "application/vnd.android.package-archive");
                this.cordova.getActivity().startActivity(intent);
            }
        }
    }

    //下载APK
    protected void downLoadApk(final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this.cordova.getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgressNumberFormat("%1d 字节/%2d 字节");
        pd.setMessage("正在下载");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    DownLoadManager mManager = new DownLoadManager();
                    File file = mManager.getFileFromServer(url, pd);
                    Log.d(TAG, url);
                    //用户体验
                    sleep(1500);
                    pd.dismiss();
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
        Log.d(TAG, "显示消息" + message);
        builer.setMessage(message);
        //当点确定按钮时从服务器上下载 新的apk 然后安装װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
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
            Log.d(TAG, "检查sd卡" + Environment.getExternalStorageState());
            //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d(TAG, "找到sd卡");
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                //获取到文件的大小
                int unitKB = 1;
                int unitMB = 1;
                if(conn.getContentLength()>1024){
                    unitKB = 1024;
                    pd.setProgressNumberFormat("%1d KB/%2d KB");
                }
                if(conn.getContentLength()>(1024*1024)){
                    unitKB = 1024;
                    unitMB = 1024;
                    pd.setProgressNumberFormat("%1d MB/%2d MB");
                }
                pd.setMax(conn.getContentLength()/unitKB/unitMB);
                InputStream is = conn.getInputStream();
                Log.d(TAG, "下载目标路径" + Environment.getExternalStorageDirectory());
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
                    pd.setProgress(total/unitKB/unitMB);
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
        Log.d(TAG, "===========init============");
    }
}
