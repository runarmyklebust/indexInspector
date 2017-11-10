package com.enonic.app.indexviewer;

import java.util.Map;

import com.enonic.xp.data.PropertyPath;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class IndexConfigMapMapper
    implements MapSerializable
{
    private final IndexedDataMap indexDataMap;

    public IndexConfigMapMapper( final IndexedDataMap indexedDataMap )
    {
        this.indexDataMap = indexedDataMap;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        final Map<PropertyPath, IndexedDataMap.IndexAndValue> indexAndValueMap = indexDataMap.getIndexConfigs();

        indexAndValueMap.keySet().forEach( key -> {

            gen.map( key.toString() );
            serialize( gen, indexAndValueMap.get( key ) );
            gen.end();
        } );
    }

    private void serialize( final MapGenerator gen, final IndexedDataMap.IndexAndValue indexAndValue )
    {
        gen.value( "value", indexAndValue.getValue() );
        gen.value( "enabled", indexAndValue.getIndexConfig().isEnabled() );
        gen.value( "fulltext", indexAndValue.getIndexConfig().isFulltext() );
        //gen.value( "decideByType", indexConfig.isDecideByType() );
        gen.value( "alltext", indexAndValue.getIndexConfig().isIncludeInAllText() );
        //gen.value( "nGram", indexConfig.isnGram() );
        //gen.value( "path", indexConfig.isPath() );
        gen.array( "processors" );
        indexAndValue.getIndexConfig().getIndexValueProcessors().forEach( processor -> {
            gen.value( processor.getName() );
        } );
        gen.end();

    }
}
