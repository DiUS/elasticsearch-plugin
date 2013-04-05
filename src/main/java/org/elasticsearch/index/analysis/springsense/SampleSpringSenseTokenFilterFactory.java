
package org.elasticsearch.index.analysis.springsense;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

/**
 *
 */
public class SampleSpringSenseTokenFilterFactory extends AbstractTokenFilterFactory {


    @Inject
    public SampleSpringSenseTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
    	return new SampleSpringSenseTokenFilter(tokenStream);
    }
}