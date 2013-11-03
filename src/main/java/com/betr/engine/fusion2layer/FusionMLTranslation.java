package com.betr.engine.fusion2layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.betr.engine.CoreTranslationInterface;
import com.betr.engine.TranslateException;
import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.Translation.Sentences;
import com.betr.evaluation.TranslationEvaluator;
import com.betr.util.Util;

public class FusionMLTranslation extends CoreTranslationInterface {
	
	protected TranslationInterface translator;
	protected TranslationEvaluator evaluator;

	public FusionMLTranslation(TranslationInterface translator, TranslationEvaluator evaluator) {
		super();
		this.translator = translator;
		this.evaluator = evaluator;
	}

	public List<Sentences> translate(TranslationLanguage sourceLang, TranslationLanguage targetLang,
			String text) throws TranslateException {
		int i=0;
		Map<Integer,List<Sentences>> tempTranslations;
		Map<Integer,List<Sentences>> translations = new HashMap<Integer,List<Sentences>>();
		Map<Integer,List<Sentences>> reverseTranslations = new HashMap<Integer,List<Sentences>>();
		
		/* First layer translation(source to X) */
		System.out.println(sourceLang.getName());
		for(TranslationLanguage middleLang : TranslationLanguage.values()) {
			if(middleLang!=sourceLang) {
				List<Sentences> translation = translator.translate(
						sourceLang, 
						middleLang, 
						text);
				translations.put(i++, translation);
				System.out.print(middleLang.getName()+" ");
			}
		}
		
		/* Second layer translation(bridge) */
		for(int j=0; j<2; j++) {
			i = 0;
			System.out.println();
			tempTranslations = new HashMap<Integer, List<Sentences>>();
			for(List<Sentences> list : translations.values()) {
				if(list!=null && list.size()>0) {
					TranslationLanguage middleLang = list.get(0).getTargetLanguage();
					for(TranslationLanguage bridgeLang : TranslationLanguage.values()) {
						if(middleLang!=bridgeLang) {
							List<Sentences> translation = translator.translate(
									middleLang, 
									bridgeLang, 
									list);
							tempTranslations.put(i++,translation);
							System.out.print(bridgeLang.getName()+" ");
						}
					}
				}
			}
			translations = tempTranslations;
		}
		
		/* Finel layer translation(bridge to target) */
		i = 0;
		System.out.println();
		tempTranslations = new HashMap<Integer, List<Sentences>>();
		for(List<Sentences> list : translations.values()) {
			if(list!=null && list.size()>0) {
				TranslationLanguage middleLang = list.get(0).getTargetLanguage();
				if(middleLang!=targetLang) {
					List<Sentences> translation = translator.translate(
							middleLang, 
							targetLang, 
							list);
					tempTranslations.put(i++,translation);
					System.out.print(targetLang.getName()+" ");
				}
			}
		}
		translations = tempTranslations;
		
		/* Reverse translation for bleu score(target to source) */
		for(int key : translations.keySet()) {
			List<Sentences> list = translations.get(key);
			if(list!=null && list.size()>0) {
				List<Sentences> translation = translator.translate(
						targetLang, 
						sourceLang, 
						list);
					reverseTranslations.put(key,translation);
			}
		}
		
		/* Evaluate translations */
		int sentCount = calculateBleu(reverseTranslations);
		
		/* Select the best translation(fusion) */
		List<Sentences> translation = new ArrayList<Sentences>();
		for(i=0; i<sentCount; i++) {
			double maxBleu = 0;
			int maxKey = 0;
			for(int key : reverseTranslations.keySet()) {
				List<Sentences> reverse = reverseTranslations.get(key);
				Sentences sent = reverse.get(i);
				if(maxBleu<sent.getScore()) {
					maxBleu = sent.getScore();
					maxKey = key;
				}
			}
			
			Sentences maxSentence = translations.get(maxKey).get(i);
			maxSentence.setScore(maxBleu);
			translation.add(translations.get(maxKey).get(i));
		}
		
		/* Logging */
		System.out.println("Translations: ");
		for(int key:translations.keySet()){
			List<Sentences> tr = translations.get(key);
			List<TranslationLanguage> intLangs = tr.get(0).getIntermadiateLanguages();
			System.out.print(sourceLang.getName()+"-");
			for(TranslationLanguage lang : intLangs) {
				System.out.print(lang.getName()+"-");
			}
			System.out.print(targetLang.getName());
			System.out.println("     "+Util.convertSentencesToMarkedString(reverseTranslations.get(key), false));
		}
		
		return translation;
	}

	private int calculateBleu(Map<Integer, List<Sentences>> reverseTranslations) {
		int numberSentences = 0;
		for(List<Sentences> candidate : reverseTranslations.values()) {
			
			/* Get sentences count */
			if(numberSentences == 0) {
				numberSentences = candidate.size();
			} else {
				numberSentences = Math.min(numberSentences, candidate.size());
			}
			
			/* For every sentence calculate BLEU */
			for(Sentences candSentence : candidate) {
				evaluator.reset();
				
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
	            evaluator.addSentence(refTokens, candTokens);
	            candSentence.setScore(evaluator.calculateScore());
			}
		}
		return numberSentences;
	}

}
