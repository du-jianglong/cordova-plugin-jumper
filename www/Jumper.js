/**
 * Created by initmrd on 16/9/5.
 */
var exec = require('cordova/exec');

exports.AppGo = function (appName) {
    exec(success,error,"Jumper","AppGo", []);
}