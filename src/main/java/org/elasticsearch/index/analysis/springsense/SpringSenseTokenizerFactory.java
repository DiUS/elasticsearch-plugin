package org.elasticsearch.index.analysis.springsense;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.Reader;

public class SpringSenseTokenizerFactory  extends AbstractTokenizerFactory {

	    private final int bufferSize;

	    @Inject
	    public SpringSenseTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
	        super(index, indexSettings, name, settings);
	        bufferSize = settings.getAsInt("buffer_size", 256);
	    }

	    @Override
	    public Tokenizer create(Reader reader) {
	        return new SpringSenseTokenizer(reader, bufferSize);
	    }
	}

