package org.elasticsearch.index.analysis.springsense;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import com.springsense.disambig.DisambiguationResult;
import com.springsense.disambig.MeaningRecognitionAPI;
import com.springsense.disambig.DisambiguationResult.Sentence;

public class SpringSenseTokenizerTest {
	
	
	String fullJsonResponse = "[" +
			"{\"terms\":[" +
			"{\"term\":\"black box\"," +
			"\"lemma\":\"black_box\"," +
			"\"word\":\"black_box\"," +
			"\"POS\":\"NN\"," +
			"\"offset\":0," +
			"\"meanings\":[" +
				"{\"definition\":\"equipment that records information about the performance of an aircraft during flight\"," +
				"\"meaning\":\"black_box_n_01\"}" +
			"]" +
		"}]" +
		",\"scores\":[" +
			"1.0" +
		"]}" +
	"]";
	
	@Test
	public void shouldReadInCompleteBufferOver2Passes() throws IOException {
		
		StringBuilder myString = new StringBuilder();
		for(int i = 0 ; i < 50;i++){
			myString.append("0123456789");
		}
		
		Reader reader = new StringReader(myString.toString()) ;
		
		MeaningRecognitionAPIFactory mockFactory = mock(MeaningRecognitionAPIFactory.class);
		MeaningRecognitionAPI mockApi = mock(MeaningRecognitionAPI.class);

		
		final String simpleSingleTermResult = "["
				+   "{"
				+      "\"terms\":["
				+         "{"
				+            "\"term\":\"cat\","
				+            "\"meanings\":["
				+               "{"
				+                  "\"meaning\":\"cat_n_01\""
				+               "}"
				+            "]"
				+         "}"
				+      "],"
				+      "\"scores\":["
				+         "0.3340627674438098"
				+      "]"
				+   "}"
				+"]";
		DisambiguationResult expectedResult = DisambiguationResult.fromJson(simpleSingleTermResult);

		
		when(mockFactory.getAPI()).thenReturn(mockApi);
		when(mockApi.recognize(myString.toString())).thenReturn(expectedResult);

		SpringSenseTokenizer sut = new SpringSenseTokenizer( reader, 255);
		sut.setAPIFactory(mockFactory);

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("cat_n_01"));

		assertThat(sut.incrementToken(),is(false));


		
		
	}
	
	@Test
	public void shouldOutputAllTermsOfSentences() throws IOException {
		
		Reader reader = new StringReader("expectedTerm") ;
		
		
		MeaningRecognitionAPIFactory mockFactory = mock(MeaningRecognitionAPIFactory.class);
		MeaningRecognitionAPI mockApi = mock(MeaningRecognitionAPI.class);

		
		final String twoSentancesEachWith2Terms = "["
	            +   "{"
	            +      "\"terms\":["
	            +         "{"
	            +            "\"term\":\"cat\","
	            +            "\"meanings\":["
	            +               "{"
	            +                  "\"meaning\":\"term_1_from_sentance_1\""
	            +               "}"
	            +            "]"
	            +         "},"
	            +         "{"
	            +            "\"term\":\"vet\","
	            +            "\"meanings\":["
	            +               "{"
	            +                  "\"meaning\":\"term_2_from_sentance_1\""
	            +               "}"
	            +            "]"
	            +         "}"
	            +      "],"
	            +      "\"scores\":["
	            +         "0.33289539625560377"
	            +      "]"
	            +   "},"
	            +   "{"
	            +      "\"terms\":["
	            +         "{"
	            +            "\"term\":\"cat\","
	            +            "\"meanings\":["
	            +               "{"
	            +                  "\"meaning\":\"term_1_from_sentance_2\""
	            +               "}"
	            +            "]"
	            +         "},"
	            +         "{"
	            +            "\"term\":\"vet\","
	            +            "\"meanings\":["
	            +               "{"
	            +                  "\"meaning\":\"term_2_from_sentance_2\""
	            +               "}"
	            +            "]"
	            +         "}"
	            +      "],"
	            +      "\"scores\":["
	            +         "0.33289539625560377"
	            +      "]"
	            +   "}"	            
	            +"]";
		DisambiguationResult expectedResult = DisambiguationResult.fromJson(twoSentancesEachWith2Terms);

		
		when(mockFactory.getAPI()).thenReturn(mockApi);
		when(mockApi.recognize("expectedTerm")).thenReturn(expectedResult);

		SpringSenseTokenizer sut = new SpringSenseTokenizer( reader, 255);
		sut.setAPIFactory(mockFactory);

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("term_1_from_sentance_1"));

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("term_2_from_sentance_1"));		
		

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("term_1_from_sentance_2"));

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("term_2_from_sentance_2"));			

		assertThat(sut.incrementToken(),is(false));
		
	}	
	
	@Test
	public void shouldOutputTermIfNoMeaning() throws IOException {

		Reader reader = new StringReader("expectedTerm") ;
		
		
		MeaningRecognitionAPIFactory mockFactory = mock(MeaningRecognitionAPIFactory.class);
		MeaningRecognitionAPI mockApi = mock(MeaningRecognitionAPI.class);
		
		final String simpleSingleTermResult = "["
				+   "{"
				+      "\"terms\":["
				+         "{"
				+            "\"term\":\"cat\","
				+            "\"meanings\":["
				+            "]"
				+         "}"
				+      "],"
				+      "\"scores\":["
				+      "]"
				+   "}"
				+"]";
		DisambiguationResult expectedResult = DisambiguationResult.fromJson(simpleSingleTermResult);

		
		when(mockFactory.getAPI()).thenReturn(mockApi);
		when(mockApi.recognize("expectedTerm")).thenReturn(expectedResult);

		SpringSenseTokenizer sut = new SpringSenseTokenizer( reader, 255);
		sut.setAPIFactory(mockFactory);

		assertThat(sut.incrementToken(),is(true));

		assertThat(sut.getCurrentTerm(),is("cat"));

		assertThat(sut.incrementToken(),is(false));
		
	}

}
