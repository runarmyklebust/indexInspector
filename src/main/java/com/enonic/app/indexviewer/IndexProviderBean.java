package com.enonic.app.indexviewer;


import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class IndexProviderBean
    implements ScriptBean
{
    private NodeService nodeService;


    @Override
    public void initialize( final BeanContext context )
    {
        this.nodeService = context.getService( NodeService.class ).get();
    }

    @SuppressWarnings("unused")
    public Object resolveIndexDataMap( final String nodeId )
    {
        final IndexedDataMap result = IndexedDataMapResolver.create().
            nodeService( this.nodeService ).
            nodeId( NodeId.from( nodeId ) ).
            build().
            resolve();

        return new IndexConfigMapMapper( result );
    }
}
