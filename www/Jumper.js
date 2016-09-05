/**
 * Created by initmrd on 16/9/5.
 */
var exec = require('cordova/exec');

exports.AppGo = function (appName,success,error) {
    console.info(exec);
    exec(success,error,"Jumper","appGo", [appName]);
}