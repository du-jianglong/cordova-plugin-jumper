# cordova-plugin-jumper

### APP打开另一个APP

## 安装

### cordova plugin add https://github.com/initMrD/cordova-plugin-jumper.git

## 实例：
### 在js中如何使用
#### 打开app:
<pre>
    <code>
var appInfo =
    {
        urlSchema: '', //ios的Schema或者android的包名
        suffixText: '', //传入参数(option)
        downloadUrl: '' //APP下载地址（apk或者plist），当手机内找不到该应用会提示用户下载
    }
cordova.plugins.Jumper.AppGo(appInfo,success);
    </code>
</pre>

#### 打开webapp:
<pre>
    <code>
cordova.plugins.Jumper.AppLiteGo("webapp的url");
    </code>
</pre>

---

### App open other App

## Setup

### cordova plugin add https://github.com/initMrD/cordova-plugin-jumper.git

## for example：
### in ionic1
#### open app:
<pre>
    <code>
var appInfo =
    {
        urlSchema: '', //App Schema or App package name
        suffixText: '', //param(option)
        downloadUrl: '' //App download Url(plist or apk),if can not open APP,will be download it
    }
cordova.plugins.Jumper.AppGo(appInfo,success);
    </code>
</pre>

#### open webapp:
<pre>
    <code>
cordova.plugins.Jumper.AppLiteGo("your webapp url");
    </code>
</pre>

