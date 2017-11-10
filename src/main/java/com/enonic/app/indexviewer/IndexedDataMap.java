package com.enonic.app.indexviewer;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.xp.data.PropertyPath;
import com.enonic.xp.data.Value;
import com.enonic.xp.index.IndexConfig;

public class IndexedDataMap
{
    final Map<PropertyPath, IndexAndValue> indexConfigs;

    private IndexedDataMap( final Builder builder )
    {
        indexConfigs = builder.indexConfigs;
    }

    public Map<PropertyPath, IndexAndValue> getIndexConfigs()
    {
        return indexConfigs;
    }

    public static Builder create()
    {
        return new Builder();
    }


    public static final class Builder
    {
        private Map<PropertyPath, IndexAndValue> indexConfigs = Maps.newHashMap();

        private Builder()
        {
        }

        public Builder add( final PropertyPath path, final IndexConfig indexConfig, final Value value )
        {
            indexConfigs.put( path, new IndexAndValue( indexConfig, value ) );
            return this;
        }

        public IndexedDataMap build()
        {
            return new IndexedDataMap( this );
        }
    }

    public static class IndexAndValue
    {
        private IndexConfig indexConfig;

        private Value value;

        public IndexAndValue( final IndexConfig indexConfig, final Value value )
        {
            this.indexConfig = indexConfig;
            this.value = value;
        }

        public IndexConfig getIndexConfig()
        {
            return indexConfig;
        }

        public Value getValue()
        {
            return value;
        }
    }

}
