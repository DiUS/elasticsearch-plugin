
package org.elasticsearch.plugin.analysis;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.springsense.SpringSenseAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 */
public class SpringSensePlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "springsense";
    }

    @Override
    public String description() {
        return "SpringSense Meaning Recognition";
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new SpringSenseAnalysisBinderProcessor());
    }
}

