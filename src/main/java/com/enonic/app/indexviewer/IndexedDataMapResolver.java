package com.enonic.app.indexviewer;

import com.enonic.xp.data.Property;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.data.PropertyVisitor;
import com.enonic.xp.index.IndexConfig;
import com.enonic.xp.index.IndexConfigDocument;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeService;

public class IndexedDataMapResolver
{
    private final NodeService nodeService;

    private final NodeId nodeId;

    private IndexedDataMapResolver( final Builder builder )
    {
        nodeService = builder.nodeService;
        nodeId = builder.nodeId;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public IndexedDataMap resolve()
    {
        final IndexedDataMap.Builder result = IndexedDataMap.create();

        final Node node = this.nodeService.getById( NodeId.from( nodeId ) );

        final IndexConfigDocument indexConfig = node.getIndexConfigDocument();

        final PropertyTree data = node.data();

        new PropertyVisitor()
        {
            @Override
            public void visit( final Property property )
            {
                final IndexConfig processedIndexConfig = processIndexConfigValue( property, indexConfig );
                result.add( property.getPath().resetAllIndexesTo( 0 ), processedIndexConfig, property.getValue() );
            }
        }.traverse( data.getRoot() );

        return result.build();
    }

    private IndexConfig processIndexConfigValue( final Property property, final IndexConfigDocument indexConfig )
    {
        boolean fulltext = false;
        boolean allText = false;
        boolean nGram = false;

        final IndexConfig configForPath = indexConfig.getConfigForPath( property.getPath() );

        if ( configForPath.isEnabled() && configForPath.isDecideByType() )
        {
            if ( property.getValue().isText() )
            {
                fulltext = true;
                nGram = true;
                allText = true;
            }
        }
        else
        {
            fulltext = configForPath.isFulltext();
            allText = configForPath.isIncludeInAllText();
            nGram = configForPath.isnGram();
        }

        return IndexConfig.create().
            addIndexValueProcessor(
                configForPath.getIndexValueProcessors().size() > 0 ? configForPath.getIndexValueProcessors().get( 0 ) : null ).
            fulltext( fulltext ).
            includeInAllText( allText ).
            decideByType( configForPath.isDecideByType() ).
            nGram( nGram ).
            build();
    }


    public static final class Builder
    {
        private NodeService nodeService;

        private NodeId nodeId;

        private Builder()
        {
        }

        public Builder nodeService( final NodeService val )
        {
            nodeService = val;
            return this;
        }

        public Builder nodeId( final NodeId val )
        {
            nodeId = val;
            return this;
        }

        public IndexedDataMapResolver build()
        {
            return new IndexedDataMapResolver( this );
        }
    }
}
