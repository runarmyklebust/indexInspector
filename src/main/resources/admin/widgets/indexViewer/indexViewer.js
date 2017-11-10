var contentLib = require('/lib/xp/content');
var portalLib = require('/lib/xp/portal');
var thymeleaf = require('/lib/xp/thymeleaf');
var indexConfigProvider = require('/lib/index-config-provider');

var view = resolve('indexViewer.html');

function handleGet(req) {
    var uid = req.params.uid;

    var nodeId = req.params.contentId;
    if (!nodeId) {
        nodeId = portalLib.getContent()._id;
    }

    var indexConfig = indexConfigProvider.getIndexConfig(nodeId);

    processIndexConfig(indexConfig);

    var params = {
        uid: uid,
        indexConfig: indexConfig
    };

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, params),
        pageContributions: {
            "headBegin": [
                createJsContribution("js/jquery-3.1.1.min.js"),
                createJsContribution("js/jquery.dataTables.min.js"),
                createCssContribution("css/jquery.dataTables.min.css")
            ],
            "bodyEnd": [
                createJsContribution("js/main.js")
            ]
        }
    };
}

exports.get = handleGet;


var processIndexConfig = function (indexConfig) {

    for (var key in indexConfig) {

        var config = indexConfig[key];

        if (config.value && config.value.length > 400) {
            indexConfig[key].value = config.value.substring(0, 400) + "..."
        }

    }
};

var createCssContribution = function (styleSheetPath) {

    var cssAssetPath = portalLib.assetUrl({
        path: styleSheetPath
    });

    if (!cssAssetPath) {
        log.warn("Could not find css-asset: %s", styleSheetPath);
    }

    return '<link rel="stylesheet" type="text/css" href="' + cssAssetPath + '">';
};

var createJsContribution = function (jsPath) {

    var jsAssetPath = portalLib.assetUrl({
        path: jsPath
    });

    if (!jsAssetPath) {
        log.warn("Could not find js-asset: %s", jsPath);
    }

    return '<script type="text/javascript" src="' + jsAssetPath + '"></script>';
};
