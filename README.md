# cordova-plugin-jumper

### App open other App

## Setup

###cordova plugin add https://github.com/initMrD/cordova-plugin-jumper.git

##for example：
<pre>
    <code>
var appInfo =
    {
        urlSchema: '', //App Schema or App package name
        suffixText: '', //param(option)
        appType: '', //App type(iOS only) appstore/inhouse/ 
        downloadUrl: '' //App download Url
    }
    cordova.plugins.Jumper.AppGo(appInfo);
    </code>
</pre>
