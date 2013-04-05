package org.elasticsearch.index.analysis.springsense;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;


public class SpringSenseTokenizer  extends Tokenizer {
	  /** Default read buffer size */ 
	  public static final int DEFAULT_BUFFER_SIZE = 256;

	  private boolean done = false;
	  private int finalOffset;
	  private int currentToken = 0;
	  private int totalToken = 5;
	  private String baseString;
	  private StringBuilder contentToParse = new StringBuilder();
	  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	  private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	  
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
		setupBaseStringIfNotSet(); 

		termAtt.setEmpty();
		String currentTokenContent = baseString+"_"+currentToken;
		termAtt.append(currentTokenContent);
		termAtt.setLength(currentTokenContent.length());
		finalOffset = correctOffset(currentTokenContent.length());
		  
		offsetAtt.setOffset(correctOffset(0), finalOffset);
		  
		if(currentToken < totalToken){
			currentToken++;
			return true;
		}
		return false;
	  }

	private void setupBaseStringIfNotSet() throws IOException {
		if(baseString == null) 
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
		      }
		      baseString = contentToParse.toString();
		}
	}
	
	public String getBaseString(){
		return baseString;
	}
	
	public String getCurrentTerm(){
		return termAtt.toString();
	}	
	  
	  @Override
	  public final void end() {
	    // set final offset 
	    offsetAtt.setOffset(finalOffset, finalOffset);
	  }

	  @Override
	  public void reset() throws IOException {
	    this.done = false;
	  }
	}

