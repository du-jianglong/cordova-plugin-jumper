/**
 * Created by initmrd on 16/9/5.
 */
var exec = require('cordova/exec');

function Jumper() {}

Jumper.prototype.AppGo = function (appName,success,error) {
    //APP跳转,需要提供
    /*
        {
            urlSchema: '',       //App Schema or App package name
            suffixText: '',     //传入参数
            appType: '',        //App类型 appstore/inhouse/
            downloadUr: ''      //App下载地址
        }
    */
    exec(success,error,"Jumper","appGo", [appName]);
}

Jumper.prototype.AppLiteGo = function (url,success,error) {
    exec(success,error,"Jumper","appLiteGo", [url]);
}

module.exports = new Jumper();