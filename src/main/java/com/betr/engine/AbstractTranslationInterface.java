package com.betr.engine;

import java.util.List;

import com.betr.engine.gogl.Translation.Sentences;
import com.betr.util.Util;

public abstract class AbstractTranslationInterface implements TranslationInterface{
	
	public List<Sentences> translate(TranslationLanguage from,
			TranslationLanguage to, List<Sentences> inputSentences) {		
		List<Sentences> translation = translate(
				from, 
				to, 
				Util.convertSentencesToString(inputSentences));
		
		/* Add initial origin and intermediate translation */
		int sentCount = Math.min(inputSentences.size(), translation.size());
		for(int i=0; i<sentCount; i++) {
			Sentences input = inputSentences.get(i);
			Sentences output = translation.get(i);
			if(input.getInitialOrig() == null) {
				output.setInitialOrig(input.getOrig());
			} else {
				output.setInitialOrig(input.getInitialOrig());
			}
			output.getIntermadiateLang().add(input.getTargetLanguage());
		}
		
		return translation;
	}
	
}
