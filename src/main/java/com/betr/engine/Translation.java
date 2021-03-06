package com.betr.engine;

import java.util.ArrayList;
import java.util.List;


public class Translation {	
	public static class Sentences {
		private String trans;
		private String initialOrig;
		private String orig;
		private TranslationLanguage origLanguage;
		private TranslationLanguage targetLanguage;
		private List<TranslationLanguage> intermediateLanguages;
		private double score;
		
		public TranslationLanguage getTargetLanguage() {
			return targetLanguage;
		}
		public void setTargetLanguage(TranslationLanguage targetLanguage) {
			this.targetLanguage = targetLanguage;
		}
		public List<TranslationLanguage> getIntermadiateLanguages() {
			return intermediateLanguages;
		}
		public void setIntermadiateLanguages(
				List<TranslationLanguage> intermadiateLanguages) {
			this.intermediateLanguages = intermadiateLanguages;
		}
		public double getScore() {
			return score;
		}
		public void setScore(double score) {
			this.score = score;
		}
		public String getTrans() {
			return trans;
		}
		public void setTrans(String trans) {
			this.trans = trans;
		}
		public String getOrig() {
			return orig;
		}
		public void setOrig(String orig) {
			this.orig = orig;
		}
		public String getInitialOrig() {
			return initialOrig;
		}
		public void setInitialOrig(String initialOrig) {
			this.initialOrig = initialOrig;
		}
		@Override
		public Sentences clone() {
			Sentences sent = new Sentences();
			sent.setInitialOrig(initialOrig);
			if(intermediateLanguages!=null) {
				sent.setIntermadiateLanguages(new ArrayList<TranslationLanguage>(intermediateLanguages));
			}
			sent.setOrig(orig);
			sent.setScore(score);
			sent.setTargetLanguage(targetLanguage);
			sent.setTrans(trans);
			return sent;
		}
		public TranslationLanguage getOrigLanguage() {
			return origLanguage;
		}
		public void setOrigLanguage(TranslationLanguage origLanguage) {
			this.origLanguage = origLanguage;
		}
	}
	
	public static class Dict {
		String pos;
		List<String> terms;
		List<Entry> entry;
		String base_form;
		int pos_enum;
		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}
		public List<String> getTerms() {
			return terms;
		}
		public void setTerms(List<String> terms) {
			this.terms = terms;
		}
		public List<Entry> getEntry() {
			return entry;
		}
		public void setEntry(List<Entry> entry) {
			this.entry = entry;
		}
		public String getBase_form() {
			return base_form;
		}
		public void setBase_form(String base_form) {
			this.base_form = base_form;
		}
		public int getPos_enum() {
			return pos_enum;
		}
		public void setPos_enum(int pos_enum) {
			this.pos_enum = pos_enum;
		}
	}
	
	public static class Entry {
		String word;
		List<String> reverse_translation;
		double score;
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public List<String> getReverse_translation() {
			return reverse_translation;
		}
		public void setReverse_translation(List<String> reverse_translation) {
			this.reverse_translation = reverse_translation;
		}
		public double getScore() {
			return score;
		}
		public void setScore(double score) {
			this.score = score;
		}
	}
	
	private List<Sentences> sentences;
	private List<Dict> dict;
	private String src;
	private long server_time;
	
	public List<Dict> getDict() {
		return dict;
	}
	public void setDict(List<Dict> dict) {
		this.dict = dict;
	}
	public List<Sentences> getSentences() {
		return sentences;
	}
	public void setSentences(List<Sentences> sentences) {
		this.sentences = sentences;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public long getServer_time() {
		return server_time;
	}
	public void setServer_time(long server_time) {
		this.server_time = server_time;
	}
	
	
}
