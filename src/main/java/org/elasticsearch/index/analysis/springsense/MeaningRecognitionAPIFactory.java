package org.elasticsearch.index.analysis.springsense;

import com.springsense.disambig.MeaningRecognitionAPI;

public class MeaningRecognitionAPIFactory {
	
	public MeaningRecognitionAPI getAPI(){
		return  new MeaningRecognitionAPI("http://v2.api.springsense.com:8080/disambiguate", null, null);
	}

}
