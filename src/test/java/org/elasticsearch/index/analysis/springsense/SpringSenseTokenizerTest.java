package org.elasticsearch.index.analysis.springsense;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class SpringSenseTokenizerTest {

	@Test
	public void shouldReadInCompleteBufferOver2Passes() throws IOException {
		
		StringBuilder myString = new StringBuilder();
		for(int i = 0 ; i < 50;i++){
			myString.append("0123456789");
		}
		
		Reader reader = new StringReader(myString.toString()) ;
		
		
		SpringSenseTokenizer sut = new SpringSenseTokenizer( reader, 255);
		
		sut.incrementToken();
		
		assertThat(sut.getBaseString(),is(myString.toString()));
		
	}
	
	@Test
	public void shouldOutput5TermsForSetTerm() throws IOException {
		
		Reader reader = new StringReader("expectedTerm") ;
		
		
		SpringSenseTokenizer sut = new SpringSenseTokenizer( reader, 255);
		
		assertThat(sut.incrementToken(),is(true));
		assertThat(sut.getCurrentTerm(),is("expectedTerm_0"));
		assertThat(sut.incrementToken(),is(true));
		assertThat(sut.getCurrentTerm(),is("expectedTerm_1"));
		assertThat(sut.incrementToken(),is(true));
		assertThat(sut.getCurrentTerm(),is("expectedTerm_2"));
		assertThat(sut.incrementToken(),is(true));
		assertThat(sut.getCurrentTerm(),is("expectedTerm_3"));
		assertThat(sut.incrementToken(),is(true));
		assertThat(sut.getCurrentTerm(),is("expectedTerm_4"));
		assertThat(sut.incrementToken(),is(false));
		
	}	

}
