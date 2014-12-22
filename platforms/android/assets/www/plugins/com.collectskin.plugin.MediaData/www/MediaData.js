cordova.define("com.collectskin.plugin.MediaData.MediaData", function(require, exports, module) { var exec = require('cordova/exec');

exports.coolMethod = function(success, error) {
    exec(success, error, "MediaData", "get_art_path", []);
};

});
