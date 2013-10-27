package com.betr.engine.fusion2layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.betr.engine.AbstractTranslationInterface;
import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.gogl.Translation.Sentences;
import com.betr.evaluation.BleuEvaluator;
import com.betr.util.Util;

public class FusionTranslation extends AbstractTranslationInterface {
	
	protected TranslationInterface translator;

	public FusionTranslation(TranslationInterface translator) {
		super();
		this.translator = translator;
	}

	public List<Sentences> translate(TranslationLanguage sourceLang, TranslationLanguage targetLang,
			String text) {
		Map<TranslationLanguage, List<Sentences>> translations = new TreeMap<TranslationLanguage, List<Sentences>>();
		Map<TranslationLanguage, List<Sentences>> reverseTranslations = new TreeMap<TranslationLanguage, List<Sentences>>();
		
		/* First layer translation(source to X) */
		for(TranslationLanguage middleLang : TranslationLanguage.values()) {
			if(middleLang!=sourceLang) {
				List<Sentences> translation = translator.translate(
						sourceLang, 
						middleLang, 
						text);
				translations.put(middleLang, translation);
			}
		}
		
		/* Second layer translation(X to target) */
		for(TranslationLanguage middleLang : TranslationLanguage.values()) {
			if(middleLang!=sourceLang && middleLang!=targetLang) {
				List<Sentences> translation = translator.translate(
						middleLang, 
						targetLang, 
						Util.convertSentencesToString(translations.get(middleLang)));
				translations.put(middleLang, translation);
			}
		}
		
		/* Reverse translation for bleu score(target to source) */
		for(TranslationLanguage lang : translations.keySet()) {
			List<Sentences> translation = translator.translate(
					targetLang, 
					sourceLang, 
					Util.convertSentencesToString(translations.get(lang)));
			reverseTranslations.put(lang, translation);
		}
		
		/* Evaluate translations */
		int sentCount = calculateBleu(reverseTranslations);
		
		/* Select the best translation(fusion) */
		List<Sentences> translation = new ArrayList<Sentences>();
		for(int i=0; i<sentCount; i++) {
			double maxBleu = 0;
			TranslationLanguage maxLang = targetLang;
			for(TranslationLanguage lang : reverseTranslations.keySet()) {
				Sentences sent = reverseTranslations.get(lang).get(i);
				if(maxBleu<sent.getScore()) {
					maxBleu = sent.getScore();
					maxLang = lang;
				}
			}
			
			Sentences maxSentence = translations.get(maxLang).get(i);
			maxSentence.setScore(maxBleu);
			maxSentence.setOrig(maxLang.getName());
			translation.add(translations.get(maxLang).get(i));
		}
		
		/* Logging */
		System.out.println();
		for(TranslationLanguage lang : translations.keySet()){
			System.out.println(lang.getName()+": "+Util.convertSentencesToString(translations.get(lang)));
			System.out.println("    "+Util.convertSentencesToMarkedString(reverseTranslations.get(lang)));
		}
		
		return translation;
	}

	private int calculateBleu(Map<TranslationLanguage, List<Sentences>> candidates) {
		int numberSentences = 0;
		for(TranslationLanguage lang : candidates.keySet()) {
			List<Sentences> candidate = candidates.get(lang);
			
			/* Get sentences count */
			if(numberSentences == 0) {
				numberSentences = candidate.size();
			} else {
				numberSentences = Math.min(numberSentences, candidate.size());
			}
			
			/* For every sentence calculate BLEU */
			for(Sentences candSentence : candidate) {
				BleuEvaluator bm = new BleuEvaluator();
				
				String cand = candSentence.getTrans(); 
				String ref = candSentence.getInitialOrig();
				
				//Removes points
				cand = cand.replaceAll("\\.", " ");
				ref = ref.replaceAll("\\.", " ");
				
				//Trims
				cand = cand.trim();
				ref = ref.trim();

				String [] candTokens = cand.split("\\s+");
	            String [] refTokens = ref.split("\\s+");
	            bm.addSentence(refTokens, candTokens);
	            candSentence.setScore(bm.calculateScore());
			}
		}
		return numberSentences;
	}

}
