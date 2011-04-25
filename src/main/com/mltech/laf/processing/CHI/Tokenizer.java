package com.mltech.laf.processing.CHI;

import java.io.IOException;
import java.io.StringReader;


import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import com.mltech.laf.annotations.Annotation;
import com.mltech.laf.annotations.FeatureSet;
import com.mltech.laf.document.Document;

public class Tokenizer extends com.mltech.laf.processing.Tokenizer {
	private Dictionary _dictionary;
	private Seg _segmenter;

	private int addTokenAnnotation(Document document, int start, int length, int offset, String token, String type) {
		FeatureSet fs = new FeatureSet();
		fs.put("string", token);
		fs.put("type", type);
		if (offset > -1) {
			document.annotations("token")
					.add(new Annotation(start + offset, start + length + offset, fs));
		}
		start += length + offset;
		return start;
	}

	// TODO: optimize
	// TODO: filter whitespace from punctuation annotation
	@Override
	protected void parse(Document document) {
		String text = document.text().toLowerCase(); //TODO: always necessary?
		String remainingText = text;
		MMSeg mmSeg = new MMSeg(new StringReader(text), _segmenter);
		Word word = null;
		int start = 0;
		int offset = 0;
		try {
			while ((word = mmSeg.next()) != null) {
				String token = word.getString();
				offset = remainingText.indexOf(token);
				if(offset > 0) {
					String sep = document.text().substring(start, start + offset);
					addTokenAnnotation(document, start, sep.length(), 0, sep, "punctuation");
				}
				remainingText = remainingText.substring(offset + token.length());
				start = addTokenAnnotation(document, start, token.length(), offset, token, "word");
			}
			addTokenAnnotation(document, start, remainingText.length(), 0, remainingText, "punctuation");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void init() {
		_dictionary = Dictionary.getInstance();
		_segmenter = new ComplexSeg(_dictionary);
	}
}
