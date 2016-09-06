/**
 * Created by initmrd on 16/9/5.
 */
var exec = require('cordova/exec');

exports.AppGo = function (appName,success,error) {
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