package org.elasticsearch.index.analysis.springsense;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.io.StringReader;

import static org.hamcrest.CoreMatchers.is;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.junit.Test;

import com.springsense.disambig.MeaningRecognitionAPI;

public class SpringSenseTokenizerFactoryTest {

	@Test
	public void settingsHaveCorrectDefaults() {
		Index mockIndex = mock(Index.class);
		Settings passedIndexSettings = mock(Settings.class);
		Settings passedSettings = mock(Settings.class);
		

		when(passedSettings.getAsInt("buffer_size", SpringSenseTokenizer.DEFAULT_BUFFER_SIZE)).thenReturn(SpringSenseTokenizer.DEFAULT_BUFFER_SIZE);
		when(passedSettings.getAsBoolean("transform_to_lowercase", SpringSenseTokenizer.DEFAULT_TRANSFORM_TO_LOWER_CASE)).thenReturn(SpringSenseTokenizer.DEFAULT_TRANSFORM_TO_LOWER_CASE);
		
		SpringSenseTokenizerFactory sut = new SpringSenseTokenizerFactory(mockIndex,passedIndexSettings,"name",passedSettings);
		
		assertThat(sut.getBufferSize(),is(SpringSenseTokenizer.DEFAULT_BUFFER_SIZE));
		assertThat(sut.isTransformToLowercase(),is(SpringSenseTokenizer.DEFAULT_TRANSFORM_TO_LOWER_CASE));
		
	}
	
	

	
	@Test
	public void settingsHaveDifferntValuesWhenSet() {
		Index mockIndex = mock(Index.class);
		Settings passedIndexSettings = mock(Settings.class);
		Settings passedSettings = mock(Settings.class);
		

		when(passedSettings.getAsInt(eq("buffer_size"), anyInt())).thenReturn(1000);
		when(passedSettings.getAsBoolean(eq("transform_to_lowercase"), anyBoolean())).thenReturn(true);
		
		SpringSenseTokenizerFactory sut = new SpringSenseTokenizerFactory(mockIndex,passedIndexSettings,"name",passedSettings);
		
		assertThat(sut.getBufferSize(),is(1000));
		assertThat(sut.isTransformToLowercase(),is(true));
		
	}	
	
	@Test
	public void settingsAreUsedWhenCreatingTokeniser() {

		SpringSenseTokenizerFactory sut = createFactoryWithValues(1000,true);
		
		Tokenizer createdTokeniser = sut.create(new StringReader("not important"));
		
		assertThat(((SpringSenseTokenizer)createdTokeniser).getBufferSize(),is(1000));
		assertThat(((SpringSenseTokenizer)createdTokeniser).isTransformToLowerCase(),is(true));
		
	}	
	
	private SpringSenseTokenizerFactory createFactoryWithValues(int bufferSize,boolean transformToLowerCase){
		Index mockIndex = mock(Index.class);
		Settings passedIndexSettings = mock(Settings.class);
		Settings passedSettings = mock(Settings.class);
		

		when(passedSettings.getAsInt(eq("buffer_size"), anyInt())).thenReturn(bufferSize);
		when(passedSettings.getAsBoolean(eq("transform_to_lowercase"), anyBoolean())).thenReturn(transformToLowerCase);

		return new SpringSenseTokenizerFactory(mockIndex,passedIndexSettings,"name",passedSettings);
	}
	
}
