# cordova-plugin-jumper

### APP打开另一个APP

## 安装

### cordova plugin add https://github.com/initMrD/cordova-plugin-jumper.git

## 实例：
### 在js文件中如何使用
#### 打开APP:
```
var appInfo =
    {
        urlSchema: '', //ios的Schema或者android的包名
        suffixText: '', //传入参数(option)
        downloadUrl: '' //APP下载地址（apk或者plist），当手机内找不到该应用会提示用户下载
    }
cordova.plugins.Jumper.AppGo(appInfo,function (data) {
    console.info(JSON.stringify(data));
});
```

#### 打开webapp:
```
cordova.plugins.Jumper.AppLiteGo("webapp的url");
```
### 在ts文件中如何使用
#### 打开APP：
```
declare let cordova;

var appInfo =
    {
        urlSchema: '', //ios的Schema或者android的包名
        suffixText: '', //传入参数(option)
        downloadUrl: '' //APP下载地址（apk或者plist），当手机内找不到该应用会提示用户下载
    };
cordova.plugins.Jumper.AppGo(appInfo,function (data) {
    console.info(JSON.stringify(data));
});
```
#### 打开webapp:
```
declare let cordova;

cordova.plugins.Jumper.AppLiteGo("webapp的url");
```


---

### App open other App

## Setup

### cordova plugin add https://github.com/initMrD/cordova-plugin-jumper.git

## for example：
### how to used in .js file:
#### open APP:
```
var appInfo =
    {
        urlSchema: '', //App Schema or App package name
        suffixText: '', //param(option)
        downloadUrl: '' //App download Url(plist or apk),if can not open APP,will be download it
    };
cordova.plugins.Jumper.AppGo(appInfo,function (data) {
    console.info(JSON.stringify(data));
});
```

#### open webapp:
```
cordova.plugins.Jumper.AppLiteGo("your webapp url");
```
### how to used in .ts file:
#### open APP:
```
declare let cordova;

var appInfo =
    {
        urlSchema: '', //App Schema or App package name
        suffixText: '', //param(option)
        downloadUrl: '' //App download Url(plist or apk),if can not open APP,will be download it
    };
cordova.plugins.Jumper.AppGo(appInfo,function (data) {
    console.info(JSON.stringify(data));
});
```
#### open webapp:
```
declare let cordova;

cordova.plugins.Jumper.AppLiteGo("your webapp url");
```

