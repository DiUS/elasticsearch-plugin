
package org.elasticsearch.index.analysis.springsense;

import org.elasticsearch.index.analysis.AnalysisModule;

/**
 */
public class SpringSenseAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("sampleSprinSenseFilter", SampleSpringSenseTokenFilterFactory.class);
    }
    
    @Override
    public void processTokenizers(TokenizersBindings tokenizerBindings) {
    	tokenizerBindings.processTokenizer("springSenseTokenizer", SpringSenseTokenizerFactory.class);
    }
}
