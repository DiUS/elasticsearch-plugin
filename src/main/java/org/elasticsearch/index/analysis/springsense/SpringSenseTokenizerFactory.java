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
	    private final boolean transformToLowercase;

	    @Inject
	    public SpringSenseTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
	        super(index, indexSettings, name, settings);
	        bufferSize = settings.getAsInt("buffer_size", SpringSenseTokenizer.DEFAULT_BUFFER_SIZE);
	        transformToLowercase = settings.getAsBoolean("transform_to_lowercase", SpringSenseTokenizer.DEFAULT_TRANSFORM_TO_LOWER_CASE);
	    }
	    	    

	    @Override
	    public Tokenizer create(Reader reader) {
	        return new SpringSenseTokenizer(reader, bufferSize,transformToLowercase);
	    }

		protected int getBufferSize() {
			return bufferSize;
		}

		protected boolean isTransformToLowercase() {
			return transformToLowercase;
		}
	}

