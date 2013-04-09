package org.elasticsearch.index.analysis.springsense;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;

import com.springsense.disambig.DisambiguationResult;
import com.springsense.disambig.MeaningRecognitionAPI;


public class SpringSenseTokenizer  extends Tokenizer {
	  /** Default read buffer size */ 
	  public static final int DEFAULT_BUFFER_SIZE = 256;

	  private boolean done = false;
	  private int finalOffset;
	  private int currentSentance = 0;
	  private int currentTerm = 0;
	  private StringBuilder contentToParse = new StringBuilder();
	  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	  private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	  DisambiguationResult disambiguationResult;
	  
	  public MeaningRecognitionAPIFactory apiFactory = new MeaningRecognitionAPIFactory();
	  
	  public SpringSenseTokenizer(Reader input) {
	    this(input, DEFAULT_BUFFER_SIZE);
	  }

	  public SpringSenseTokenizer(Reader input, int bufferSize) {
	    super(input);
	    if (bufferSize <= 0) {
	      throw new IllegalArgumentException("bufferSize must be > 0");
	    }
	    termAtt.resizeBuffer(bufferSize);
	  }

	  public SpringSenseTokenizer(AttributeSource source, Reader input, int bufferSize) {
	    super(source, input);
	    if (bufferSize <= 0) {
	      throw new IllegalArgumentException("bufferSize must be > 0");
	    }
	    termAtt.resizeBuffer(bufferSize);
	  }

	  public SpringSenseTokenizer(AttributeFactory factory, Reader input, int bufferSize) {
	    super(factory, input);
	    if (bufferSize <= 0) {
	      throw new IllegalArgumentException("bufferSize must be > 0");
	    }
	    termAtt.resizeBuffer(bufferSize);
	  }
	  
	  @Override
	  public final boolean incrementToken() throws IOException {
		setupDisembigutaionTreeIfNotSet(); 

		termAtt.setEmpty();
		if(currentTerm >= disambiguationResult.getVariants().get(0).getSentences().get(currentSentance).getTerms().size()){
			currentSentance++;
			currentTerm = 0;
		}
		if(currentSentance >= disambiguationResult.getVariants().get(0).getSentences().size()){
			return false;
		}
		String currentTokenContent = null;
		if(disambiguationResult.getVariants().get(0).getSentences().get(currentSentance).getTerms().get(currentTerm).getMeaning() == null){
			currentTokenContent = disambiguationResult.getVariants().get(0).getSentences().get(currentSentance).getTerms().get(currentTerm).getTerm();
		} else{
			currentTokenContent = disambiguationResult.getVariants().get(0).getSentences().get(currentSentance).getTerms().get(currentTerm).getMeaning().getMeaning();
		}
		currentTerm++;
		termAtt.append(currentTokenContent);
		termAtt.setLength(currentTokenContent.length());
		finalOffset = correctOffset(currentTokenContent.length());
		  
		offsetAtt.setOffset(correctOffset(0), finalOffset);
		return true;
	  }
	  

	private void setupDisembigutaionTreeIfNotSet() throws IOException {
		if(disambiguationResult == null) 
		{
		      clearAttributes();
		     
		      while (true) {
		        char[] readInChars = new char[256];	  
		        final int length = input.read(readInChars, 0, 256);
		        if (length == -1) break;
		        if(length != 256) {
		        	char[] destArray = new char[length];
		        	System.arraycopy(readInChars, 0,
		        			destArray, 0, length);
		        	readInChars = destArray;
		        }
		        contentToParse.append(new String(readInChars));
		      };
			  MeaningRecognitionAPI api = apiFactory.getAPI();
		      disambiguationResult = api.recognize(contentToParse.toString());
		}
	}
	
	
	public String getCurrentTerm(){
		return termAtt.toString();
	}	
	
	public void setAPIFactory(MeaningRecognitionAPIFactory factory){
		apiFactory = factory;
	}	
	  
	  @Override
	  public final void end() {
	    // set final offset 
		currentSentance = 0;
		currentTerm = 0;
		contentToParse = new StringBuilder();
	    this.done = false;
	    disambiguationResult = null;
	    offsetAtt.setOffset(finalOffset, finalOffset);
	  }

	  @Override
	  public void reset() throws IOException {
		currentSentance = 0;
		currentTerm = 0;
		contentToParse = new StringBuilder();
	    this.done = false;
	    disambiguationResult = null;
	  }
	}

