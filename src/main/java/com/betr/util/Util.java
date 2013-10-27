package com.betr.util;

import java.util.List;

import com.betr.engine.TranslationLanguage;
import com.betr.engine.gogl.Translation.Sentences;

public class Util {
	
	public static String convertSentencesToMarkedString(List<Sentences> sentences) {
		String text = "";
		if(sentences!=null) {
			for(Sentences sent: sentences) {
				String interLang = "";
				if(sent.getIntermadiateLanguages() != null) {
					for(TranslationLanguage lang : sent.getIntermadiateLanguages()) {
						interLang += lang.getName() + "-";
					}
				}
				interLang += sent.getTargetLanguage().getName();
				
				text += sent.getTrans() + "(" + interLang + " , " + sent.getScore() + ")";
			}
		}
		return text;
	}
	
	public static String convertSentencesToString(List<Sentences> sentences) {
		String text = "";
		if(sentences!=null) {
			for(Sentences sent: sentences) {
				text += sent.getTrans();
			}
		}
		return text;
	}
}
