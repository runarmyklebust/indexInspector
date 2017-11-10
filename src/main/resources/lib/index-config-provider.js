var bean = __.newBean('com.enonic.app.indexviewer.IndexProviderBean');

exports.getIndexConfig = function (nodeId) {
    var result = bean.resolveIndexDataMap(nodeId);
    return __.toNativeObject(result);
};
