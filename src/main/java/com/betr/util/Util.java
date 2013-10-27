package com.betr.util;

import java.util.List;

import com.betr.engine.gogl.Translation.Sentences;

public class Util {
	
	public static String convertSentencesToMarkedString(List<Sentences> sentences) {
		String text = "";
		if(sentences!=null) {
			for(Sentences sent: sentences) {
				text += sent.getTrans() + "(" + sent.getOrig() + " , " + sent.getScore() + ")";
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
