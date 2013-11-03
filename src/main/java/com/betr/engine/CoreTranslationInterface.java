package com.betr.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betr.engine.Translation.Sentences;
import com.betr.util.Util;

public class CoreTranslationInterface implements TranslationInterface{
	
	protected static long totalTranslations = 0;
	
	protected static long cacheHits = 0;
	
	protected static long cacheSize = 0;
	
	protected static Map<String,Map<TranslationLanguage,List<Sentences>>> translationCache = new HashMap<String, Map<TranslationLanguage,List<Sentences>>>();
	
	protected void addToCache(String text, TranslationLanguage to,
			List<Sentences> translation) {
		String form = text.trim().toLowerCase();
		Map<TranslationLanguage,List<Sentences>> map = translationCache.get(form);
		if(map==null) {
			map = new HashMap<TranslationLanguage, List<Sentences>>();
			translationCache.put(form, map);
		}
		map.put(to, translation);
		cacheSize++;
	}
	
	public List<Sentences> translate(TranslationLanguage from,
			TranslationLanguage to, String text) throws TranslateException {
		Map<TranslationLanguage,List<Sentences>> base = translationCache.get(text.trim().toLowerCase());
		totalTranslations++;
		if(base!=null) {
			List<Sentences> ret = base.get(to);
			if(ret!=null) {
				cacheHits++;
				List<Sentences> retClone = new ArrayList<Sentences>();
				for(Sentences sent:ret){
					retClone.add(sent.clone());
				}
				return retClone;
			}
		}
		return null;
	}
	
	public List<Sentences> translate(TranslationLanguage from,
			TranslationLanguage to, List<Sentences> inputSentences) throws TranslateException {		
		List<Sentences> translation = translate(
				from, 
				to, 
				Util.convertSentencesToString(inputSentences));
		
		merge(translation, inputSentences);
		
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
			
			/* Add intermediate language*/
			if(output.getIntermadiateLanguages() == null) {
				output.setIntermadiateLanguages(new ArrayList<TranslationLanguage>());
				if(input.getIntermadiateLanguages() != null) {
					output.getIntermadiateLanguages().addAll(input.getIntermadiateLanguages());
				}
			}
			output.getIntermadiateLanguages().add(from);
		}
		
		return translation;
	}

	/**
	 * This method merges translation sentences and input sentences 
	 * to rich its equal sizes.
	 * @param translation
	 * @param inputSentences
	 * @return
	 */
	private List<Sentences> merge(List<Sentences> translation,
			List<Sentences> inputSentences) {
		List<Sentences> ret = new ArrayList<Sentences>();
		int j = 0;
		if(translation.size()!=inputSentences.size()) {
			for(int i=0; i<translation.size(); i++) {
				Sentences newSent = translation.get(i).clone();
				String trans = newSent.getTrans();
				String orig = newSent.getOrig();
				String origComp = inputSentences.get(j++).getTrans();
				while(!orig.trim().toLowerCase().equals(origComp.trim().toLowerCase()) && i<translation.size()-1){
					while(orig.trim().length()>origComp.trim().length() && j<inputSentences.size()){
						origComp = origComp + inputSentences.get(j++).getTrans();
					}
					while(orig.trim().length()<origComp.trim().length() && i<translation.size()-1){
						i++;
						trans = trans + translation.get(i).getTrans();
						orig = orig + translation.get(i).getOrig();
					}
				}
				newSent.setTrans(trans);
				newSent.setOrig(orig);
			}
		}
		return ret;
	}

	public static long getTotalTranslations() {
		return totalTranslations;
	}

	public static long getCacheHits() {
		return cacheHits;
	}

	public static long getCacheSize() {
		return cacheSize;
	}
	
	
	
}
