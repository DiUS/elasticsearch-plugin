package org.elasticsearch.index.analysis.springsense;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.AttributeSource;

import com.springsense.disambig.DisambiguationResult;
import com.springsense.disambig.DisambiguationResult.ResolvedTerm;
import com.springsense.disambig.DisambiguationResult.Variant;
import com.springsense.disambig.DisambiguationResult.VariantSentence;
import com.springsense.disambig.MeaningRecognitionAPI;

public class SpringSenseTokenizer extends Tokenizer {
	/** Default read buffer size */
	public static final int DEFAULT_BUFFER_SIZE = 256;
	public static final boolean DEFAULT_TRANSFORM_TO_LOWER_CASE = false;
	
	public int bufferSize;
	public boolean transformToLowerCase;
	

	private int finalOffset;
	private int currentSentance = 0;
	private int currentTerm = 0;
	private StringBuilder contentToParse = new StringBuilder();
	private final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
	private OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);
	private DisambiguationResult disambiguationResult;

	public MeaningRecognitionAPIFactory apiFactory = new MeaningRecognitionAPIFactory();

	public SpringSenseTokenizer(Reader input) {
		this(input, DEFAULT_BUFFER_SIZE,DEFAULT_TRANSFORM_TO_LOWER_CASE);
	}

	public SpringSenseTokenizer(Reader input, int bufferSize,boolean transformToLowerCase) {
		super(input);
		init(bufferSize,transformToLowerCase);
	}



	public SpringSenseTokenizer(AttributeSource source, Reader input, int bufferSize,boolean transformToLowerCase) {
		super(source, input);
		init(bufferSize,transformToLowerCase);
	}

	public SpringSenseTokenizer(AttributeFactory factory, Reader input, int bufferSize,boolean transformToLowerCase) {
		super(factory, input);
		init(bufferSize,transformToLowerCase);
	}
	
	private void init(int bufferSize,boolean transformToLowerCase) {
		if (bufferSize <= 0) {
			throw new IllegalArgumentException("bufferSize must be > 0");
		}
		this.bufferSize = bufferSize;
		this.transformToLowerCase = transformToLowerCase;
		termAttribute.resizeBuffer(bufferSize);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		setupDisembigutaionTreeIfNotSet();

		termAttribute.setEmpty();
		if (getFirstVariant() == null) {
			return false;
		}

		if (currentTerm >= getCurrentSentence().getTerms().size()) {
			currentSentance++;
			currentTerm = 0;
		}

		if (currentSentance >= getFirstVariant().getSentences().size()) {
			return false;
		}

		String currentTokenContent = null;
		if (getCurrentTerm().getMeaning() == null) {
			currentTokenContent = getCurrentTerm().getTerm();
		} else {
			currentTokenContent = getCurrentTerm().getMeaning().getMeaning();
		}

		currentTerm++;
		termAttribute.append(currentTokenContent);
		termAttribute.setLength(currentTokenContent.length());
		finalOffset = correctOffset(currentTokenContent.length());

		offsetAttribute.setOffset(correctOffset(0), finalOffset);
		return true;
	}

	private ResolvedTerm getCurrentTerm() {
		return getCurrentSentence().getTerms().get(currentTerm);
	}

	private VariantSentence getCurrentSentence() {
		return getFirstVariant().getSentences().get(currentSentance);
	}

	private Variant getFirstVariant() {
		return disambiguationResult.getVariants().isEmpty() ? null : disambiguationResult.getVariants().get(0);
	}

	private void setupDisembigutaionTreeIfNotSet() throws IOException {
		if (disambiguationResult == null) {
			clearAttributes();

			while (true) {
				char[] readInChars = new char[bufferSize];
				final int length = input.read(readInChars, 0, bufferSize);
				if (length == -1)
					break;
				if (length != bufferSize) {
					char[] destArray = new char[length];
					System.arraycopy(readInChars, 0, destArray, 0, length);
					readInChars = destArray;
				}
				contentToParse.append(new String(readInChars));
			}
			;
			MeaningRecognitionAPI api = apiFactory.getAPI();
			String contentToDisambiguate = transformToLowerCase ? contentToParse.toString().toLowerCase() : contentToParse.toString();
			disambiguationResult = api.recognize(contentToDisambiguate);
		}
	}

	protected String peekCurrentTermAttribute() {
		return termAttribute.toString();
	}

	public void setAPIFactory(MeaningRecognitionAPIFactory factory) {
		apiFactory = factory;
	}

	@Override
	public final void end() {
		resetContent();
		offsetAttribute.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset() throws IOException {
		resetContent();
	}

	private void resetContent() {
		currentSentance = 0;
		currentTerm = 0;
		contentToParse = new StringBuilder();
		disambiguationResult = null;
	}

	protected int getBufferSize() {
		return bufferSize;
	}

	protected boolean isTransformToLowerCase() {
		return transformToLowerCase;
	}
}
