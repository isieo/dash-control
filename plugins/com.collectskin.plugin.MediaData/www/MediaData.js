var exec = require('cordova/exec');

exports.coolMethod = function(success, error) {
    exec(success, error, "MediaData", "get_art_path", []);
};
