package org.elasticsearch.index.analysis.springsense;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public final class SampleSpringSenseTokenFilter extends TokenFilter {
	
	
  private CharTermAttribute charTermAttr;
 
  public SampleSpringSenseTokenFilter(TokenStream ts) {
    super(ts);
    this.charTermAttr = addAttribute(CharTermAttribute.class);
  }
 
  @Override
  public boolean incrementToken() throws IOException {
    if (!input.incrementToken()) {
      return false;
    }
 
    String currentToken = charTermAttr.toString();
    if(currentToken.equalsIgnoreCase("yellow"))
    {
	    charTermAttr.setEmpty();
	    charTermAttr.append("grey");
    }
    return true;
  }
}